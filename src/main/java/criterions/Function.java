package criterions;

import data.ProductionParameters;

public interface Function {
    double evaluate(ProductionParameters parameters);
}
