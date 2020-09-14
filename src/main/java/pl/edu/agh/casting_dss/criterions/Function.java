package pl.edu.agh.casting_dss.criterions;

import pl.edu.agh.casting_dss.data.ProductionParameters;

public interface Function {
    double evaluate(ProductionParameters parameters);
}
