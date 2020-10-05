package pl.edu.agh.casting_dss.solution;

import org.jamesframework.core.problems.Problem;
import org.jamesframework.core.problems.sol.Solution;
import org.jamesframework.core.search.Search;
import org.jamesframework.core.search.algo.*;
import org.jamesframework.core.search.algo.exh.ExhaustiveSearch;
import org.jamesframework.core.search.algo.exh.SolutionIterator;
import org.jamesframework.core.search.algo.tabu.FullTabuMemory;
import org.jamesframework.core.search.algo.tabu.TabuSearch;
import org.jamesframework.core.search.algo.vns.VariableNeighbourhoodDescent;
import org.jamesframework.core.search.neigh.Neighbourhood;
import pl.edu.agh.casting_dss.data.PossibleValues;
import pl.edu.agh.casting_dss.single_criteria_opt.OptimizedADISolution;
import pl.edu.agh.casting_dss.single_criteria_opt.ProdParamsNeighborhood;

public class SearchAlgorithmsFactory {
    public static Search<OptimizedADISolution> getSearchAlgorithm(Problem<OptimizedADISolution> problem, SearchType type, PossibleValues possibleValues, OptimizedADISolution actualSolution) {
        ProdParamsNeighborhood neighborhood = new ProdParamsNeighborhood(possibleValues);
        switch (type) {
            case RANDOM_SEARCH:
                return new RandomSearch<>(problem);
            case TABU_SEARCH:
                TabuSearch<OptimizedADISolution> tabuSearch = new TabuSearch<>(problem, neighborhood, new FullTabuMemory<>(200));
                tabuSearch.setCurrentSolution(actualSolution);
                return tabuSearch;
            case EXHAUSTIVE_SEARCH:
                return new ExhaustiveSearch<>(problem, new ADISolutionIterator(possibleValues, actualSolution.getProductionParameters().getThickness()));
            case METROPOLIS_SEARCH:
                return new MetropolisSearch<>(problem, neighborhood, 1000.0);
            case PARALLEL_TEMPERING:
                return new ParallelTempering<>(problem, neighborhood, 200, 200, 1000);
            case RANDOM_DESCENT:
                return new RandomDescent<>(problem, neighborhood);
            case STEEPEST_DESCENT:
                return new SteepestDescent<>(problem, neighborhood);
        }
        return new RandomSearch<>(problem);
    }
}
