package pl.edu.agh.casting_dss.gui.model;

import javafx.beans.property.*;
import lombok.Getter;

@Getter
public class DSSModel {
    private final ObjectProperty<Integer> thicknessProperty = new SimpleObjectProperty<>(25);
    private final ObjectProperty<Double> avgIronCostProperty = new SimpleObjectProperty<>(1350.0);
    private final ObjectProperty<Double> avgBatchWeightProperty = new SimpleObjectProperty<>(200.0);
    private final ObjectProperty<Double> niPriceProperty = new SimpleObjectProperty<>(16.0);
    private final ObjectProperty<Double> cuPriceProperty = new SimpleObjectProperty<>(12.0);
    private final ObjectProperty<Double> moPriceProperty = new SimpleObjectProperty<>(7.0);
    private final DoubleProperty costQualityProportionProperty = new SimpleDoubleProperty(0.5);

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
