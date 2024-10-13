import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.SecureRandom;

public class UA {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 2729);
        System.out.println("Connected To Attacker (Believing Its User B)...");
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BigInteger q = new BigInteger("162259276829213363391578010288127");
        BigInteger alpha = new BigInteger("5");
        System.out.println("q:" + q);
        System.out.println("Alpha:" + alpha);
        out.println(q);
        out.println(alpha);
        System.out.println("q and alpha are sent to the Attacker (Believing Its User B)...");
        BigInteger Xa;
        SecureRandom rand = new SecureRandom();
        do {
            Xa = new BigInteger(q.bitLength(), rand);
        } while (Xa.compareTo(q) >= 0 || Xa.equals(BigInteger.ZERO));
        System.out.println("Generated UserA's Private Key:" + Xa);
        BigInteger Ya = alpha.modPow(Xa, q);
        System.out.println("Computed UserA's Public Key:" + Ya);
        out.println(Ya);
        System.out.println("Public Key is Sent To Attacker (Believing Its User B)...");
        BigInteger Yb = new BigInteger(in.readLine());
        System.out.println("Received Attacker's Public Key (Believing Its User B):" + Yb);
        BigInteger Ka = Yb.modPow(Xa, q);
        System.out.println("Shared Secret Key Of User A:" + Ka);
        socket.close();
        in.close();
        out.close();
    }
}