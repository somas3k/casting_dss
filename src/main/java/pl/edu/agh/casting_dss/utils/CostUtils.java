package pl.edu.agh.casting_dss.utils;

import pl.edu.agh.casting_dss.criterions.CostFunction;
import pl.edu.agh.casting_dss.data.ChemicalComposition;
import pl.edu.agh.casting_dss.data.PossibleValues;
import pl.edu.agh.casting_dss.data.ProductionParameters;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CostUtils {

    public static Pair<Double, Double> getMinMaxCost(CostFunction fun, PossibleValues values, int thickness) {
        List<ChemicalComposition> compositionList = new ArrayList<>(values.getChemicalCompositions());
        compositionList.sort(Comparator.comparingDouble(fun::getChemCompIncrease));
        ChemicalComposition minComposition = compositionList.get(0);
        ChemicalComposition maxComposition = compositionList.get(compositionList.size() - 1);

        ProductionParameters minCostParams = new ProductionParameters(
                minComposition,
                values.getPossibleAustTemps().get(0),
                values.getPossibleAustTimes().get(0),
                values.getPossibleAusfTemps().get(0),
                values.getPossibleAusfTimes().get(0),
                thickness
        );

        ProductionParameters maxCostParams = new ProductionParameters(
                maxComposition,
                values.getPossibleAustTemps().get(values.getPossibleAustTemps().size() - 1),
                values.getPossibleAustTimes().get(values.getPossibleAustTimes().size() - 1),
                values.getPossibleAusfTemps().get(values.getPossibleAusfTemps().size() - 1),
                values.getPossibleAusfTimes().get(values.getPossibleAusfTimes().size() - 1),
                thickness
        );
        return Pair.of(fun.evaluate(minCostParams), fun.evaluate(maxCostParams));
    }
}
