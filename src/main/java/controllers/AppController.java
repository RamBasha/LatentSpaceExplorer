package controllers;

import commands.*;
import math.VectorOperations;
import models.WordVector;
import state.AppState;
import state.CommandHistory;
import view2d.ControlPanel;
import view2d.MetricsPanel;
import javafx.concurrent.Task;
import view3d.Space3D;

import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class AppController {
    private final Space3D space3D;
    private final ControlPanel controlPanel;
    private final MetricsPanel metricsPanel;
    private final CommandHistory history;

    public AppController(Space3D space3D, ControlPanel controlPanel, MetricsPanel metricsPanel) {
        this.space3D = space3D;
        this.controlPanel = controlPanel;
        this.metricsPanel = metricsPanel;
        this.history = new CommandHistory();

        bindEvents();
    }


    public void loadInitialData() {
        metricsPanel.updateStatus("Running Python script... Please wait (might take a minute).");

        Task<Void> pythonTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                ProcessBuilder pb = new ProcessBuilder("python", "embedder.py");
                pb.redirectErrorStream(true);
                Process process = pb.start();

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("Embedder.py: " + line);
                    }
                }

                int exitCode = process.waitFor();

                if (exitCode != 0) {
                    throw new RuntimeException("Python script failed with exit code: " + exitCode + ". Check console for details.");
                }

                io.DataLoader loader = new io.JsonDataLoader();
                state.AppState.getInstance().initializeData(
                        loader.loadVectors("full_vectors.json", models.VectorType.FULL_DIMENSION),
                        loader.loadVectors("pca_vectors.json", models.VectorType.PCA_REDUCED)
                );
                return null;
            }

            @Override
            protected void succeeded() {
                space3D.populateSpace();
                space3D.bindNodeClicks(word -> {
                    int k = controlPanel.getKSpinner().getValue();
                    try {
                        history.executeCommand(new KnnCommand(space3D, word, k, getSelectedDistanceStrategy()));
                        metricsPanel.updateStatus("Clicked: Found " + k + " neighbors for '" + word + "' using " + getMetricName());
                    } catch (Exception ex) {
                        metricsPanel.updateStatus("Error on click KNN: " + ex.getMessage());
                    }
                });
                metricsPanel.updateStatus("Data loaded successfully.");
            }

            @Override
            protected void failed() {
                metricsPanel.updateStatus("Error: " + getException().getMessage());
                getException().printStackTrace();
            }
        };

        new Thread(pythonTask).start();
    }




    private math.DistanceStrategy getSelectedDistanceStrategy() {
        if ("Euclidean Distance".equals(getMetricName())) {
            return new math.EuclideanDistance();
        }
        return new math.CosineSimilarity();
    }

    private String getMetricName() {
        return controlPanel.getComboDistance().getValue();
    }

    private void bindEvents() {

        controlPanel.getComboDistance().setOnAction(e -> {
            metricsPanel.updateStatus("Distance metric switched to: " + getMetricName());
        });

        javafx.event.EventHandler<javafx.event.ActionEvent> axisChangeHandler = e -> {
            int nx = controlPanel.getComboX().getValue();
            int ny = controlPanel.getComboY().getValue();
            int nz = controlPanel.getComboZ().getValue();
            try {
                history.executeCommand(new ChangeAxisCommand(space3D, nx, ny, nz));
            } catch (Exception ex) {
                metricsPanel.updateStatus("Error changing axes: " + ex.getMessage());
            }
        };
        controlPanel.getComboX().setOnAction(axisChangeHandler);
        controlPanel.getComboY().setOnAction(axisChangeHandler);
        controlPanel.getComboZ().setOnAction(axisChangeHandler);

        AppState.getInstance().addListener(() -> {
            space3D.refreshPositions();
            metricsPanel.updateStatus("PCA Axes updated.");
        });

        // Search & KNN
        controlPanel.getBtnSearch().setOnAction(e -> {
            String word = controlPanel.getSearchField().getText().trim().toLowerCase();
            if (word.isEmpty()) {
                metricsPanel.updateStatus("Error: Search field is empty.");
                return;
            }
            int k = controlPanel.getKSpinner().getValue();
            try {
                history.executeCommand(new KnnCommand(space3D, word, k, getSelectedDistanceStrategy()));
                metricsPanel.updateStatus("Search: Found " + k + " neighbors for '" + word + "' using " + getMetricName());
            } catch (Exception ex) {
                metricsPanel.updateStatus("Error searching: " + ex.getMessage());
            }
        });

        // Centroid
        controlPanel.getBtnCentroid().setOnAction(e -> {
            String input = controlPanel.getSearchField().getText().trim().toLowerCase();
            if (input.isEmpty()) {
                metricsPanel.updateStatus("Error: Enter words separated by commas.");
                return;
            }
            String[] words = input.split(",");
            int k = controlPanel.getKSpinner().getValue();

            try {
                history.executeCommand(new CentroidCommand(space3D, words, k, getSelectedDistanceStrategy()));
                metricsPanel.updateStatus("Centroid calculated for " + words.length + " words using " + getMetricName());
            } catch (Exception ex) {
                metricsPanel.updateStatus("Error calculating centroid: " + ex.getMessage());
            }
        });

        // Equation
        controlPanel.getBtnEquation().setOnAction(e -> {
            String rawText = controlPanel.getEquationField().getText().trim().toLowerCase();
            String eq = rawText.replace("+", " + ").replace("-", " - ").replaceAll("\\s+", " ").trim();
            String[] tokens = eq.split("\\s+");

            if (tokens.length < 3 || tokens.length % 2 == 0) {
                metricsPanel.updateStatus("Error: Invalid equation format. Use spaces, e.g. 'king - man + woman'");
                return;
            }

            Command equationCommand = new Command() {
                @Override
                public void execute() throws Exception {
                    Set<String> equationWords = new HashSet<>();
                    List<String> pathWords = new ArrayList<>();

                    Optional<WordVector> currentOpt = AppState.getInstance().getFullVector(tokens[0]);
                    if (currentOpt.isEmpty()) throw new Exception("Word not found: " + tokens[0]);

                    double[] currentVec = currentOpt.get().coordinates().clone();
                    equationWords.add(tokens[0]);
                    pathWords.add(tokens[0]);

                    for (int i = 1; i < tokens.length; i += 2) {
                        String op = tokens[i];
                        String nextWord = tokens[i+1];

                        Optional<WordVector> nextOpt = AppState.getInstance().getFullVector(nextWord);
                        if (nextOpt.isEmpty()) throw new Exception("Word not found: " + nextWord);

                        if (op.equals("+")) {
                            currentVec = VectorOperations.add(currentVec, nextOpt.get().coordinates());
                        } else if (op.equals("-")) {
                            currentVec = VectorOperations.subtract(currentVec, nextOpt.get().coordinates());
                        } else {
                            throw new Exception("Invalid operator: " + op);
                        }
                        equationWords.add(nextWord);
                        pathWords.add(nextWord);
                    }

                    int k = controlPanel.getKSpinner().getValue();
                    List<WordVector> allFull = new ArrayList<>(AppState.getInstance().getAllFullVectors());
                    List<WordVector> results = VectorOperations.findKNearest(currentVec, allFull, getSelectedDistanceStrategy(), k, equationWords);

                    List<String> resultNames = results.stream().map(WordVector::word).toList();
                    space3D.drawEquationPath(pathWords, resultNames);

                    if (!resultNames.isEmpty()) {
                        metricsPanel.updateStatus("Equation solved using " + getMetricName() + ". Result: " + resultNames.get(0));
                    } else {
                        metricsPanel.updateStatus("Equation solved, but no results found.");
                    }
                }

                @Override
                public void undo() {
                    space3D.clearVectors();
                }
            };

            try {
                history.executeCommand(equationCommand);
            } catch (Exception ex) {
                metricsPanel.updateStatus("Error solving equation: " + ex.getMessage());
            }
        });

        // Projection
        controlPanel.getBtnProjection().setOnAction(e -> {
            String w1 = controlPanel.getProjWord1Field().getText().trim().toLowerCase();
            String w2 = controlPanel.getProjWord2Field().getText().trim().toLowerCase();

            if (w1.isEmpty() || w2.isEmpty()) {
                metricsPanel.updateStatus("Error: Please enter two words for projection.");
                return;
            }

            try {
                history.executeCommand(new CustomProjectionCommand(w1, w2, space3D));
                metricsPanel.updateStatus("Projected space on axis: " + w1 + " -> " + w2);
            } catch (Exception ex) {
                metricsPanel.updateStatus("Error in projection: " + ex.getMessage());
            }
        });

        // Distance
        controlPanel.getBtnDistance().setOnAction(e -> {
            String w1 = controlPanel.getProjWord1Field().getText().trim().toLowerCase();
            String w2 = controlPanel.getProjWord2Field().getText().trim().toLowerCase();

            if (w1.isEmpty() || w2.isEmpty()) {
                metricsPanel.updateStatus("Error: Please enter two words to calculate distance.");
                return;
            }

            try {
                Optional<WordVector> v1 = AppState.getInstance().getFullVector(w1);
                Optional<WordVector> v2 = AppState.getInstance().getFullVector(w2);

                if (v1.isPresent() && v2.isPresent()) {
                    double dist = getSelectedDistanceStrategy().calculate(v1.get().coordinates(), v2.get().coordinates());
                    metricsPanel.updateStatus(getMetricName() + " between '" + w1 + "' and '" + w2 + "' is: " + String.format("%.4f", dist));
                    space3D.drawDistanceLineAndDim(w1, w2);
                } else {
                    metricsPanel.updateStatus("Error: One or both words not found in dictionary.");
                }
            } catch (Exception ex) {
                metricsPanel.updateStatus("Error calculating distance: " + ex.getMessage());
            }
        });

        // 2D / 3D
        controlPanel.getBtn2D().setOnAction(e -> {
            space3D.set2DMode(true);
            metricsPanel.updateStatus("Switched to 2D Mode");
        });

        controlPanel.getBtn3D().setOnAction(e -> {
            space3D.set2DMode(false);
            metricsPanel.updateStatus("Switched to 3D Mode");
        });

        // Undo
        controlPanel.getBtnUndo().setOnAction(e -> {
            history.undo();
            metricsPanel.updateStatus("Undo performed.");
        });
        // Redo
        controlPanel.getBtnRedo().setOnAction(e -> {
            history.redo();
            metricsPanel.updateStatus("Redo executed.");
        });
    }
}