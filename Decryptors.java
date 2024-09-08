public class Decryptors {
    private final int m;
    private final String alphaList;
    private final HillCipher helpers;

    public Decryptors(int m, String alphaList) {
        this.m = m;
        this.alphaList = alphaList;
        this.helpers = new HillCipher(m, alphaList);
    }

    public int[][] knowPtCt(String pt, String ct, int size) {
        int[][] ptMatrix = helpers.convertStrMat(pt, size);
        System.out.println("Plain text matrix is: ");
        printMatrix(ptMatrix);
        int[][] ctMatrix = helpers.convertStrMat(ct, size);
        System.out.println("Cipher text matrix is: ");
        printMatrix(ctMatrix);
        int detCt = helpers.findDeterminant(ctMatrix);
        System.out.println("Determinant of cipher text matrix is: " + detCt);
        int detInv = helpers.extendedEuclids(detCt, m);
        System.out.println("The inverse of " + detCt + " is " + detInv);
        int[][] adjCt = helpers.findAdjoint(ctMatrix);
        System.out.println("The adjoint of cipher text matrix is: ");
        printMatrix(adjCt);
        int[][] ctInv = helpers.scalarMultiply(detInv, adjCt);
        System.out.println("The inverse of cipher text matrix is: ");
        printMatrix(ctInv);
        int[][] keyInv = helpers.findMatMul(ptMatrix, ctInv);
        System.out.println("The inverse of key matrix is: ");
        printMatrix(keyInv);
        return keyInv;
    }

    public String findPlainText(int[][] keyInv, String ct) {
        int[][] ctMatrix = helpers.convertStrMat(ct, keyInv.length);
        int[][] plainTextMatrix = helpers.findMatMul(keyInv, ctMatrix);
        return helpers.convertMatStr(plainTextMatrix);
    }

    private void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Decryptors decryptor = new Decryptors(26, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        int[][] keyInv = decryptor.knowPtCt("JAMESBOND", "TEPHBNXSW", 3);
        String cipherText = "FBRTLWUGA";
        String originalText = decryptor.findPlainText(keyInv, cipherText);
        System.out.println("Decrypted text: " + originalText);
    }
}
