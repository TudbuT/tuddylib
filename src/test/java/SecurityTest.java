import de.tudbut.security.AccessKiller;
import de.tudbut.security.DataKeeper;
import de.tudbut.security.StrictnessBuilder;
import de.tudbut.security.permissionmanager.CallClassPermissionManager;

import java.lang.reflect.Field;

public class SecurityTest {

    private static final DataKeeper<String> secret = new DataKeeper<>(new CallClassPermissionManager(AllowedAccessClass.class), StrictnessBuilder.empty(), "hii");

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(500);
        AccessKiller.killReflectionFor(SecurityTest.class);
        AllowedAccessClass.print();
        System.out.println("THIS IS GOOD!");
        System.out.println();
        try {
            Field secretField = SecurityTest.class.getDeclaredField("secret");
            secretField.setAccessible(true);
            System.out.println(secretField.get(null));
            System.out.println("THIS SHOULD NOT HAVE HAPPENED!!");
        } catch (Throwable e) {
            e.printStackTrace(System.out);
            System.out.println("THIS IS GOOD!");
        }
        System.out.println();
        try {
            secret.access(x -> System.out.println("Evil access (NOT OK): " + x.getValue()));
        } catch (Throwable e) {
            e.printStackTrace(System.out);
            System.out.println("THIS IS GOOD!");
        }
        Thread.sleep(500);
        System.exit(0);
    }

    public static class AllowedAccessClass {
        public static void print() {
            secret.access(x -> System.out.println("Normal access (OK): " + x.getValue()));
        }
    }
}
