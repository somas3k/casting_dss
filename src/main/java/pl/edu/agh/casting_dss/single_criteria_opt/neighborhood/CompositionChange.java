package pl.edu.agh.casting_dss.single_criteria_opt.neighborhood;

import org.jamesframework.core.search.neigh.Move;
import pl.edu.agh.casting_dss.data.ProductionParameters;
import pl.edu.agh.casting_dss.solution.james.OptimizedADISolution;

import java.util.Map;

public class CompositionChange implements Move<OptimizedADISolution> {
    private final Map<String, Double> newComposition;
    private Map<String, Double> prevComposition;

    public CompositionChange(Map<String, Double> newComposition) {
        this.newComposition = newComposition;
    }

    @Override
    public void apply(OptimizedADISolution optimizedADISolution) {
        ProductionParameters productionParameters = optimizedADISolution.getProductionParameters();
        prevComposition = productionParameters.getChemicalComposition();
        productionParameters.setChemicalComposition(newComposition);
    }

    @Override
    public void undo(OptimizedADISolution optimizedADISolution) {
        optimizedADISolution.getProductionParameters().setChemicalComposition(prevComposition);
    }
}
