package pl.edu.agh.casting_dss.solution;

import org.apache.commons.lang3.tuple.Pair;
import org.jamesframework.core.problems.GenericProblem;
import org.jamesframework.core.search.Search;
import org.jamesframework.core.search.algo.RandomSearch;
import org.jamesframework.core.search.listeners.SearchListener;
import org.jamesframework.core.search.status.SearchStatus;
import org.jamesframework.core.search.stopcriteria.MaxSteps;
import org.nd4j.linalg.api.ops.Op;
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
import pl.edu.agh.casting_dss.utils.SystemConfigurationUtils;

import java.io.File;
import java.io.IOException;

import static pl.edu.agh.casting_dss.factories.PossibleValuesFactory.POSSIBLE_VALUES_FACTORY;
import static pl.edu.agh.casting_dss.factories.XGBModelFactory.XGB_MODEL_FACTORY;
import static pl.edu.agh.casting_dss.utils.MechanicalProperty.HB;

public class SolutionFinder {

    public static void findSolution(DSSModel dssModel, SystemConfiguration configuration, SearchListener<OptimizedADISolution> listener) throws IOException, InterruptedException, ModelLoadingException {
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

        ADISolutionGenerator generator = new ADISolutionGenerator(dssModel.getThickness());
        GenericProblem<OptimizedADISolution, MechanicalPropertiesModel> problem = new GenericProblem<>(model, qcFun, generator);
        problem.addMandatoryConstraint(new NormConstraints(280, 320));
        Search<OptimizedADISolution> search = new RandomSearch<>(problem);
        search.addStopCriterion(new MaxSteps(2000));
        search.addSearchListener(listener);
        search.start();
        search.wait();
        OptimizedADISolution bestSolution = search.getBestSolution();
        System.out.println(bestSolution);
        System.out.println(costFunction.evaluate(bestSolution.getProductionParameters()));
    }
}
