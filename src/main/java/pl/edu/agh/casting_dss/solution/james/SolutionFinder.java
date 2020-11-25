package pl.edu.agh.casting_dss.solution.james;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;
import org.jamesframework.core.problems.GenericProblem;
import org.jamesframework.core.search.Search;
import org.jamesframework.core.search.listeners.SearchListener;
import org.jamesframework.core.search.stopcriteria.MaxRuntime;
import pl.edu.agh.casting_dss.criterions.CostFunction;
import pl.edu.agh.casting_dss.criterions.QualityFunction;
import pl.edu.agh.casting_dss.criterions.WeightedNormalizedScalar;
import pl.edu.agh.casting_dss.data.ProductionParameters;
import pl.edu.agh.casting_dss.gui.model.DSSModel;
import pl.edu.agh.casting_dss.model.MechanicalPropertiesModel;
import pl.edu.agh.casting_dss.single_criteria_opt.ADISolutionGenerator;
import pl.edu.agh.casting_dss.single_criteria_opt.QCFunction;
import pl.edu.agh.casting_dss.utils.CostUtils;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static pl.edu.agh.casting_dss.Main.EXECUTOR_SERVICE;

@Setter
@Getter
public class SolutionFinder {
    private final ADISolutionGenerator generator;
    private CostFunction costFunction;
    private QualityFunction qualityFunction;
    private MechanicalPropertiesModel model;

    public SolutionFinder(ADISolutionGenerator generator, MechanicalPropertiesModel model, CostFunction cFunction, QualityFunction qFunction) {
        this.generator = generator;
        this.model = model;
        this.costFunction = cFunction;
        this.qualityFunction = qFunction;
    }

    public ProductionParameters findRandomSolution(Random random) {
        return generator.create(random, model).getProductionParameters();
    }

    public Search<OptimizedADISolution> findSolution(DSSModel dssModel, SearchListener<OptimizedADISolution> listener) {
        Pair<Double, Double> minMaxCost = CostUtils.getMinMaxCost(costFunction, model.getPossibleValues(), dssModel.getThickness());

        QCFunction qcFun = new QCFunction(
                new WeightedNormalizedScalar(
                        costFunction,
                        (1.0 - dssModel.getCostQualityProportion()) * 100,
                        minMaxCost.getLeft(),
                        minMaxCost.getRight(),
                        false),
                new WeightedNormalizedScalar(
                        qualityFunction,
                        dssModel.getCostQualityProportion() * 100,
                        qualityFunction.getMinValue(),
                        qualityFunction.getMaxValue(),
                        true
                )
        );

        GenericProblem<OptimizedADISolution, MechanicalPropertiesModel> problem = new GenericProblem<>(model, qcFun, generator);
        problem.addMandatoryConstraint(dssModel.calculateMatchingNorm());
        Search<OptimizedADISolution> search = dssModel.getAlgoFactory().getSearchAlgorithm(problem, dssModel.getSearchType(), model.getPossibleValues(), new OptimizedADISolution(dssModel.getActualSolutionParameters()));
        search.setRandom(dssModel.getRandom());
        search.addStopCriterion(new MaxRuntime(dssModel.getMaxRuntime().get(), TimeUnit.SECONDS));
        search.addSearchListener(listener);
        EXECUTOR_SERVICE.execute(() -> {
            search.start();
            search.dispose();
        });
        return search;
    }
    public Search<OptimizedADISolution> findSolutionSynchronously(DSSModel dssModel, SearchListener<OptimizedADISolution> listener) {
        Pair<Double, Double> minMaxCost = CostUtils.getMinMaxCost(costFunction, model.getPossibleValues(), dssModel.getThickness());

        QCFunction qcFun = new QCFunction(
                new WeightedNormalizedScalar(
                        costFunction,
                        (1.0 - dssModel.getCostQualityProportion()) * 100,
                        minMaxCost.getLeft(),
                        minMaxCost.getRight(),
                        false),
                new WeightedNormalizedScalar(
                        qualityFunction,
                        dssModel.getCostQualityProportion() * 100,
                        qualityFunction.getMinValue(),
                        qualityFunction.getMaxValue(),
                        true
                )
        );

        GenericProblem<OptimizedADISolution, MechanicalPropertiesModel> problem = new GenericProblem<>(model, qcFun, generator);
        problem.addMandatoryConstraint(dssModel.calculateMatchingNorm());
        Search<OptimizedADISolution> search = dssModel.getAlgoFactory().getSearchAlgorithm(problem, dssModel.getSearchType(), model.getPossibleValues(), new OptimizedADISolution(dssModel.getActualSolutionParameters()));
        search.setRandom(dssModel.getRandom());
        search.addStopCriterion(new MaxRuntime(dssModel.getMaxRuntime().get(), TimeUnit.SECONDS));
        search.addSearchListener(listener);

        search.start();
        search.dispose();
        return search;
    }
}
