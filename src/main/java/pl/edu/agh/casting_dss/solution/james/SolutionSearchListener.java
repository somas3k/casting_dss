package pl.edu.agh.casting_dss.solution.james;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jamesframework.core.problems.constraints.validations.Validation;
import org.jamesframework.core.problems.objectives.evaluations.Evaluation;
import org.jamesframework.core.search.LocalSearch;
import org.jamesframework.core.search.Search;
import org.jamesframework.core.search.algo.RandomSearch;
import org.jamesframework.core.search.listeners.SearchListener;
import org.jamesframework.core.search.status.SearchStatus;
import pl.edu.agh.casting_dss.gui.model.DSSModel;
import pl.edu.agh.casting_dss.model.MechanicalPropertiesModel;
import pl.edu.agh.casting_dss.single_criteria_opt.DetailedEvaluation;
import pl.edu.agh.casting_dss.single_criteria_opt.QCFunction;

import java.util.*;

@Getter
public class SolutionSearchListener implements SearchListener<OptimizedADISolution> {

    private TextArea searchingLog;
    private final DSSModel model;
    private int stepStep = 100;
    private long lastStep = 1;
    private List<RunStep> history = new ArrayList<>();
    private long startTime = System.currentTimeMillis();

    public void resetHistory() {
        history = new ArrayList<>();
        lastStep = 0;
    }

    public SolutionSearchListener(TextArea searchingLog, DSSModel model) {
        this.searchingLog = searchingLog;
        this.model = model;
    }

    public SolutionSearchListener(DSSModel model) {
        this.model = model;
    }

    @Override
    public void searchStarted(Search<? extends OptimizedADISolution> search) {
        addNewLine("Przeszukiwanie rozpoczęte");
        if (search instanceof RandomSearch) {

        }
        startTime = System.currentTimeMillis();
    }

    @Override
    public void searchStopped(Search<? extends OptimizedADISolution> search) {
        addNewLine("Przeszukiwanie zakończone");
    }

    @Override
    public void newBestSolution(Search<? extends OptimizedADISolution> search, OptimizedADISolution newBestSolution, Evaluation newBestSolutionEvaluation, Validation newBestSolutionValidation) {
        DetailedEvaluation evaluation = (DetailedEvaluation) newBestSolutionEvaluation;
        addNewLine(String.format("Krok %d: Znaleziono nowe najlepsze rozwiązanie, koszt: %.2f, jakość: %.2f", lastStep, evaluation.getCostValue(), evaluation.getQualityValue()));
        RunStep newBest = new RunStep(lastStep, "BEST", evaluation.getCostValue(), evaluation.getQualityValue(), evaluation.getValue(), QCFunction.EVALUATIONS, MechanicalPropertiesModel.EVALUATIONS, System.currentTimeMillis() - startTime);
        if (history.size() == 0 || history.get(history.size() - 1).qc != newBest.qc) {
            history.add(newBest);
        }
        history.add(newBest);
        if (searchingLog != null) {
            Platform.runLater(() -> model.setActualSolution(newBestSolution.getProductionParameters()));
        }
    }

    @Override
    public void newCurrentSolution(LocalSearch<? extends OptimizedADISolution> search, OptimizedADISolution newCurrentSolution, Evaluation newCurrentSolutionEvaluation, Validation newCurrentSolutionValidation) {
        DetailedEvaluation evaluation = (DetailedEvaluation) newCurrentSolutionEvaluation;
//        addNewLine(String.format("Krok %d: Nowe obecne rozwiazanie, koszt: %.2f, jakość: %.2f, poprawne: %s", lastStep, evaluation.getCostValue(), evaluation.getQualityValue(), newCurrentSolutionValidation.passed() ? "tak": "nie"));
//        history.add(new RunStep(lastStep, "BEST", evaluation.getCostValue(), evaluation.getQualityValue(), evaluation.getValue(), QCFunction.EVALUATIONS, MechanicalPropertiesModel.EVALUATIONS, System.currentTimeMillis() - startTime));
    }

    @Override
    public void stepCompleted(Search<? extends OptimizedADISolution> search, long numSteps) {
        lastStep = numSteps+1;
        if (numSteps % stepStep == 0) {
            addNewLine("Krok " + numSteps + " zakończony");
            if (numSteps > 1000) {
                stepStep = 1000;
            }
            if (numSteps > 10000) {
                stepStep = 10000;
            }
        }
    }

    @Override
    public void statusChanged(Search<? extends OptimizedADISolution> search, SearchStatus newStatus) {
        //addNewLine("Zmiana statusu: " + newStatus);
    }

    private void addNewLine(String text) {
        if (searchingLog != null) {
            Platform.runLater(() -> {
                String actualText = searchingLog.getText();
                if (actualText != null && !actualText.equals("")) {
                    searchingLog.appendText("\n" + text);
                } else {
                    searchingLog.setText(text);
                }
            });
        }
    }
}
