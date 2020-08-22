package de.tudbut;

import tudbut.parsing.Lang;
import tudbut.rendering.tph.TPH301;

public class T3 {
    public static void main(String[] ignored) {
        Lang lang = Lang.factory().addLanguage("en", "tudbut:TudbuT").build("en");

        
        System.out.println(lang.get("tudbut"));
    }
}
