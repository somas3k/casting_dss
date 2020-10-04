package pl.edu.agh.casting_dss.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.edu.agh.casting_dss.criterions.ProductionRange;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class ProductionRangesUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static List<ProductionRange> loadFromFile(URL file) throws IOException {
        return Arrays.asList(MAPPER.readValue(file, ProductionRange[].class));
    }
}
