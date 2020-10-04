package pl.edu.agh.casting_dss.criterions;

import lombok.AllArgsConstructor;
import pl.edu.agh.casting_dss.data.ProductionParameters;

import java.util.List;

@AllArgsConstructor
public class QualityFunction implements Function {
    private final List<ProductionRange> productionRanges;

    public double evaluate(ProductionParameters parameters) {
        double quality = 0;
        for (ProductionRange range : productionRanges) {
            Number value = parameters.getParameterValue(range.getParamName());
            if (value.doubleValue() >= range.getMinimum() && value.doubleValue() <= range.getMaximum()) {
                quality += range.getWeight();
            }
        }
        return quality;
    }

    public Double getMinValue() {
        return 0.0;
    }

    public Double getMaxValue() {
        return productionRanges.stream().mapToDouble(ProductionRange::getWeight).sum();
    }


}
