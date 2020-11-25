package pl.edu.agh.casting_dss.solution.james;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class RunStep {
    long step;
    String type;
    double cost;
    double quality;
    double qc;
    int qcEvals;
    int modelEvals;
    long millisFromStart;

}
