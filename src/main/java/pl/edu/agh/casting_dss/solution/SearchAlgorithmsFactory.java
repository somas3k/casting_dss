package pl.edu.agh.casting_dss.solution;

import org.jamesframework.core.problems.Problem;
import org.jamesframework.core.problems.sol.Solution;
import org.jamesframework.core.search.Search;
import org.jamesframework.core.search.algo.RandomSearch;
import org.jamesframework.core.search.algo.exh.ExhaustiveSearch;
import org.jamesframework.core.search.algo.exh.SolutionIterator;
import org.jamesframework.core.search.algo.tabu.FullTabuMemory;
import org.jamesframework.core.search.algo.tabu.TabuSearch;
import org.jamesframework.core.search.neigh.Neighbourhood;
import pl.edu.agh.casting_dss.data.PossibleValues;
import pl.edu.agh.casting_dss.single_criteria_opt.OptimizedADISolution;
import pl.edu.agh.casting_dss.single_criteria_opt.ProdParamsNeighborhood;

public class SearchAlgorithmsFactory {
    public static Search<OptimizedADISolution> getSearchAlgorithm(Problem<OptimizedADISolution> problem, SearchType type, PossibleValues possibleValues, OptimizedADISolution actualSolution) {
        switch (type) {
            case RANDOM_SEARCH:
                return new RandomSearch<>(problem);
            case TABU_SEARCH:
                TabuSearch<OptimizedADISolution> tabuSearch = new TabuSearch<>(problem, new ProdParamsNeighborhood(possibleValues), new FullTabuMemory<>(200));
                tabuSearch.setCurrentSolution(actualSolution);
                return tabuSearch;
            case EXHAUSTIVE_SEARCH:
                return new ExhaustiveSearch<>(problem, new ADISolutionIterator(possibleValues, actualSolution.getProductionParameters().getThickness()));
        }
        return new RandomSearch<>(problem);
    }
}
