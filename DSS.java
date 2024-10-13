import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;
public class DSS {
    private static boolean isPrime(int num) {
        if (num < 2)
            return false;
        for (int i = 2; i < num / 2; i++) {
            if (num % 2 == 0)
                return false;
        }
        return true;
    }
    private static BigInteger primeDivisor(BigInteger num) {
        for (BigInteger i = new BigInteger("101"); i.compareTo(num.subtract(BigInteger.ONE)) < 0; i =
                i.add(BigInteger.ONE)) {
            if (num.mod(i).equals(BigInteger.ZERO) && isPrime(i.intValue())) {
                return i;
            }
        }
        return null;
    }
    private static BigInteger[] keyGen() {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter The Prime (p):");
        BigInteger p = s.nextBigInteger();
        if (!isPrime(p.intValue())) {
            System.out.println("P is not Prime.!");
            return null;
        }
        BigInteger q = primeDivisor(p.subtract(BigInteger.ONE));
        BigInteger h = new BigInteger(p.bitLength(), new SecureRandom()).mod(p.subtract(BigInteger.ONE)).add(BigInteger.ONE);
        BigInteger g = h.modPow(p.subtract(BigInteger.ONE).divide(q), p);
        BigInteger x = new BigInteger(q.bitLength(), new SecureRandom()).mod(q.subtract(BigInteger.ONE)).add(BigInteger.ONE);
        BigInteger y = g.modPow(x, p);
        System.out.print("Generated Keys: p=" + p + ",q=" + q + ",h=" + h + ",g=" + g + ",x=" + x + ",y=" + y);
        return new BigInteger[] { p, q, h, g, x, y };
    }

    private static BigInteger[] SignGen(BigInteger p, BigInteger q, BigInteger g, BigInteger x, BigInteger HashMsg) {
        BigInteger k = new BigInteger(q.bitLength(), new SecureRandom()).mod(q.subtract(BigInteger.ONE)).add(BigInteger.ONE);
        BigInteger r = g.modPow(k, p).mod(q);
        BigInteger s = k.modInverse(q).multiply(HashMsg.add(x.multiply(r))).mod(q);
        return new BigInteger[] { r, s };
    }
    private static void SignVerification(BigInteger p, BigInteger q, BigInteger g, BigInteger r, BigInteger s,
                                         BigInteger y, BigInteger hashMsg) {
        BigInteger w = s.modInverse(q);
        BigInteger u1 = hashMsg.multiply(w).mod(q);
        BigInteger u2 = r.multiply(w).mod(q);
        BigInteger v = (g.modPow(u1, p).multiply(y.modPow(u2, p))).mod(p).mod(q);
        if (v.equals(r)) {
            System.out.println("Signature Verified Successfully.");
        } else {
            System.out.println("Signature Verification Failed.");
        }
    }
    private static BigInteger hashMsg(String msg) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(msg.getBytes());
            return new BigInteger(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    private static BigInteger HashReadFile(String filePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            return hashMsg(content);
        } catch (IOException e) {
            System.out.println("File Not Found.!");
            return null;
        }
    }
    public static void main(String[] args) {
        BigInteger p = null, q = null, h = null, g = null, x = null, y = null;
        Scanner s = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\nMENU.");
            System.out.println("1->Key Generation.");
            System.out.println("2->Signature Generation (File).");
            System.out.println("3->Signature Verification (File).");
            System.out.println("4->Signature Generation (Message).");
            System.out.println("5->Signature Verification (Message).");
            System.out.println("6->Exit.");

            System.out.print("Enter Your Choice:");
            choice = s.nextInt();
            switch (choice) {
                case 1:
                    BigInteger[] keys = keyGen();
                    if (keys != null) {
                        p = keys[0];
                        q = keys[1];
                        h = keys[2];
                        g = keys[3];
                        x = keys[4];
                        y = keys[5];
                    }
                    break;
                case 2:
                    if (p == null || q == null || h == null || g == null || x == null || y == null) {
                        System.out.println("Keys Are Not Generated Yet.Generate Keys First.!");
                        continue;
                    }
                    System.out.print("Enter The File Path For Signature Generation:");
                    s.nextLine();
                    String path = s.nextLine();
                    BigInteger filehash = HashReadFile(path);
                    if (filehash != null) {
                        BigInteger[] signs = SignGen(p, q, g, x, filehash);
                        System.out.println("Generated Signature: r=" + signs[0] + ",s=" + signs[1]);
                    }
                    break;
                case 3:
                    if (p == null || q == null || h == null || g == null || x == null || y == null) {
                        System.out.println("Keys Are Not Generated Yet.Generate Keys First.!");
                        continue;
                    }
                    System.out.print("Enter The File Path For Signature Verification:");
                    s.nextLine();
                    String vpath = s.nextLine();
                    BigInteger vfilehash = HashReadFile(vpath);
                    if (vfilehash != null) {
                        System.out.print("Enter r:");
                        BigInteger r = s.nextBigInteger();
                        System.out.print("Enter s:");
                        BigInteger S = s.nextBigInteger();
                        SignVerification(p, q, g, r, S, y, vfilehash);
                    }
                    break;
                case 4:
                    if (p == null || q == null || g == null || y == null || x == null) {
                        System.out.println("Keys not generated yet. Please generate keys first.");
                        continue;

                    }
                    System.out.print("Enter the message to sign: ");
                    s.nextLine(); // Consume the newline
                    String message = s.nextLine();
                    BigInteger messageHash = hashMsg(message);
                    BigInteger[] messageSignature = SignGen(p,q,g,x,messageHash);
                    System.out.println("Generated Signature for Message: r=" + messageSignature[0] + ", s=" +
                            messageSignature[1]);
                    break;
                case 5:
                    if (p == null || q == null || g == null || y == null) {
                        System.out.println("Keys not generated yet. Please generate keys first.");
                        continue;
                    }
                    System.out.print("Enter the message for verification: ");
                    s.nextLine(); // Consume the newline
                    String verifyMessage = s.nextLine();
                    BigInteger verifyMessageHash = hashMsg(verifyMessage);
                    System.out.print("Enter r: ");
                    BigInteger rMsg = s.nextBigInteger();
                    System.out.print("Enter s: ");
                    BigInteger sMsg = s.nextBigInteger();
                    SignVerification(p, q, g, rMsg, sMsg, y, verifyMessageHash);
                    break;
                case 6:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        } while (choice != 6);
        s.close();
    }
}