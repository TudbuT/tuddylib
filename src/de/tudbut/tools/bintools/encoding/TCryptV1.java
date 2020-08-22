package de.tudbut.tools.bintools.encoding;

import de.tudbut.type.ByteArrayList;

public class TCryptV1 {

    protected Seed seed;

    public TCryptV1(String seed) {
        if (seed == null) {
            seed = Seed.random();
        }

        this.seed = new Seed(seed);
    }

    public Value encrypt(byte[] content) {
        ByteArrayList newContent = new ByteArrayList();

        //System.out.println("Encrypting with seed: " + seed.getSeed());

        for (int c = 0; c < content.length; c += seed.getSeed().length()) {

            for (int i = 0; i < seed.getSeed().length(); i++) {

                int add;
                switch (seed.getSeed().charAt(i)) {
                    case '0':
                        add = 0;
                        break;
                    case '1':
                        add = 1;
                        break;
                    case '2':
                        add = 2;
                        break;
                    case '3':
                        add = 3;
                        break;
                    case '4':
                        add = 4;
                        break;
                    case '5':
                        add = 5;
                        break;
                    case '6':
                        add = 6;
                        break;
                    case '7':
                        add = 7;
                        break;
                    case '8':
                        add = 8;
                        break;
                    case '9':
                        add = 9;
                        break;
                    case 'a':
                        add = -1;
                        break;
                    case 'b':
                        add = -2;
                        break;
                    case 'c':
                        add = -3;
                        break;
                    case 'd':
                        add = -4;
                        break;
                    case 'e':
                        add = -5;
                        break;
                    case 'f':
                        add = -6;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + seed.getSeed().charAt(i));
                }

                byte theByte;

                if ((c + i) >= content.length)
                    break;
                else
                    theByte = content[c + i];

                //System.out.println("Adding char: " + theChar + " -> " + (char) (theChar + (char) add));
                newContent.add((byte) (theByte + add));
            }
        }

        return new Value(newContent.toByteArray());
    }

    public Value decrypt(byte[] content) {
        ByteArrayList newContent = new ByteArrayList();

        //System.out.println("Decrypting with seed: " + seed.getSeed());

        for (int c = 0; c < content.length; c += seed.getSeed().length()) {

            for (int i = 0; i < seed.getSeed().length(); i++) {

                int rem;
                switch (seed.getSeed().charAt(i)) {
                    case '0':
                        rem = 0;
                        break;
                    case '1':
                        rem = 1;
                        break;
                    case '2':
                        rem = 2;
                        break;
                    case '3':
                        rem = 3;
                        break;
                    case '4':
                        rem = 4;
                        break;
                    case '5':
                        rem = 5;
                        break;
                    case '6':
                        rem = 6;
                        break;
                    case '7':
                        rem = 7;
                        break;
                    case '8':
                        rem = 8;
                        break;
                    case '9':
                        rem = 9;
                        break;
                    case 'a':
                        rem = -1;
                        break;
                    case 'b':
                        rem = -2;
                        break;
                    case 'c':
                        rem = -3;
                        break;
                    case 'd':
                        rem = -4;
                        break;
                    case 'e':
                        rem = -5;
                        break;
                    case 'f':
                        rem = -6;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + seed.getSeed().charAt(i));
                }

                byte theByte;

                if ((c + i) >= content.length)
                    break;
                else
                    theByte = content[c + i];

                //System.out.println("Adding char: " + theChar + " -> " + (char) (theChar + (char) add));
                newContent.add((byte) (theByte - rem));
            }
        }

        return new Value(newContent.toByteArray());
    }
}
