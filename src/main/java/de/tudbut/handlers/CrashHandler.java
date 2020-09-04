package de.tudbut.handlers;

import de.tudbut.tools.Application;
import de.tudbut.tools.FileRW;
import de.tudbut.tools.Hasher;
import de.tudbut.tools.Tools;
import de.tudbut.type.StringArray;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.Arrays;
import java.util.Date;

public class CrashHandler {
    private Application app;
    private boolean toFile;
    private String apphash = "ERROR while hashing app!";
    
    public CrashHandler(Application application, boolean writeToFile) {
        app = application;
        app.getLogger().info("Added CrashHandler " + toString());
        String fields = "";
        for(Field field : app.getClass().getFields()) {
            fields += field.hashCode() + field.getGenericType().getTypeName() + field.toGenericString() + field.toString() + field.getName();
        }
        try {
            apphash =
                    Hasher.sha512hex(app.hashCode() + app.getClass().getName() + app.getClass().getCanonicalName() + app.getClass().toString() + app.toString() + fields);
        } catch (Exception ignore) {
        
        }
        toFile = writeToFile;
    }
    
    public String toString() {
        return app.toString() + "_" + this.getClass().toString();
    }
    
    public void handle(Exception e) {
        try {
            app.getLogger().error(e);
            if (toFile) {
                if (!Files.exists(Paths.get("./errors"))) {
                    Files.createDirectory(Paths.get("./errors"));
                }
    
                StringArray stackTrace = new StringArray();
                for(StackTraceElement element : e.getStackTrace())
                    stackTrace.add("    " + element.toString());
                
                FileRW writer =
                        new FileRW("./errors/" + app.toString() + "_" + new Date().toString().replaceAll("[ ]", "_") +
                                "_" + e.getClass().getName() + "_" + apphash.hashCode());
                writer.setContent(new StringArray(new String[]{
                        "//",
                        "// " + Tools.randomOutOfArray(new StringArray(new String[]{
                                "That shouldn't have happened...",
                                "Oopsie!",
                                "Something went terribly wrong!",
                                "Oh no!",
                                "That's not good..."
                        })),
                        "//",
                        "",
                        "Type:           " + e.getClass().getCanonicalName(),
                        "Error message:  " + e.getMessage(),
                        "Stack trace: ",
                        stackTrace.join("\n"),
                        "",
                        "Application hash: " + apphash
                }));
            }
        } catch (Exception e1) {
            app.getLogger().error("Error while trying to handle error! Does " +
                    "the application have enough permissions to write files?");
            app.getLogger().error(e1);
        }
    }
}
