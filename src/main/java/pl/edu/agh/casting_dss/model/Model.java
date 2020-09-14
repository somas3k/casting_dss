package pl.edu.agh.casting_dss.model;

import pl.edu.agh.casting_dss.data.ProductionParameters;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class Model {
    protected final ModelInputConfiguration inputConfiguration;

    public double evaluate(ProductionParameters parameters) throws Exception{
        return 0.0;
    }
}
