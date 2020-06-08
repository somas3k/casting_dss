package single_criteria_opt;

import model.MechanicalPropertiesModel;
import data.ProductionParameters;
import org.jamesframework.core.problems.sol.RandomSolutionGenerator;

import java.util.List;
import java.util.Random;

public class ADISolutionGenerator implements RandomSolutionGenerator<OptimizedADISolution, MechanicalPropertiesModel> {

    @Override
    public OptimizedADISolution create(Random random, MechanicalPropertiesModel model) {
        return new OptimizedADISolution(new ProductionParameters(
                getRandomElementFromList(model.getChemicalCompositions(), random),
                getRandomElementFromList(model.getPossibleAustTemps(), random),
                getRandomElementFromList(model.getPossibleAustTimes(), random),
                getRandomElementFromList(model.getPossibleAusfTemps(), random),
                getRandomElementFromList(model.getPossibleAusfTimes(), random))
        );
    }


    private static <T> T getRandomElementFromList(List<T> list, Random random) {
        return list.get(random.nextInt(list.size()));
    }
}
