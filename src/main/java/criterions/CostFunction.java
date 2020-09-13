package criterions;

import data.ChemicalComposition;
import data.ProductionParameters;

public class CostFunction implements Function {
    private final double avgIronCost;
    private final double avgBatchWeight;
    private final double niCost;
    private final double cuCost;
    private final double moCost;

    public CostFunction(float avgIronCost, float avgBatchWeight, float niCost, float cuCost, float moCost) {
        this.avgIronCost = avgIronCost;
        this.avgBatchWeight = avgBatchWeight;
        this.niCost = niCost;
        this.cuCost = cuCost;
        this.moCost = moCost;
    }

    @Override
    public double evaluate(ProductionParameters parameters) {
        double chemCompIncrease = getChemCompIncrease(parameters.getComposition());
        double austTempIncrease = getAustTempIncrease(parameters.getAustTemp());
        double austTimeIncrease = getAustTimeIncrease(parameters.getAustTime());
        double ausfTempIncrease = getAusfTempIncrease(parameters.getAusfTemp());
        double ausfTimeIncrease = getAusfTimeIncrease(parameters.getAusfTime());
        return avgIronCost *
                (1 + (chemCompIncrease + austTempIncrease + austTimeIncrease + ausfTempIncrease + ausfTimeIncrease) / 100) *
                avgBatchWeight;
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

    public double getChemCompIncrease(ChemicalComposition composition) {
        return (((composition.getNi() / 100) * niCost + (composition.getCu() / 100) * cuCost + (composition.getMo() / 100) * moCost) / avgIronCost) * 100;
    }
}
