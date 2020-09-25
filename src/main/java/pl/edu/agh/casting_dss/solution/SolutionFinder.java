package pl.edu.agh.casting_dss.solution;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;
import org.jamesframework.core.problems.GenericProblem;
import org.jamesframework.core.search.algo.tabu.FullTabuMemory;
import org.jamesframework.core.search.algo.tabu.TabuSearch;
import org.jamesframework.core.search.listeners.SearchListener;
import org.jamesframework.core.search.stopcriteria.MaxRuntime;
import pl.edu.agh.casting_dss.criterions.CostFunction;
import pl.edu.agh.casting_dss.criterions.QualityFunction;
import pl.edu.agh.casting_dss.criterions.WeightedNormalizedScalar;
import pl.edu.agh.casting_dss.data.ProductionParameters;
import pl.edu.agh.casting_dss.gui.model.DSSModel;
import pl.edu.agh.casting_dss.model.MechanicalPropertiesModel;
import pl.edu.agh.casting_dss.single_criteria_opt.*;
import pl.edu.agh.casting_dss.utils.CostUtils;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Setter
@Getter
public class SolutionFinder {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
    private final ADISolutionGenerator generator;
    private int thickness;
    private NormConstraints constraints;
    private CostFunction costFunction = new CostFunction(14.0f, 250.0f, 45f, 30f, 15f);
    private QualityFunction qualityFunction = new QualityFunction();
    private MechanicalPropertiesModel model;

    public SolutionFinder(int thickness, NormConstraints constraints, MechanicalPropertiesModel model) {
        this.constraints = constraints;
        this.thickness = thickness;
        this.generator = new ADISolutionGenerator(thickness, constraints);
        this.model = model;
    }

    public ProductionParameters findRandomSolution() {
        return generator.create(new Random(), model).getProductionParameters();
    }

    public void findSolution(DSSModel dssModel, SearchListener<OptimizedADISolution> listener) {
        Pair<Double, Double> minMaxCost = CostUtils.getMinMaxCost(costFunction, model.getPossibleValues(), thickness);

        QCFunction qcFun = new QCFunction(
                new WeightedNormalizedScalar(
                        costFunction,
                        (1.0 - dssModel.getCostQualityProportion()) * 100,
                        minMaxCost.getLeft(),
                        minMaxCost.getRight()),
                new WeightedNormalizedScalar(
                        qualityFunction,
                        dssModel.getCostQualityProportion() * 100,
                        0.0,
                        0.0
                )
        );

        GenericProblem<OptimizedADISolution, MechanicalPropertiesModel> problem = new GenericProblem<>(model, qcFun, generator);
        problem.addMandatoryConstraint(constraints);
        TabuSearch<OptimizedADISolution> search = new TabuSearch<>(problem, new ProdParamsNeighborhood(model.getPossibleValues()), new FullTabuMemory<>(200));
        search.addStopCriterion(new MaxRuntime(20, TimeUnit.SECONDS));
//        Search<OptimizedADISolution> search = new RandomSearch<>(problem);
//        search.addStopCriterion(new MaxSteps(10000));
        search.addSearchListener(listener);
        EXECUTOR_SERVICE.execute(() -> {
            search.start();
            OptimizedADISolution bestSolution = search.getBestSolution();
            System.out.println(bestSolution);
            System.out.println(costFunction.evaluate(bestSolution.getProductionParameters()));
            search.dispose();
        });
    }
}
