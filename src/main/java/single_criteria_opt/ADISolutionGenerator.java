package single_criteria_opt;

import lombok.AllArgsConstructor;
import lombok.Setter;
import model.MechanicalPropertiesModel;
import data.ProductionParameters;
import org.jamesframework.core.problems.sol.RandomSolutionGenerator;

import java.util.List;
import java.util.Random;

@AllArgsConstructor
public class ADISolutionGenerator implements RandomSolutionGenerator<OptimizedADISolution, MechanicalPropertiesModel> {
    private final int thickness;

    @Override
    public OptimizedADISolution create(Random random, MechanicalPropertiesModel model) {
        return new OptimizedADISolution(new ProductionParameters(
                getRandomElementFromList(model.getPossibleValues().getChemicalCompositions(), random),
                getRandomElementFromList(model.getPossibleValues().getPossibleAustTemps(), random),
                getRandomElementFromList(model.getPossibleValues().getPossibleAustTimes(), random),
                getRandomElementFromList(model.getPossibleValues().getPossibleAusfTemps(), random),
                getRandomElementFromList(model.getPossibleValues().getPossibleAusfTimes(), random),
                thickness)
        );
    }


    private static <T> T getRandomElementFromList(List<T> list, Random random) {
        return list.get(random.nextInt(list.size()));
    }
}
