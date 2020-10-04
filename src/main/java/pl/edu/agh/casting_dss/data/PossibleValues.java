package pl.edu.agh.casting_dss.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PossibleValues {
    private List<Map<String, Double>> chemicalCompositions;
    private List<Integer> possibleAustTemps;
    private List<Integer> possibleAustTimes;
    private List<Integer> possibleAusfTemps;
    private List<Integer> possibleAusfTimes;
}
