package tudbut.obj;

import com.sun.org.apache.bcel.internal.util.ClassLoader;
import de.tudbut.tools.Tools;
import tudbut.tools.ArrayTools;
import tudbut.tools.ClassUtils;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TLList<T> {
    private final Class<T> tClass;
    private PAtomic<T>[] array = new PAtomic[8];
    private int size = 0;
    private int actualSize = 8;


    public TLList() {
        this.tClass = ;
        HashSet
    }

    public void add(T t) {
        if (size >= actualSize) {
            actualSize += 8;
            Tools.copyArray(array, array = new PAtomic[actualSize], actualSize - 8);
        }
        array[size] = new PAtomic<T>(t);
        size++;
    }

    public void add(T[] t) {
        for (int i = 0; i < t.length; i++) {
            add(t[i]);
        }
    }

    public void add(TLList<T> t) {
        add(ArrayTools.arrayFromList(t));
    }

    public void add(List<T> t) {
        add(ArrayTools.arrayFromList(t));
    }

    public void set(int i, T t) {
        try {
            array[i].object = t;
        } catch (NullPointerException e) {
            throw new ArrayIndexOutOfBoundsException(i);
        }
    }

    public T get(int i) {
        if(i < size) {
            return array[i].object;
        }
        else {
            throw new ArrayIndexOutOfBoundsException(i);
        }
    }

    public void remove(int i) {
        if(i < size) {
            i++;
            size--;
            PAtomic<T>[] newArray = new PAtomic[actualSize-8 <= size ? actualSize = actualSize-8 : actualSize];
            boolean took = false;
            for (int j = 0, k = 0; j < actualSize; j++) {
                if(j == i && !took) {
                    took = true;
                    j--;
                }
                newArray[j] = array[k];

                k++;
            }
            array = newArray;
        }
        else {
            throw new ArrayIndexOutOfBoundsException(i);
        }
    }

    public int length() {
        return size;
    }

    public int lengthInMemory() {
        return actualSize;
    }

    public T[] toArray() {
        T[] r = ClassUtils.instantiateArray(tClass, size);
        Tools.copyArray(PAtomic.toArray(array), r, size);
        return r;
    }

    public void replace(T replace, T[] with) {
        TLList<T> list = new TLList<T>(tClass);
        for (int i = 0; i < array.length; i++) {
            if(array[i].object.equals(replace)){
                add(with);
            }
            else
                add(array[i].object);
        }
        clear();
        add(list);
    }

    public void replace(T replace, T with) {
        T[] list = ArrayTools.replace(PAtomic.toArray(array), replace, with);
        clear();
        add(list);
    }

    public void set(TLList<T> list) {
        clear();
        add(list);
    }

    public void set(List<T> list) {
        clear();
        add(list);
    }

    public void clear() {
        array = new PAtomic[8];
        size = 0;
        actualSize = 8;
    }
}
