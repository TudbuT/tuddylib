package de.tudbut.net.nethandler;

import de.tudbut.net.ws.Connection;
import de.tudbut.type.Vector3d;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public interface PacketData {
    int getID();

    default String getString0() {
        return null;
    }

    default String getString1() {
        return null;
    }

    default int getInt0() {
        return 0;
    }

    default int getInt1() {
        return 0;
    }

    default long getLong0() {
        return 0;
    }

    default long getLong1() {
        return 0;
    }

    default Map<String, String> getMap0() {
        return new HashMap<>();
    }

    default Map<String, String> getMap1() {
        return new HashMap<>();
    }

    default double getDouble0() {
        return 0;
    }

    default double getDouble1() {
        return 0;
    }

    default float getFloat0() {
        return 0f;
    }

    default float getFloat1() {
        return 0f;
    }

    default Vector3d getVector0() {
        return new Vector3d(0, 0, 0);
    }

    default Vector3d getVector1() {
        return new Vector3d(0, 0, 0);
    }


    default Packet toPacket() {
        return new Packet(this);
    }

    default String asString() {
        return new Packet(this).toString();
    }

    default void sendInPieces(int bytesPerPiece, Connection connection) throws IOException {
        new Packet(this).sendAsPieces(bytesPerPiece, connection);
    }
}
