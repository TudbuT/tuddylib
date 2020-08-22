package de.tudbut.software.fent;

import de.tudbut.tools.FileRW;
import de.tudbut.tools.bintools.BinFileRW;
import de.tudbut.tools.bintools.encoding.Seed;
import de.tudbut.tools.bintools.encoding.TCryptV3;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class X {
    
    public static void main(String[] args) throws Exception {
        FileRW file = new FileRW("encodepls/y.txt");
        
        System.out.println(new ScriptEngineManager().getEngineByExtension("js").eval("JSON.stringify(\"" + file.getContent().join("\n") + "\")"));
    }
}
