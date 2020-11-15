package pl.edu.agh.casting_dss.solution;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import org.jamesframework.core.problems.constraints.validations.Validation;
import org.jamesframework.core.problems.objectives.evaluations.Evaluation;
import org.jamesframework.core.search.LocalSearch;
import org.jamesframework.core.search.Search;
import org.jamesframework.core.search.listeners.SearchListener;
import org.jamesframework.core.search.status.SearchStatus;
import pl.edu.agh.casting_dss.gui.model.DSSModel;
import pl.edu.agh.casting_dss.single_criteria_opt.DetailedEvaluation;
import pl.edu.agh.casting_dss.single_criteria_opt.OptimizedADISolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SolutionSearchListener implements SearchListener<OptimizedADISolution> {

    private final TextArea searchingLog;
    private final DSSModel model;
    private int stepStep = 100;
    private long lastStep = 0;
    private List<List<Object>> history = new ArrayList<>();


    public SolutionSearchListener(TextArea searchingLog, DSSModel model) {
        this.searchingLog = searchingLog;
        this.model = model;
        history.add(Arrays.asList("Step", "Type", "Cost", "Quality", "QC"));
    }

    @Override
    public void searchStarted(Search<? extends OptimizedADISolution> search) {
        addNewLine("Przeszukiwanie rozpoczęte");
    }

    @Override
    public void searchStopped(Search<? extends OptimizedADISolution> search) {
        addNewLine("Przeszukiwanie zakończone");
    }

    @Override
    public void newBestSolution(Search<? extends OptimizedADISolution> search, OptimizedADISolution newBestSolution, Evaluation newBestSolutionEvaluation, Validation newBestSolutionValidation) {
        DetailedEvaluation evaluation = (DetailedEvaluation) newBestSolutionEvaluation;
        addNewLine(String.format("Krok %d: Znaleziono nowe najlepsze rozwiązanie, koszt: %.2f, jakość: %.2f", lastStep, evaluation.getCostValue(), evaluation.getQualityValue()));
        history.add(Arrays.asList(lastStep, "BEST", evaluation.getCostValue(), evaluation.getQualityValue(), evaluation.getValue()));
        Platform.runLater(() -> model.setActualSolution(newBestSolution.getProductionParameters()));
    }

    @Override
    public void newCurrentSolution(LocalSearch<? extends OptimizedADISolution> search, OptimizedADISolution newCurrentSolution, Evaluation newCurrentSolutionEvaluation, Validation newCurrentSolutionValidation) {
        DetailedEvaluation evaluation = (DetailedEvaluation) newCurrentSolutionEvaluation;
//        addNewLine(String.format("Krok %d: Nowe obecne rozwiazanie, koszt: %.2f, jakość: %.2f, poprawne: %s", lastStep, evaluation.getCostValue(), evaluation.getQualityValue(), newCurrentSolutionValidation.passed() ? "tak": "nie"));
        history.add(Arrays.asList(lastStep, "CURRENT", evaluation.getCostValue(), evaluation.getQualityValue(), evaluation.getValue()));
    }

    @Override
    public void stepCompleted(Search<? extends OptimizedADISolution> search, long numSteps) {
        lastStep = numSteps;
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
