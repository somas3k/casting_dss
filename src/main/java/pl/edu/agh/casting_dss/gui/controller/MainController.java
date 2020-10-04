package pl.edu.agh.casting_dss.gui.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import org.jamesframework.core.search.Search;
import org.nd4j.common.io.StringUtils;
import pl.edu.agh.casting_dss.data.NormType;
import pl.edu.agh.casting_dss.criterions.ProductionRange;
import pl.edu.agh.casting_dss.data.MechanicalProperties;
import pl.edu.agh.casting_dss.gui.model.DSSModel;
import pl.edu.agh.casting_dss.gui.model.ProductionParametersModel;
import pl.edu.agh.casting_dss.single_criteria_opt.NormConstraint;
import pl.edu.agh.casting_dss.single_criteria_opt.OptimizedADISolution;
import pl.edu.agh.casting_dss.solution.SolutionSearchListener;

import java.net.URL;
import java.util.*;
import java.util.function.Function;

import static pl.edu.agh.casting_dss.data.Norms.THICKNESS_RANGES_ORDER;
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
    public Button stop;
    public Button clear;
    public TextArea searchingLog;
    public Label costQualityPropValue;
    public TableView<ProductionParametersModel> solution;
    public TextField price;
    public TextField quality;
    public Button generateRandomSolution;
    public TextField rm;
    public TextField rp02;
    public TextField a5;
    public TextField hb;
    public TableView<ProductionRange> productionRanges;
    public ComboBox<NormType> norm;
    public TableView<NormConstraint> norms;
    public ComboBox<String> thicknessRangeChooser;

    private DSSModel model;
    private Search<OptimizedADISolution> searchReference;

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
        rm.textProperty().bindBidirectional(model.getMechanicalPropertiesProperty(), getConverterForField(MechanicalProperties::getRm));
        rp02.textProperty().bindBidirectional(model.getMechanicalPropertiesProperty(), getConverterForField(MechanicalProperties::getRp02));
        a5.textProperty().bindBidirectional(model.getMechanicalPropertiesProperty(), getConverterForField(MechanicalProperties::getA5));
        hb.textProperty().bindBidirectional(model.getMechanicalPropertiesProperty(), getConverterForField(MechanicalProperties::getHb));
        initRanges(model.getRanges());
        initTable(model);
        thicknessRangeChooser.getItems().addAll(THICKNESS_RANGES_ORDER);
        initNorms();
        thicknessRangeChooser.getSelectionModel().select(0);
        thicknessRangeChooser.getSelectionModel().selectedItemProperty().addListener((observableValue, old, neew) -> norms.setItems(FXCollections.observableArrayList(model.getNorms().getNorms().get(neew))));
        norm.getItems().addAll(NormType.values());
        norm.getSelectionModel().select(0);
        model.getSelectedNormType().bind(norm.getSelectionModel().selectedItemProperty());
    }

    private void initNorms() {
        TableColumn<NormConstraint, NormType> symbol = new TableColumn<>("Symbol");
        symbol.setCellValueFactory(cellDataFeatures -> new SimpleObjectProperty<>(cellDataFeatures.getValue().getType()));
        symbol.setEditable(false);
        symbol.setSortable(false);
        symbol.setReorderable(false);

        TableColumn<NormConstraint, Double> rmMinimum = new TableColumn<>("Rm min");
        rmMinimum.setCellFactory(TextFieldTableCell.forTableColumn(TWO_DECIMAL_CONVERTER));
        rmMinimum.setCellValueFactory(cellDataFeatures -> new SimpleObjectProperty<>(cellDataFeatures.getValue().getRmMin()));
        rmMinimum.setOnEditCommit(cellEditEvent -> cellEditEvent.getRowValue().setRmMin(cellEditEvent.getNewValue()));
        rmMinimum.setEditable(true);
        rmMinimum.setSortable(false);
        rmMinimum.setReorderable(false);

        TableColumn<NormConstraint, Double> rp02Minimum = new TableColumn<>("Rp02 min");
        rp02Minimum.setCellFactory(TextFieldTableCell.forTableColumn(TWO_DECIMAL_CONVERTER));
        rp02Minimum.setCellValueFactory(cellDataFeatures -> new SimpleObjectProperty<>(cellDataFeatures.getValue().getRp02Min()));
        rp02Minimum.setOnEditCommit(cellEditEvent -> cellEditEvent.getRowValue().setRp02Min(cellEditEvent.getNewValue()));
        rp02Minimum.setEditable(true);
        rp02Minimum.setSortable(false);
        rp02Minimum.setReorderable(false);

        TableColumn<NormConstraint, Double> a5Minimum = new TableColumn<>("A5 min");
        a5Minimum.setCellFactory(TextFieldTableCell.forTableColumn(TWO_DECIMAL_CONVERTER));
        a5Minimum.setCellValueFactory(cellDataFeatures -> new SimpleObjectProperty<>(cellDataFeatures.getValue().getA5Min()));
        a5Minimum.setOnEditCommit(cellEditEvent -> cellEditEvent.getRowValue().setA5Min(cellEditEvent.getNewValue()));
        a5Minimum.setEditable(true);
        a5Minimum.setSortable(false);
        a5Minimum.setReorderable(false);

        TableColumn<NormConstraint, Double> hbMinimum = new TableColumn<>("HB min");
        hbMinimum.setCellFactory(TextFieldTableCell.forTableColumn(TWO_DECIMAL_CONVERTER));
        hbMinimum.setCellValueFactory(cellDataFeatures -> new SimpleObjectProperty<>(cellDataFeatures.getValue().getHbMin()));
        hbMinimum.setOnEditCommit(cellEditEvent -> cellEditEvent.getRowValue().setHbMin(cellEditEvent.getNewValue()));
        hbMinimum.setEditable(true);
        hbMinimum.setSortable(false);
        hbMinimum.setReorderable(false);

        TableColumn<NormConstraint, Double> hbMaximum = new TableColumn<>("HB max");
        hbMaximum.setCellFactory(TextFieldTableCell.forTableColumn(TWO_DECIMAL_CONVERTER));
        hbMaximum.setCellValueFactory(cellDataFeatures -> new SimpleObjectProperty<>(cellDataFeatures.getValue().getHbMax()));
        hbMaximum.setOnEditCommit(cellEditEvent -> cellEditEvent.getRowValue().setHbMax(cellEditEvent.getNewValue()));
        hbMaximum.setEditable(true);
        hbMaximum.setSortable(false);
        hbMaximum.setReorderable(false);

        TableColumn<NormConstraint, Double> kMinimum = new TableColumn<>("K min");
        kMinimum.setCellFactory(TextFieldTableCell.forTableColumn(TWO_DECIMAL_CONVERTER));
        kMinimum.setCellValueFactory(cellDataFeatures -> new SimpleObjectProperty<>(cellDataFeatures.getValue().getKkMin()));
        kMinimum.setOnEditCommit(cellEditEvent -> cellEditEvent.getRowValue().setKkMin(cellEditEvent.getNewValue()));
        kMinimum.setEditable(true);
        kMinimum.setSortable(false);
        kMinimum.setReorderable(false);

        norms.getColumns().addAll(Arrays.asList(symbol, rmMinimum, rp02Minimum, a5Minimum, hbMinimum, hbMaximum, kMinimum));
        norms.setItems(FXCollections.observableArrayList(model.getNorms().getNorms().get(THICKNESS_RANGES_ORDER.get(0))));
        norms.setEditable(true);
    }

    private void initRanges(ObservableList<ProductionRange> ranges) {
        TableColumn<ProductionRange, String> paramName = new TableColumn<>("Nazwa parametru");
        paramName.setCellFactory(TextFieldTableCell.forTableColumn());
        paramName.setOnEditCommit(cellEditEvent -> cellEditEvent.getRowValue().setParamName(cellEditEvent.getNewValue()));
        paramName.setCellValueFactory(cellDataFeatures -> {
            SimpleObjectProperty<String> property = new SimpleObjectProperty<>(cellDataFeatures.getValue().getParamName());
            property.addListener((observableValue, s, t1) -> recalculateQuality());
            return property;
        });
        paramName.setSortable(false);
        paramName.setReorderable(false);
        TableColumn<ProductionRange, Double> minimum = new TableColumn<>("Minimum");
        minimum.setCellFactory(TextFieldTableCell.forTableColumn(TWO_DECIMAL_CONVERTER));
        minimum.setOnEditCommit(cellEditEvent -> cellEditEvent.getRowValue().setMinimum(cellEditEvent.getNewValue()));
        minimum.setCellValueFactory(cellDataFeatures -> {
            SimpleObjectProperty<Double> property = new SimpleObjectProperty<>(cellDataFeatures.getValue().getMinimum());
            property.addListener((observableValue, s, t1) -> recalculateQuality());
            return property;
        });
        minimum.setSortable(false);
        minimum.setReorderable(false);
        TableColumn<ProductionRange, Double> maximum = new TableColumn<>("Maksimum");
        maximum.setCellFactory(TextFieldTableCell.forTableColumn(TWO_DECIMAL_CONVERTER));
        maximum.setOnEditCommit(cellEditEvent -> cellEditEvent.getRowValue().setMaximum(cellEditEvent.getNewValue()));
        maximum.setCellValueFactory(cellDataFeatures -> {
            SimpleObjectProperty<Double> property = new SimpleObjectProperty<>(cellDataFeatures.getValue().getMaximum());
            property.addListener((observableValue, s, t1) -> recalculateQuality());
            return property;
        });
        maximum.setSortable(false);
        maximum.setReorderable(false);
        TableColumn<ProductionRange, Double> weight = new TableColumn<>("Waga");
        weight.setCellFactory(TextFieldTableCell.forTableColumn(TWO_DECIMAL_CONVERTER));
        weight.setOnEditCommit(cellEditEvent -> cellEditEvent.getRowValue().setWeight(cellEditEvent.getNewValue()));
        weight.setCellValueFactory(cellDataFeatures -> {
            SimpleObjectProperty<Double> property = new SimpleObjectProperty<>(cellDataFeatures.getValue().getWeight());
            property.addListener((observableValue, s, t1) -> recalculateQuality());
            return property;
        });
        weight.setSortable(false);
        weight.setReorderable(false);
        productionRanges.getColumns().addAll(Arrays.asList(paramName, minimum, maximum, weight));
        productionRanges.setItems(ranges);
        productionRanges.setEditable(true);
    }

    private void recalculateQuality() {
        model.updateCostAndQuality();
    }

    private void initTable(DSSModel model) {
        TableColumn<ProductionParametersModel, String> chemicalComposition = new TableColumn<>("Skład chemiczny");
        chemicalComposition.setSortable(false);
        chemicalComposition.setResizable(false);
        chemicalComposition.setReorderable(false);
        TableColumn<ProductionParametersModel, String> heatTreatment = new TableColumn<>("Obróbka termiczna");
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

    private List<TableColumn<ProductionParametersModel, String>>  getChemicalCompositionColumns() {
        List<TableColumn<ProductionParametersModel, String>> tableColumns = getTableColumns(ProductionParametersModel.chemicalCompositionParamNames, StringUtils::capitalize, TWO_DECIMAL_CONVERTER);
        tableColumns.forEach(column -> column.setPrefWidth(40));
        return tableColumns;
    }

    private List<TableColumn<ProductionParametersModel, String>> getHeatTreatmentColumns() {
        List<TableColumn<ProductionParametersModel, String>> tableColumns = getTableColumns(ProductionParametersModel.heatTreatmentParamNames, Function.identity(), NATURAL_CONVERTER);
        tableColumns.forEach(column -> column.setPrefWidth(85));
        return tableColumns;
    }

    private <T extends Number> List<TableColumn<ProductionParametersModel, String>> getTableColumns(List<String> paramNames, Function<String, String> nameConverter, StringConverter<T> valueConverter) {
        List<TableColumn<ProductionParametersModel, String>> columns = new ArrayList<>();
        for (String paramName : paramNames) {
            TableColumn<ProductionParametersModel, String> column = new TableColumn<>(nameConverter.apply(paramName));
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

    public void handleRun(MouseEvent e) {
        SolutionSearchListener searchListener = new SolutionSearchListener(searchingLog, model);
        if (validateNorms()) {
            searchReference = model.getFinder().findSolution(model, searchListener);
        } else {
            new Alert(Alert.AlertType.ERROR,"Wybrana norma nie została uzupełniona\ndla podanej grubości", ButtonType.CANCEL).show();
        }
    }

    private boolean validateNorms() {
        NormConstraint constraint = model.calculateMatchingNorm();
        return constraint.normHasAllRequiredValues();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        thickness.setTextFormatter(getNaturalTextFormatter());
        avgIronCost.setTextFormatter(getDoubleTextFormatter());
        avgBatchWeight.setTextFormatter(getDoubleTextFormatter());
        niPrice.setTextFormatter(getDoubleTextFormatter());
        cuPrice.setTextFormatter(getDoubleTextFormatter());
        moPrice.setTextFormatter(getDoubleTextFormatter());
        run.setOnMouseClicked(this::handleRun);
        stop.setOnMouseClicked(this::handleStop);
        clear.setOnMouseClicked(mouseEvent -> searchingLog.clear());
        costQualityPropValue.textProperty().bindBidirectional(costQualityProportion.valueProperty(), (StringConverter) TWO_DECIMAL_CONVERTER);
        generateRandomSolution.setOnMouseClicked(mouseEvent -> {
            if (model != null) {
                model.generateRandomSolution();
            }
        });
    }

    private void handleStop(MouseEvent e) {
        if (searchReference != null) {
            searchReference.stop();
        }
    }
}
