import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;

public class Dsspra
{

    public static boolean isPrime(int num)
    {
        if(num<2)
        {
            return false;
        }
        for(int i=2;i<num/2;i++)
        {
            if(num%i==0)
            {
                return false;
            }
        }
        return true;
    }

    public static BigInteger primediv(BigInteger num)
    {
        for(BigInteger i = new BigInteger("101"); i.compareTo(num.subtract(BigInteger.ONE)) <0;i=i.add(BigInteger.ONE))
        {
            if(num.mod(i).equals(BigInteger.ONE) && isPrime(i.intValue()))
            {
                return i;
            }
        }
        return null;
    }

    public static BigInteger[] keygen()
    {
        Scanner sc = new Scanner(System.in);
        BigInteger p = new BigInteger("1000007");
        BigInteger q = primediv(p.subtract(BigInteger.ONE));
        BigInteger h = new BigInteger(p.bitLength(), new SecureRandom()).mod(p.subtract(BigInteger.ONE)).add(BigInteger.ONE);
        BigInteger g = h.modPow(p.subtract(BigInteger.ONE).divide(q),p);
        BigInteger x = new BigInteger(q.bitLength(), new SecureRandom()).mod(q.subtract(BigInteger.ONE)).add(BigInteger.ONE);
        BigInteger Y = g.modPow(x,p);
        return new BigInteger[]{p,q,h,g,x,Y};
    }

    public static BigInteger hashmsg(String msg) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(msg.getBytes());
        return new BigInteger(hash);
    }

    public static BigInteger[] Sgen(BigInteger p,BigInteger q,BigInteger g,BigInteger x,BigInteger m)
    {
        BigInteger k = new BigInteger(q.bitLength(), new SecureRandom()).mod(q.subtract(BigInteger.ONE)).add(BigInteger.ONE);
        BigInteger r = g.modPow(k,p).mod(q);
        BigInteger s = k.modInverse(q).multiply(m.add(x.multiply(r))).mod(q);
        return new BigInteger[] {r,s};
    }

    public static void Signver(BigInteger p,BigInteger q,BigInteger g,BigInteger r,BigInteger s,BigInteger y,BigInteger mes)
    {
        BigInteger w = s.modInverse(q);
        BigInteger u1 = mes.multiply(w).mod(q);
        BigInteger u2 = r.multiply(w).mod(q);
        BigInteger v = (g.modPow(u1,p).multiply(y.modPow(u2,p))).mod(p).mod(q);
        System.out.println("V="+v);
        if(v.equals(r))
        {
            System.out.println("Verified");
        }
        else
        {
            System.out.println("Not verified");
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        BigInteger p,q,h,g,x,y;
        Scanner sc = new Scanner(System.in);
        BigInteger[] key = keygen();
        p=key[0];
        q=key[1];
        h=key[2];
        g=key[3];
        x=key[4];
        y=key[5];
        System.out.println("Enter message");
        String message = "Gokul";
        BigInteger hm = hashmsg(message);
        BigInteger[] mess = Sgen(p,q,g,x,hm);
        System.out.println("r = "+mess[0]+" s = "+mess[1]);
        System.out.println("Enter mes for verify");
        String ver = sc.nextLine();
        BigInteger mesver = hashmsg(ver);
        System.out.print("Enter r: ");
        BigInteger rMsg = sc.nextBigInteger();
        System.out.print("Enter s: ");
        BigInteger sMsg = sc.nextBigInteger();
        Signver(p,q,g,rMsg,sMsg,y,mesver);
    }
}
