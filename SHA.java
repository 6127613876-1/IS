import java.util.Scanner;

public class SHA {
    private static final int H0 = 0x67452301;
    private static final int H1 = 0xEFCDAB89;
    private static final int H2 = 0x98BADCFE;
    private static final int H3 = 0x10325476;
    private static final int H4 = 0xC3D2E1F0;

    private static int leftRotate(int value, int shift)
    {
        return (value << shift) | (value >>> (32 - shift));
    }

    private static byte[] padMessage(byte[] message) {
        int originalLength = message.length;
        long originalLengthBits = (long) originalLength * 8;
        int paddingLength = (56 - (originalLength + 1) % 64 + 64) % 64;
        byte[] paddedMessage = new byte[originalLength + paddingLength + 9];

        System.arraycopy(message, 0, paddedMessage, 0, originalLength);
        paddedMessage[originalLength] = (byte) 0x80;

        for (int i = 0; i < 8; i++) {
            paddedMessage[paddedMessage.length - 1 - i] = (byte) (originalLengthBits >>> (i * 8));
        }
        return paddedMessage;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the text to hash: ");
        String inputText = scanner.nextLine();

        byte[] paddedMessage = padMessage(inputText.getBytes());

        System.out.print("Enter the round number (1-4): ");
        int round = scanner.nextInt();
        System.out.print("Enter the step number (1-79): ");
        int step = scanner.nextInt();

        if (round < 1 || round > 4 || step < 0 || step > 79) {
            System.out.println("Invalid round or step number. Please enter valid values.");
            return ;
        }

        int[] w = new int[80];

        for (int i = 0; i < 16; i++) {
            w[i] = ((paddedMessage[i * 4] & 0xFF) << 24) |
                    ((paddedMessage[i * 4 + 1] & 0xFF) << 16) |
                    ((paddedMessage[i * 4 + 2] & 0xFF) << 8) |
                    (paddedMessage[i * 4 + 3] & 0xFF);
        }

        // Extend the sixteen 32-bit words into eighty 32-bit words
        for (int i = 16; i < 80; i++) {
            w[i] = leftRotate(w[i - 3] ^ w[i - 8] ^ w[i - 14] ^ w[i - 16], 1);
        }

        // Initial hash values
        int a = H0, b = H1, c = H2, d = H3, e = H4;

        // Determine the value of f and k based on the round number
        int f, k;
        if (round == 1) {
            f = (b & c) | (~b & d);  // First 20 rounds
            k = 0x5A827999;
        } else if (round == 2) {
            f = b ^ c ^ d;           // 20 to 39 rounds
            k = 0x6ED9EBA1;
        } else if (round == 3) {
            f = (b & c) | (b & d) | (c & d);  // 40 to 59 rounds
            k = 0x8F1BBCDC;
        } else {
            f = b ^ c ^ d;           // 60 to 79 rounds
            k = 0xCA62C1D6;
        }

        int temp = leftRotate(a, 5) + f + e + k + w[step];
        e = d;
        d = c;
        c = leftRotate(b, 30);
        b = a;
        a = temp;

        // Output the updated hash values after the specific step
        System.out.printf("After round %d and step %d:\n a = %08x\n b = %08x\n c = %08x\n d = %08x\n e = %08x\n", round, step, a, b, c, d, e);

        scanner.close();
    }
}
