    import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStreamReader;
    import java.io.PrintWriter;
    import java.math.BigInteger;
    import java.net.ServerSocket;
    import java.net.Socket;
    import java.util.Random;

    public class UserA {
        public static void main(String[] args) throws IOException {
            ServerSocket ss = new ServerSocket(2729);
            System.out.println("Server (User A) is Running... Waiting For UserB to connect.");
            Socket s = ss.accept();
            System.out.println("Connected to UserB.");

            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true); // true for auto-flushing

            BigInteger q = new BigInteger("162259276829213363391578010288127");
            BigInteger alpha = new BigInteger("5");

            out.println(q);
            out.println(alpha);
            System.out.println("q:"+q);
            System.out.println("Alpha:"+alpha);

            BigInteger Xa;
            Random rand = new Random();
            do {
                Xa = new BigInteger(q.bitLength(), rand);
            } while (Xa.compareTo(q) >= 0 || Xa.equals(BigInteger.ZERO));

            System.out.println("UserA's private Key: " + Xa);
            BigInteger Ya = alpha.modPow(Xa, q);
            System.out.println("Computed UserA's Public Key: " + Ya);
            out.println(Ya);


            BigInteger Yb = new BigInteger(in.readLine());
            BigInteger K = Yb.modPow(Xa, q);
            System.out.println("UserA's Shared Secret Key: " + K);

            in.close();
            out.close();
            s.close();
            ss.close();
        }
    }
