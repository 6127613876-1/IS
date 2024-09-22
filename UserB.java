import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Random;

public class UserB {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 2729);
        System.out.println("Connected To Server (UserA)...");
        
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // true for auto-flushing

        BigInteger q = new BigInteger(in.readLine());
        BigInteger alpha = new BigInteger(in.readLine());
        BigInteger Ya = new BigInteger(in.readLine());
        System.out.println("Received q:"+q);
        System.out.println("Received alpha:"+alpha);
        System.out.println("Received UserA's Public Key:"+Ya);
        Random rand = new Random();
        BigInteger Xb;
        do {
            Xb = new BigInteger(q.bitLength(), rand);
        } while (Xb.compareTo(q) >= 0 || Xb.equals(BigInteger.ZERO));

        System.out.println("UserB's Private Key: " + Xb);
        BigInteger Yb = alpha.modPow(Xb, q);
        System.out.println("Computed UserB's Public Key: " + Yb);
        out.println(Yb);
        BigInteger K = Ya.modPow(Xb, q);
        System.out.println("UserB's Shared Secret Key: " + K);

        in.close();
        out.close();
        socket.close();
    }
}
