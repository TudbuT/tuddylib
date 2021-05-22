import tudbut.parsing.DatabaseTCNConverter;
import tudbut.parsing.TCN;

public class TCNDBTest {
    
    public static void main(String[] args) {
        TCN tcn = new TCN();
        tcn.set("rigrj", "th");
        tcn.set("rigj", tcn);
        TCN main = new TCN();
        main.set("main", tcn);
        System.out.println(DatabaseTCNConverter.usableToDB(main));
        System.out.println(DatabaseTCNConverter.dbToUsable(DatabaseTCNConverter.usableToDB(main)));
    }
}
