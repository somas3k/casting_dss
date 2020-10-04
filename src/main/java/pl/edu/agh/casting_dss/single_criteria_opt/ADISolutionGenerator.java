package pl.edu.agh.casting_dss.single_criteria_opt;

import lombok.AllArgsConstructor;
import lombok.Setter;
import pl.edu.agh.casting_dss.model.MechanicalPropertiesModel;
import pl.edu.agh.casting_dss.data.ProductionParameters;
import org.jamesframework.core.problems.sol.RandomSolutionGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@AllArgsConstructor
@Setter
public class ADISolutionGenerator implements RandomSolutionGenerator<OptimizedADISolution, MechanicalPropertiesModel> {
    private int thickness;
    private NormConstraint constraint;

    @Override
    public synchronized OptimizedADISolution create(Random random, MechanicalPropertiesModel model) {
        OptimizedADISolution optimizedADISolution;

        do {
            optimizedADISolution = new OptimizedADISolution(new ProductionParameters(
                    getRandomElementFromList(model.getPossibleValues().getChemicalCompositions(), random),
                    new HashMap<>(Map.of(
                            "aust_temp", getRandomElementFromList(model.getPossibleValues().getPossibleAustTemps(), random),
                            "aust_czas", getRandomElementFromList(model.getPossibleValues().getPossibleAustTimes(), random),
                            "ausf_temp", getRandomElementFromList(model.getPossibleValues().getPossibleAusfTemps(), random),
                            "ausf_czas", getRandomElementFromList(model.getPossibleValues().getPossibleAusfTimes(), random)
                    )),
                    thickness)
            );
        } while (!constraint.validate(optimizedADISolution, model).passed());

        return optimizedADISolution;
    }

    private static <T> T getRandomElementFromList(List<T> list, Random random) {
        return list.get(random.nextInt(list.size()));
    }
}
