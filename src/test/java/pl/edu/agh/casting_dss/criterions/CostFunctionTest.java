package pl.edu.agh.casting_dss.criterions;

import pl.edu.agh.casting_dss.data.ChemicalComposition;
import pl.edu.agh.casting_dss.data.ProductionParameters;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CostFunctionTest {

    @Test
    void evaluate() {
        CostFunction function = new CostFunction(14.0f, 250.0f, 45f, 30f, 15f);

        ChemicalComposition cc = new ChemicalComposition(0, 3.75, 0.0, 2.29, 0.374, 0.04, 0.2, 0.18, 0.0, 0.014, 0.34, 0, 0.0);
        ProductionParameters params = new ProductionParameters(cc, 860, 75, 400, 75, 25);

        double cost = function.evaluate(params);
        assertEquals(3734.75, cost, 0.01);
    }
}