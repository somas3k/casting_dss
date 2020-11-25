package pl.edu.agh.casting_dss.solution.jmetal;

import org.uma.jmetal.problem.integerproblem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.integersolution.IntegerSolution;
import org.uma.jmetal.solution.integersolution.impl.DefaultIntegerSolution;
import pl.edu.agh.casting_dss.criterions.Function;
import pl.edu.agh.casting_dss.data.PossibleValues;
import pl.edu.agh.casting_dss.data.ProductionParameters;
import pl.edu.agh.casting_dss.model.MechanicalPropertiesModel;
import pl.edu.agh.casting_dss.single_criteria_opt.NormConstraint;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ADIIntegerProblem extends AbstractIntegerProblem {
    private final MechanicalPropertiesModel model;
    private final List<Function> objectives;
    private final int thickness;
    private final NormConstraint constraints;

    public ADIIntegerProblem(MechanicalPropertiesModel model, List<Function> objectives, int thickness, NormConstraint constraints) {
        this.model = model;
        this.objectives = objectives;
        this.thickness = thickness;
        this.constraints = constraints;
        setName("ADIInteger");
        setNumberOfVariables(5);

        setNumberOfObjectives(2);
        setNumberOfConstraints(1);

        List<Integer> lowerLimit = Arrays.asList(0, 0, 0, 0, 0);
        PossibleValues possibleValues = model.getPossibleValues();
        List<Integer> upperLimit = Arrays.asList(
                possibleValues.getChemicalCompositions().size() - 1,
                possibleValues.getPossibleAustTemps().size() - 1,
                possibleValues.getPossibleAustTimes().size() - 1,
                possibleValues.getPossibleAusfTemps().size() - 1,
                possibleValues.getPossibleAusfTimes().size() - 1
        );
        setVariableBounds(lowerLimit, upperLimit);
    }

    @Override
    public void evaluate(IntegerSolution integerSolution) {
        ProductionParameters parameters = getProductionParameters(model, integerSolution, thickness);

        for (int i = 0; i < objectives.size(); i++) {
            integerSolution.setObjective(i, objectives.get(i).evaluate(parameters));
        }

        evaluateConstraints(integerSolution, parameters);
    }

    private void evaluateConstraints(IntegerSolution solution, ProductionParameters parameters) {
        if (constraints.validate(parameters, model).passed()) {
            solution.setConstraint(0, 1);
        } else {
            solution.setConstraint(0, -1000000);
        }
    }

    @Override
    public IntegerSolution createSolution() {
        return new DefaultIntegerSolution(getVariableBounds(), getNumberOfObjectives(), getNumberOfConstraints());
    }

    public static ProductionParameters getProductionParameters(MechanicalPropertiesModel model, IntegerSolution integerSolution, int thickness) {
        PossibleValues possibleValues = model.getPossibleValues();
        int chemIdx = integerSolution.getVariable(0);
        int austTempIdx = integerSolution.getVariable(1);
        int austTimeIdx = integerSolution.getVariable(2);
        int ausfTempIdx = integerSolution.getVariable(3);
        int ausfTimeIdx = integerSolution.getVariable(4);
        return new ProductionParameters(possibleValues.getChemicalCompositions().get(chemIdx),
                Map.of(
                        "aust_temp", possibleValues.getPossibleAustTemps().get(austTempIdx),
                        "aust_czas", possibleValues.getPossibleAustTimes().get(austTimeIdx),
                        "ausf_temp", possibleValues.getPossibleAusfTemps().get(ausfTempIdx),
                        "ausf_czas", possibleValues.getPossibleAusfTimes().get(ausfTimeIdx)
                ), thickness);
    }
}
