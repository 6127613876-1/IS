import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;

public class Ds
{
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(1234);
        Socket s = ss.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        BigInteger q = new BigInteger(in.readLine());
        BigInteger alpha = new BigInteger(in.readLine());
        BigInteger xb;
        SecureRandom rand = new SecureRandom();
        do
        {
            xb = new BigInteger(q.bitLength(),rand);
        }while(xb.compareTo(q)>=0 || xb.equals(BigInteger.ZERO));

        BigInteger yb = alpha.modPow(xb,q);
        System.out.println(yb);
        in.close();
        s.close();
    }
}
