import java.math.BigInteger;
import java.util.Scanner;

public class prac
{

    public int eto(int n)
    {
        int result = n;
        for(int p = 2;p*p<=n;p++)
        {
            if(n%p==0)
            {
                while (n%p==0)
                {
                    n/=p;
                }
                result -=result/p;
            }
        }
        if (n>1)
        {
            result -=result/n;
        }
        return result;
    }

    public boolean coprime(int a, int b)
    {
        return BigInteger.valueOf(a).gcd(BigInteger.valueOf(b)).equals(BigInteger.ONE);
    }

    public BigInteger modInvers(BigInteger e,BigInteger phi)
    {
        return e.modInverse(phi);
    }

    public BigInteger encrypt(BigInteger M ,BigInteger e, BigInteger n)
    {
        return M.modPow(e,n);
    }

    public BigInteger decrypt(BigInteger C ,BigInteger d, BigInteger n)
    {
        return C.modPow(d,n);
    }


    public static void main(String[] args) {
        prac pr =new prac();
        Scanner sc = new Scanner(System.in);
        int p = sc.nextInt();
        int q = sc.nextInt();
        int n = p*q;
        int phi = pr.eto(p) * pr.eto(q);
        BigInteger pbig = BigInteger.valueOf(phi);
        int e;
        do{
            e = sc.nextInt();
        }while (pr.coprime(e,phi));

        BigInteger eBig = BigInteger.valueOf(e);
        BigInteger d = pr.modInvers(eBig,pbig);

        BigInteger M = sc.nextBigInteger();

        BigInteger C = pr.encrypt(M,eBig,BigInteger.valueOf(n));
        BigInteger dec = pr.decrypt(C,d,BigInteger.valueOf(n));


    }
}
