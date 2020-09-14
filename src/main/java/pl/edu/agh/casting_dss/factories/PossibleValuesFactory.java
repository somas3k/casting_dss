package pl.edu.agh.casting_dss.factories;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.edu.agh.casting_dss.data.PossibleValues;

import java.io.File;
import java.io.IOException;

public class PossibleValuesFactory {
    public static final PossibleValuesFactory POSSIBLE_VALUES_FACTORY = new PossibleValuesFactory();
    private final ObjectMapper mapper = new ObjectMapper();
    {
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }
    public PossibleValues getFromFile(File file) throws IOException {
        return mapper.readValue(file, PossibleValues.class);
    }
}
