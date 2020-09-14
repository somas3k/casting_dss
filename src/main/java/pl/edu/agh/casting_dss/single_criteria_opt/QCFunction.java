package pl.edu.agh.casting_dss.single_criteria_opt;

import pl.edu.agh.casting_dss.criterions.WeightedNormalizedScalar;
import pl.edu.agh.casting_dss.data.ProductionParameters;
import lombok.AllArgsConstructor;
import pl.edu.agh.casting_dss.model.MechanicalPropertiesModel;
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