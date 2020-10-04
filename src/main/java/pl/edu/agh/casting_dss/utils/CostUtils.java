package pl.edu.agh.casting_dss.utils;

import pl.edu.agh.casting_dss.criterions.CostFunction;
import pl.edu.agh.casting_dss.data.PossibleValues;
import pl.edu.agh.casting_dss.data.ProductionParameters;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class CostUtils {

    public static Pair<Double, Double> getMinMaxCost(CostFunction fun, PossibleValues values, int thickness) {
        List<Map<String, Double>> compositionList = new ArrayList<>(values.getChemicalCompositions());
        compositionList.sort(Comparator.comparingDouble(fun::getChemCompIncrease));
        Map<String, Double> minComposition = compositionList.get(0);
        Map<String, Double> maxComposition = compositionList.get(compositionList.size() - 1);

        ProductionParameters minCostParams = new ProductionParameters(
                minComposition,
                Map.of(
                        "aust_temp", values.getPossibleAustTemps().get(0),
                        "aust_czas", values.getPossibleAustTimes().get(0),
                        "ausf_temp", values.getPossibleAusfTemps().get(0),
                        "ausf_czas", values.getPossibleAusfTimes().get(0)
                ),
                thickness
        );

        ProductionParameters maxCostParams = new ProductionParameters(
                maxComposition,
                Map.of(
                        "aust_temp", values.getPossibleAustTemps().get(values.getPossibleAustTemps().size() - 1),
                        "aust_czas", values.getPossibleAustTimes().get(values.getPossibleAustTimes().size() - 1),
                        "ausf_temp", values.getPossibleAusfTemps().get(values.getPossibleAusfTemps().size() - 1),
                        "ausf_czas", values.getPossibleAusfTimes().get(values.getPossibleAusfTimes().size() - 1)
                ),
                thickness
        );
        return Pair.of(fun.evaluate(minCostParams), fun.evaluate(maxCostParams));
    }
}
