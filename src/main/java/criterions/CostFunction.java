package criterions;

import data.ChemicalComposition;
import data.ProductionParameters;

public class CostFunction implements Function{
    private final float avgIronCost;
    private final float avgBatchWeight;
    private final float niCost;
    private final float cuCost;
    private final float moCost;

    public CostFunction(float avgIronCost, float avgBatchWeight, float niCost, float cuCost, float moCost) {
        this.avgIronCost = avgIronCost;
        this.avgBatchWeight = avgBatchWeight;
        this.niCost = niCost;
        this.cuCost = cuCost;
        this.moCost = moCost;
    }

    public double evaluate(ProductionParameters parameters) {
        float chemCompIncrease = getChemCompIncrease(parameters.getComposition());
        float austTempIncrease = getAustTempIncrease(parameters.getAustTemp());
        float austTimeIncrease = getAustTimeIncrease(parameters.getAustTime());
        float ausfTempIncrease = getAusfTempIncrease(parameters.getAusfTemp());
        float ausfTimeIncrease = getAusfTimeIncrease(parameters.getAusfTime());
        return avgIronCost *
                (1 + (chemCompIncrease + austTempIncrease + austTimeIncrease + ausfTempIncrease + ausfTimeIncrease) / 100) *
                avgBatchWeight;
    }

    private float getAusfTimeIncrease(int ausfTime) {
        return 0.1f;
    }

    private float getAusfTempIncrease(int ausfTemp) {
        if (ausfTemp <= 270) {
            return 0;
        }
        return (9.0f / 650) * ausfTemp - 243.0f / 65;
    }

    private float getAustTimeIncrease(int austTime) {
        return 0;
    }

    private float getAustTempIncrease(int austTemp) {
        if (austTemp <= 860) {
            return 0;
        }
        if (austTemp <= 890) {
            return (1.0f / 43) * austTemp - 20;
        }
        return 0.005f * austTemp - 3.75f;
    }

    private float getChemCompIncrease(ChemicalComposition composition) {
        return 1 + (composition.getNi() / 100) * niCost + (composition.getCu() / 100) * cuCost + (composition.getMo() / 100) * moCost;
    }
}
