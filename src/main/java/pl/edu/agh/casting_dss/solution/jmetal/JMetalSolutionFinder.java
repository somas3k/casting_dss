package pl.edu.agh.casting_dss.solution.jmetal;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.espea.util.AchievementScalarizationComparator;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.example.AlgorithmRunner;
import org.uma.jmetal.operator.crossover.impl.IntegerSBXCrossover;
import org.uma.jmetal.operator.mutation.impl.IntegerPolynomialMutation;
import org.uma.jmetal.operator.selection.impl.BestSolutionSelection;
import org.uma.jmetal.operator.selection.impl.BinaryTournamentSelection;
import org.uma.jmetal.solution.integersolution.IntegerSolution;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import pl.edu.agh.casting_dss.criterions.CostFunction;
import pl.edu.agh.casting_dss.criterions.Function;
import pl.edu.agh.casting_dss.criterions.QualityFunction;
import pl.edu.agh.casting_dss.criterions.ReversedQualityFunction;
import pl.edu.agh.casting_dss.data.ProductionParameters;
import pl.edu.agh.casting_dss.gui.model.DSSModel;
import pl.edu.agh.casting_dss.model.MechanicalPropertiesModel;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static pl.edu.agh.casting_dss.Main.EXECUTOR_SERVICE;

public class JMetalSolutionFinder {

    private final MechanicalPropertiesModel model;
    private final Function costFunction;
    private final Function qualityFunction;

    public JMetalSolutionFinder(MechanicalPropertiesModel model, CostFunction cFunction, QualityFunction qFunction) {
        this.model = model;
        this.costFunction = cFunction;
        this.qualityFunction = new ReversedQualityFunction(qFunction);
    }

    public void findSolution(DSSModel dssModel, Consumer<List<ProductionParameters>> resultConsumer) {
        ADIIntegerProblem problem = new ADIIntegerProblem(model, Arrays.asList(costFunction, qualityFunction), dssModel.getThickness(), dssModel.calculateMatchingNorm());
        Algorithm<List<IntegerSolution>> algo = new NSGAII<>(problem, 10000, 2000, 1000, 1000,
                new IntegerSBXCrossover(0.4, 0.5),
                new IntegerPolynomialMutation(0.3, 0.6),
                new BestSolutionSelection<>(new AchievementScalarizationComparator<>(0)),
                new SequentialSolutionListEvaluator<>());


        EXECUTOR_SERVICE.execute(() -> {
            new AlgorithmRunner.Executor(algo).execute();
            List<IntegerSolution> solutions = algo.getResult();
            resultConsumer.accept(translateResults(solutions, dssModel.getThickness()));
        });
    }

    private List<ProductionParameters> translateResults(List<IntegerSolution> solutions, int thickness) {
        return solutions.stream()
                .map(intSol -> ADIIntegerProblem.getProductionParameters(model, intSol, thickness))
                .collect(Collectors.toList());
    }
}
