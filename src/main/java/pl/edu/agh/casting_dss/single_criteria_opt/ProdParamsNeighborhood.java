package pl.edu.agh.casting_dss.single_criteria_opt;

import org.jamesframework.core.search.neigh.Move;
import org.jamesframework.core.search.neigh.Neighbourhood;
import pl.edu.agh.casting_dss.data.ChemicalComposition;
import pl.edu.agh.casting_dss.data.PossibleValues;
import pl.edu.agh.casting_dss.data.ProductionParameters;

import java.util.ArrayList;
import java.util.List;
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
                List<ChemicalComposition> possibleChemicalCompositions = new ArrayList<>(possibleValues.getChemicalCompositions());
                possibleChemicalCompositions.remove(productionParameters.getComposition());
                return new CompositionChange(possibleChemicalCompositions.get(random.nextInt(possibleChemicalCompositions.size())));
            case 1:
                List<Integer> possibleAustTemps = new ArrayList<>(possibleValues.getPossibleAustTemps());
                possibleAustTemps.remove((Object)productionParameters.getAustTemp());
                return new HeatTreatmentChange((productionParam, newValue) -> {
                    int prevValue = productionParam.getAustTemp();
                    productionParam.setAustTemp(newValue);
                    return prevValue;
                }, possibleAustTemps.get(random.nextInt(possibleAustTemps.size())));
            case 2:
                List<Integer> possibleAusfTemps = new ArrayList<>(possibleValues.getPossibleAusfTemps());
                possibleAusfTemps.remove((Object)productionParameters.getAusfTemp());
                return new HeatTreatmentChange((productionParam, newValue) -> {
                    int prevValue = productionParam.getAusfTemp();
                    productionParam.setAusfTemp(newValue);
                    return prevValue;
                }, possibleAusfTemps.get(random.nextInt(possibleAusfTemps.size())));
            case 3:
                List<Integer> possibleAustTimes = new ArrayList<>(possibleValues.getPossibleAustTimes());
                possibleAustTimes.remove((Object)productionParameters.getAustTime());
                return new HeatTreatmentChange((productionParam, newValue) -> {
                    int prevValue = productionParam.getAustTime();
                    productionParam.setAustTime(newValue);
                    return prevValue;
                }, possibleAustTimes.get(random.nextInt(possibleAustTimes.size())));
            case 4:
                List<Integer> possibleAusfTimes = new ArrayList<>(possibleValues.getPossibleAusfTimes());
                possibleAusfTimes.remove((Object)productionParameters.getAustTime());
                return new HeatTreatmentChange((productionParam, newValue) -> {
                    int prevValue = productionParam.getAusfTime();
                    productionParam.setAusfTime(newValue);
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
        List<ChemicalComposition> possibleChemicalCompositions = new ArrayList<>(possibleValues.getChemicalCompositions());
        possibleChemicalCompositions.remove(productionParameters.getComposition());
        for (ChemicalComposition cc : possibleChemicalCompositions) {
            moves.add(new CompositionChange(cc));
        }
        List<Integer> possibleAustTemps = new ArrayList<>(possibleValues.getPossibleAustTemps());
        possibleAustTemps.remove((Object)productionParameters.getAustTemp());
        for (Integer austTemp : possibleAustTemps) {
            moves.add(new HeatTreatmentChange((productionParam, newValue) -> {
                int prevValue = productionParam.getAustTemp();
                productionParam.setAustTemp(newValue);
                return prevValue;
            }, austTemp));
        }
        List<Integer> possibleAusfTemps = new ArrayList<>(possibleValues.getPossibleAusfTemps());
        possibleAusfTemps.remove((Object)productionParameters.getAusfTemp());
        for (Integer ausfTemp : possibleAusfTemps) {
            moves.add(new HeatTreatmentChange((productionParam, newValue) -> {
                int prevValue = productionParam.getAusfTemp();
                productionParam.setAusfTemp(newValue);
                return prevValue;
            }, ausfTemp));
        }
        List<Integer> possibleAustTimes = new ArrayList<>(possibleValues.getPossibleAustTimes());
        possibleAustTimes.remove((Object)productionParameters.getAustTime());
        for (Integer austTime : possibleAustTimes) {
            moves.add(new HeatTreatmentChange((productionParam, newValue) -> {
                int prevValue = productionParam.getAustTime();
                productionParam.setAustTime(newValue);
                return prevValue;
            }, austTime));
        }

        List<Integer> possibleAusfTimes = new ArrayList<>(possibleValues.getPossibleAusfTimes());
        possibleAustTimes.remove((Object)productionParameters.getAusfTime());
        for (Integer ausfTime : possibleAusfTimes) {
            moves.add(new HeatTreatmentChange((productionParam, newValue) -> {
                int prevValue = productionParam.getAusfTime();
                productionParam.setAusfTime(newValue);
                return prevValue;
            }, ausfTime));
        }
        return moves;
    }
}
