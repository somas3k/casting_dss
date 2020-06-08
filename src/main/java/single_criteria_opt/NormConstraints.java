package single_criteria_opt;

import data.MechanicalProperties;
import model.MechanicalPropertiesModel;
import org.jamesframework.core.problems.constraints.Constraint;
import org.jamesframework.core.problems.constraints.validations.SimpleValidation;
import org.jamesframework.core.problems.constraints.validations.Validation;

public class NormConstraints implements Constraint<OptimizedADISolution, MechanicalPropertiesModel> {
    private float rmMin;
    private float rp02Min;
    private float a5Min;
    private float hbMin;
    private float hbMax;
    private float kMin;
    private static final SimpleValidation TRUE = new SimpleValidation(true);
    private static final SimpleValidation FALSE = new SimpleValidation(false);

    @Override
    public Validation validate(OptimizedADISolution optimizedADISolution, MechanicalPropertiesModel model) {
        MechanicalProperties properties = model.evaluateProductionParameters(optimizedADISolution.getProductionParameters());
        if (properties.getRm() >= rmMin &&
                properties.getRp02() >= rp02Min &&
                properties.getA5() >= a5Min &&
                properties.getHb() >= hbMin &&
                properties.getHb() <= hbMax &&
                properties.getK() >= kMin
        ) {
            return TRUE;
        }
        return FALSE;
    }
}
