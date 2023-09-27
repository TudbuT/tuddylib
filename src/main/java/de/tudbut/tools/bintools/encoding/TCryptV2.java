package de.tudbut.tools.bintools.encoding;

import de.tudbut.tools.Tools;
import de.tudbut.tools.bintools.BinFileRW;

import java.io.IOException;

public class TCryptV2 extends TCryptV1 {
    public TCryptV2(String seed) {
        super(seed);
    }

    public Value encryptFile(String file) throws IOException {
        Value n = this.encrypt(Tools.charArrayToByteArray(Tools.intArrayToCharArray(new BinFileRW(file).getBinContent())));
        int[] r = new int[n.getB().length];

        for (int i = 0; i < r.length; i++) {
            r[i] = Byte.toUnsignedInt(n.getB()[i]);
        }

        return new Value(r);
    }

    public Value decryptFile(String file) throws IOException {
        Value n = this.decrypt(Tools.charArrayToByteArray(Tools.intArrayToCharArray(new BinFileRW(file).getBinContent())));
        int[] r = new int[n.getB().length];

        for (int i = 0; i < r.length; i++) {
            r[i] = Byte.toUnsignedInt(n.getB()[i]);
        }

        return new Value(r);
    }
}
