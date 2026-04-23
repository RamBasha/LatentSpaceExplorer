package exceptions;

public class WordNotFoundException extends LatentSpaceException {
    public WordNotFoundException(String word) {
        super("The word '" + word + "' was not found in the latent space.");
    }
}