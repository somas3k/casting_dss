package pl.edu.agh.casting_dss.single_criteria_opt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jamesframework.core.problems.objectives.evaluations.Evaluation;

@AllArgsConstructor
@Getter
public class DetailedEvaluation implements Evaluation {
    private final double value;
    private final double costValue;
    private final double qualityValue;

    @Override
    public double getValue() {
        return value;
    }
}
