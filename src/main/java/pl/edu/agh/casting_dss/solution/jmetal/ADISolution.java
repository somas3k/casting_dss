package pl.edu.agh.casting_dss.solution.jmetal;

import org.apache.commons.lang3.tuple.Pair;
import org.uma.jmetal.solution.AbstractSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.integersolution.IntegerSolution;

import java.util.List;

public class ADISolution extends AbstractSolution<Integer> implements IntegerSolution {
    private List<Pair<Integer, Integer>> bounds;


    protected ADISolution(List<Pair<Integer, Integer>> bounds, int numberOfVariables, int numberOfObjectives) {
        super(numberOfVariables, numberOfObjectives, 1);
        this.bounds = bounds;
    }

    @Override
    public Integer getLowerBound(int index) {
        return bounds.get(index).getLeft();
    }

    @Override
    public Integer getUpperBound(int index) {
        return bounds.get(index).getRight();
    }

    @Override
    public Solution<Integer> copy() {
        return null;
    }
}
