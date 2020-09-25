package pl.edu.agh.casting_dss.gui.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.agh.casting_dss.data.ChemicalComposition;

import java.util.*;

@Getter
@Setter
public class ProductionParameters {
    public static final List<String> chemicalCompositionParamNames = Arrays.asList("c", "ce", "si", "mn", "mg", "cu", "ni", "mo", "s", "p", "cr", "v");
    public static final List<String> heatTreatmentParamNames = Arrays.asList("austTemp", "austTime", "ausfTemp", "ausfTime");
    private final Map<String, ObjectProperty<Number>> parametersMap = new HashMap<>();
    private pl.edu.agh.casting_dss.data.ProductionParameters params;

    public ProductionParameters() {
        chemicalCompositionParamNames.forEach(param -> parametersMap.put(param, new SimpleObjectProperty<>(0.0)));
        heatTreatmentParamNames.forEach(param -> parametersMap.put(param, new SimpleObjectProperty<>(0)));
    }

    public ProductionParameters(pl.edu.agh.casting_dss.data.ProductionParameters parameters) {
        this();
        this.params = parameters;
        ChemicalComposition composition = parameters.getComposition();
        parametersMap.get("c").setValue(composition.getC());
        parametersMap.get("ce").setValue(composition.getCe());
        parametersMap.get("si").setValue(composition.getSi());
        parametersMap.get("mn").setValue(composition.getMn());
        parametersMap.get("mg").setValue(composition.getMg());
        parametersMap.get("cu").setValue(composition.getCu());
        parametersMap.get("ni").setValue(composition.getNi());
        parametersMap.get("mo").setValue(composition.getMo());
        parametersMap.get("s").setValue(composition.getS());
        parametersMap.get("p").setValue(composition.getP());
        parametersMap.get("cr").setValue(composition.getCr());
        parametersMap.get("v").setValue(composition.getV());
        parametersMap.get("austTemp").setValue(parameters.getAustTemp());
        parametersMap.get("austTime").setValue(parameters.getAustTime());
        parametersMap.get("ausfTemp").setValue(parameters.getAusfTemp());
        parametersMap.get("ausfTime").setValue(parameters.getAusfTime());
    }

    public void setParam(String paramName, Number value) {
        parametersMap.get(paramName).setValue(value);
    }

    private void updateParams() {
        ChemicalComposition composition = params.getComposition();
        composition.setC((Double) getParam("c"));
        composition.setCe((Double) getParam("ce"));
        composition.setSi((Double) getParam("si"));
        composition.setMn((Double) getParam("mn"));
        composition.setMg((Double) getParam("mg"));
        composition.setCu((Double) getParam("cu"));
        composition.setNi((Double) getParam("ni"));
        composition.setMo((Double) getParam("mo"));
        composition.setS((Double) getParam("s"));
        composition.setP((Double) getParam("p"));
        composition.setCr((Double) getParam("cr"));
        composition.setV((Double) getParam("v"));;
        params.setAustTemp((Integer) getParam("austTemp"));
        params.setAustTime((Integer) getParam("austTime"));
        params.setAusfTemp((Integer) getParam("ausfTemp"));
        params.setAusfTime((Integer) getParam("ausfTime"));
    }

    public Number getParam(String paramName) {
        return parametersMap.get(paramName).getValue();
    }

    public void addListenerToAllParameters(ChangeListener<? super Number> listener) {
        parametersMap.values().forEach(numberObjectProperty -> numberObjectProperty.addListener((observableValue, number, t1) -> updateParams()));
        parametersMap.values().forEach(numberObjectProperty -> numberObjectProperty.addListener(listener));

    }
}
