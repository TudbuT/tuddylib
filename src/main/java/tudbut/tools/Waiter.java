package tudbut.tools;

import java.util.Date;

public class Waiter {
    public static void wait(int ms) {
        long startedAt = new Date().getTime();
        while (new Date().getTime() - startedAt < ms);
    }
}
