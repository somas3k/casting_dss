package single_criteria_opt;

import criterions.WeightedNormalizedScalar;
import data.ProductionParameters;
import lombok.AllArgsConstructor;
import model.MechanicalPropertiesModel;
import org.jamesframework.core.problems.objectives.Objective;
import org.jamesframework.core.problems.objectives.evaluations.Evaluation;
import org.jamesframework.core.problems.objectives.evaluations.SimpleEvaluation;

@AllArgsConstructor
public class QCFunction implements Objective<OptimizedADISolution, MechanicalPropertiesModel> {
    private final WeightedNormalizedScalar cost;
    private final WeightedNormalizedScalar quality;

    @Override
    public Evaluation evaluate(OptimizedADISolution optimizedADISolution, MechanicalPropertiesModel model) {
        ProductionParameters parameters = optimizedADISolution.getProductionParameters();
//        return SimpleEvaluation.WITH_VALUE(cost.evaluate(parameters) + quality.evaluate(parameters));
        return SimpleEvaluation.WITH_VALUE(cost.evaluate(parameters));
    }


    @Override
    public boolean isMinimizing() {
        return true;
    }
}
