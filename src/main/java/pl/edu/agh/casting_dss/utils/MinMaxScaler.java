package pl.edu.agh.casting_dss.utils;

import pl.edu.agh.casting_dss.data.ProductionParameters;

import java.util.HashMap;
import java.util.Map;

public class MinMaxScaler {
    public static Map<String, Double> scale(ProductionParameters params, ProductionParameters min, ProductionParameters max, float offset) {
        Map<String, Double> scaled = new HashMap<>();
        scaled.put("C", scale(params.getComposition().getC(), min.getComposition().getC(), max.getComposition().getC(), offset));
        scaled.put("Si", scale(params.getComposition().getSi(), min.getComposition().getSi(), max.getComposition().getSi(), offset));
        scaled.put("Mn", scale(params.getComposition().getMn(), min.getComposition().getMn(), max.getComposition().getMn(), offset));
        scaled.put("Mg", scale(params.getComposition().getMg(), min.getComposition().getMg(), max.getComposition().getMg(), offset));
        scaled.put("Cu", scale(params.getComposition().getCu(), min.getComposition().getCu(), max.getComposition().getCu(), offset));
        scaled.put("Ni", scale(params.getComposition().getNi(), min.getComposition().getNi(), max.getComposition().getNi(), offset));
        scaled.put("Mo", scale(params.getComposition().getMo(), min.getComposition().getMo(), max.getComposition().getMo(), offset));
        scaled.put("S", scale(params.getComposition().getS(), min.getComposition().getS(), max.getComposition().getS(), offset));
        scaled.put("P", scale(params.getComposition().getP(), min.getComposition().getP(), max.getComposition().getP(), offset));
        scaled.put("Cr", scale(params.getComposition().getCr(), min.getComposition().getCr(), max.getComposition().getCr(), offset));
        scaled.put("V", scale(params.getComposition().getV(), min.getComposition().getV(), max.getComposition().getV(), offset));
        scaled.put("CE", scale(params.getComposition().getCe(), min.getComposition().getCe(), max.getComposition().getCe(), offset));

        scaled.put("aust_temp", scale(params.getAustTemp(), min.getAustTemp(), max.getAustTemp(), offset));
        scaled.put("aust_czas", scale(params.getAustTime(), min.getAustTime(), max.getAustTime(), offset));
        scaled.put("ausf_temp", scale(params.getAusfTemp(), min.getAusfTemp(), max.getAusfTemp(), offset));
        scaled.put("ausf_czas", scale(params.getAusfTime(), min.getAusfTime(), max.getAusfTime(), offset));
        scaled.put("grubosc", scale(params.getThickness(), min.getThickness(), max.getThickness(), offset));
        scaled.put("log_10(ausf_czas_sec)", scale(Math.log10(params.getAusfTime() * 60), Math.log10(min.getAusfTime() * 60), Math.log10(max.getAusfTime() * 60), offset));
        return scaled;
    }

    private static double scale(double value, double min, double max, double offset) {
        return ((value - min) / (max - min)) - offset;
    }
}
