package model;

import data.ProductionParameters;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;

public class RmModel extends Model {
    protected RmModel(MultiLayerNetwork model, ModelInputConfiguration inputConfiguration) {
        super(model, inputConfiguration);
    }

    @Override
    INDArray prepareInput(ProductionParameters parameters) {
        return null;
    }
}
