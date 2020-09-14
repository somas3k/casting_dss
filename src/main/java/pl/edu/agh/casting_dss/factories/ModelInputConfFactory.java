package pl.edu.agh.casting_dss.factories;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.edu.agh.casting_dss.model.ModelInputConfiguration;

import java.io.File;
import java.io.IOException;

public class ModelInputConfFactory {
    public static final ModelInputConfFactory MODEL_INPUT_CONF_FACTORY = new ModelInputConfFactory();
    private final ObjectMapper mapper = new ObjectMapper();
    {
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }
    public ModelInputConfiguration getConfiguration(File jsonPath) throws IOException {
        return mapper.readValue(jsonPath, ModelInputConfiguration.class);
    }
}
