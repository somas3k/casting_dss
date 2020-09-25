package pl.edu.agh.casting_dss.gui.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import lombok.Getter;
import pl.edu.agh.casting_dss.data.MechanicalProperties;
import pl.edu.agh.casting_dss.data.PossibleValues;
import pl.edu.agh.casting_dss.factories.ModelLoadingException;
import pl.edu.agh.casting_dss.model.MechanicalPropertiesModel;
import pl.edu.agh.casting_dss.model.Model;
import pl.edu.agh.casting_dss.single_criteria_opt.NormConstraints;
import pl.edu.agh.casting_dss.solution.SolutionFinder;
import pl.edu.agh.casting_dss.utils.SystemConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

import static pl.edu.agh.casting_dss.factories.PossibleValuesFactory.POSSIBLE_VALUES_FACTORY;
import static pl.edu.agh.casting_dss.factories.XGBModelFactory.XGB_MODEL_FACTORY;
import static pl.edu.agh.casting_dss.utils.MechanicalProperty.HB;

@Getter
public class DSSModel {
    private final ObjectProperty<Integer> thicknessProperty = new SimpleObjectProperty<>(25);
    private final ObjectProperty<Double> avgIronCostProperty = new SimpleObjectProperty<>(1350.0);
    private final ObjectProperty<Double> avgBatchWeightProperty = new SimpleObjectProperty<>(200.0);
    private final ObjectProperty<Double> niPriceProperty = new SimpleObjectProperty<>(16.0);
    private final ObjectProperty<Double> cuPriceProperty = new SimpleObjectProperty<>(12.0);
    private final ObjectProperty<Double> moPriceProperty = new SimpleObjectProperty<>(7.0);
    private final ObjectProperty<Double> actualPrice = new SimpleObjectProperty<>(0.0);
    private final ObjectProperty<Double> actualQuality = new SimpleObjectProperty<>(0.0);
    private final DoubleProperty costQualityProportionProperty = new SimpleDoubleProperty(0.5);
    private final ObservableList<ProductionParameters> actualSolution = FXCollections.observableArrayList(new ProductionParameters());
    private final NormConstraints constraints = new NormConstraints(280, 320);
    private final SolutionFinder finder;

    public DSSModel(SystemConfiguration configuration) throws IOException, ModelLoadingException {
        PossibleValues possibleValues = POSSIBLE_VALUES_FACTORY.getFromFile(new File(configuration.getPossibleValuesPath()));
        Model hbModel = XGB_MODEL_FACTORY.getHBModel(
                new File(configuration.getModelPath(HB)),
                new File(configuration.getModelInputConfigurationPath(HB)));
        MechanicalPropertiesModel model = new MechanicalPropertiesModel(possibleValues, hbModel);
        finder = new SolutionFinder(thicknessProperty.getValue(), constraints, model);
        actualSolution.addListener((ListChangeListener<ProductionParameters>) change -> {
            if (change.getList().size() == 1)
                updateCostAndQuality(() -> change.getList().get(0));
        });
    }

    private void updateCostAndQuality(Supplier<ProductionParameters> prodSupplier) {
        pl.edu.agh.casting_dss.data.ProductionParameters params = prodSupplier.get().getParams();
        actualPrice.setValue(finder.getCostFunction().evaluate(params));
        actualQuality.setValue(finder.getQualityFunction().evaluate(params));
    }

    public void generateRandomSolution() {
        actualSolution.clear();
        ProductionParameters e = new ProductionParameters(finder.findRandomSolution());
        e.addListenerToAllParameters((observableValue, number, t1) -> updateCostAndQuality(() -> actualSolution.get(0)));
        actualSolution.add(e);
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
}
