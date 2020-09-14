package pl.edu.agh.casting_dss.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
