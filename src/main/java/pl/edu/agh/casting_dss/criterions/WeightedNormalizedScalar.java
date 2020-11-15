package pl.edu.agh.casting_dss.criterions;

import pl.edu.agh.casting_dss.data.ProductionParameters;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WeightedNormalizedScalar implements Function {
    private final Function fun;
    private final double weight;
    private final double minValue;
    private final double maxValue;
    private final boolean reverse;

    public double getWeight() {
        return weight;
    }

    @Override
    public double evaluate(ProductionParameters parameters) {

        double normalized = (fun.evaluate(parameters) - minValue) / (maxValue - minValue);
        if (reverse) {
            normalized = 1.0 - normalized;
        }
        return normalized * weight;
    }

    public double evaluateFromFunction(ProductionParameters parameters) {
        return fun.evaluate(parameters);
    }
}
