package pl.edu.agh.casting_dss.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class SystemConfiguration {
    private Map<MechanicalProperty, ModelConfiguration> modelWithInputConfigurationPaths;
    private String possibleValuesPath;

    public String getModelPath(MechanicalProperty property) {
        return modelWithInputConfigurationPaths.get(property).getModelPath();
    }

    public String getModelInputConfigurationPath(MechanicalProperty property) {
        return modelWithInputConfigurationPaths.get(property).getModelInputConfigPath();
    }
}
