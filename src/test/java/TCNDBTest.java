import de.tudbut.parsing.JSON;
import de.tudbut.parsing.TCN;

public class TCNDBTest {
    
    public static void main(String[] args) {
        TCN tcn = new TCN();
        tcn.set("{}{}{}\"\"", "{}}][{\"\"\"");
        System.out.println(JSON.write(tcn));
    
    
    }
}
