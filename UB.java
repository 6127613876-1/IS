import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.SecureRandom;

public class UB {
    public static void main(String[] args) throws IOException {
        Socket socket=new Socket("localhost",2728);
        System.out.println("Connected To Attacker (Believing Its User A)...");
        BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out=new PrintWriter(socket.getOutputStream(),true);
        BigInteger q=new BigInteger(in.readLine());
        BigInteger alpha=new BigInteger(in.readLine());
        System.out.println("Received q:"+q);
        System.out.println("Received Alpha:"+alpha);
        BigInteger Xb;
        SecureRandom rand=new SecureRandom();
        do{
            Xb=new BigInteger(q.bitLength(),rand);
        }while(Xb.compareTo(q)>=0||Xb.equals(BigInteger.ZERO));
        System.out.println("Generated UserB's Private Key:"+Xb);
        BigInteger Yb=alpha.modPow(Xb,q);
        System.out.println("Computed UserB's Public Key:"+Yb);
        out.println(Yb);
        System.out.println("Public Key is Sent To Attcker (Believing Its User A)...");
        BigInteger Ya=new BigInteger(in.readLine());
        System.out.println("Received Attacker's Public Key (Believing Its User A):"+Ya);
        BigInteger Kb=Ya.modPow(Xb,q);
        System.out.println("Shared Secret Key Of User A:"+Kb);
        socket.close();
        in.close();
        out.close();
    }
    
}