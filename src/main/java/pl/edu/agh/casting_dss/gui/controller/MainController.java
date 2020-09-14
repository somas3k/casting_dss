package pl.edu.agh.casting_dss.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.jamesframework.core.search.listeners.SearchListener;
import pl.edu.agh.casting_dss.factories.ModelLoadingException;
import pl.edu.agh.casting_dss.gui.model.DSSModel;
import pl.edu.agh.casting_dss.single_criteria_opt.OptimizedADISolution;
import pl.edu.agh.casting_dss.solution.SolutionFinder;
import pl.edu.agh.casting_dss.solution.SolutionSearchListener;
import pl.edu.agh.casting_dss.utils.SystemConfiguration;
import pl.edu.agh.casting_dss.utils.SystemConfigurationUtils;

import java.io.IOException;

import static pl.edu.agh.casting_dss.utils.FormattingUtils.*;

public class MainController {
    public Slider costQualityProportion;
    public TextField thickness;
    public TextField avgIronCost;
    public TextField avgBatchWeight;
    public TextField niPrice;
    public TextField cuPrice;
    public TextField moPrice;
    public Button print;
    public TextArea searchingLog;

    private DSSModel model;
    private SystemConfiguration configuration;

    public void initialize() {
        thickness.setTextFormatter(getNaturalTextFormatter());
        avgIronCost.setTextFormatter(getDoubleTextFormatter());
        avgBatchWeight.setTextFormatter(getDoubleTextFormatter());
        niPrice.setTextFormatter(getDoubleTextFormatter());
        cuPrice.setTextFormatter(getDoubleTextFormatter());
        moPrice.setTextFormatter(getDoubleTextFormatter());
        print.setOnMouseClicked(this::handleClick);
    }

    public void setConfiguration(SystemConfiguration configuration) {
        this.configuration = configuration;
    }

    public void initModel(DSSModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model;
        costQualityProportion.valueProperty().bindBidirectional(model.getCostQualityProportionProperty());
        thickness.textProperty().bindBidirectional(model.getThicknessProperty(), NATURAL_CONVERTER);
        avgIronCost.textProperty().bindBidirectional(model.getAvgIronCostProperty(), DOUBLE_CONVERTER);
        avgBatchWeight.textProperty().bindBidirectional(model.getAvgBatchWeightProperty(), DOUBLE_CONVERTER);
        niPrice.textProperty().bindBidirectional(model.getNiPriceProperty(), DOUBLE_CONVERTER);
        cuPrice.textProperty().bindBidirectional(model.getCuPriceProperty(), DOUBLE_CONVERTER);
        moPrice.textProperty().bindBidirectional(model.getMoPriceProperty(), DOUBLE_CONVERTER);
    }


    public void handleClick(MouseEvent e) {
        try {
            SolutionSearchListener searchListener = new SolutionSearchListener(searchingLog.textProperty());
            SolutionFinder.findSolution(model, configuration, searchListener);
        } catch (IOException | InterruptedException | ModelLoadingException ioException) {
            ioException.printStackTrace();
        }
    }


}
