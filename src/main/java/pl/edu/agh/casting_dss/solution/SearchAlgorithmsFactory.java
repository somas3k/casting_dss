package pl.edu.agh.casting_dss.solution;

import org.jamesframework.core.problems.Problem;
import org.jamesframework.core.search.LocalSearch;
import org.jamesframework.core.search.Search;
import org.jamesframework.core.search.algo.*;
import org.jamesframework.core.search.algo.exh.ExhaustiveSearch;
import org.jamesframework.core.search.algo.tabu.FullTabuMemory;
import org.jamesframework.core.search.algo.tabu.TabuSearch;
import pl.edu.agh.casting_dss.data.PossibleValues;
import pl.edu.agh.casting_dss.single_criteria_opt.OptimizedADISolution;
import pl.edu.agh.casting_dss.single_criteria_opt.ProdParamsNeighborhood;

public class SearchAlgorithmsFactory {
    public static Search<OptimizedADISolution> getSearchAlgorithm(Problem<OptimizedADISolution> problem, SearchType type, PossibleValues possibleValues, OptimizedADISolution actualSolution) {
        ProdParamsNeighborhood neighborhood = new ProdParamsNeighborhood(possibleValues);
        Search<OptimizedADISolution> chosenSearch = new RandomSearch<>(problem);
        switch (type) {
            case RANDOM_SEARCH:
                chosenSearch = new RandomSearch<>(problem);
                break;
            case TABU_SEARCH:
                chosenSearch = new TabuSearch<>(problem, neighborhood, new FullTabuMemory<>(300));
                break;
            case EXHAUSTIVE_SEARCH:
                chosenSearch = new ExhaustiveSearch<>(problem, new ADISolutionIterator(possibleValues, actualSolution.getProductionParameters().getThickness()));
                break;
            case METROPOLIS_SEARCH:
                chosenSearch = new MetropolisSearch<>(problem, neighborhood, 10.0);
                break;
            case PARALLEL_TEMPERING:
                chosenSearch = new ParallelTempering<>(problem, neighborhood, 10, 1, 200, (p, n, t) -> {
                    MetropolisSearch<OptimizedADISolution> search = new MetropolisSearch<>(p, n, t);
                    search.setCurrentSolution(actualSolution);
                    return search;
                });
                break;
            case RANDOM_DESCENT:
                chosenSearch = new RandomDescent<>(problem, neighborhood);
                break;
            case STEEPEST_DESCENT:
                chosenSearch = new SteepestDescent<>(problem, neighborhood);
                break;
        }
        if (chosenSearch instanceof LocalSearch) {
            ((LocalSearch<OptimizedADISolution>) chosenSearch).setCurrentSolution(actualSolution);
        }
        return chosenSearch;
    }
}
