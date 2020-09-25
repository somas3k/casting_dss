package pl.edu.agh.casting_dss.single_criteria_opt;

import org.jamesframework.core.search.neigh.Move;
import pl.edu.agh.casting_dss.data.ChemicalComposition;
import pl.edu.agh.casting_dss.data.ProductionParameters;

public class CompositionChange implements Move<OptimizedADISolution> {
    private final ChemicalComposition newComposition;
    private ChemicalComposition prevComposition;

    public CompositionChange(ChemicalComposition newComposition) {
        this.newComposition = newComposition;
    }

    @Override
    public void apply(OptimizedADISolution optimizedADISolution) {
        ProductionParameters productionParameters = optimizedADISolution.getProductionParameters();
        prevComposition = productionParameters.getComposition();
        productionParameters.setComposition(newComposition);
    }

    @Override
    public void undo(OptimizedADISolution optimizedADISolution) {
        optimizedADISolution.getProductionParameters().setComposition(prevComposition);
    }
}
