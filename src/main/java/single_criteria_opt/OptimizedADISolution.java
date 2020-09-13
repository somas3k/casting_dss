package single_criteria_opt;

import data.ProductionParameters;
import org.jamesframework.core.problems.sol.Solution;

import java.util.Objects;

public class OptimizedADISolution extends Solution {
    private ProductionParameters productionParameters;

    public OptimizedADISolution(ProductionParameters productionParameters) {
        this.productionParameters = productionParameters;
    }

    public Solution copy() {
        return new OptimizedADISolution(new ProductionParameters(productionParameters));
    }

    public ProductionParameters getProductionParameters() {
        return productionParameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OptimizedADISolution that = (OptimizedADISolution) o;
        return productionParameters.equals(that.productionParameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productionParameters);
    }

    @Override
    public String toString() {
        return "OptimizedADISolution{" +
                "productionParameters=" + productionParameters +
                '}';
    }
}
