package org.ammbra.advent.request;

import io.github.ralfspoeth.json.*;
import io.github.ralfspoeth.json.io.JsonReader;
import org.ammbra.advent.surprise.Celebration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public final class RequestConverter {

    private RequestConverter() {
    }

    public static RequestData convert(JsonObject jsonObject) {
        RequestData.Builder builder = new RequestData.Builder();

        if (jsonObject.members().keySet().containsAll(Arrays.asList("sender", "receiver", "celebration", "option"))) {
            for (String key : jsonObject.members().keySet()) {
                var value = jsonObject.members().get(key);
                switch (key) {
                    case "sender" -> builder.sender(toString(value));
                    case "receiver" -> builder.receiver(toString(value));
                    case "celebration" -> builder.celebration(toEnum(Celebration.class, value));
                    case "option" -> builder.choice(toEnum(Choice.class, value));
                    case "itemPrice" -> builder.itemPrice(Math.abs(toDouble(value)));
                    case "boxPrice" -> builder.boxPrice(Math.abs(toDouble(value)));
                }
            }
        }
        return builder.build();
    }

    public static <E extends Enum<E>> E toEnum(Class<E> enumClass, Element elem) {
        return switch (elem) {
            case JsonString str -> Enum.valueOf(enumClass, str.value().toUpperCase());
			case null -> null;
            default -> throw new RuntimeException(STR."cannot convert \{elem} to Choice");
        };
    }

    public static double toDouble(Element elem) {
        return switch(elem) {
            case JsonNumber num -> num.numVal();
            case JsonString str -> Double.parseDouble(str.value());
            default -> throw new RuntimeException(STR."cannot convert \{elem} to double");
        };
    }

    public static String toString(Element elem) {
        return switch (elem) {
            case null -> "";
            default -> elem.toString();
        };
    }

    public static JsonObject parse(InputStream reqBody) throws IOException {
        try (var r = new JsonReader(new InputStreamReader(reqBody))) {
            return (JsonObject)r.readElement();
        }
    }

}