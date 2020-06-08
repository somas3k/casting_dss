package model;

import data.ProductionParameters;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;

public class A5Model extends Model {

    protected A5Model(MultiLayerNetwork model, ModelInputConfiguration inputConfiguration) {
        super(model, inputConfiguration);
    }

    @Override
    public float evaluate(ProductionParameters parameters) {
        return 0;
    }

    @Override
    INDArray prepareInput(ProductionParameters parameters) {
        return null;
    }
}
