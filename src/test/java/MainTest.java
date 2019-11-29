import org.junit.Test;

import java.io.*;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void forTest() {
        Main m = new Main();
        String s = m.forTest();
        assertEquals("kuku", s);
    }

    @Test
    public void main() {
        String line;
        String input;
        String output;

        try {
            BufferedReader br = new BufferedReader(new FileReader(".\\src\\test\\resources\\tests.txt"));
            while((line = br.readLine()) != null) {
                String[] s_array = line.split("\\t");
                input = s_array[0];
                output = s_array[1].toUpperCase();

                System.setIn(new ByteArrayInputStream(input.getBytes()));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                System.setOut(ps);

                Main.main(null);

                assertEquals(output, baos.toString().toUpperCase());
            }
        } catch (IOException e) {
            System.out.println(Paths.get(".").toAbsolutePath().normalize().toString());
        }
    }
}
