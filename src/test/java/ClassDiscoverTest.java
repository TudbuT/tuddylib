import de.tudbut.tools.DiscoverClasses;

public class ClassDiscoverTest {
    public static void main(String[] args) {
        System.out.println(DiscoverClasses.of(ClassLoader.getSystemClassLoader()).in("de.tudbut.tools").run());
    }
}
