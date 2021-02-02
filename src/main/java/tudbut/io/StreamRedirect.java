package tudbut.io;

import de.tudbut.timer.AsyncTask;
import de.tudbut.type.Nothing;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class StreamRedirect {
    
    public static AsyncTask<Nothing> redirect(InputStream from, OutputStream to, Map<Integer, Integer[]> replacers) {
        return new AsyncTask<>(() -> {
            int i;
            while ((i = from.read()) != -1) {
                block0:
                {
                    for (Integer key : replacers.keySet()) {
                        if (i == key) {
                            Integer[] r = replacers.get(key);
                            for (Integer j : r) {
                                to.write(j);
                            }
                            break block0;
                        }
                    }
                    to.write(i);
                }
            }
            return null;
        });
    }
}
