package io;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import exceptions.DataLoadException;
import models.VectorType;
import models.WordVector;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JsonDataLoader implements DataLoader {
    private final Gson gson;

    public JsonDataLoader() {
        this.gson = new Gson();
    }

    private static class JsonItem {
        String word;
        double[] vector;
    }

    @Override
    public List<WordVector> loadVectors(String filePath, VectorType type) throws DataLoadException {
        List<WordVector> vectors = new ArrayList<>();
        Type listType = new TypeToken<List<JsonItem>>(){}.getType();

        try (InputStream is = new FileInputStream(filePath);
             InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {

            List<JsonItem> items = gson.fromJson(reader, listType);

            if (items != null) {
                for (JsonItem item : items) {
                    if (item.word != null && item.vector != null) {
                        vectors.add(new WordVector(item.word, item.vector, type));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("CRITICAL ERROR loading " + filePath);
            e.printStackTrace();
            throw new DataLoadException("Error loading JSON from " + filePath, e);
        }
        return vectors;
    }
}