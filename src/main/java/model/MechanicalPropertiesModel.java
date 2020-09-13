package model;

import data.MechanicalProperties;
import data.PossibleValues;
import data.ProductionParameters;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MechanicalPropertiesModel {
    private final PossibleValues possibleValues;
//    private final Model rmModel;
//    private final Model rp02Model;
//    private final Model a5Model;
    private final Model hbModel;
//    private final Model kModel;


    public MechanicalProperties evaluateProductionParameters(ProductionParameters parameters) throws Exception {
        return new MechanicalProperties(
                0,
                0,
                0,
                hbModel.evaluate(parameters),
                0
        );
    }
}
