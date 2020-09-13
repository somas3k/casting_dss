package model.keras;

import data.ProductionParameters;
import model.Model;
import model.ModelInputConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;

public abstract class KerasModel extends Model {
    protected final MultiLayerNetwork model;

    protected KerasModel(MultiLayerNetwork model, ModelInputConfiguration inputConfiguration) {
        super(inputConfiguration);
        this.model = model;
    }

    @Override
    public double evaluate(ProductionParameters parameters) {
        return model.output(prepareInput(parameters)).getFloat(0);
    }

    abstract INDArray prepareInput(ProductionParameters parameters);
}
