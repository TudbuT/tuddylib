package de.tudbut.net.nethandler;

import de.tudbut.net.ws.Connection;
import de.tudbut.tools.Tools;
import de.tudbut.type.Vector3d;
import tudbut.global.DebugStateManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Packet {
    private final AtomicInteger id = new AtomicInteger(-1);
    private final AtomicReference<PacketData> data = new AtomicReference<>();

    public Packet(PacketData data) {
        setData(data);
    }

    public Packet() {
    }

    public static PacketData fromString(String packet) {
        try {
            return fromString(packet, null);
        }
        catch (Exception ignore) {
            DebugStateManager.getDebugLogger().debug("Packet couldn't be received. Returning null!");
            return null;
        }
    }

    public static PacketData fromString(String packet, Connection c) throws IOException {
        if (packet.startsWith("[BEGIN")) {
            StringBuilder d = new StringBuilder(packet);

            if (!d.toString().startsWith("[BEGIN(") && !d.toString().endsWith(")["))
                throw new IllegalStateException("Next data in line is not a splitPacket!");

            d = new StringBuilder();
            while (!d.toString().endsWith("]END]")) {
                d.append(c.receive());
            }

            d = new StringBuilder(d.substring(0, d.length() - "]END]".length()));

            return fromString(d.toString());
        }


        Map<String, String> map = Tools.stringToMap(packet);

        return new PacketData() {
            public int getID() {
                return Integer.parseInt(map.get("id"));
            }

            public String getString0() {
                return map.get("s0");
            }

            public String getString1() {
                return map.get("s1");
            }

            public int getInt0() {
                return Integer.parseInt(map.getOrDefault("i0", "0"));
            }

            public int getInt1() {
                return Integer.parseInt(map.getOrDefault("i1", "0"));
            }

            public long getLong0() {
                return Long.parseLong(map.getOrDefault("l0", "0"));
            }

            public long getLong1() {
                return Long.parseLong(map.getOrDefault("l1", "0"));
            }

            public Map<String, String> getMap0() {
                return Tools.stringToMap(map.get("m0"));
            }

            public Map<String, String> getMap1() {
                return Tools.stringToMap(map.get("m1"));
            }

            public double getDouble0() {
                return Double.parseDouble(map.getOrDefault("d0", "0"));
            }

            public double getDouble1() {
                return Double.parseDouble(map.getOrDefault("d1", "0"));
            }

            public float getFloat0() {
                return Float.parseFloat(map.getOrDefault("f0", "0"));
            }

            public float getFloat1() {
                return Float.parseFloat(map.getOrDefault("f1", "0"));
            }

            public Vector3d getVector0() {
                return new Vector3d(Double.parseDouble(Tools.stringToMap(map.get("v0")).get("x")), Double.parseDouble(Tools.stringToMap(map.get("v0")).get("y")), Double.parseDouble(Tools.stringToMap(map.get("v0")).get("z")));
            }

            public Vector3d getVector1() {
                return new Vector3d(Double.parseDouble(Tools.stringToMap(map.get("v1")).get("x")), Double.parseDouble(Tools.stringToMap(map.get("v1")).get("y")), Double.parseDouble(Tools.stringToMap(map.get("v1")).get("z")));
            }
        };
    }

    public static PacketData receivePieces(Connection c) throws IOException {
        try {
            StringBuilder d = new StringBuilder(c.receive());

            if (!d.toString().startsWith("[BEGIN(") && !d.toString().endsWith(")["))
                throw new IllegalStateException("Next data in line is not a splitPacket!");

            int bytesPerPiece = Integer.parseInt(d.toString().split("\\(")[1].split("\\)")[0]);

            d = new StringBuilder();
            while (!d.toString().endsWith("]END]")) {
                d.append(c.receive());
            }

            d = new StringBuilder(d.substring(0, d.length() - "]END]".length()));

            return fromString(d.toString());
        }
        catch (IOException e) {
            throw e;
        }
        catch (Exception e) {
            throw new IllegalStateException("Next data in line is not a splitPacket!");
        }
    }

    public void send(Connection connection) throws IOException {
        connection.send(this.toString());
    }

    public int getID() {
        return id.get();
    }

    public void setID(int i) {
        id.set(i);
    }

    public <T> T getDataValue(PacketDataID id) {
        switch (id) {
            case ID:
                return (T) new Integer(getData().getID());
            case INT0:
                return (T) new Integer(getData().getInt0());
            case INT1:
                return (T) new Integer(getData().getInt1());
            case STRING0:
                return (T) getData().getString0();
            case STRING1:
                return (T) getData().getString1();
            case LONG0:
                return (T) new Long(getData().getLong0());
            case LONG1:
                return (T) new Long(getData().getLong1());
            case DOUBLE0:
                return (T) new Double(getData().getDouble0());
            case DOUBLE1:
                return (T) new Double(getData().getDouble1());
            case FLOAT0:
                return (T) new Float(getData().getFloat0());
            case FLOAT1:
                return (T) new Float(getData().getFloat1());
            case MAP0:
                return (T) getData().getMap0();
            case MAP1:
                return (T) getData().getMap1();
            case VECTOR0:
                return (T) getData().getVector0();
            case VECTOR1:
                return (T) getData().getVector1();
        }
        return null;
    }

    public PacketData getData() {
        return data.get();
    }

    public void setData(PacketData d) {
        data.set(d);
        setID(data.get().getID());
    }

    @Override
    public String toString() {
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(getID()));
        map.put("i0", String.valueOf(((Integer) getDataValue(PacketDataID.INT0)).intValue()));
        map.put("i1", String.valueOf(((Integer) getDataValue(PacketDataID.INT1)).intValue()));
        map.put("s0", getDataValue(PacketDataID.STRING0));
        map.put("s1", getDataValue(PacketDataID.STRING1));
        map.put("l0", String.valueOf(((Long) getDataValue(PacketDataID.LONG0)).longValue()));
        map.put("l1", String.valueOf(((Long) getDataValue(PacketDataID.LONG1)).longValue()));
        map.put("d0", String.valueOf(((Double) getDataValue(PacketDataID.DOUBLE0)).doubleValue()));
        map.put("d1", String.valueOf(((Double) getDataValue(PacketDataID.DOUBLE1)).doubleValue()));
        map.put("f0", String.valueOf(((Float) getDataValue(PacketDataID.FLOAT0)).floatValue()));
        map.put("f1", String.valueOf(((Float) getDataValue(PacketDataID.FLOAT1)).floatValue()));
        map.put("m0", Tools.mapToString(getDataValue(PacketDataID.MAP0)));
        map.put("m1", Tools.mapToString(getDataValue(PacketDataID.MAP1)));
        Map<String, String> v0map = new HashMap<>();
        v0map.put("x", String.valueOf(((Vector3d) getDataValue(PacketDataID.VECTOR0)).getX()));
        v0map.put("y", String.valueOf(((Vector3d) getDataValue(PacketDataID.VECTOR0)).getY()));
        v0map.put("z", String.valueOf(((Vector3d) getDataValue(PacketDataID.VECTOR0)).getZ()));
        map.put("v0", Tools.mapToString(v0map));
        Map<String, String> v1map = new HashMap<>();
        v1map.put("x", String.valueOf(((Vector3d) getDataValue(PacketDataID.VECTOR1)).getX()));
        v1map.put("y", String.valueOf(((Vector3d) getDataValue(PacketDataID.VECTOR1)).getY()));
        v1map.put("z", String.valueOf(((Vector3d) getDataValue(PacketDataID.VECTOR1)).getZ()));
        map.put("v1", Tools.mapToString(v1map));
        return Tools.mapToString(map);
    }

    public void sendAsPieces(int bytesPerPiece, Connection c) throws IOException {
        String d = toString();
        c.send("[BEGIN(" + d.length() + ")[");

        for (int i = 0; i < d.length(); i += bytesPerPiece) {
            StringBuilder piece = new StringBuilder();
            for (int j = 0; j < bytesPerPiece; j++) {
                try {
                    piece.append(d.toCharArray()[i + j]);
                }
                catch (ArrayIndexOutOfBoundsException ignore) {
                }
            }
            c.send(piece.toString());
        }

        c.send("]END]");
    }
}
