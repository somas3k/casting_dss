package model;

import data.ProductionParameters;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ModelInputConfiguration {
    private ProductionParameters minParams;
    private ProductionParameters maxParams;
    private float offset;
    private List<String> modelInput;
}
