import tudbut.tools.*;
import de.tudbut.tools.*;
import tudbut.parsing.*;
import de.tudbut.io.*;
import java.io.FileInputStream;

public class CSTCN2 {
    private final String t1     = null;
    private final String[][] t2 = null;
    private static int t3 = 0;
    private static boolean bool = false;

  //public CSTCN2(String t1, String[][] t2) {
  //    this.t1 = t1;
  //    this.t2 = t2;
  //}
    
    public String toString() {
        return "t1=" + t1 + ",t2=" + t2 + ",t3=" + t3;
    }

    public static void main(String[] args) throws Exception {
        CSTCN2 obj = (CSTCN2)ConfigSaverTCN2.read(JSON.read(new StreamReader(new FileInputStream("test.json")).readAllAsString()), null);
        System.out.println(JSON.writeReadable((TCN)ConfigSaverTCN2.write(obj, true, true)));

        String s = "Hi there, I'm BRUH.";
        System.out.println(Tools.readf("Hi there, I'm {}.", s));
    }
}
