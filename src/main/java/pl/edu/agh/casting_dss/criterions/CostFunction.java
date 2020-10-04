package pl.edu.agh.casting_dss.criterions;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import pl.edu.agh.casting_dss.data.ProductionParameters;

import java.util.Map;

public class CostFunction implements Function {
    private final ObjectProperty<Double> avgIronCost;
    private final ObjectProperty<Double> avgBatchWeight;
    private final ObjectProperty<Double> niCost;
    private final ObjectProperty<Double> cuCost;
    private final ObjectProperty<Double> moCost;

    public CostFunction(ObjectProperty<Double> avgIronCost, ObjectProperty<Double> avgBatchWeight, ObjectProperty<Double> niCost, ObjectProperty<Double> cuCost, ObjectProperty<Double> moCost) {
        this.avgIronCost = avgIronCost;
        this.avgBatchWeight = avgBatchWeight;
        this.niCost = niCost;
        this.cuCost = cuCost;
        this.moCost = moCost;
    }

    @Override
    public double evaluate(ProductionParameters parameters) {
        double chemCompIncrease = getChemCompIncrease(parameters.getChemicalComposition());
        double austTempIncrease = getAustTempIncrease(parameters.getHeatTreatment().get("aust_temp"));
        double austTimeIncrease = getAustTimeIncrease(parameters.getHeatTreatment().get("aust_czas"));
        double ausfTempIncrease = getAusfTempIncrease(parameters.getHeatTreatment().get("ausf_temp"));
        double ausfTimeIncrease = getAusfTimeIncrease(parameters.getHeatTreatment().get("ausf_czas"));
        return avgIronCost.getValue() *
                (1 + (chemCompIncrease + austTempIncrease + austTimeIncrease + ausfTempIncrease + ausfTimeIncrease) / 100) *
                avgBatchWeight.getValue();
    }

    private double getAusfTimeIncrease(int ausfTime) {
        if (ausfTime <= 30) {
            return 1.5;
        }
        if (ausfTime <= 120) {
            return (((2.5 - 1.5) * (ausfTime - 30)) / (120 - 30)) + 1.5;
        }
        return (((10.5 - 2.5) * (ausfTime - 120)) / (180 - 120)) + 2.5;
    }

    private double getAusfTempIncrease(int ausfTemp) {
        if (ausfTemp <= 270) {
            return 0;
        }
        return (((1.8 - 0.0) * (ausfTemp - 270)) / (400 - 270)) + 0.0;
    }

    private double getAustTimeIncrease(int austTime) {
        if (austTime <= 30) {
            return 1.7;
        }
        if (austTime <= 120) {
            return (((2.1 - 1.7) * (austTime - 30)) / (120 - 30)) + 1.7;
        }
        return (((8.7 - 2.1) * (austTime - 120)) / (240 - 120)) + 2.1;
    }

    private double getAustTempIncrease(int austTemp) {
        if (austTemp <= 860) {
            return 0;
        }
        if (austTemp <= 890) {
            return (((0.7 - 0.0) * (austTemp - 860)) / (890 - 860)) + 0.0;
        }
        return (((1.0 - 0.7) * (austTemp - 890)) / (950 - 890)) + 0.7;
    }

    public double getChemCompIncrease(Map<String, Double> composition) {
        return (((composition.get("Ni") / 100) * niCost.getValue() + (composition.get("Cu") / 100) * cuCost.getValue() + (composition.get("Mo") / 100) * moCost.getValue()) / avgIronCost.getValue()) * 100;
    }
}
