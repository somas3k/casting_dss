package pl.edu.agh.casting_dss.model.keras;

import pl.edu.agh.casting_dss.data.ProductionParameters;
import pl.edu.agh.casting_dss.model.ModelInputConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;

public class KKerasModel extends KerasModel {


    protected KKerasModel(MultiLayerNetwork model, ModelInputConfiguration inputConfiguration) {
        super(model, inputConfiguration);
    }

    @Override
    INDArray prepareInput(ProductionParameters parameters) {
        return null;
    }
}
