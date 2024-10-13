import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;

public class Attacker {
    public static void main(String[] args) throws IOException {
        ServerSocket ssA=new ServerSocket(2729);
        ServerSocket ssB=new ServerSocket(2728);
        System.out.println("Attacker Is Waiting To Interupt A Exchange Between UserA And UserB...");
        Socket socketA=ssA.accept();
        Socket socketB=ssB.accept();
        System.out.println("Attacker Successfully interrupted UserA And UserB.");
        BufferedReader inA=new BufferedReader(new InputStreamReader(socketA.getInputStream()));
        PrintWriter outA=new PrintWriter(socketA.getOutputStream(),true);
        BufferedReader inB=new BufferedReader(new InputStreamReader(socketB.getInputStream()));
        PrintWriter outB=new PrintWriter(socketB.getOutputStream(),true);
        BigInteger q=new BigInteger(inA.readLine());
        BigInteger alpha=new BigInteger(inA.readLine());
        System.out.println("Received q:"+q);
        System.out.println("Received Alpha:"+alpha);
        outB.println(q);
        outB.println(alpha);
        System.out.println("q and alpha are sent to UserB...");
        BigInteger Xc;
        SecureRandom rand=new SecureRandom();
        do{
            Xc=new BigInteger(q.bitLength(),rand);
        }while(Xc.compareTo(q)>=0||Xc.equals(BigInteger.ZERO));
        System.out.println("Generated Attacker's Private Key:"+Xc);
        BigInteger Yc=alpha.modPow(Xc,q);
        System.out.println("Computed Attacker's Public Key:"+Yc);
        outA.println(Yc);
        outB.println(Yc);
        System.out.println("Attacker's Public Key is Sent To Both UserA And UserB...");
        BigInteger Ya=new BigInteger(inA.readLine());
        System.out.println("Received UserA's Public Key By interupting:"+Ya);
        BigInteger Yb=new BigInteger(inB.readLine());
        System.out.println("Received UserB's Public Key By interupting:"+Yb);
        BigInteger Kb=Yb.modPow(Xc,q);
        BigInteger Ka=Ya.modPow(Xc,q);
        System.out.println("Shared Secret Key Of UserA:"+Ka);
        System.out.println("Shared Secret Key Of UserB:"+Kb);
        ssA.close();
        ssB.close();
    }
}