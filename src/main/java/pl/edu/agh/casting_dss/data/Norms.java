package pl.edu.agh.casting_dss.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.agh.casting_dss.single_criteria_opt.NormConstraint;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Norms {
    public static final List<String> THICKNESS_RANGES_ORDER = Arrays.asList("t<=30", "30<t<=60", "60<t<=100");
    private Map<String, List<NormConstraint>> norms;
}
