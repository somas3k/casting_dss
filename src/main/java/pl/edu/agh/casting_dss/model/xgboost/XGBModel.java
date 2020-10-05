package pl.edu.agh.casting_dss.model.xgboost;

import org.apache.commons.lang3.ArrayUtils;
import pl.edu.agh.casting_dss.data.ProductionParameters;
import ml.dmlc.xgboost4j.java.Booster;
import ml.dmlc.xgboost4j.java.DMatrix;
import ml.dmlc.xgboost4j.java.XGBoostError;
import pl.edu.agh.casting_dss.model.Model;
import pl.edu.agh.casting_dss.model.ModelInputConfiguration;
import pl.edu.agh.casting_dss.utils.MinMaxScaler;

import java.util.Map;

public class XGBModel extends Model {
    private final Booster model;

    public XGBModel(Booster model, ModelInputConfiguration inputConfiguration) {
        super(inputConfiguration);
        this.model = model;
    }

    @Override
    public double evaluate(ProductionParameters parameters) throws XGBoostError {
        return model.predict(prepareInput(parameters))[0][0];
    }

    DMatrix prepareInput(ProductionParameters parameters) throws XGBoostError {
        Map<String, Double> scaled = MinMaxScaler.scale(parameters,
                inputConfiguration.getMinParams(),
                inputConfiguration.getMaxParams(),
                inputConfiguration.getOffset()
        );

        Float[] input = inputConfiguration.getModelInput().stream()
                .map(param -> scaled.get(param).floatValue()).toArray(Float[]::new);
        return new DMatrix(ArrayUtils.toPrimitive(input), 1, input.length);
    }
}
