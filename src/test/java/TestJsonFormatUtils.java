import org.junit.Assert;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class TestJsonFormatUtils {

    private void createJsonFile(final String fileName, final String content) throws IOException {
        try (PrintStream out = new PrintStream(new FileOutputStream(fileName))) {
            out.print(content);
        }
    }

    @Test
    public void testJsonFormat1() throws Exception {
        final String input = "{\"a\": 1, \"b\": true, \"c\": {\"d\": 3, \"e\": \"test\"}}";
        final String expected = "{\"a\": 1, \"b\": true, \"c.d\": 3, \"c.e\": \"test\"}";
        final String jsonFile =  "/tmp/test1.json";
        createJsonFile(jsonFile, input);
        final String output = JsonFormatUtils.processJsonFromFile(jsonFile);
        Assert.assertTrue(output.equals(expected));
    }

    @Test
    public void testJsonFormat2() throws Exception {
        final String input = "{\"a\": 1, \"b\": true, \"c\": {\"d\": {\"n_k\": 5, \"n_k2\": \"n_value\"}, \"e\": \"test\"}}";
        final String expected = "{\"a\": 1, \"b\": true, \"c.d.n_k\": 5, \"c.d.n_k2\": \"n_value\", \"c.e\": \"test\"}";
        final String jsonFile =  "/tmp/test2.json";
        createJsonFile(jsonFile, input);
        final String output = JsonFormatUtils.processJsonFromFile(jsonFile);
        Assert.assertTrue(output.equals(expected));
    }

}
