package single_criteria_opt;

import criterions.CostFunction;
import criterions.QualityFunction;
import model.MechanicalPropertiesModel;
import data.ProductionParameters;
import org.apache.commons.lang3.tuple.Pair;
import org.jamesframework.core.problems.objectives.Objective;
import org.jamesframework.core.problems.objectives.evaluations.Evaluation;
import org.jamesframework.core.problems.objectives.evaluations.SimpleEvaluation;

public class QCFunction implements Objective<OptimizedADISolution, MechanicalPropertiesModel> {
    private final Pair<CostFunction, Double> costWeight;
    private final Pair<QualityFunction, Double> qualityWeight;

    public QCFunction(Pair<CostFunction, Double> costWeight, Pair<QualityFunction, Double> qualityWeight) {
        this.costWeight = costWeight;
        this.qualityWeight = qualityWeight;
    }

    @Override
    public Evaluation evaluate(OptimizedADISolution optimizedADISolution, MechanicalPropertiesModel model) {
        ProductionParameters parameters = optimizedADISolution.getProductionParameters();
        return SimpleEvaluation.WITH_VALUE(costWeight.getLeft().evaluate(parameters) * costWeight.getRight() +
                qualityWeight.getLeft().evaluate(parameters) * qualityWeight.getRight());
    }


    @Override
    public boolean isMinimizing() {
        return true;
    }
}
