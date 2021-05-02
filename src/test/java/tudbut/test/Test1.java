package tudbut.test;

import de.tudbut.tools.Tools;
import tudbut.net.http.HTTPUtils;
import tudbut.parsing.JSON;
import tudbut.parsing.TCN;
import tudbut.tools.encryption.Key;
import tudbut.tools.encryption.RawKey;

import java.io.IOException;

public class Test1 {
    
    public static void main(String[] args) throws IOException, JSON.JSONFormatException, TCN.TCNException {
    
        System.out.println(Tools.mapToString(JSON.read("[\n" +
                                           "    {\n" +
                                           "        \"urlset\": [\n" +
                                           "            {\n" +
                                           "                \"url\": [\n" +
                                           "                    {\"loc\": \"http://tudbut.de/\"},\n" +
                                           "                    {\"lastmod\": \"2021-05-02\"},\n" +
                                           "                    {\"changefreq\": \"weekly\"},\n" +
                                           "                    {\"priority\": 1}\n" +
                                           "                ]\n" +
                                           "            },\n" +
                                           "            {\n" +
                                           "                \"url\": [\n" +
                                           "                    {\"loc\": \"http://tudbut.de/docs\"},\n" +
                                           "                    {\"lastmod\": \"2021-05-02\"},\n" +
                                           "                    {\"changefreq\": \"daily\"},\n" +
                                           "                    {\"priority\": 1}\n" +
                                           "                ]\n" +
                                           "            },\n" +
                                           "            {\n" +
                                           "                \"url\": [\n" +
                                           "                    {\"loc\": \"http://tudbut.de/embed\"},\n" +
                                           "                    {\"lastmod\": \"2021-05-02\"},\n" +
                                           "                    {\"changefreq\": \"weekly\"},\n" +
                                           "                    {\"priority\": 0.5}\n" +
                                           "                ]\n" +
                                           "            },\n" +
                                           "            {\n" +
                                           "                \"url\": [\n" +
                                           "                    {\"loc\": \"http://tudbut.de/apigui\"},\n" +
                                           "                    {\"lastmod\": \"2021-05-02\"},\n" +
                                           "                    {\"changefreq\": \"weekly\"},\n" +
                                           "                    {\"priority\": 1}\n" +
                                           "                ]\n" +
                                           "            }\n" +
                                           "        ]\n" +
                                           "    }\n" +
                                           "]").toMap()));
    }
}
