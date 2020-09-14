package pl.edu.agh.casting_dss.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModelConfiguration {
    private ModelType type;
    private String modelPath;
    private String modelInputConfigPath;
}
