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
import pl.edu.agh.casting_dss.single_criteria_opt.OptimizedADISolution;

public class SolutionSearchListener implements SearchListener<OptimizedADISolution> {

    private final TextArea searchingLog;

    public SolutionSearchListener(TextArea searchingLog) {
        this.searchingLog = searchingLog;
    }

    @Override
    public void searchStarted(Search<? extends OptimizedADISolution> search) {
        addNewLine("Searching started");
    }

    @Override
    public void searchStopped(Search<? extends OptimizedADISolution> search) {
        addNewLine("Searching stopped");
    }

    @Override
    public void newBestSolution(Search<? extends OptimizedADISolution> search, OptimizedADISolution newBestSolution, Evaluation newBestSolutionEvaluation, Validation newBestSolutionValidation) {
        addNewLine("New best solution found");
    }

    @Override
    public void newCurrentSolution(LocalSearch<? extends OptimizedADISolution> search, OptimizedADISolution newCurrentSolution, Evaluation newCurrentSolutionEvaluation, Validation newCurrentSolutionValidation) {
        addNewLine("New current solution found");
    }

    @Override
    public void stepCompleted(Search<? extends OptimizedADISolution> search, long numSteps) {
        if (numSteps % 100 == 0) {
            addNewLine("Step " + numSteps + " completed");
        }
    }

    @Override
    public void statusChanged(Search<? extends OptimizedADISolution> search, SearchStatus newStatus) {
        addNewLine("Status changed to: " + newStatus);
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
