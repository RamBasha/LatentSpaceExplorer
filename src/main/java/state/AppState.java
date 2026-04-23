package state;

import models.VectorSpaceBounds;
import models.WordVector;
import java.util.*;

public class AppState {
    private static AppState instance;
    private final Map<String, WordVector> fullVectors = new HashMap<>();
    private final Map<String, WordVector> pcaVectors = new HashMap<>();
    private final VectorSpaceBounds pcaBounds = new VectorSpaceBounds();

    private int xAxisIndex = 0;
    private int yAxisIndex = 1;
    private int zAxisIndex = 2;

    public interface StateListener {
        void onAxisChanged();
    }
    private final List<StateListener> listeners = new ArrayList<>();

    private AppState() {}

    public static synchronized AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }
        return instance;
    }

    public void addListener(StateListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (StateListener listener : listeners) {
            listener.onAxisChanged();
        }
    }

    public void initializeData(List<WordVector> full, List<WordVector> pca) {
        fullVectors.clear();
        pcaVectors.clear();
        full.forEach(v -> fullVectors.put(v.word().toLowerCase(), v));
        pca.forEach(v -> {
            pcaVectors.put(v.word().toLowerCase(), v);
            pcaBounds.updateBounds(v.coordinates());
        });
    }

    public Optional<WordVector> getFullVector(String word) {
        return Optional.ofNullable(fullVectors.get(word.toLowerCase()));
    }

    public Collection<WordVector> getAllPcaVectors() {
        return pcaVectors.values();
    }

    public Collection<WordVector> getAllFullVectors() {
        return fullVectors.values();
    }

    public VectorSpaceBounds getPcaBounds() { return pcaBounds; }

    public int getXAxisIndex() { return xAxisIndex; }
    public void setXAxisIndex(int xAxisIndex) {
        this.xAxisIndex = xAxisIndex;
        notifyListeners();
    }

    public int getYAxisIndex() { return yAxisIndex; }
    public void setYAxisIndex(int yAxisIndex) {
        this.yAxisIndex = yAxisIndex;
        notifyListeners();
    }

    public int getZAxisIndex() { return zAxisIndex; }
    public void setZAxisIndex(int zAxisIndex) {
        this.zAxisIndex = zAxisIndex;
        notifyListeners();
    }
}