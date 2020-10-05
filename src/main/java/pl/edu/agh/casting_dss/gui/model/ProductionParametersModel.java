package pl.edu.agh.casting_dss.gui.model;

import javafx.beans.binding.ObjectExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.casting_dss.data.ProductionParameters;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Getter
@Setter
public class ProductionParametersModel {
    public static final List<String> chemicalCompositionParamNames = Arrays.asList("C", "Si", "CE", "Mn", "Mg", "Cu", "Ni", "Mo", "S", "P", "Cr", "V");
    public static final List<String> heatTreatmentParamNames = Arrays.asList("aust_temp", "aust_czas", "ausf_temp", "ausf_czas");
    private final Map<String, ObjectProperty<Number>> parametersMap = new LinkedHashMap<>();
    private pl.edu.agh.casting_dss.data.ProductionParameters params;

    public ProductionParametersModel(ProductionParameters parameters) {
        this.params = parameters;
        chemicalCompositionParamNames.forEach(name -> parametersMap.put(name, new SimpleObjectProperty<>(parameters.getChemicalComposition().get(name))));
        heatTreatmentParamNames.forEach(name -> parametersMap.put(name, new SimpleObjectProperty<>(parameters.getHeatTreatment().get(name))));
        parameters.getOtherParameters().forEach((name, value) -> parametersMap.put(name, new SimpleObjectProperty<>(value)));
    }

    public void setParam(String paramName, Number value) {
        parametersMap.get(paramName).setValue(value);
    }

    private void updateParams() {
        params.getChemicalComposition().replaceAll((name, value) -> parametersMap.get(name).getValue().doubleValue());
        params.getHeatTreatment().replaceAll((name, value) -> parametersMap.get(name).getValue().intValue());
        params.getOtherParameters().replaceAll((name, value) -> parametersMap.get(name).getValue());
    }

    public Number getParam(String paramName) {
        return parametersMap.get(paramName).getValue();
    }

    public void addListenerToAllParameters(ChangeListener<? super Number> listener) {
        parametersMap.values().forEach(numberObjectProperty -> numberObjectProperty.addListener((observableValue, number, t1) -> updateParams()));
        parametersMap.values().forEach(numberObjectProperty -> numberObjectProperty.addListener(listener));
    }

    @Override
    public String toString() {
        return parametersMap.values().stream().map(prop -> prop.getValue().toString()).collect(Collectors.joining(" "));
    }
}
