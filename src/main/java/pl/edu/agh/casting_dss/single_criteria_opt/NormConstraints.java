package pl.edu.agh.casting_dss.single_criteria_opt;

import pl.edu.agh.casting_dss.data.MechanicalProperties;
import lombok.AllArgsConstructor;
import pl.edu.agh.casting_dss.model.MechanicalPropertiesModel;
import org.jamesframework.core.problems.constraints.Constraint;
import org.jamesframework.core.problems.constraints.validations.SimpleValidation;
import org.jamesframework.core.problems.constraints.validations.Validation;

@AllArgsConstructor
public class NormConstraints implements Constraint<OptimizedADISolution, MechanicalPropertiesModel> {
//    private float rmMin;
//    private float rp02Min;
//    private float a5Min;
    private final float hbMin;
    private final float hbMax;
//    private float kMin;
    private static final SimpleValidation TRUE = new SimpleValidation(true);
    private static final SimpleValidation FALSE = new SimpleValidation(false);



    @Override
    public Validation validate(OptimizedADISolution optimizedADISolution, MechanicalPropertiesModel model) {
        MechanicalProperties properties;
        try {
            properties = model.evaluateProductionParameters(optimizedADISolution.getProductionParameters());
        } catch (Exception e) {
            e.printStackTrace();
            return FALSE;
        }
        if (
//                properties.getRm() >= rmMin &&
//                properties.getRp02() >= rp02Min &&
//                properties.getA5() >= a5Min &&
                properties.getHb() >= hbMin &&
                properties.getHb() <= hbMax
//                properties.getK() >= kMin
        ) {
            return TRUE;
        }
        return FALSE;
    }
}
