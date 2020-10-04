package pl.edu.agh.casting_dss.single_criteria_opt;

import org.jamesframework.core.search.neigh.Move;
import org.jamesframework.core.search.neigh.Neighbourhood;
import pl.edu.agh.casting_dss.data.PossibleValues;
import pl.edu.agh.casting_dss.data.ProductionParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
                possibleAustTemps.remove(Integer.valueOf(productionParameters.getParameterValue("aust_temp").intValue()));
                return new HeatTreatmentChange((productionParam, newValue) -> {
                    int prevValue = productionParam.getParameterValue("aust_temp").intValue();
                    productionParam.getHeatTreatment().put("aust_temp", newValue);
                    return prevValue;
                }, possibleAustTemps.get(random.nextInt(possibleAustTemps.size())));
            case 2:
                List<Integer> possibleAusfTemps = new ArrayList<>(possibleValues.getPossibleAusfTemps());
                possibleAusfTemps.remove(Integer.valueOf(productionParameters.getParameterValue("ausf_temp").intValue()));
                return new HeatTreatmentChange((productionParam, newValue) -> {
                    int prevValue = productionParam.getParameterValue("ausf_temp").intValue();
                    productionParam.getHeatTreatment().put("ausf_temp", newValue);
                    return prevValue;
                }, possibleAusfTemps.get(random.nextInt(possibleAusfTemps.size())));
            case 3:
                List<Integer> possibleAustTimes = new ArrayList<>(possibleValues.getPossibleAustTimes());
                possibleAustTimes.remove(Integer.valueOf(productionParameters.getParameterValue("aust_czas").intValue()));
                return new HeatTreatmentChange((productionParam, newValue) -> {
                    int prevValue = productionParam.getParameterValue("aust_czas").intValue();
                    productionParam.getHeatTreatment().put("aust_czas", newValue);
                    return prevValue;
                }, possibleAustTimes.get(random.nextInt(possibleAustTimes.size())));
            case 4:
                List<Integer> possibleAusfTimes = new ArrayList<>(possibleValues.getPossibleAusfTimes());
                possibleAusfTimes.remove(Integer.valueOf(productionParameters.getParameterValue("ausf_czas").intValue()));
                return new HeatTreatmentChange((productionParam, newValue) -> {
                    int prevValue = productionParam.getParameterValue("ausf_czas").intValue();
                    productionParam.getHeatTreatment().put("ausf_czas", newValue);
                    return prevValue;
                }, possibleAusfTimes.get(random.nextInt(possibleAusfTimes.size())));
            default:
                return null;
        }
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
        List<Integer> possibleAustTemps = new ArrayList<>(possibleValues.getPossibleAustTemps());
        possibleAustTemps.remove(Integer.valueOf(productionParameters.getParameterValue("aust_temp").intValue()));
        for (Integer austTemp : possibleAustTemps) {
            moves.add(new HeatTreatmentChange((productionParam, newValue) -> {
                int prevValue = productionParam.getParameterValue("aust_temp").intValue();
                productionParam.getHeatTreatment().put("aust_temp", newValue);
                return prevValue;
            }, austTemp));
        }
        List<Integer> possibleAusfTemps = new ArrayList<>(possibleValues.getPossibleAusfTemps());
        possibleAusfTemps.remove(Integer.valueOf(productionParameters.getParameterValue("ausf_temp").intValue()));
        for (Integer ausfTemp : possibleAusfTemps) {
            moves.add(new HeatTreatmentChange((productionParam, newValue) -> {
                int prevValue = productionParam.getParameterValue("ausf_temp").intValue();
                productionParam.getHeatTreatment().put("ausf_temp", newValue);
                return prevValue;
            }, ausfTemp));
        }
        List<Integer> possibleAustTimes = new ArrayList<>(possibleValues.getPossibleAustTimes());
        possibleAustTimes.remove(Integer.valueOf(productionParameters.getParameterValue("aust_czas").intValue()));
        for (Integer austTime : possibleAustTimes) {
            moves.add(new HeatTreatmentChange((productionParam, newValue) -> {
                int prevValue = productionParam.getParameterValue("aust_czas").intValue();
                productionParam.getHeatTreatment().put("aust_czas", newValue);
                return prevValue;
            }, austTime));
        }

        List<Integer> possibleAusfTimes = new ArrayList<>(possibleValues.getPossibleAusfTimes());
        possibleAustTimes.remove(Integer.valueOf(productionParameters.getParameterValue("ausf_czas").intValue()));
        for (Integer ausfTime : possibleAusfTimes) {
            moves.add(new HeatTreatmentChange((productionParam, newValue) -> {
                int prevValue = productionParam.getParameterValue("ausf_czas").intValue();
                productionParam.getHeatTreatment().put("ausf_czas", newValue);
                return prevValue;
            }, ausfTime));
        }
        return moves;
    }
}
