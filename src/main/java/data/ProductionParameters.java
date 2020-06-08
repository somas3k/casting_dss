package data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
public class ProductionParameters {
    private ChemicalComposition composition;
    private int austTemp;
    private int austTime;
    private int ausfTemp;
    private int ausfTime;

    public ProductionParameters(ProductionParameters parameters) {
        this.composition = parameters.composition;
        this.austTemp = parameters.austTemp;
        this.austTime = parameters.austTime;
        this.ausfTemp = parameters.ausfTemp;
        this.ausfTime = parameters.ausfTime;
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
}
