# LatentSpace Explorer 🌌
**A system for visual exploration of latent representations (Word Embeddings)**

Final project in the Object-Oriented Programming (OOP) course, Winter 2026.

---

## ⚠️ Important Note Before Running
**Upon launching the system, a Python script runs in the background. On the first run, the script downloads the language model (GloVe) and performs heavy PCA calculations. This process takes time (may take a few minutes). Please wait patiently until the points appear in the 3D space on the screen.**

---

## 📖 About the Project
This system allows visual and mathematical exploration of a latent word space (Latent Space). The system converts words into vectors in a multi-dimensional space, allowing the user to understand the semantic connections between them through graphical interaction (3D/2D) and performing algebraic operations on vectors.

The project is built in a hybrid architecture:
* **Computational Engine (Python):** Responsible for loading the `GloVe` model (100 dimensions) and performing dimensionality reduction (PCA) to 50 dimensions for display and efficiency. The data is exported to JSON files.
* **Interface and Management (Java + JavaFX):** Reads the data, displays it in a rich graphical interface, and manages the logic, state, and user action history.

## ✨ Key Features
* **Flexible Display (2D / 3D):** Smooth transition between an interactive 3D display and a 2D display.
* **Axis Control:** Ability to change in real-time which of the 50 PCA components will be displayed on the X, Y, and Z axes.
* **K-Nearest Neighbors (KNN):** Searching for a word and displaying the K closest words to it (visually and mathematically).
* **Centroid:** Calculating the center point of a comma-separated group of words.
* **Semantic Algebra (Equations):** Solving vector equations such as `king - man + woman = queen` and displaying the path in space.
* **Custom Projection:** Changing the display space based on an axis defined between two selected words.
* **Distance Between Words (Distance):** Calculating the exact distance between two words, dimming the rest of the space, and drawing a visual line between them.
* **Undo / Redo Mechanism:** Going back and forth between all actions performed in the system.

## 🛠️ Installation and Running Requirements

### 1. Prerequisites
* **Java:** Version 21 and above (JDK 23 recommended according to `pom.xml`).
* **Python:** Version 3.11 or 3.12.
* **Maven:** Installed and configured in the workspace (can use the included Wrapper).

### 2. Installing Python Libraries (Dependencies)
Before running the project, open a terminal (Terminal/CMD) in the project directory and run the following command to install the required libraries for Python:
```bash
python -m pip install gensim scikit-learn numpy
