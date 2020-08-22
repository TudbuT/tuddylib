package tudbut.tools;

import tudbut.obj.TLList;

import java.util.List;

public class ArrayTools {
    public static <T> T[] replace(T[] array, T repl, T with) { array=array.clone();for (int i=0;i<array.length;i++){if(array[i]!=null){if(array[i].equals(repl))array[i]=with;}else if(repl==null)array[i]=with;}return array; }

    public static <T> T[] arrayFromList(List<T> list) {
        T[] array = ClassUtils.instantiateArray(ClassUtils.getTypeArguments(ClassUtils.getMethodByName(ArrayTools.class, "arrayFromList"))[0], list.size());
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }
        return array;
    }
    public static <T> T[] arrayFromList(TLList<T> list) {
        return list.toArray();
    }
}
