package pl.edu.agh.casting_dss.model.xgboost;

import pl.edu.agh.casting_dss.data.ProductionParameters;
import ml.dmlc.xgboost4j.java.Booster;
import ml.dmlc.xgboost4j.java.DMatrix;
import ml.dmlc.xgboost4j.java.XGBoostError;
import pl.edu.agh.casting_dss.model.ModelInputConfiguration;
import org.apache.commons.lang3.ArrayUtils;
import pl.edu.agh.casting_dss.utils.MinMaxScaler;

import java.util.Map;

public class HBBoostModel extends XGBModel {

    public HBBoostModel(Booster model, ModelInputConfiguration inputConfiguration) {
        super(model, inputConfiguration);
    }

    @Override
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