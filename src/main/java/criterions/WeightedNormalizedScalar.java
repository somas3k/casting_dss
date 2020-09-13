package criterions;

import data.ProductionParameters;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WeightedNormalizedScalar implements Function {
    private final Function fun;
    private final double weight;
    private final double minValue;
    private final double maxValue;

    @Override
    public double evaluate(ProductionParameters parameters) {
        return ((fun.evaluate(parameters) - minValue) / (maxValue - minValue)) * weight;
    }
}
