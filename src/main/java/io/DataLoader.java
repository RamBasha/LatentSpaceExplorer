package io;

import exceptions.DataLoadException;
import models.VectorType;
import models.WordVector;
import java.util.List;

public interface DataLoader {
    List<WordVector> loadVectors(String filePath, VectorType type) throws DataLoadException;
}