import de.tudbut.tools.Tools;
import tudbut.parsing.AddressedTCN;
import tudbut.parsing.JSON;
import tudbut.parsing.TCN;

public class TCNDBTest {
    
    public static void main(String[] args) {
        System.out.println("START");
        TCN other = new TCN();
        TCN tcn = new TCN();
        tcn.set("rigr", other);
        other.set("main", tcn);
        other.set("wirufh ", "ouheir");
        tcn.set("rigrj", "th");
        tcn.set("rigj", tcn);
        System.out.println(JSON.write(AddressedTCN.normalToAddressed(tcn)));
        System.out.println(JSON.writeReadable(AddressedTCN.normalToAddressed(tcn)));
        System.out.println(Tools.mapToString(AddressedTCN.normalToAddressed(tcn).toMap()));
        System.out.println(TCN.readMap(Tools.stringToMap(Tools.mapToString(AddressedTCN.normalToAddressed(tcn).toMap()))));
        System.out.println(AddressedTCN.normalToAddressed(AddressedTCN.addressedToNormal(AddressedTCN.normalToAddressed(tcn))));
        tcn = AddressedTCN.addressedToNormal(AddressedTCN.normalToAddressed(tcn));
        
        
        
    }
}
