package pl.edu.agh.casting_dss.solution;

import org.apache.commons.lang3.tuple.Pair;
import org.jamesframework.core.problems.GenericProblem;
import org.jamesframework.core.search.Search;
import org.jamesframework.core.search.algo.RandomSearch;
import org.jamesframework.core.search.listeners.SearchListener;
import org.jamesframework.core.search.stopcriteria.MaxSteps;
import pl.edu.agh.casting_dss.criterions.CostFunction;
import pl.edu.agh.casting_dss.criterions.QualityFunction;
import pl.edu.agh.casting_dss.criterions.WeightedNormalizedScalar;
import pl.edu.agh.casting_dss.data.PossibleValues;
import pl.edu.agh.casting_dss.factories.ModelLoadingException;
import pl.edu.agh.casting_dss.gui.model.DSSModel;
import pl.edu.agh.casting_dss.model.MechanicalPropertiesModel;
import pl.edu.agh.casting_dss.model.Model;
import pl.edu.agh.casting_dss.single_criteria_opt.ADISolutionGenerator;
import pl.edu.agh.casting_dss.single_criteria_opt.NormConstraints;
import pl.edu.agh.casting_dss.single_criteria_opt.OptimizedADISolution;
import pl.edu.agh.casting_dss.single_criteria_opt.QCFunction;
import pl.edu.agh.casting_dss.utils.CostUtils;
import pl.edu.agh.casting_dss.utils.SystemConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static pl.edu.agh.casting_dss.factories.PossibleValuesFactory.POSSIBLE_VALUES_FACTORY;
import static pl.edu.agh.casting_dss.factories.XGBModelFactory.XGB_MODEL_FACTORY;
import static pl.edu.agh.casting_dss.utils.MechanicalProperty.HB;

public class SolutionFinder {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
    public static void findSolution(DSSModel dssModel, SystemConfiguration configuration, SearchListener<OptimizedADISolution> listener) throws IOException, ModelLoadingException {
        PossibleValues possibleValues = POSSIBLE_VALUES_FACTORY.getFromFile(new File(configuration.getPossibleValuesPath()));
        Model hbModel = XGB_MODEL_FACTORY.getHBModel(
                new File(configuration.getModelPath(HB)),
                new File(configuration.getModelInputConfigurationPath(HB)));
        MechanicalPropertiesModel model = new MechanicalPropertiesModel(possibleValues, hbModel);
        CostFunction costFunction = new CostFunction(14.0f, 250.0f, 45f, 30f, 15f);
        Pair<Double, Double> minMaxCost = CostUtils.getMinMaxCost(costFunction, possibleValues, dssModel.getThickness());
        QualityFunction qualityFunction = new QualityFunction();

        QCFunction qcFun = new QCFunction(
                new WeightedNormalizedScalar(
                        costFunction,
                        dssModel.getCostQualityProportion() * 100,
                        minMaxCost.getLeft(),
                        minMaxCost.getRight()),
                new WeightedNormalizedScalar(
                        qualityFunction,
                        (1.0 - dssModel.getCostQualityProportion()) * 100,
                        0.0,
                        0.0
                )
        );

        ADISolutionGenerator generator = new ADISolutionGenerator(dssModel.getThickness());
        GenericProblem<OptimizedADISolution, MechanicalPropertiesModel> problem = new GenericProblem<>(model, qcFun, generator);
        problem.addMandatoryConstraint(new NormConstraints(280, 320));
        Search<OptimizedADISolution> search = new RandomSearch<>(problem);
        search.addStopCriterion(new MaxSteps(10000));
        search.addSearchListener(listener);
        EXECUTOR_SERVICE.execute(() -> {
            search.start();
            OptimizedADISolution bestSolution = search.getBestSolution();
            System.out.println(bestSolution);
            System.out.println(costFunction.evaluate(bestSolution.getProductionParameters()));
        });
    }
}
