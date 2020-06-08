package model;

import data.ProductionParameters;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;

public abstract class Model {
    protected final MultiLayerNetwork model;
    protected final ModelInputConfiguration inputConfiguration;

    protected Model(MultiLayerNetwork model, ModelInputConfiguration inputConfiguration) {
        this.model = model;
        this.inputConfiguration = inputConfiguration;
    }


    float evaluate(ProductionParameters parameters) {
        return model.output(prepareInput(parameters)).getFloat(0);
    }

    abstract INDArray prepareInput(ProductionParameters parameters);
}
