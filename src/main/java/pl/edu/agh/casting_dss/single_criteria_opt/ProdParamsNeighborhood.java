package pl.edu.agh.casting_dss.single_criteria_opt;

import org.jamesframework.core.search.neigh.Move;
import org.jamesframework.core.search.neigh.Neighbourhood;
import pl.edu.agh.casting_dss.data.PossibleValues;
import pl.edu.agh.casting_dss.data.ProductionParameters;

import java.util.*;

public class ProdParamsNeighborhood implements Neighbourhood<OptimizedADISolution> {
    private final PossibleValues possibleValues;

    public ProdParamsNeighborhood(PossibleValues possibleValues) {
        this.possibleValues = possibleValues;
    }

    @Override
    public Move<? super OptimizedADISolution> getRandomMove(OptimizedADISolution optimizedADISolution, Random random) {
        ProductionParameters productionParameters = optimizedADISolution.getProductionParameters();
        switch (random.nextInt(5)) {
            case 0:
                List<Map<String, Double>> possibleChemicalCompositions = new ArrayList<>(possibleValues.getChemicalCompositions());
                possibleChemicalCompositions.remove(productionParameters.getChemicalComposition());
                return new CompositionChange(possibleChemicalCompositions.get(random.nextInt(possibleChemicalCompositions.size())));
            case 1:
                List<Integer> possibleAustTemps = new ArrayList<>(possibleValues.getPossibleAustTemps());
                Integer aust_temp = productionParameters.getParameterValue("aust_temp").intValue();
                return getHTChange(possibleAustTemps, "aust_temp", getIndexOfNeigh(possibleAustTemps.indexOf(aust_temp), possibleAustTemps.size(), random));
            case 2:
                List<Integer> possibleAusfTemps = new ArrayList<>(possibleValues.getPossibleAusfTemps());
                Integer ausf_temp = productionParameters.getParameterValue("ausf_temp").intValue();
                return getHTChange(possibleAusfTemps, "ausf_temp", getIndexOfNeigh(possibleAusfTemps.indexOf(ausf_temp), possibleAusfTemps.size(), random));
            case 3:
                List<Integer> possibleAustTimes = new ArrayList<>(possibleValues.getPossibleAustTimes());
                Integer aust_czas = productionParameters.getParameterValue("aust_czas").intValue();
                return getHTChange(possibleAustTimes, "aust_czas", getIndexOfNeigh(possibleAustTimes.indexOf(aust_czas), possibleAustTimes.size(), random));
            case 4:
                List<Integer> possibleAusfTimes = new ArrayList<>(possibleValues.getPossibleAusfTimes());
                Integer ausf_czas = productionParameters.getParameterValue("ausf_czas").intValue();
                return getHTChange(possibleAusfTimes, "ausf_czas", getIndexOfNeigh(possibleAusfTimes.indexOf(ausf_czas), possibleAusfTimes.size(), random));
            default:
                return null;
        }
    }

    private int getIndexOfNeigh(int currentIndex, int listSize, Random random) {
        if (random.nextBoolean()) {
            currentIndex += 1;
        } else {
            currentIndex -= 1;
        }
        currentIndex = handleBoundaries(currentIndex, listSize);
        return currentIndex;
    }

    private int handleBoundaries(int currentIndex, int listSize) {
        if (currentIndex == -1) {
            currentIndex = 1;
        }
        if (currentIndex == listSize) {
            currentIndex = listSize - 2;
        }
        return currentIndex;
    }

    @Override
    public List<? extends Move<? super OptimizedADISolution>> getAllMoves(OptimizedADISolution optimizedADISolution) {
        List<Move<OptimizedADISolution>> moves = new ArrayList<>();
        ProductionParameters productionParameters = optimizedADISolution.getProductionParameters();
        List<Map<String, Double>> possibleChemicalCompositions = new ArrayList<>(possibleValues.getChemicalCompositions());
        possibleChemicalCompositions.remove(productionParameters.getChemicalComposition());
        for (Map<String, Double> cc : possibleChemicalCompositions) {
            moves.add(new CompositionChange(cc));
        }

        moves.addAll(getMoves(productionParameters, possibleValues.getPossibleAustTemps(), "aust_temp"));
        moves.addAll(getMoves(productionParameters, possibleValues.getPossibleAusfTemps(), "ausf_temp"));
        moves.addAll(getMoves(productionParameters, possibleValues.getPossibleAustTimes(), "aust_czas"));
        moves.addAll(getMoves(productionParameters, possibleValues.getPossibleAusfTimes(), "ausf_czas"));
        return moves;
    }

    private List<Move<OptimizedADISolution>> getMoves(ProductionParameters productionParameters, List<Integer> possibleParams, String paramKey) {
        int currentIndex = possibleParams.indexOf(productionParameters.getParameterValue(paramKey).intValue());
        if (currentIndex - 1 == -1) {
            return Collections.singletonList(getHTChange(possibleParams, paramKey, currentIndex + 1));
        }
        if (currentIndex + 1 == possibleParams.size()) {
            return Collections.singletonList(getHTChange(possibleParams, paramKey, currentIndex - 1));
        }
        return Arrays.asList(getHTChange(possibleParams, paramKey, currentIndex + 1), getHTChange(possibleParams, paramKey, currentIndex - 1));
    }

    private HeatTreatmentChange getHTChange(List<Integer> possibleParams, String paramKey, int i) {
        return new HeatTreatmentChange((productionParam, newValue) -> {
            int prevValue = productionParam.getParameterValue(paramKey).intValue();
            productionParam.getHeatTreatment().put(paramKey, newValue);
            return prevValue;
        }, possibleParams.get(i));
    }
}
