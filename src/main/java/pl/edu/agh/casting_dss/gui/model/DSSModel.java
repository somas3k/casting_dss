package pl.edu.agh.casting_dss.gui.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import lombok.Getter;
import pl.edu.agh.casting_dss.criterions.CostFunction;
import pl.edu.agh.casting_dss.criterions.ProductionRange;
import pl.edu.agh.casting_dss.criterions.QualityFunction;
import pl.edu.agh.casting_dss.data.*;
import pl.edu.agh.casting_dss.factories.ModelLoadingException;
import pl.edu.agh.casting_dss.model.MechanicalPropertiesModel;
import pl.edu.agh.casting_dss.model.Model;
import pl.edu.agh.casting_dss.single_criteria_opt.ADISolutionGenerator;
import pl.edu.agh.casting_dss.single_criteria_opt.NormConstraint;
import pl.edu.agh.casting_dss.solution.SearchType;
import pl.edu.agh.casting_dss.solution.SolutionFinder;
import pl.edu.agh.casting_dss.utils.MechanicalProperty;
import pl.edu.agh.casting_dss.utils.SystemConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static pl.edu.agh.casting_dss.factories.KerasModelFactory.KERAS_MODEL_FACTORY;
import static pl.edu.agh.casting_dss.factories.PossibleValuesFactory.POSSIBLE_VALUES_FACTORY;
import static pl.edu.agh.casting_dss.factories.XGBModelFactory.XGB_MODEL_FACTORY;
import static pl.edu.agh.casting_dss.utils.MechanicalProperty.*;

@Getter
public class DSSModel {
    private final ObjectProperty<Integer> thicknessProperty = new SimpleObjectProperty<>(25);
    private final ObjectProperty<Integer> maxRuntime = new SimpleObjectProperty<>(60);
    private final ObjectProperty<Double> avgIronCostProperty = new SimpleObjectProperty<>(1350.0);
    private final ObjectProperty<Double> avgBatchWeightProperty = new SimpleObjectProperty<>(200.0);
    private final ObjectProperty<Double> niPriceProperty = new SimpleObjectProperty<>(16.0);
    private final ObjectProperty<Double> cuPriceProperty = new SimpleObjectProperty<>(12.0);
    private final ObjectProperty<Double> moPriceProperty = new SimpleObjectProperty<>(7.0);
    private final ObjectProperty<Double> actualPrice = new SimpleObjectProperty<>(0.0);
    private final ObjectProperty<Double> actualQuality = new SimpleObjectProperty<>(0.0);
    private final DoubleProperty costQualityProportionProperty = new SimpleDoubleProperty(0.5);
    private final ObjectProperty<MechanicalProperties> mechanicalPropertiesProperty = new SimpleObjectProperty<>(new MechanicalProperties());
    private final ObjectProperty<NormType> selectedNormType = new SimpleObjectProperty<>(NormType.GJS_800_10);
    private final ObjectProperty<NormConstraint> matchingNormProperty = new SimpleObjectProperty<>();
    private final ObservableList<ProductionParametersModel> actualSolution = FXCollections.observableArrayList();
    private final SolutionFinder finder;
    private final ObservableList<ProductionRange> ranges;
    private final Norms norms;
    private final ADISolutionGenerator generator;
    private ProductionParametersModel savedSolution;
    private SearchType searchType = SearchType.RANDOM_SEARCH;

    public DSSModel(SystemConfiguration configuration, List<ProductionRange> ranges, Norms norms) throws IOException, ModelLoadingException {
        this.ranges = FXCollections.observableArrayList(ranges);
        this.norms = norms;
        matchingNormProperty.setValue(calculateMatchingNorm());
        selectedNormType.addListener((observableValue, normType, t1) -> matchingNormProperty.set(calculateMatchingNorm()));
        PossibleValues possibleValues = POSSIBLE_VALUES_FACTORY.getFromFile(new File(configuration.getPossibleValuesPath()));
        Model hbModel = getModel(configuration, HB);
        Model rmModel = getModel(configuration, RM);
        Model rp02Model = getModel(configuration, RP02);
        Model a5Model = getModel(configuration, A5);
        Model kModel = getModel(configuration, K);
        MechanicalPropertiesModel model = new MechanicalPropertiesModel(possibleValues, rmModel, rp02Model, a5Model, hbModel, kModel);
        generator = new ADISolutionGenerator(thicknessProperty.getValue(), matchingNormProperty.get());
        finder = new SolutionFinder(
                generator,
                model,
                new CostFunction(avgIronCostProperty, avgBatchWeightProperty, niPriceProperty, cuPriceProperty, moPriceProperty),
                new QualityFunction(this.ranges));
        thicknessProperty.addListener((observableValue, integer, t1) -> {
            matchingNormProperty.setValue(calculateMatchingNorm());
            generator.setThickness(t1);
        });
        matchingNormProperty.addListener((observableValue, normConstraint, t1) -> generator.setConstraint(t1));
        actualSolution.addListener((ListChangeListener<ProductionParametersModel>) change -> {
            if (change.getList().size() == 1) {
                updateCostAndQuality(() -> change.getList().get(0));
                try {
                    mechanicalPropertiesProperty.setValue(finder.getModel().evaluateProductionParameters(change.getList().get(0).getParams()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        addListeners();
    }

    private Model getModel(SystemConfiguration configuration, MechanicalProperty property) throws ModelLoadingException {
        switch (configuration.getModelType(property)) {
            case XGB:
                return XGB_MODEL_FACTORY.getXGBModel(
                        new File(configuration.getModelPath(property)),
                        new File(configuration.getModelInputConfigurationPath(property)));
            case KERAS:
                return KERAS_MODEL_FACTORY.getKerasModel(
                        new File(configuration.getModelPath(property)),
                        new File(configuration.getModelInputConfigurationPath(property))
                );
        }
        throw new ModelLoadingException("Model type: " + configuration.getModelType(property) + " is not supported");
    }

    private Norms wrapListsWithObservableLists(Norms norms) {
        Map<String, List<NormConstraint>> normsMap = norms.getNorms();
        normsMap.replaceAll((k, v) -> FXCollections.observableArrayList(v));
        return norms;
    }

    private void addListeners() {
        avgIronCostProperty.addListener((observableValue, aDouble, t1) -> updateCostAndQuality());
        avgBatchWeightProperty.addListener((observableValue, aDouble, t1) -> updateCostAndQuality());
        niPriceProperty.addListener((observableValue, aDouble, t1) -> updateCostAndQuality());
        moPriceProperty.addListener((observableValue, aDouble, t1) -> updateCostAndQuality());
        cuPriceProperty.addListener((observableValue, aDouble, t1) -> updateCostAndQuality());
        avgIronCostProperty.addListener((observableValue, aDouble, t1) -> updateCostAndQuality());
    }

    public void setActualSolution(ProductionParameters parameters) {
        actualSolution.clear();
        ProductionParametersModel e = new ProductionParametersModel(parameters);
        e.addListenerToAllParameters((observableValue, number, t1) -> updateCostAndQuality(() -> actualSolution.get(0)));
        actualSolution.add(e);
    }

    private void updateCostAndQuality(Supplier<ProductionParametersModel> prodSupplier) {
        ProductionParametersModel productionParametersModel = prodSupplier.get();
        if (productionParametersModel != null) {
            ProductionParameters params = productionParametersModel.getParams();
            actualPrice.setValue(finder.getCostFunction().evaluate(params));
            actualQuality.setValue(finder.getQualityFunction().evaluate(params));
        }
    }

    public void updateCostAndQuality() {
        updateCostAndQuality(() -> actualSolution.size() > 0 ? actualSolution.get(0) : null);
    }

    public ProductionParameters getActualSolutionParameters() {
        if (actualSolution.size() == 0) {
            generateRandomSolution();
        }
        return actualSolution.get(0).getParams();
    }

    public ProductionParametersModel getActualSolutionModel() {
        if (actualSolution.size() == 0) {
            generateRandomSolution();
        }
        return actualSolution.get(0);
    }

    public void generateRandomSolution() {
        setActualSolution(finder.findRandomSolution());
    }

    public void setThickness(Integer thickness) {
        thicknessProperty.setValue(thickness);
    }

    public Integer getThickness() {
        return thicknessProperty.getValue();
    }

    public void setAvgIronCost(Double thickness) {
        avgIronCostProperty.setValue(thickness);
    }

    public Double getAvgIronCost() {
        return avgIronCostProperty.getValue();
    }

    public void setAvgBatchWeight(Double avgBatchWeight) {
        avgBatchWeightProperty.setValue(avgBatchWeight);
    }

    public Double getAvgBatchWeight() {
        return avgBatchWeightProperty.getValue();
    }

    public void setNiPrice(Double niPrice) {
        niPriceProperty.setValue(niPrice);
    }

    public Double getNiPrice() {
        return niPriceProperty.getValue();
    }

    public void setCuPrice(Double cuPrice) {
        cuPriceProperty.setValue(cuPrice);
    }

    public Double getCuPrice() {
        return cuPriceProperty.getValue();
    }

    public void setMoPrice(Double moPrice) {
        moPriceProperty.setValue(moPrice);
    }

    public Double getMoPrice() {
        return moPriceProperty.getValue();
    }

    public void setCostQualityProportion(Double costQualityProportion) {
        costQualityProportionProperty.setValue(costQualityProportion);
    }

    public Double getCostQualityProportion() {
        return costQualityProportionProperty.getValue();
    }

    @Override
    public String toString() {
        return "DSSModel{" +
                "thickness=" + getThickness() +
                ", avgIronCost=" + getAvgIronCost()+
                ", avgBatchWeight=" + getAvgBatchWeight() +
                ", niPrice=" + getNiPrice() +
                ", cuPrice=" + getCuPrice() +
                ", moPrice=" + getMoPrice() +
                ", costQualityProportion=" + getCostQualityProportion() +
                '}';
    }

    public NormConstraint calculateMatchingNorm() {
        return norms.getNorms().get(getThicknessRange(getThickness())).stream()
                .filter(normConstraint -> normConstraint.getType().equals(selectedNormType.getValue())).findAny().orElseThrow();
    }

    private String getThicknessRange(Integer thickness) {
        if (thickness <= 30) {
            return "t<=30";
        }
        if (thickness <= 60) {
            return "30<t<=60";
        }
        return "60<t<=100";
    }

    public void clearSolution() {
        actualSolution.clear();
    }

    public void saveSolution() {
        this.savedSolution = actualSolution.get(0);
    }

    public void restoreSolution() {
        this.actualSolution.clear();
        this.actualSolution.add(savedSolution);
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }

    public void setActualSolutionModel(ProductionParametersModel parseSolutionInput) {

    }
}
