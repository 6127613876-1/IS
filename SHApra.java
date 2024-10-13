import java.util.Scanner;

public class SHApra
{

    public static int left(int val,int shift)
    {
        return (val <<shift) | (val >>> (32-shift));
    }

    public static byte[] pdm(byte[] message)
    {
        int olen = message.length;
        long obit = (long) olen*8;
        int padlen = (56-(olen+1)%64+64)%64;
        byte[] pad = new byte[padlen+olen+9];
        System.arraycopy(message,0,pad,0,olen);
        pad[olen]=(byte) 0x80;

        for (int i=0;i<8;i++)
        {
            pad[pad.length-1-i]= (byte) (obit>>>(i*8));
        }
        return pad;
    }

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        byte[] pm = pdm(input.getBytes());
        int round = sc.nextInt();
        int step = sc.nextInt();

        int[] w = new int[80];

        for(int i=0;i<16;i++)
        {
            w[i] = ((pm[i*4]&0xFF)<<24 | (pm[i*4+1]&0xFF)<<16 | (pm[i*4+2]&0xFF)<<8 |(pm[i*4+3]&0xFF));
        }

        for(int i=16;i<80;i++)
        {
            w[i] = left(w[i-3] ^ w[i-8] ^ w[i-14] ^ w[i-16],1);
        }

        int a = 0x67452301,b=0xEFCDAB89,c=0x98BADCFE,d=0x10325476,e=0xC3D2E1F0;

        int f,k;
        if(round==1)
        {
            f=(b&c) | (~b&d);
            k=0x5A827999;
        }
        else if (round==2)
        {
            f = b^c^d;
            k=0x6ED9EBA1;
        }
        else if (round == 3) {
            f = (b & c) | (b & d) | (c & d);  // 40 to 59 rounds
            k = 0x8F1BBCDC;
        } else {
            f = b ^ c ^ d;           // 60 to 79 rounds
            k = 0xCA62C1D6;
        }

        int temp = left(a,5)+e+f+k+w[step];
        e=d;
        d=c;
        c=left(b,30);
        b=a;
        a=temp;
        System.out.printf("After round %d and step %d:\n a = %08x\n b = %08x\n c = %08x\n d = %08x\n e = %08x\n", round, step, a, b, c, d, e);

    }

}
