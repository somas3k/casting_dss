package pl.edu.agh.casting_dss.utils;

import pl.edu.agh.casting_dss.data.ProductionParameters;

import java.util.HashMap;
import java.util.Map;

public class MinMaxScaler {
    public static Map<String, Double> scale(ProductionParameters params, ProductionParameters min, ProductionParameters max, float offset) {
        Map<String, Double> scaled = new HashMap<>();
        for (String paramName : params.getAllParameterNames()) {
            scaled.put(
                    paramName,
                    scale(params.getParameterValue(paramName).doubleValue(),
                            min.getParameterValue(paramName).doubleValue(),
                            max.getParameterValue(paramName).doubleValue(),
                            offset
                    ));
        }
        scaled.put("grubosc", scale(params.getThickness(), min.getThickness(), max.getThickness(), offset));
        scaled.put(
                "log_10(ausf_czas_sec)",
                scale(Math.log10(params.getParameterValue("aust_czas").doubleValue() * 60),
                        Math.log10(min.getParameterValue("aust_czas").doubleValue() * 60),
                        Math.log10(max.getParameterValue("aust_czas").doubleValue() * 60),
                        offset
                ));
        return scaled;
    }

    private static double scale(double value, double min, double max, double offset) {
        return ((value - min) / (max - min)) - offset;
    }
}
