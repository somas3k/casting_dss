package pl.edu.agh.casting_dss.single_criteria_opt;

import lombok.AllArgsConstructor;
import org.jamesframework.core.problems.objectives.Objective;
import pl.edu.agh.casting_dss.criterions.WeightedNormalizedScalar;
import pl.edu.agh.casting_dss.data.ProductionParameters;
import pl.edu.agh.casting_dss.model.MechanicalPropertiesModel;
import pl.edu.agh.casting_dss.solution.james.OptimizedADISolution;

@AllArgsConstructor
public class QCFunction implements Objective<OptimizedADISolution, MechanicalPropertiesModel> {
    private final WeightedNormalizedScalar cost;
    private final WeightedNormalizedScalar quality;
    public static Integer EVALUATIONS = 0;

    @Override
    public DetailedEvaluation evaluate(OptimizedADISolution optimizedADISolution, MechanicalPropertiesModel model) {
        EVALUATIONS++;
        ProductionParameters parameters = optimizedADISolution.getProductionParameters();
        return new DetailedEvaluation(cost.evaluate(parameters) + quality.evaluate(parameters), cost.evaluateFromFunction(parameters), quality.evaluateFromFunction(parameters));
    }


    @Override
    public boolean isMinimizing() {
        return true;
    }
}
