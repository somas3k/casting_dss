package pl.edu.agh.casting_dss.factories;

public class ModelLoadingException extends Exception {

    public ModelLoadingException(String message) {
        super(message);
    }

    public ModelLoadingException(String s, Throwable throwable) {
        super(s, throwable);
    }

}
