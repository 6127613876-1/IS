public class prac
{
    public String alp = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public int findgcd(int a,int b)
    {
        if(a<b)
        {
            int temp = a;
            a=b;
            b=temp;
        }
        while(b!=0)
        {
            int temp = b;
            b=a%b;
            a=temp;
        }
        System.out.println(a);
        return a;
    }

    public int extecu(int a,int m)
    {
        int t1=0,t2=1;
        int b = m;
        if(a<b)
        {
            int temp = a;
            a=b;
            b=temp;
        }
        while(b!=0)
        {
            int q=a/b;
            int temp =b;
            b=a%b;
            a=temp;
            int t = t1-q*t2;
            t1=t2;
            t2=t;
        }
        System.out.println(t1);
        if (t1<0)
        {
            return t1+m;
        }
        else
        {
            return t1;
        }
    }

    public int[][] cvstrmat(String word,int sy)
    {
        while(word.length()%sy!=0)
        {
            word+='X';
        }
        int r = sy;
        int c = word.length()/sy;
        int[][] res = new int[r][c];
        int k=0;
        for (int i=0;i<r;i++)
        {
            for(int j=0;j<c;j++)
            {
                res[i][j] = alp.indexOf(word.charAt(k++));
            }
        }
        for (int i=0;i<r;i++) {
            for (int j = 0; j < c; j++) {
                System.out.print(res[i][j]+" ");
            }
            System.out.println(" ");
        }
        return res;
    }

    public int[][] rev(int[][] mat, int i, int j)
    {
        int size = mat.length-1;
        int res[][] =  new int[size][size];

        for(int row=0,newrow=0;row<mat.length;row++)
        {
            if(row==i)
            {
                continue;
            }
            for (int col=0,newcol=0;col<mat[0].length;col++)
            {
                if (col==j)
                {
                    continue;
                }
                res[newrow][newcol++] = mat[row][col];
            }
            newrow++;
        }
        return res;
    }
    public boolean checkSquareMatrix(int[][] mat) {
        for (int[] row : mat) {
            if (row.length != mat.length) return false;
        }
        return true;
    }
    public int finddet(int[][] mat)
    {
        if (!checkSquareMatrix(mat)) return -1;
        if (mat.length == 2) {
            return (mat[0][0] * mat[1][1] - mat[0][1] * mat[1][0]) % 26;
        }
        int det=0;
        for(int c = 0 ;c<mat.length;c++) {
            det = (det+(c % 2 == 0 ? 1 : -1) * mat[0][c] * finddet(rev(mat, 0, c))) % 26;
        }
        return det<0?det+26:det;
    }

    public static void main(String[] args)
    {
        prac p =new prac();
        p.findgcd(59,17);
        p.extecu(53,17);
        p.cvstrmat("BBBBB",3);
        int[][] mat = {{1,3,-2},{-1,2,1},{1,0,-2}};
        p.rev(mat,0,0);
        int a = p.finddet(mat);
        System.out.println("The det is " + a);
    }
}
