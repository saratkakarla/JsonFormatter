import com.google.gson.*;

import java.io.FileReader;
import java.util.*;

public class JsonFormatUtils {
    private static final String JSON_TOKEN_DELIMITER    = ".";
    private static final String JSON_ATTR_FORMAT        = "\"%s\": %s";
    private static final String JSON_ATTR_PRETTY_FORMAT = "  \"%s\": %s";
    private static final String JSON_FIELD_DELIMITER    = ", ";
    private static final String PRETTY_PRINTING = "\n";

    /**
     * Flattens the JSON key value, for example if the input json obect is:
     *    { key: value, key2: { nkey: nvalue } }
     * would be converted to:
     *    { key: value, key2.nkey: nvalue }
     */
    public static String processJsonFromFile(final String jsonFileName) {
        try {
            final JsonParser jsonParser = new JsonParser();
            final JsonObject obj = jsonParser.parse(new FileReader(jsonFileName)).getAsJsonObject();
            final String result = flattenJsonObj("", obj);
            return isPrettyPrintingEnabled() ? "{\n" + result + "\n}" : "{" + result + "}";
        } catch (final Exception ex) {
            System.out.println("Error in parsing the json from file: " + jsonFileName + ex.getMessage());
            return null;
        }
    }

    private static boolean isPrettyPrintingEnabled() {
        return Boolean.getBoolean("JSON_PRETTY_PRINTING");
    }

    private static String flattenJsonObj(final String prefix,  final JsonObject jsonObj) {
        String result= "";
        final Set<Map.Entry<String, JsonElement>> entries = jsonObj.entrySet();
        for (final Map.Entry<String, JsonElement> entry : entries) {
            final String key = prefix.isEmpty() ? entry.getKey() : prefix + JSON_TOKEN_DELIMITER + entry.getKey();
            // append the field separator before adding the field.
            if (!result.isEmpty())  {
                result += JSON_FIELD_DELIMITER;
                if (isPrettyPrintingEnabled()) {
                    result += PRETTY_PRINTING;
                }
            }

            // If the actual value is another nested JSON object then process the result recurssively,
            // otherwise, simply add the key and value to the result.
            if (entry.getValue().isJsonObject()) {
                result += flattenJsonObj(key, entry.getValue().getAsJsonObject());
            } else {
                result += String.format(isPrettyPrintingEnabled() ? JSON_ATTR_PRETTY_FORMAT :
                        JSON_ATTR_FORMAT, key, entry.getValue());
            }
        }

        return result;
    }

    /**
     * This main is added only for demonstration purposes
     * @param argv
     * @return
     */
    public static void main(String[] argv) {
        for (final String s: argv) {
            System.out.println(s);
        }
        if (argv.length != 1) {
            System.out.println("Usage: JsonFormatUtils test.json");
            System.exit(1);
        } else {
           System.setProperty("JSON_PRETTY_PRINTING", "true");
           final String output = JsonFormatUtils.processJsonFromFile(argv[0]);
           System.out.println("Output : \n" + output);
        }
    }

}
