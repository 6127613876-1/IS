import java.math.BigInteger;
import java.util.Scanner;

public class RSA {

    public int eulerTotient(int n) {
        int result = n;
        for (int p = 2; p * p <= n; p++) {
            if (n % p == 0) {
                while (n % p == 0) {
                    n /= p;
                }
                result -= result / p;
            }
        }
        if (n > 1) {
            result -= result / n;
        }
        return result;
    }

    public boolean areCoprime(int a, int b) {
        return BigInteger.valueOf(a).gcd(BigInteger.valueOf(b)).equals(BigInteger.ONE);
    }

    public BigInteger modInverse(BigInteger e, BigInteger phi) {
        return e.modInverse(phi);
    }

    public BigInteger encrypt(BigInteger M, BigInteger e, BigInteger n) {
        return M.modPow(e, n);
    }

    public BigInteger decrypt(BigInteger C, BigInteger d, BigInteger n) {
        return C.modPow(d, n);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        RSA rsa = new RSA();

        System.out.println("Enter prime number p:");
        int p = sc.nextInt();
        System.out.println("Enter prime number q:");
        int q = sc.nextInt();

        int n = p * q;
        int phi = rsa.eulerTotient(p) * rsa.eulerTotient(q);
        BigInteger phiBig = BigInteger.valueOf(phi);

        int e;
        do {
            System.out.println("Enter public exponent e (should be coprime with φ(n)):");
            e = sc.nextInt();
        } while (!rsa.areCoprime(e, phi));

        BigInteger eBig = BigInteger.valueOf(e);
        BigInteger d = rsa.modInverse(eBig, phiBig);

        System.out.println("Public Key: (e = " + e + ", n = " + n + ")");
        System.out.println("Private Key: (d = " + d + ", n = " + n + ")");

        System.out.println("Enter the message M (as an integer):");
        BigInteger M = sc.nextBigInteger();

        BigInteger C = rsa.encrypt(M, eBig, BigInteger.valueOf(n));
        System.out.println("Encrypted message C = " + C);

        BigInteger decryptedMessage = rsa.decrypt(C, d, BigInteger.valueOf(n));
        System.out.println("Decrypted message M = " + decryptedMessage);
    }
}
