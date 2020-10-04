package pl.edu.agh.casting_dss.data;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;

import java.util.*;

@Setter
@Getter
@NoArgsConstructor
public class ProductionParameters {
    private Map<String, Double> chemicalComposition;
    private Map<String, Integer> heatTreatment;
    @JsonAlias({"grubosc"})
    private int thickness;
    private Map<String, Number> otherParameters = new HashMap<>();

    public ProductionParameters(Map<String, Double> chemicalComposition, Map<String, Integer> heatTreatment, int thickness) {
        this.chemicalComposition = chemicalComposition;
        this.heatTreatment = heatTreatment;
        this.thickness = thickness;
    }

    @JsonAnyGetter
    public Map<String, Number> otherParameters() {
        return otherParameters;
    }

    @JsonAnySetter
    public void setOtherParameter(String name, Number value) {
        otherParameters.put(name, value);
    }

    public ProductionParameters(ProductionParameters parameters) {
        this.chemicalComposition = new HashMap<>(parameters.chemicalComposition);
        this.heatTreatment = new HashMap<>(parameters.heatTreatment);
        this.thickness = parameters.thickness;
        this.otherParameters = parameters.otherParameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductionParameters that = (ProductionParameters) o;
        return heatTreatment.equals(that.heatTreatment) &&
                chemicalComposition.equals(that.chemicalComposition) &&
                thickness == that.thickness &&
                otherParameters.equals(that.otherParameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chemicalComposition, heatTreatment, thickness, otherParameters);
    }

    @Override
    public String toString() {
        return "ProductionParameters{" +
                "chemicalComposition=" + chemicalComposition +
                ", heatTreatment=" + heatTreatment +
                ", thickness=" + thickness +
                ", otherParameters=" + otherParameters +
                '}';
    }

    public Number getParameterValue(String parameterName) {
        if (chemicalComposition.containsKey(parameterName)) {
            return chemicalComposition.get(parameterName);
        }
        if (heatTreatment.containsKey(parameterName)) {
            return heatTreatment.get(parameterName);
        }
        if (otherParameters.containsKey(parameterName)) {
            return otherParameters.get(parameterName);
        }
        return null;
    }

    public List<String> getAllParameterNames() {
        List<String> parameterNames = new ArrayList<>(chemicalComposition.keySet());
        parameterNames.remove("id");
        parameterNames.addAll(heatTreatment.keySet());
        parameterNames.addAll(otherParameters.keySet());
        return parameterNames;
    }
}
