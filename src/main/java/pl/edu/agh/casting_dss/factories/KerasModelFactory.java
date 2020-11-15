package pl.edu.agh.casting_dss.factories;

import pl.edu.agh.casting_dss.model.Model;
import pl.edu.agh.casting_dss.model.keras.KerasModel;
import pl.edu.agh.casting_dss.model.ModelInputConfiguration;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

import java.io.File;
import java.io.IOException;

public class KerasModelFactory {
    public static final KerasModelFactory KERAS_MODEL_FACTORY = new KerasModelFactory();
    private final ModelInputConfFactory confFactory = new ModelInputConfFactory();

    public Model getKerasModel(File modelPath, File modelInputConfiguration) throws ModelLoadingException {
        try {
            MultiLayerNetwork model = KerasModelImport.
                    importKerasSequentialModelAndWeights(modelPath.getPath());
            ModelInputConfiguration conf = confFactory.getConfiguration(modelInputConfiguration);
            return new KerasModel(model, conf);
        } catch (IOException e) {
            throw new ModelLoadingException("Wrong path", e);
        } catch (InvalidKerasConfigurationException e) {
            throw new ModelLoadingException("Problem with Keras", e);
        } catch (UnsupportedKerasConfigurationException e) {
            throw new ModelLoadingException("Not compatible with Keras", e);
        }
    }
}
