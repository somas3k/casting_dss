package model.keras;

import data.ProductionParameters;
import model.ModelInputConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import utils.MinMaxScaler;

import java.util.Map;
import java.util.stream.Collectors;

public class HBKerasModel extends KerasModel {

    public HBKerasModel(MultiLayerNetwork model, ModelInputConfiguration inputConfiguration) {
        super(model, inputConfiguration);
    }

    @Override
    INDArray prepareInput(ProductionParameters parameters) {
        Map<String, Double> scaled = MinMaxScaler.scale(parameters,
                inputConfiguration.getMinParams(),
                inputConfiguration.getMaxParams(),
                inputConfiguration.getOffset()
        );

        return Nd4j.create(
                inputConfiguration.getModelInput().stream()
                        .map(scaled::get)
                        .collect(Collectors.toList())
        );
    }
}
