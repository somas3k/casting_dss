package pl.edu.agh.casting_dss.solution.james;

import lombok.Getter;
import lombok.Setter;
import org.jamesframework.core.problems.Problem;
import org.jamesframework.core.search.LocalSearch;
import org.jamesframework.core.search.Search;
import org.jamesframework.core.search.algo.*;
import org.jamesframework.core.search.algo.exh.ExhaustiveSearch;
import org.jamesframework.core.search.algo.tabu.FullTabuMemory;
import org.jamesframework.core.search.algo.tabu.TabuSearch;
import pl.edu.agh.casting_dss.data.PossibleValues;
import pl.edu.agh.casting_dss.single_criteria_opt.ADISolutionIterator;
import pl.edu.agh.casting_dss.single_criteria_opt.neighborhood.ProdParamsNeighborhood;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class SearchAlgorithmsFactory {
    public static final String TABU_MEMORY_KEY = "tabuMemory";
    public static final String METROPOLIS_TEMP_KEY = "metropolisTemperature";
    public static final String PARALLEL_TEMPERING_REPLICAS_KEY = "parallelTemperingReplicas";
    public static final String PARALLEL_TEMPERING_MIN_TEMP_KEY = "parallelTemperingMinTemp";
    public static final String PARALLEL_TEMPERING_MAX_TEMP_KEY = "parallelTemperingMaxTemp";
    private Map<String, Number> algoParams = new HashMap<>();

    {
        algoParams.put(TABU_MEMORY_KEY, 300);
        algoParams.put(METROPOLIS_TEMP_KEY, 10.0);
        algoParams.put(PARALLEL_TEMPERING_REPLICAS_KEY, 10);
        algoParams.put(PARALLEL_TEMPERING_MIN_TEMP_KEY, 1.0);
        algoParams.put(PARALLEL_TEMPERING_MAX_TEMP_KEY, 200.0);
    }

    public void setAlgoParams(Map<String, Number> params) {
        algoParams.putAll(params);
    }

    public Search<OptimizedADISolution> getSearchAlgorithm(Problem<OptimizedADISolution> problem, SearchType type, PossibleValues possibleValues, OptimizedADISolution actualSolution) {
        ProdParamsNeighborhood neighborhood = new ProdParamsNeighborhood(possibleValues);
        Search<OptimizedADISolution> chosenSearch = new RandomSearch<>(problem);
        switch (type) {
            case RANDOM_SEARCH:
                chosenSearch = new RandomSearch<>(problem);
                break;
            case TABU_SEARCH:
                chosenSearch = new TabuSearch<>(problem, neighborhood, new FullTabuMemory<>(algoParams.get(TABU_MEMORY_KEY).intValue()));
                break;
            case EXHAUSTIVE_SEARCH:
                chosenSearch = new ExhaustiveSearch<>(problem, new ADISolutionIterator(possibleValues, actualSolution.getProductionParameters().getThickness()));
                break;
            case METROPOLIS_SEARCH:
                chosenSearch = new MetropolisSearch<>(problem, neighborhood, algoParams.get(METROPOLIS_TEMP_KEY).doubleValue());
                break;
            case PARALLEL_TEMPERING:
                chosenSearch = new ParallelTempering<>(problem, neighborhood, algoParams.get(PARALLEL_TEMPERING_REPLICAS_KEY).intValue(),
                        algoParams.get(PARALLEL_TEMPERING_MIN_TEMP_KEY).doubleValue(), algoParams.get(PARALLEL_TEMPERING_MAX_TEMP_KEY).doubleValue(), (p, n, t) -> {
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
