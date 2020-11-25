package pl.edu.agh.casting_dss;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.edu.agh.casting_dss.criterions.ProductionRange;
import pl.edu.agh.casting_dss.data.MechanicalProperties;
import pl.edu.agh.casting_dss.data.NormType;
import pl.edu.agh.casting_dss.data.Norms;
import pl.edu.agh.casting_dss.factories.ModelLoadingException;
import pl.edu.agh.casting_dss.gui.model.DSSModel;
import pl.edu.agh.casting_dss.model.MechanicalPropertiesModel;
import pl.edu.agh.casting_dss.single_criteria_opt.QCFunction;
import pl.edu.agh.casting_dss.solution.james.*;
import pl.edu.agh.casting_dss.utils.NormsUtil;
import pl.edu.agh.casting_dss.utils.ProductionRangesUtils;
import pl.edu.agh.casting_dss.utils.SystemConfiguration;
import pl.edu.agh.casting_dss.utils.SystemConfigurationUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static pl.edu.agh.casting_dss.solution.james.SearchAlgorithmsFactory.*;

public class Research {

    public static void main(String[] args) throws IOException, ModelLoadingException {
        SystemConfiguration configuration = SystemConfigurationUtils.loadFromFile(Research.class.getResource("system_configuration.json"));
        List<ProductionRange> productionRanges = ProductionRangesUtils.loadFromFile(Research.class.getResource("production_ranges.json"));
        Norms norms = NormsUtil.loadFromFile(Research.class.getResource("norms.json"));
        SearchAlgorithmsFactory factory = new SearchAlgorithmsFactory();
        DSSModel model = new DSSModel(configuration, productionRanges, norms, factory);
        SolutionSearchListener searchListener = new SolutionSearchListener(model);
        ObjectMapper mapper = new ObjectMapper();

        Integer maxTime = 10;
        NormType normType = NormType.GJS_800_10;
        int thickness = 30;
        model.getMaxRuntime().set(maxTime);
        model.getSelectedNormType().set(normType);
        model.getThicknessProperty().set(thickness);
        model.setRandom(new Random(1023645));
        OptimizedADISolution lowestQuality = model.generateRandomSolution();
        for (int i = 0; i < 300; i++) {
            OptimizedADISolution cur = model.generateRandomSolution();
            if (model.getQualityFunction().evaluate(cur.getProductionParameters()) < model.getQualityFunction().evaluate(lowestQuality.getProductionParameters())) {
                lowestQuality = cur;
            }
        }
        OptimizedADISolution highestCost = model.generateRandomSolution();
        for (int i = 0; i < 300; i++) {
            OptimizedADISolution cur = model.generateRandomSolution();
            if (model.getCostFunction().evaluate(cur.getProductionParameters()) > model.getCostFunction().evaluate(lowestQuality.getProductionParameters())) {
                highestCost = cur;
            }
        }
        List<OptimizedADISolution> startingSolutionsList = Arrays.asList(lowestQuality, highestCost);

        for (OptimizedADISolution optimizedADISolution : startingSolutionsList) {
            System.out.println(optimizedADISolution);
            System.out.println(model.getCostFunction().evaluate(optimizedADISolution.getProductionParameters()));
            System.out.println(model.getQualityFunction().evaluate(optimizedADISolution.getProductionParameters()));
        }
//        List<Double> costToQualityList = Arrays.asList(0.0, 0.3, 0.5);
        List<Double> costToQualityList = Arrays.asList(0.5);
        List<Map<String, Number>> METROPOLIS_PARAMS = Arrays.asList(Map.of(METROPOLIS_TEMP_KEY, 20), Map.of(METROPOLIS_TEMP_KEY, 50));
        List<Map<String, Number>> TABU_PARAMS = Arrays.asList(Map.of(TABU_MEMORY_KEY, 5), Map.of(TABU_MEMORY_KEY, 10), Map.of(TABU_MEMORY_KEY, 50));
        List<Map<String, Number>> PARALLEL_TEMP_PARAMS = Arrays.asList(Map.of(PARALLEL_TEMPERING_REPLICAS_KEY, 5, PARALLEL_TEMPERING_MIN_TEMP_KEY, 1.0, PARALLEL_TEMPERING_MAX_TEMP_KEY, 200.0),
                Map.of(PARALLEL_TEMPERING_REPLICAS_KEY, 10, PARALLEL_TEMPERING_MIN_TEMP_KEY, 10, PARALLEL_TEMPERING_MAX_TEMP_KEY, 100.0));
        List<SearchType> searchTypes = Arrays.asList(SearchType.TABU_SEARCH, SearchType.RANDOM_DESCENT, SearchType.STEEPEST_DESCENT, SearchType.METROPOLIS_SEARCH, SearchType.PARALLEL_TEMPERING);
        List<Integer> randomSeeds = Arrays.asList(13, 29, 59, 73, 113, 64518415,84164584,8494987,46516514,48945);


        for (int i = 0; i < startingSolutionsList.size(); i++) {
            Map<Integer, Object> forSeedsResults = new LinkedHashMap<>();
            for (int k = 0; k < randomSeeds.size(); k++) {
                for (int j = 0; j < costToQualityList.size(); j++) {
                    Map<String, Map<String, Object>> results = new LinkedHashMap<>();
                    for (SearchType type : searchTypes) {
                        model.setSearchType(type);
                        model.setActualSolution(((OptimizedADISolution) startingSolutionsList.get(i).copy()).getProductionParameters());
                        model.getCostQualityProportionProperty().set(costToQualityList.get(j));
                        switch (type) {

                            case METROPOLIS_SEARCH:
                                for (Map<String, Number> params : METROPOLIS_PARAMS) {
                                    factory.setAlgoParams(params);
                                    model.setActualSolution(((OptimizedADISolution) startingSolutionsList.get(i).copy()).getProductionParameters());
                                    model.setRandom(new Random(1963));
                                    results.put(String.format("METRO_%s", params.get(METROPOLIS_TEMP_KEY)), runFinderWithWaiting(model, searchListener));
                                }
                                break;
                            case PARALLEL_TEMPERING:
                                for (Map<String, Number> params : PARALLEL_TEMP_PARAMS) {
                                    factory.setAlgoParams(params);
                                    model.setActualSolution(((OptimizedADISolution) startingSolutionsList.get(i).copy()).getProductionParameters());
                                    model.setRandom(new Random(1963));
                                    results.put(String.format("PARALLEL_%s_%s_%s", params.get(PARALLEL_TEMPERING_REPLICAS_KEY), params.get(PARALLEL_TEMPERING_MIN_TEMP_KEY), params.get(PARALLEL_TEMPERING_MAX_TEMP_KEY)),
                                            runFinderWithWaiting(model, searchListener));
                                }
                                break;
                            case TABU_SEARCH:
                                for (Map<String, Number> params : TABU_PARAMS) {
                                    factory.setAlgoParams(params);
                                    model.setActualSolution(((OptimizedADISolution) startingSolutionsList.get(i).copy()).getProductionParameters());
                                    model.setRandom(new Random(1963));
                                    results.put(String.format("TABU_%s", params.get(TABU_MEMORY_KEY)),
                                            runFinderWithWaiting(model, searchListener));
                                }
                                break;
                            default:
                                model.setRandom(new Random(1963));
                                model.setActualSolution(((OptimizedADISolution) startingSolutionsList.get(i).copy()).getProductionParameters());
                                results.put(type.toString(), runFinderWithWaiting(model, searchListener));
                        }
                    }
                    forSeedsResults.put(k, results);
                }
            }
            String filename = String.format("./python_analysis/solution_%d_cqp_0_5_2.json", i + 1);
            mapper.writeValue(new File(filename), forSeedsResults);
            System.out.println(filename + " done");
        }
    }


    private static Map<String, Object> runFinderWithWaiting(DSSModel model, SolutionSearchListener listener) {
        QCFunction.EVALUATIONS = 0;
        MechanicalPropertiesModel.EVALUATIONS = 0;
        model.getFinder().findSolutionSynchronously(model, listener);
        List<RunStep> history = new ArrayList<>(listener.getHistory());
        listener.resetHistory();
        return Map.of("history", history);
    }
}
