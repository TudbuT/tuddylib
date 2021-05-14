package tudbut.test;

import tudbut.parsing.JSON;
import tudbut.parsing.TCN;
import tudbut.parsing.TCNArray;

public class TCNFullTest {
    
    public static void main(String[] args) throws TCN.TCNException, JSON.JSONFormatException {
        String check = "";
        String s = "";
        
        TCN test = new TCN();
        TCN other = new TCN();
        other.set("otherArray", new TCNArray());
        other.getArray("otherArray").add(3);
        other.set("test", "lol");
        other.set("terr", "lolrr");
        test.set("array", new TCNArray());
        test.set("object", other);
        test.getArray("array").add("LMAO");
        test.getArray("array").add("Lhh");
        test.getArray("array").add(other);
        test.set("otherArray", new TCNArray());
    
        System.out.println(s="TCN 1: ");
        check += s + "\n";
        System.out.println(s=test.toString());
        check += s + "\n";
        System.out.println(s="\n\n\nTCN 2: ");
        check += s + "\n";
        System.out.println(s=TCN.read(TCN.readMap(test.toMap()).toString()).toString());
        check += s + "\n";
        System.out.println(s="\n\n\nTCN Read: ");
        check += s + "\n";
        System.out.println(s=TCN.read(test.toString()).getSub("object").getArray("otherArray").getInteger(0).toString());
        check += s + "\n";
        System.out.println(s="\n\n\nJSON 1: ");
        check += s + "\n";
        System.out.println(s=JSON.writeReadable(test));
        check += s + "\n";
        System.out.println(s="\n\n\nJSON-TCN 1: ");
        check += s + "\n";
        System.out.println(s=JSON.read(JSON.writeReadable(test)).toString());
        check += s + "\n";
        System.out.println(s="\n\n\nJSON 2: ");
        check += s + "\n";
        System.out.println(s=JSON.writeReadable(JSON.read(JSON.writeReadable(test))));
        check += s + "\n";
        System.out.println(s="\n\n\nJSON 3: ");
        check += s + "\n";
        System.out.println(s=JSON.write(test));
        check += s + "\n";
        System.out.println(s="\n\n\nJSON Part: ");
        check += s + "\n";
        System.out.println(s=JSON.writeReadable(JSON.read(JSON.writeReadable(TCN.read(test.toString()).getArray("array").getSub(2).getArray("otherArray").toTCN()))));
        check += s + "\n";
        
        
        System.out.println("\n--- TEST DONE ---\n");
        
        System.out.println("Expected output: ");
        System.out.println(("\nTCN 1: \n" +
                            "\n" +
                            "array [\n" +
                            "    ; LMAO\n" +
                            "    ; Lhh\n" +
                            "\n" +
                            "    ; {\n" +
                            "\n" +
                            "        otherArray [\n" +
                            "            ; 3\n" +
                            "        ]\n" +
                            "\n" +
                            "        test: lol\n" +
                            "        terr: lolrr\n" +
                            "    }\n" +
                            "\n" +
                            "]\n" +
                            "\n" +
                            "\n" +
                            "object {\n" +
                            "\n" +
                            "    otherArray [\n" +
                            "        ; 3\n" +
                            "    ]\n" +
                            "\n" +
                            "    test: lol\n" +
                            "    terr: lolrr\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "otherArray [\n" +
                            "]\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "TCN 2: \n" +
                            "\n" +
                            "array [\n" +
                            "    ; LMAO\n" +
                            "    ; Lhh\n" +
                            "\n" +
                            "    ; {\n" +
                            "\n" +
                            "        otherArray [\n" +
                            "            ; 3\n" +
                            "        ]\n" +
                            "\n" +
                            "        test: lol\n" +
                            "        terr: lolrr\n" +
                            "    }\n" +
                            "\n" +
                            "]\n" +
                            "\n" +
                            "\n" +
                            "object {\n" +
                            "\n" +
                            "    otherArray [\n" +
                            "        ; 3\n" +
                            "    ]\n" +
                            "\n" +
                            "    test: lol\n" +
                            "    terr: lolrr\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "TCN Read: \n" +
                            "3\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "JSON 1: \n" +
                            "{\n" +
                            "  \"array\": [\n" +
                            "    \"LMAO\", \n" +
                            "    \"Lhh\", \n" +
                            "    {\n" +
                            "      \"otherArray\": [\n" +
                            "        3\n" +
                            "      ], \n" +
                            "      \"test\": \"lol\", \n" +
                            "      \"terr\": \"lolrr\"\n" +
                            "    }\n" +
                            "  ], \n" +
                            "  \"object\": {\n" +
                            "    \"otherArray\": [\n" +
                            "      3\n" +
                            "    ], \n" +
                            "    \"test\": \"lol\", \n" +
                            "    \"terr\": \"lolrr\"\n" +
                            "  }, \n" +
                            "  \"otherArray\": [\n" +
                            "  ]\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "JSON-TCN 1: \n" +
                            "\n" +
                            "array [\n" +
                            "    ; LMAO\n" +
                            "    ; Lhh\n" +
                            "\n" +
                            "    ; {\n" +
                            "\n" +
                            "        otherArray [\n" +
                            "            ; 3\n" +
                            "        ]\n" +
                            "\n" +
                            "        test: lol\n" +
                            "        terr: lolrr\n" +
                            "    }\n" +
                            "\n" +
                            "]\n" +
                            "\n" +
                            "\n" +
                            "object {\n" +
                            "\n" +
                            "    otherArray [\n" +
                            "        ; 3\n" +
                            "    ]\n" +
                            "\n" +
                            "    test: lol\n" +
                            "    terr: lolrr\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "otherArray [\n" +
                            "    ; \n" +
                            "]\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "JSON 2: \n" +
                            "{\n" +
                            "  \"array\": [\n" +
                            "    \"LMAO\", \n" +
                            "    \"Lhh\", \n" +
                            "    {\n" +
                            "      \"otherArray\": [\n" +
                            "        \"3\"\n" +
                            "      ], \n" +
                            "      \"test\": \"lol\", \n" +
                            "      \"terr\": \"lolrr\"\n" +
                            "    }\n" +
                            "  ], \n" +
                            "  \"object\": {\n" +
                            "    \"otherArray\": [\n" +
                            "      \"3\"\n" +
                            "    ], \n" +
                            "    \"test\": \"lol\", \n" +
                            "    \"terr\": \"lolrr\"\n" +
                            "  }, \n" +
                            "  \"otherArray\": [\n" +
                            "    \"\"\n" +
                            "  ]\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "JSON 3: \n" +
                            "{\"array\":[\"LMAO\",\"Lhh\",{\"otherArray\":[3],\"test\":\"lol\",\"terr\":\"lolrr\"}],\"object\":{\"otherArray\":[3],\"test\":\"lol\",\"terr\":\"lolrr\"}," +
                            "\"otherArray\":[]}\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "JSON Part: \n" +
                            "[\n" +
                            "  \"3\"\n" +
                            "]\n").replaceAll("\n", "\n      | "));
        
        System.out.println("\n- CHECK -");
        boolean pass = check.equals("TCN 1: \n" +
                                    "\n" +
                                    "array [\n" +
                                    "    ; LMAO\n" +
                                    "    ; Lhh\n" +
                                    "\n" +
                                    "    ; {\n" +
                                    "\n" +
                                    "        otherArray [\n" +
                                    "            ; 3\n" +
                                    "        ]\n" +
                                    "\n" +
                                    "        test: lol\n" +
                                    "        terr: lolrr\n" +
                                    "    }\n" +
                                    "\n" +
                                    "]\n" +
                                    "\n" +
                                    "\n" +
                                    "object {\n" +
                                    "\n" +
                                    "    otherArray [\n" +
                                    "        ; 3\n" +
                                    "    ]\n" +
                                    "\n" +
                                    "    test: lol\n" +
                                    "    terr: lolrr\n" +
                                    "}\n" +
                                    "\n" +
                                    "\n" +
                                    "otherArray [\n" +
                                    "]\n" +
                                    "\n" +
                                    "\n" +
                                    "\n" +
                                    "\n" +
                                    "TCN 2: \n" +
                                    "\n" +
                                    "array [\n" +
                                    "    ; LMAO\n" +
                                    "    ; Lhh\n" +
                                    "\n" +
                                    "    ; {\n" +
                                    "\n" +
                                    "        otherArray [\n" +
                                    "            ; 3\n" +
                                    "        ]\n" +
                                    "\n" +
                                    "        test: lol\n" +
                                    "        terr: lolrr\n" +
                                    "    }\n" +
                                    "\n" +
                                    "]\n" +
                                    "\n" +
                                    "\n" +
                                    "object {\n" +
                                    "\n" +
                                    "    otherArray [\n" +
                                    "        ; 3\n" +
                                    "    ]\n" +
                                    "\n" +
                                    "    test: lol\n" +
                                    "    terr: lolrr\n" +
                                    "}\n" +
                                    "\n" +
                                    "\n" +
                                    "\n" +
                                    "\n" +
                                    "TCN Read: \n" +
                                    "3\n" +
                                    "\n" +
                                    "\n" +
                                    "\n" +
                                    "JSON 1: \n" +
                                    "{\n" +
                                    "  \"array\": [\n" +
                                    "    \"LMAO\", \n" +
                                    "    \"Lhh\", \n" +
                                    "    {\n" +
                                    "      \"otherArray\": [\n" +
                                    "        3\n" +
                                    "      ], \n" +
                                    "      \"test\": \"lol\", \n" +
                                    "      \"terr\": \"lolrr\"\n" +
                                    "    }\n" +
                                    "  ], \n" +
                                    "  \"object\": {\n" +
                                    "    \"otherArray\": [\n" +
                                    "      3\n" +
                                    "    ], \n" +
                                    "    \"test\": \"lol\", \n" +
                                    "    \"terr\": \"lolrr\"\n" +
                                    "  }, \n" +
                                    "  \"otherArray\": [\n" +
                                    "  ]\n" +
                                    "}\n" +
                                    "\n" +
                                    "\n" +
                                    "\n" +
                                    "JSON-TCN 1: \n" +
                                    "\n" +
                                    "array [\n" +
                                    "    ; LMAO\n" +
                                    "    ; Lhh\n" +
                                    "\n" +
                                    "    ; {\n" +
                                    "\n" +
                                    "        otherArray [\n" +
                                    "            ; 3\n" +
                                    "        ]\n" +
                                    "\n" +
                                    "        test: lol\n" +
                                    "        terr: lolrr\n" +
                                    "    }\n" +
                                    "\n" +
                                    "]\n" +
                                    "\n" +
                                    "\n" +
                                    "object {\n" +
                                    "\n" +
                                    "    otherArray [\n" +
                                    "        ; 3\n" +
                                    "    ]\n" +
                                    "\n" +
                                    "    test: lol\n" +
                                    "    terr: lolrr\n" +
                                    "}\n" +
                                    "\n" +
                                    "\n" +
                                    "otherArray [\n" +
                                    "    ; \n" +
                                    "]\n" +
                                    "\n" +
                                    "\n" +
                                    "\n" +
                                    "\n" +
                                    "JSON 2: \n" +
                                    "{\n" +
                                    "  \"array\": [\n" +
                                    "    \"LMAO\", \n" +
                                    "    \"Lhh\", \n" +
                                    "    {\n" +
                                    "      \"otherArray\": [\n" +
                                    "        \"3\"\n" +
                                    "      ], \n" +
                                    "      \"test\": \"lol\", \n" +
                                    "      \"terr\": \"lolrr\"\n" +
                                    "    }\n" +
                                    "  ], \n" +
                                    "  \"object\": {\n" +
                                    "    \"otherArray\": [\n" +
                                    "      \"3\"\n" +
                                    "    ], \n" +
                                    "    \"test\": \"lol\", \n" +
                                    "    \"terr\": \"lolrr\"\n" +
                                    "  }, \n" +
                                    "  \"otherArray\": [\n" +
                                    "    \"\"\n" +
                                    "  ]\n" +
                                    "}\n" +
                                    "\n" +
                                    "\n" +
                                    "\n" +
                                    "JSON 3: \n" +
                                    "{\"array\":[\"LMAO\",\"Lhh\",{\"otherArray\":[3],\"test\":\"lol\",\"terr\":\"lolrr\"}],\"object\":{\"otherArray\":[3],\"test\":\"lol\",\"terr\":\"lolrr\"}," +
                                    "\"otherArray\":[]}\n" +
                                    "\n" +
                                    "\n" +
                                    "\n" +
                                    "JSON Part: \n" +
                                    "[\n" +
                                    "  \"3\"\n" +
                                    "]\n");
        if(pass) {
            System.out.println("PASSED");
            System.exit(0);
        } else {
            System.out.println("FAILED");
            System.exit(1);
        }
    }
    
}
