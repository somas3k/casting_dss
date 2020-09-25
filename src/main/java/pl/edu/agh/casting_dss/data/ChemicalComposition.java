package pl.edu.agh.casting_dss.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChemicalComposition {
    public static final ChemicalComposition NOT_ALLOYED = new ChemicalComposition(-1, 0.0,0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    private int id;
    private double c;
    private double ce;
    private double si;
    private double mn;
    private double mg;
    private double cu;
    private double ni;
    private double mo;
    private double s;
    private double p;
    private double cr;
    private double v;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChemicalComposition that = (ChemicalComposition) o;
        return id == that.id &&
                Double.compare(that.c, c) == 0 &&
                Double.compare(that.ce, ce) == 0 &&
                Double.compare(that.si, si) == 0 &&
                Double.compare(that.mn, mn) == 0 &&
                Double.compare(that.mg, mg) == 0 &&
                Double.compare(that.cu, cu) == 0 &&
                Double.compare(that.ni, ni) == 0 &&
                Double.compare(that.mo, mo) == 0 &&
                Double.compare(that.s, s) == 0 &&
                Double.compare(that.p, p) == 0 &&
                Double.compare(that.cr, cr) == 0 &&
                Double.compare(that.v, v) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, c, ce, si, mn, mg, cu, ni, mo, s, p, cr, v);
    }

    @Override
    public String toString() {
        return "ChemicalComposition{" +
                "id=" + id +
                ", c=" + c +
                ", si=" + si +
                ", mn=" + mn +
                ", mg=" + mg +
                ", cu=" + cu +
                ", ni=" + ni +
                ", mo=" + mo +
                ", s=" + s +
                ", p=" + p +
                ", cr=" + cr +
                ", v=" + v +
                '}';
    }
}
