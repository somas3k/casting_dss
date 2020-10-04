package pl.edu.agh.casting_dss.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.edu.agh.casting_dss.data.Norms;

import java.io.IOException;
import java.net.URL;

public class NormsUtil {
    private static final ObjectMapper mapper = new ObjectMapper();
    public static Norms loadFromFile(URL file) throws IOException {
        return mapper.readValue(file, Norms.class);
    }
}
