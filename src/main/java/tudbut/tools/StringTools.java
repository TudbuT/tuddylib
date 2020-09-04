package tudbut.tools;

public class StringTools {

    public static String removeIndents(String s) {
        while(s.contains("\n ")) {
            s = s.replaceAll("\n ", "\n");
        }
        return s;
    }
}
