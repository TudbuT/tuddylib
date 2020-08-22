package tudbut.net.http;

import tudbut.obj.DoubleObject;

public class HTTPHeader {
    private final String name;
    private final String value;
    private final DoubleObject<String> param;

    public HTTPHeader(String nameIn, String valueIn) {
        this(nameIn, valueIn, null);
    }

    public HTTPHeader(String nameIn, String valueIn, DoubleObject<String> paramIn) {
        name = nameIn;
        value = valueIn;
        param = paramIn;
    }

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
