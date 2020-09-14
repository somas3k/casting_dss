package pl.edu.agh.casting_dss.data;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
public class ProductionParameters {
    @JsonAlias({"composition", "chemicalComposition"})
    private ChemicalComposition composition;
    @JsonAlias({"aust_temp", "austTemp"})
    private int austTemp;
    @JsonAlias({"aust_czas", "austTime"})
    private int austTime;
    @JsonAlias({"ausf_temp", "ausfTemp"})
    private int ausfTemp;
    @JsonAlias({"ausf_czas", "ausfTime"})
    private int ausfTime;
    @JsonAlias({"grubosc"})
    private int thickness;

    private Map<String, Object> otherParameters = new HashMap<>();

    public ProductionParameters(ChemicalComposition composition, int austTemp, int austTime, int ausfTemp, int ausfTime, int thickness) {
        this.composition = composition;
        this.austTemp = austTemp;
        this.austTime = austTime;
        this.ausfTemp = ausfTemp;
        this.ausfTime = ausfTime;
        this.thickness = thickness;
    }

    @JsonAnyGetter
    public Map<String, Object> otherParameters() {
        return otherParameters;
    }

    @JsonAnySetter
    public void setOtherParameter(String name, Object value) {
        otherParameters.put(name, value);
    }

    public ProductionParameters(ProductionParameters parameters) {
        this.composition = parameters.composition;
        this.austTemp = parameters.austTemp;
        this.austTime = parameters.austTime;
        this.ausfTemp = parameters.ausfTemp;
        this.ausfTime = parameters.ausfTime;
        this.thickness = parameters.thickness;
        this.otherParameters = parameters.otherParameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductionParameters that = (ProductionParameters) o;
        return austTemp == that.austTemp &&
                austTime == that.austTime &&
                ausfTemp == that.ausfTemp &&
                ausfTime == that.ausfTime &&
                composition.equals(that.composition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(composition, austTemp, austTime, ausfTemp, ausfTime);
    }

    @Override
    public String toString() {
        return "ProductionParameters{" +
                "composition=" + composition +
                ", austTemp=" + austTemp +
                ", austTime=" + austTime +
                ", ausfTemp=" + ausfTemp +
                ", ausfTime=" + ausfTime +
                ", thickness=" + thickness +
                ", otherParameters=" + otherParameters +
                '}';
    }
}
