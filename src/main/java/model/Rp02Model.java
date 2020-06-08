package model;

import data.ProductionParameters;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;

public class Rp02Model extends Model {

    protected Rp02Model(MultiLayerNetwork model, ModelInputConfiguration inputConfiguration) {
        super(model, inputConfiguration);
    }

    @Override
    INDArray prepareInput(ProductionParameters parameters) {
        return null;
    }
}
