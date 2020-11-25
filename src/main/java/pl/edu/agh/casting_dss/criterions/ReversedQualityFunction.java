package pl.edu.agh.casting_dss.criterions;

import pl.edu.agh.casting_dss.data.ProductionParameters;

import java.util.List;

public class ReversedQualityFunction implements Function{
    private final QualityFunction function;

    public ReversedQualityFunction(QualityFunction function) {

        this.function = function;
    }

    @Override
    public double evaluate(ProductionParameters parameters) {
        return function.getMaxValue() - function.evaluate(parameters);
    }
}
