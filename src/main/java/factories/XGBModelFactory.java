package factories;

import ml.dmlc.xgboost4j.java.Booster;
import ml.dmlc.xgboost4j.java.XGBoost;
import ml.dmlc.xgboost4j.java.XGBoostError;
import model.Model;
import model.ModelInputConfiguration;
import model.xgboost.HBBoostModel;

import java.io.File;
import java.io.IOException;

public class XGBModelFactory {
    public static final XGBModelFactory XGB_MODEL_FACTORY = new XGBModelFactory();
    private final ModelInputConfFactory confFactory = new ModelInputConfFactory();

    public Model getHBModel(File binFile, File confJsonPath) throws ModelLoadingException {
        try {
            Booster model = XGBoost.loadModel(binFile.getPath());
            ModelInputConfiguration conf = confFactory.getConfiguration(confJsonPath);
            return new HBBoostModel(model, conf);

        } catch (XGBoostError xgBoostError) {
            throw new ModelLoadingException("Problem with XGBoost", xgBoostError);
        } catch (IOException e) {
            throw new ModelLoadingException("Wrong path", e);
        }
    }
}
