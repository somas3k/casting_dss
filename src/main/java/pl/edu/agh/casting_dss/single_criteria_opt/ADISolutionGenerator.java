package pl.edu.agh.casting_dss.single_criteria_opt;

import lombok.AllArgsConstructor;
import pl.edu.agh.casting_dss.model.MechanicalPropertiesModel;
import pl.edu.agh.casting_dss.data.ProductionParameters;
import org.jamesframework.core.problems.sol.RandomSolutionGenerator;

import java.util.List;
import java.util.Random;

@AllArgsConstructor
public class ADISolutionGenerator implements RandomSolutionGenerator<OptimizedADISolution, MechanicalPropertiesModel> {
    private final int thickness;
    private final NormConstraints constraints;


    @Override
    public OptimizedADISolution create(Random random, MechanicalPropertiesModel model) {
        OptimizedADISolution optimizedADISolution;

        do {
            optimizedADISolution = new OptimizedADISolution(new ProductionParameters(
                    getRandomElementFromList(model.getPossibleValues().getChemicalCompositions(), random),
                    getRandomElementFromList(model.getPossibleValues().getPossibleAustTemps(), random),
                    getRandomElementFromList(model.getPossibleValues().getPossibleAustTimes(), random),
                    getRandomElementFromList(model.getPossibleValues().getPossibleAusfTemps(), random),
                    getRandomElementFromList(model.getPossibleValues().getPossibleAusfTimes(), random),
                    thickness)
            );
        } while (!constraints.validate(optimizedADISolution, model).passed());

        return optimizedADISolution;
    }

    private static <T> T getRandomElementFromList(List<T> list, Random random) {
        return list.get(random.nextInt(list.size()));
    }
}
