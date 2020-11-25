package pl.edu.agh.casting_dss.model;

import pl.edu.agh.casting_dss.data.MechanicalProperties;
import pl.edu.agh.casting_dss.data.PossibleValues;
import pl.edu.agh.casting_dss.data.ProductionParameters;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MechanicalPropertiesModel {
    private final PossibleValues possibleValues;
    private final Model rmModel;
    private final Model rp02Model;
    private final Model a5Model;
    private final Model hbModel;
    private final Model kModel;

    public static Integer EVALUATIONS = 0;


    public MechanicalProperties evaluateProductionParameters(ProductionParameters parameters) throws Exception {
        EVALUATIONS++;
        return new MechanicalProperties(
                rmModel.evaluate(parameters),
                rp02Model.evaluate(parameters),
                a5Model.evaluate(parameters),
                hbModel.evaluate(parameters),
                kModel.evaluate(parameters)
        );
    }
}
