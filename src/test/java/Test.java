import com.joojn.utils.FieldReflector;
import com.joojn.utils.FileWrapper;
import com.joojn.utils.JarUtil;
import com.joojn.utils.MethodReflector;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Test {

    public static void main(String[] args) throws IOException {
        testFileUtil();
        testReflector();
    }

    public static void testFileUtil() throws IOException {

        String text = "Hello World!";

        FileWrapper file = new FileWrapper("test.txt", StandardCharsets.UTF_8);
        file.write(text);

        String resultText = file.read();

        // assert text.equals(resultText);
        if(!text.equals(resultText))
            throw new AssertionError("Texts are not equal!");
    }

    public static void testReflector() {
        String text = "Hello World!";
        int shorten = 5;

        String result = new MethodReflector.Builder()
                // .name("substring")
                .targetClass(String.class)
                .searchAll(false)
                .returning(String.class)
                .parameters(int.class, int.class)
                .instance(text)
                .build()
                .invoke(text, shorten);

        if(!result.equals(text.substring(0, shorten)))
            throw new AssertionError("Texts are not equal!");
    }
}
