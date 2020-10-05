package pl.edu.agh.casting_dss.single_criteria_opt;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.agh.casting_dss.data.MechanicalProperties;
import lombok.AllArgsConstructor;
import pl.edu.agh.casting_dss.data.NormType;
import pl.edu.agh.casting_dss.model.MechanicalPropertiesModel;
import org.jamesframework.core.problems.constraints.Constraint;
import org.jamesframework.core.problems.constraints.validations.SimpleValidation;
import org.jamesframework.core.problems.constraints.validations.Validation;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class NormConstraint implements Constraint<OptimizedADISolution, MechanicalPropertiesModel> {
    private NormType type;
    private Double rmMin;
    private Double rp02Min;
    private Double a5Min;
    private Double hbMin;
    private Double hbMax;
    @JsonAlias("kMin")
    private Double kkMin;
    private static final SimpleValidation TRUE = new SimpleValidation(true);
    private static final SimpleValidation FALSE = new SimpleValidation(false);

    public NormConstraint(double hbMin, double hbMax) {
        this.hbMin = hbMin;
        this.hbMax = hbMax;
    }

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
                properties.getRm() >= rmMin &&
                properties.getRp02() >= rp02Min &&
                properties.getA5() >= a5Min &&
                properties.getHb() >= hbMin &&
                properties.getHb() <= hbMax &&
                properties.getK() >= kkMin
        ) {
            return TRUE;
        }
        return FALSE;
    }

    public boolean normHasAllRequiredValues() {
        return rmMin != null && rp02Min != null && hbMin != null && hbMax != null && kkMin != null;
    }
}
