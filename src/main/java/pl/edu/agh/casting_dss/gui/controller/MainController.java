package pl.edu.agh.casting_dss.gui.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import org.nd4j.common.io.StringUtils;
import pl.edu.agh.casting_dss.gui.model.DSSModel;
import pl.edu.agh.casting_dss.gui.model.ProductionParameters;
import pl.edu.agh.casting_dss.solution.SolutionSearchListener;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.function.Function;

import static pl.edu.agh.casting_dss.utils.FormattingUtils.*;

public class MainController implements Initializable {
    public Slider costQualityProportion;
    public TextField thickness;
    public TextField avgIronCost;
    public TextField avgBatchWeight;
    public TextField niPrice;
    public TextField cuPrice;
    public TextField moPrice;
    public Button run;
    public TextArea searchingLog;
    public Button clear;
    public Label costQualityPropValue;
    public TableView<ProductionParameters> solution;
    public TextField price;
    public TextField quality;
    public Button generateRandomSolution;

    private DSSModel model;


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
        price.textProperty().bindBidirectional(model.getActualPrice(), TWO_DECIMAL_CONVERTER);
        quality.textProperty().bindBidirectional(model.getActualQuality(), TWO_DECIMAL_CONVERTER);
        initTable(model);
    }

    private void initTable(DSSModel model) {
        TableColumn<ProductionParameters, String> chemicalComposition = new TableColumn<>("Skład chemiczny");
        chemicalComposition.setSortable(false);
        chemicalComposition.setResizable(false);
        chemicalComposition.setReorderable(false);
        TableColumn<ProductionParameters, String> heatTreatment = new TableColumn<>("Obróbka termiczna");
        heatTreatment.setSortable(false);
        heatTreatment.setResizable(false);
        heatTreatment.setReorderable(false);
        chemicalComposition.getColumns().addAll(getChemicalCompositionColumns());
        heatTreatment.getColumns().addAll(getHeatTreatmentColumns());
        solution.getColumns().addAll(Arrays.asList(chemicalComposition, heatTreatment));
        solution.setItems(model.getActualSolution());
        solution.setEditable(true);
        solution.setFixedCellSize(25);
        solution.prefHeightProperty().bind(Bindings.size(solution.getItems()).multiply(solution.getFixedCellSize()).add(55));
    }

    private List<TableColumn<ProductionParameters, String>>  getChemicalCompositionColumns() {
        List<TableColumn<ProductionParameters, String>> tableColumns = getTableColumns(ProductionParameters.chemicalCompositionParamNames, StringUtils::capitalize, TWO_DECIMAL_CONVERTER);
        tableColumns.forEach(column -> column.setPrefWidth(40));
        return tableColumns;
    }

    private List<TableColumn<ProductionParameters, String>> getHeatTreatmentColumns() {
        List<TableColumn<ProductionParameters, String>> tableColumns = getTableColumns(ProductionParameters.heatTreatmentParamNames, Function.identity(), NATURAL_CONVERTER);
        tableColumns.forEach(column -> column.setPrefWidth(80));
        return tableColumns;
    }

    private <T extends Number> List<TableColumn<ProductionParameters, String>> getTableColumns(List<String> paramNames, Function<String, String> nameConverter, StringConverter<T> valueConverter) {
        List<TableColumn<ProductionParameters, String>> columns = new ArrayList<>();
        for (String paramName : paramNames) {
            TableColumn<ProductionParameters, String> column = new TableColumn<>(nameConverter.apply(paramName));
            column.setCellValueFactory(cellDataFeatures -> new SimpleObjectProperty<>(valueConverter.toString((T) cellDataFeatures.getValue().getParam(paramName))));
            column.setCellFactory(TextFieldTableCell.forTableColumn());
            column.setOnEditCommit(cellEditEvent -> model.getActualSolution().get(0).setParam(paramName, valueConverter.fromString(cellEditEvent.getNewValue())));
            column.setSortable(false);
            column.setResizable(false);
            column.setReorderable(false);
            columns.add(column);
        }
        return columns;
    }

    public void handleClick(MouseEvent e) {
        SolutionSearchListener searchListener = new SolutionSearchListener(searchingLog);
        model.getFinder().findSolution(model, searchListener);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        thickness.setTextFormatter(getNaturalTextFormatter());
        avgIronCost.setTextFormatter(getDoubleTextFormatter());
        avgBatchWeight.setTextFormatter(getDoubleTextFormatter());
        niPrice.setTextFormatter(getDoubleTextFormatter());
        cuPrice.setTextFormatter(getDoubleTextFormatter());
        moPrice.setTextFormatter(getDoubleTextFormatter());
        run.setOnMouseClicked(this::handleClick);
        clear.setOnMouseClicked(mouseEvent -> searchingLog.clear());
        costQualityPropValue.textProperty().bindBidirectional(costQualityProportion.valueProperty(), (StringConverter) TWO_DECIMAL_CONVERTER);
        generateRandomSolution.setOnMouseClicked(mouseEvent -> {
            if (model != null) {
                model.generateRandomSolution();
            }
        });
    }
}
