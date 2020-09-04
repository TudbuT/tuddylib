package de.tudbut.ui.consoleui;

import de.tudbut.type.StringArray;
import de.tudbut.type.WIP;

@WIP(
        works = true
)
public class ConsoleUI {
    public static String HORIZONTAL_LINE = "─";
    public static String VERTICAL_LINE = "│";

    protected static StringArray create(
            String title, String bgColor,
            String fontColor,
            String rColor, StringArray theText
    ) {
        StringBuilder x = new StringBuilder();
        for (int i = 0; i < title.length(); i++) {
            x.append("─");
        }
        for (int i1 = 0; i1 < theText.asArray().length; i1++) {
            String s = theText.asArray()[i1];
            StringBuilder y = new StringBuilder();
            for (int i2 = 0; i2 < title.length() + 8 - s.length(); i2++) {
                y.append(" ");
            }
            theText.asArray()[i1] = color(fontColor, bgColor) + s + y;
        }
        theText.asArray()[0] = "│" + theText.asArray()[0];
        return new StringArray(
                (
                        "" +
                        color(rColor, bgColor) + "┌────" + title + "────┐\u001b[0m\n" +
                        color(rColor, bgColor) +
                        theText.join(
                                color(rColor, bgColor) +
                                "│\u001b[0m\n" + color(rColor, bgColor) +
                                "│" + color(rColor, bgColor)
                        ) +
                        color(rColor, bgColor) + "│\u001b[0m\n" +
                        color(rColor, bgColor) + "└────" + x + "────┘\u001b[0m"
                ).split("\n")
        );
    }

    private static String color(String cUpper, String cLower) {
        return "\u001b[38;5;" + cUpper + "m\u001b[48;5;" + cLower + "m";
    }

    protected static void clear() {
        for (int i = 0; i < 2048; i++) {
            System.out.print("\u001b[K\u001b[A\u001b[K");
        }
    }
}
