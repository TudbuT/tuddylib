package de.tudbut.tools;

import de.tudbut.io.StreamReader;
import de.tudbut.tools.Tools;
import tudbut.parsing.TCN;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class Registry {

    private TCN dataStore;
    private final Set<String> givenOut = new HashSet<>();

    public Registry(String fileName) throws IOException {
        try {
            FileInputStream reader = new FileInputStream(fileName);
            String s = new StreamReader(reader).readAllAsString();
            dataStore = TCN.readMap(Tools.stringToMap(s));
            reader.close();
        } catch (FileNotFoundException e) {
            dataStore = new TCN();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                FileOutputStream writer = new FileOutputStream(fileName);
                writer.write(Tools.mapToString(dataStore.toMap()).getBytes(StandardCharsets.UTF_8));
                writer.close();
            } catch (IOException e) {
                System.out.println(Tools.mapToString(dataStore.toMap()));
                throw new RuntimeException("Unable to save registry! Dumped it to stdout instead.", e);
            }
        }, "Registry shutdown hook"));
    }

    public Registry(TCN dataStore) {
        this.dataStore = dataStore;
    }

    public TCN register(String keyName) throws IllegalAccessException {
        if(givenOut.contains(keyName) && !keyName.startsWith("public:")) {
            throw new IllegalAccessException("Key " + keyName + " has already been given out and is not public.");
        }
        givenOut.add(keyName);
        TCN key = dataStore.getSub(keyName);
        if(key == null) {
            dataStore.set(keyName, key = new TCN());
        }
        return key;
    }

    public void unregister(String keyName, TCN key) throws IllegalAccessException {
        if(dataStore.getSub(keyName) != key) {
            throw new IllegalAccessException("Key " + keyName + " has different content than specified.");
        }
        givenOut.remove(keyName);
    }

    public TCN leak() throws IllegalStateException {
        if(!givenOut.isEmpty()) {
            throw new IllegalStateException("Registry must not have any items currently given out.");
        }
        return dataStore;
    }
}
