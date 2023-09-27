package de.tudbut.type;

import java.util.Arrays;
import java.util.List;

public class StringArray {
    private String[] array;

    public StringArray(String[] array) {
        this.array = array;
    }

    public StringArray() {
        this.array = new String[]{};
    }

    public String join(String s) {
        StringBuilder joinedString = null;
        for (String arrayItem : this.array) {
            if (joinedString == null)
                joinedString = new StringBuilder(arrayItem);
            else
                joinedString.append(s).append(arrayItem);
        }
        if (joinedString != null) {
            return joinedString.toString();
        }
        return "";
    }

    public String[] asArray() {
        return this.array;
    }

    public List<String> asList() {
        return Arrays.asList(this.array);
    }

    public void add(String s) {
        String[] old = this.array.clone();
        this.array = new String[this.array.length + 1];

        System.arraycopy(old, 0, this.array, 0, old.length);

        this.array[this.array.length - 1] = s;
    }

    public void clear() {
        this.array = new String[]{};
    }

    public void set(String[] array) {
        this.array = array;
    }
}
