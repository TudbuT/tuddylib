package tudbut.net.http;

import de.tudbut.tools.Tools;
import tudbut.obj.DoubleObject;
import tudbut.obj.TLMap;

/**
 * Header for HTTP exchanges
 */
public class HTTPHeader {
    private final String name;
    private final String value;
    private final TLMap<String, String> param;
    
    public HTTPHeader(String s) {
        String[] spl = s.split(": ", 2);
        name = spl[0];
        spl = spl[1].split("; ");
        value = spl[0];
        param = new TLMap<>();
        for (int i = 1 ; i < spl.length ; i++) {
            try {
                String[] kv = spl[i].split("=", 2);
                if(kv[1].startsWith("\"")) {
                    kv[1] = kv[1]
                            .split("\"", 2)[1]
                            .replaceAll("%", "%P")
                            .replaceAll("\\\\", "%B")
                            .replaceAll("%B%B", "\\")
                            .replaceAll("%Bn", "\n")
                            .replaceAll("%B\"", "\"")
                            .replaceAll("%B", "\\")
                            .replaceAll("%P", "%");
                    kv[1] = kv[1].substring(0, kv.length - 2);
                }
                else {
                    kv[1] = kv[1]
                            .replaceAll("%", "%P")
                            .replaceAll("\\\\", "%B")
                            .replaceAll("%B%B", "\\")
                            .replaceAll("%Bn", "\n")
                            .replaceAll("%B", "\\")
                            .replaceAll("%P", "%");
                }
                param.set(kv[0], kv[1]);
            } catch (Exception ignored) {}
        }
    }
    
    /**
     * Constructs a HTTPHeader from a key and a value
     * @param nameIn Key
     * @param valueIn Value
     */
    public HTTPHeader(String nameIn, String valueIn) {
        this(nameIn, valueIn, new TLMap<>());
    }
    
    /**
     * Constructs a HTTPHeader from a key, a value, and a parameter
     * @param nameIn Key
     * @param valueIn Value
     * @param paramIn Parameter
     */
    public HTTPHeader(String nameIn, String valueIn, TLMap<String, String> paramIn) {
        name = nameIn;
        value = valueIn;
        param = paramIn;
    }
    
    /**
     *
     * @return The form used in the request
     */
    public String toString() {
        String m = TLMap.mapToString(param).replaceAll(":", "=\"").replaceAll(";", "\"; ");
        if(param.size() > 0)
            m = m.substring(0, m.length()-2);
        return name + ": " + value + (param.size() == 0 ? "" : "; " + m);
    }
    
    public String key() {
        return name;
    }

    public String value() {
        return value;
    }

    public TLMap<String, String> parameter() {
        return param.clone();
    }
}
