package pl.edu.agh.casting_dss.solution;

import org.jamesframework.core.search.algo.exh.SolutionIterator;
import pl.edu.agh.casting_dss.data.PossibleValues;
import pl.edu.agh.casting_dss.data.ProductionParameters;
import pl.edu.agh.casting_dss.single_criteria_opt.OptimizedADISolution;

import java.util.Map;


public class ADISolutionIterator implements SolutionIterator<OptimizedADISolution> {
    private final PossibleValues possibleValues;
    private final Integer thickness;
    private int lastCCindex;
    private int lastAustTempIndex;
    private int lastAustTimeIndex;
    private int lastAusfTempIndex;
    private int lastAusfTimeIndex;

    public ADISolutionIterator(PossibleValues possibleValues, int thickness) {
        this.possibleValues = possibleValues;
        this.thickness = thickness;
    }

    @Override
    public boolean hasNext() {
        return lastCCindex < possibleValues.getChemicalCompositions().size() &&
                lastAustTempIndex < possibleValues.getPossibleAustTemps().size() &&
                lastAustTimeIndex < possibleValues.getPossibleAustTimes().size() &&
                lastAusfTempIndex < possibleValues.getPossibleAusfTemps().size()  &&
                lastAusfTimeIndex < possibleValues.getPossibleAusfTimes().size();
    }

    @Override
    public OptimizedADISolution next() {
        Map<String, Double> cc = possibleValues.getChemicalCompositions().get(lastCCindex);
        Integer austTemp = possibleValues.getPossibleAustTemps().get(lastAustTempIndex);
        Integer austTime = possibleValues.getPossibleAustTimes().get(lastAustTimeIndex);
        Integer ausfTemp = possibleValues.getPossibleAusfTemps().get(lastAusfTempIndex);
        Integer ausfTime = possibleValues.getPossibleAusfTimes().get(lastAusfTimeIndex);

        lastAusfTimeIndex++;
        if (lastAusfTimeIndex == possibleValues.getPossibleAusfTimes().size()) {
            lastAusfTimeIndex = 0;
            lastAusfTempIndex++;
            if (lastAusfTempIndex == possibleValues.getPossibleAusfTemps().size()) {
                lastAusfTempIndex = 0;
                lastAustTimeIndex++;
                if (lastAustTimeIndex == possibleValues.getPossibleAustTimes().size()) {
                    lastAustTimeIndex = 0;
                    lastAustTempIndex++;
                    if (lastAustTempIndex == possibleValues.getPossibleAustTemps().size()) {
                        lastAustTempIndex = 0;
                        lastCCindex++;
                    }
                }
            }
        }
        return new OptimizedADISolution(new ProductionParameters(cc, Map.of(
                "aust_temp", austTemp,
                "aust_czas", austTime,
                "ausf_temp", ausfTemp,
                "ausf_czas", ausfTime
        ), thickness));
    }
}
