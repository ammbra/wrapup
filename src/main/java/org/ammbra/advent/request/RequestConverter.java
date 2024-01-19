package org.ammbra.advent.request;

import io.github.ralfspoeth.json.JsonObject;
import org.ammbra.advent.surprise.Celebration;

import java.util.Arrays;

import static io.github.ralfspoeth.json.conv.StandardConversions.*;

public final class RequestConverter {

    private RequestConverter() {
    }

    public static RequestData convert(JsonObject jsonObject) {
        RequestData.Builder builder = new RequestData.Builder();

        if (jsonObject.members().keySet().containsAll(Arrays.asList("sender", "receiver", "celebration", "option"))) {
            for (String key : jsonObject.members().keySet()) {
                var element = jsonObject.members().get(key);
                switch (key) {
                    case "sender" -> builder.sender(stringValue(element));
                    case "receiver" -> builder.receiver(stringValue(element));
                    case "celebration" -> builder.celebration(enumValue(Celebration.class, element));
                    case "option" -> builder.choice(enumValue(Choice.class, element));
                    case "itemPrice" -> builder.itemPrice(Math.abs(doubleValue(element)));
                    case "boxPrice" -> builder.boxPrice(Math.abs(doubleValue(element)));
                }
            }
        }
        return builder.build();
    }
}