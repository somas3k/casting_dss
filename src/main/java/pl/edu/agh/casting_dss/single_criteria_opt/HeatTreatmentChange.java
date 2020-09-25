package pl.edu.agh.casting_dss.single_criteria_opt;

import org.jamesframework.core.search.neigh.Move;
import pl.edu.agh.casting_dss.data.ProductionParameters;

import java.util.function.BiFunction;

public class HeatTreatmentChange implements Move<OptimizedADISolution> {
    private final BiFunction<ProductionParameters, Integer, Integer> valueChanger;
    private final Integer newValue;
    private int prevValue;

    public HeatTreatmentChange(BiFunction<ProductionParameters, Integer, Integer> valueChanger, Integer newValue) {
        this.valueChanger = valueChanger;
        this.newValue = newValue;
    }

    @Override
    public void apply(OptimizedADISolution optimizedADISolution) {
        prevValue = valueChanger.apply(optimizedADISolution.getProductionParameters(), newValue);
    }

    @Override
    public void undo(OptimizedADISolution optimizedADISolution) {
        valueChanger.apply(optimizedADISolution.getProductionParameters(), prevValue);
    }
}
