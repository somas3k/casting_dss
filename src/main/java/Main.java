import criterions.CostFunction;
import criterions.QualityFunction;
import criterions.WeightedNormalizedScalar;
import data.PossibleValues;
import factories.ModelLoadingException;
import model.MechanicalPropertiesModel;
import model.Model;
import org.apache.commons.lang3.tuple.Pair;
import org.jamesframework.core.problems.GenericProblem;
import org.jamesframework.core.problems.Problem;
import org.jamesframework.core.search.Search;
import org.jamesframework.core.search.algo.RandomSearch;
import org.jamesframework.core.search.status.SearchStatus;
import org.jamesframework.core.search.stopcriteria.MaxSteps;
import single_criteria_opt.ADISolutionGenerator;
import single_criteria_opt.NormConstraints;
import single_criteria_opt.OptimizedADISolution;
import single_criteria_opt.QCFunction;
import utils.CostUtils;

import java.io.File;
import java.io.IOException;

import static factories.PossibleValuesFactory.POSSIBLE_VALUES_FACTORY;
import static factories.XGBModelFactory.XGB_MODEL_FACTORY;

public class Main {

    public static void main(String[] args) throws ModelLoadingException, IOException, InterruptedException {
        String modelPath = "/home/somas3k/Dokumenty/mag/src/magisterka/hb_xgb.bin";
        String inputConfPath = "/home/somas3k/Dokumenty/mag/src/magisterka/hb_model_conf.json";
        String possibleValuesPath = "/home/somas3k/Dokumenty/mag/src/magisterka/possible_values.json";
        int thickness = 30;

        PossibleValues possibleValues = POSSIBLE_VALUES_FACTORY.getFromFile(new File(possibleValuesPath));
        Model hbModel = XGB_MODEL_FACTORY.getHBModel(new File(modelPath), new File(inputConfPath));
        MechanicalPropertiesModel model = new MechanicalPropertiesModel(possibleValues, hbModel);
        CostFunction costFunction = new CostFunction(14.0f, 250.0f, 45f, 30f, 15f);
        Pair<Double, Double> minMaxCost = CostUtils.getMinMaxCost(costFunction, possibleValues, thickness);
        QualityFunction qualityFunction = new QualityFunction();

        QCFunction qcFun = new QCFunction(
                new WeightedNormalizedScalar(
                        costFunction,
                        80,
                        minMaxCost.getLeft(),
                        minMaxCost.getRight()),
                new WeightedNormalizedScalar(
                        qualityFunction,
                        20,
                        0.0,
                        0.0
                )
        );

        ADISolutionGenerator generator = new ADISolutionGenerator(thickness);
        GenericProblem<OptimizedADISolution, MechanicalPropertiesModel> problem = new GenericProblem<>(model, qcFun, generator);
        problem.addMandatoryConstraint(new NormConstraints(280, 320));
        Search<OptimizedADISolution> search = new RandomSearch<>(problem);
        search.addStopCriterion(new MaxSteps(2000));
        search.start();
        while (search.getStatus().equals(SearchStatus.RUNNING)) {
            Thread.sleep(1000);
        }
        OptimizedADISolution bestSolution = search.getBestSolution();
        System.out.println(bestSolution);
        System.out.println(costFunction.evaluate(bestSolution.getProductionParameters()));
    }
}
