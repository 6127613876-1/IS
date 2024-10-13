import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.SecureRandom;

public class Dc
{
    public static void main(String[] args) throws IOException {
        Socket s = new Socket("localhost",1234);
        PrintWriter out = new PrintWriter(s.getOutputStream(),true);
        BigInteger q=new BigInteger("444");
        BigInteger alpha = new BigInteger("123456789");
        out.println(q);
        out.println(alpha);
        BigInteger xa;
        SecureRandom rand = new SecureRandom();
        do
        {
            xa = new BigInteger(q.bitLength(),rand);
        }while(xa.compareTo(q)>=0 || xa.equals(BigInteger.ZERO));

        BigInteger ya = alpha.modPow(xa, q);
        System.out.println(ya);
        s.close();
        out.close();
    }
}
