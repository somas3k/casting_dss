package model.xgboost;

import data.ProductionParameters;
import ml.dmlc.xgboost4j.java.Booster;
import ml.dmlc.xgboost4j.java.DMatrix;
import ml.dmlc.xgboost4j.java.XGBoostError;
import model.Model;
import model.ModelInputConfiguration;

public abstract class XGBModel extends Model {
    private final Booster model;

    public XGBModel(Booster model, ModelInputConfiguration inputConfiguration) {
        super(inputConfiguration);
        this.model = model;
    }

    @Override
    public double evaluate(ProductionParameters parameters) throws XGBoostError {
        return model.predict(prepareInput(parameters))[0][0];
    }

    abstract DMatrix prepareInput(ProductionParameters parameters) throws XGBoostError;
}
