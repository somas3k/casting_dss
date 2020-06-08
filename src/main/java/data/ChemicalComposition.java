package data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ChemicalComposition {
    private final int id;
    private final float c;
    private final float si;
    private final float mn;
    private final float mg;
    private final float cu;
    private final float ni;
    private final float mo;
    private final float s;
    private final float p;
    private final float cr;
}
