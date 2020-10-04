package pl.edu.agh.casting_dss.criterions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductionRange {
    private String paramName;
    private Double minimum;
    private Double maximum;
    private Double weight;
}
