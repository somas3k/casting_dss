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

public class SolutionSearchListener implements SearchListener<OptimizedADISolution> {

    private final TextArea searchingLog;
    private final DSSModel model;
    private int stepStep = 100;

    public SolutionSearchListener(TextArea searchingLog, DSSModel model) {
        this.searchingLog = searchingLog;
        this.model = model;
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
        addNewLine(String.format("Znaleziono nowe najlepsze rozwiązanie, koszt: %.2f, jakość: %.2f", evaluation.getCostValue(), evaluation.getQualityValue()));
        Platform.runLater(() -> model.setActualSolution(newBestSolution.getProductionParameters()));
    }

    @Override
    public void newCurrentSolution(LocalSearch<? extends OptimizedADISolution> search, OptimizedADISolution newCurrentSolution, Evaluation newCurrentSolutionEvaluation, Validation newCurrentSolutionValidation) {
//        addNewLine("New current solution found");
    }

    @Override
    public void stepCompleted(Search<? extends OptimizedADISolution> search, long numSteps) {
        if (numSteps % stepStep == 0) {
            addNewLine("Krok " + numSteps + " zakończony");
            if (numSteps > 1000) {
                stepStep = 1000;
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
