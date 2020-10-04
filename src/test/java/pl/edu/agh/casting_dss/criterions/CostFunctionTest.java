package pl.edu.agh.casting_dss.criterions;

import javafx.beans.property.SimpleObjectProperty;
import pl.edu.agh.casting_dss.data.ProductionParameters;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CostFunctionTest {

    @Test
    void evaluate() {
        CostFunction function = new CostFunction(new SimpleObjectProperty<>(14.0d), new SimpleObjectProperty<>(250.0d), new SimpleObjectProperty<>(45d), new SimpleObjectProperty<>(30d), new SimpleObjectProperty<>(15d));
        Map<String, Double> cc = new HashMap<>();
        cc.put("C", 3.75);
        cc.put("CE", 0.0);
        cc.put("Si", 2.29);
        cc.put("Mn", 0.374);
        cc.put("Mg", 0.04);
        cc.put("Cu", 0.2);
        cc.put("Ni", 0.18);
        cc.put("Mo", 0.0);
        cc.put("S", 0.014);
        cc.put("P", 0.34);
        cc.put("Cr", 0.0);
        cc.put("V", 0.0);
        Map<String, Integer> heatTreatment = new HashMap<>();
        heatTreatment.put("aust_temp", 860);
        heatTreatment.put("aust_czas", 75);
        heatTreatment.put("ausf_temp", 400);
        heatTreatment.put("ausf_czas", 75);
        ProductionParameters params = new ProductionParameters(cc, heatTreatment, 25);

        double cost = function.evaluate(params);
        assertEquals(3734.75, cost, 0.01);
    }
}