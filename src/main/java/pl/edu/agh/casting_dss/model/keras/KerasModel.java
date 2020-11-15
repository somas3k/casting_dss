package pl.edu.agh.casting_dss.model.keras;

import org.nd4j.linalg.factory.Nd4j;
import pl.edu.agh.casting_dss.data.ProductionParameters;
import pl.edu.agh.casting_dss.model.Model;
import pl.edu.agh.casting_dss.model.ModelInputConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import pl.edu.agh.casting_dss.utils.MinMaxScaler;

import java.util.Map;
import java.util.stream.Collectors;

public class KerasModel extends Model {
    protected final MultiLayerNetwork model;

    public KerasModel(MultiLayerNetwork model, ModelInputConfiguration inputConfiguration) {
        super(inputConfiguration);
        this.model = model;
    }

    @Override
    public double evaluate(ProductionParameters parameters) {
        return model.output(prepareInput(parameters)).getFloat(0);
    }

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
