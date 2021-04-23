package tudbut.net.http;

import tudbut.obj.DoubleObject;

/**
 * Header for HTTP exchanges
 */
public class HTTPHeader {
    private final String name;
    private final String value;
    private final DoubleObject<String> param;
    
    /**
     * Constructs a HTTPHeader from a key and a value
     * @param nameIn Key
     * @param valueIn Value
     */
    public HTTPHeader(String nameIn, String valueIn) {
        this(nameIn, valueIn, null);
    }
    
    /**
     * Constructs a HTTPHeader from a key, a value, and a parameter
     * @param nameIn Key
     * @param valueIn Value
     * @param paramIn Parameter
     */
    public HTTPHeader(String nameIn, String valueIn, DoubleObject<String> paramIn) {
        name = nameIn;
        value = valueIn;
        param = paramIn;
    }
    
    /**
     *
     * @return The form used in the request
     */
    public String toString() {
        return name + ": " + value + (param == null ? "" : "; " + param.get1() + "=" + param.get2());
    }
    
    public String key() {
        return name;
    }

    public String value() {
        return value;
    }

    public DoubleObject<String> parameter() {
        return param.clone();
    }
}
