package data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PossibleValues {
    private List<ChemicalComposition> chemicalCompositions;
    private List<Integer> possibleAustTemps;
    private List<Integer> possibleAustTimes;
    private List<Integer> possibleAusfTemps;
    private List<Integer> possibleAusfTimes;
}
