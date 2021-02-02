package tudbut.tools;

public class StringTools {

    public static String removeIndents(String s) {
        while(s.contains("\n ")) {
            s = s.replaceAll("\n ", "\n");
        }
        while(s.contains("\n\t")) {
            s = s.replaceAll("\n\t", "\n");
        }
        return s;
    }
    
    public static String multiply(String s, int i) {
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < i; j++) {
            builder.append(s);
        }
        return builder.toString();
    }
}
