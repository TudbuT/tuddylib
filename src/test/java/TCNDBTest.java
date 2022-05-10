import de.tudbut.tools.Tools;
import tudbut.parsing.AddressedTCN;
import tudbut.parsing.JSON;
import tudbut.parsing.TCN;

public class TCNDBTest {
    
    public static void main(String[] args) {
        TCN tcn = new TCN();
        tcn.set("{}{}{}\"\"", "{}}][{\"\"\"");
        System.out.println(JSON.write(tcn));
    
    
    }
}
