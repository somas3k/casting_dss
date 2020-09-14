package pl.edu.agh.casting_dss.single_criteria_opt;

import org.jamesframework.core.search.neigh.Move;
import org.jamesframework.core.search.neigh.Neighbourhood;

import java.util.List;
import java.util.Random;

public class ProdParamsNeighborhood implements Neighbourhood<OptimizedADISolution> {
    @Override
    public Move<? super OptimizedADISolution> getRandomMove(OptimizedADISolution optimizedADISolution, Random random) {
        return null;
    }

    @Override
    public List<? extends Move<? super OptimizedADISolution>> getAllMoves(OptimizedADISolution optimizedADISolution) {
        return null;
    }
}
