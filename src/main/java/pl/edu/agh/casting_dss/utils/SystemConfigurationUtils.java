package pl.edu.agh.casting_dss.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

public class SystemConfigurationUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static SystemConfiguration loadFromFile(URL file) throws IOException {
        return MAPPER.readValue(file, SystemConfiguration.class);
    }
}
