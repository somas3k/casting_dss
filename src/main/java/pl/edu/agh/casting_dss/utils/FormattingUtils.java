package pl.edu.agh.casting_dss.utils;

import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class FormattingUtils {
    private static final Pattern VALID_EDITING_STATE_DOUBLE = Pattern.compile("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?");
    private static final Pattern VALID_EDITING_STATE_NATURAL = Pattern.compile("(([1-9][0-9]*)|0)?");
    private static final UnaryOperator<TextFormatter.Change> FILTER_DOUBLE = c -> {
        String text = c.getControlNewText();
        if (VALID_EDITING_STATE_DOUBLE.matcher(text).matches()) {
            return c ;
        } else {
            return null;
        }
    };
    private static final UnaryOperator<TextFormatter.Change> FILTER_NATURAL = c -> {
        String text = c.getControlNewText();
        if (VALID_EDITING_STATE_NATURAL.matcher(text).matches()) {
            return c;
        } else {
            return null;
        }
    };
    public static final StringConverter<Double> DOUBLE_CONVERTER = new StringConverter<>() {
        @Override
        public String toString(Double d) {
            return d != null ? d.toString(): "0.0";
        }

        @Override
        public Double fromString(String s) {
            if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
                return 0.0 ;
            } else {
                return Double.valueOf(s);
            }
        }
    };
    public static final StringConverter<Integer> NATURAL_CONVERTER = new StringConverter<>() {
        @Override
        public String toString(Integer d) {
            return d != null ? d.toString() : "0";
        }

        @Override
        public Integer fromString(String s) {
            if (s.isEmpty()) {
                return 0;
            } else {
                return Integer.valueOf(s);
            }
        }
    };
    public static TextFormatter<Double> getDoubleTextFormatter() {
        return new TextFormatter<>(DOUBLE_CONVERTER, 0.0, FILTER_DOUBLE);
    }

    public static TextFormatter<Integer> getNaturalTextFormatter() {
        return new TextFormatter<>(NATURAL_CONVERTER, 0, FILTER_NATURAL);
    }
}
