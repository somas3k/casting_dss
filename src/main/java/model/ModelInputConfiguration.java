package model;

import data.ProductionParameters;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class ModelInputConfiguration {
    private final ProductionParameters minParams;
    private final ProductionParameters maxParams;
    private final float offset;
    private final List<String> modelInput;
}
