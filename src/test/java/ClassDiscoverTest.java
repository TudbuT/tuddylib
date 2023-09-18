import de.tudbut.tools.DiscoverClasses;
import de.tudbut.tools.Registry;
import tudbut.parsing.TCN;

import java.io.IOException;

public class ClassDiscoverTest {
    public static void main(String[] args) throws IOException, IllegalAccessException {
        Registry registry = new Registry("registrytest.tcnm");
        TCN data = registry.register("test");
        if(data.get("Package") == null)
            data.set("Package", "de.tudbut.tools");
        System.out.println(DiscoverClasses.of(ClassLoader.getSystemClassLoader()).in(data.getString("Package")).run());
    }
}
