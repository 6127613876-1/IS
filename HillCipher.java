import java.util.Scanner;

public class HillCipher {
    private final String alphaList;
    private final int m;

    public HillCipher(int m, String alphaList) {
        this.alphaList = alphaList;
        this.m = m;
    }

    public int findGCD(int a, int b) {
        if (a < b) {
            int temp = a;
            a = b;
            b = temp;
        }
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        System.out.println(a);
        return a;

    }

    public int extendedEuclids(int a, int m) {
        int t1 = 0, t2 = 1;
        int b = m;
        if (a < b) {
            int temp = a;
            a = b;
            b = temp;
        }
        while (b != 0) {
            int q = a / b;
            int temp = b;
            b = a % b;
            a = temp;
            int t = t1 - q * t2;
            t1 = t2;
            t2 = t;
        }
        System.out.println(t1);
        if (t1 < 0) {
            return t1 + m;
        } else {
            return t1;
        }
    }

    public int[][] convertStrMat(String word, int syllable) {
        while (word.length() % syllable != 0) {
            word += 'X';
        }
        int rows = syllable;
        int cols = word.length() / syllable;
        int[][] result = new int[rows][cols];
        int k = 0;
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                result[j][i] = alphaList.indexOf(word.charAt(k++));
            }
        }
        return result;
    }

    public String convertMatStr(int[][] mat) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < mat[0].length; i++) {
            for (int j = 0; j < mat.length; j++) {
                text.append(alphaList.charAt(mat[j][i] % m));
            }
        }
        return text.toString();
    }

    public int[][] findMatMul(int[][] matA, int[][] matB) {
        int rowsA = matA.length;
        int colsA = matA[0].length;
        int colsB = matB[0].length;
        int[][] result = new int[rowsA][colsB];
        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    result[i][j] += matA[i][k] * matB[k][j];
                }
                result[i][j] %= m;
            }
        }
        return result;
    }

    public int[][] removeCopyMatrix(int[][] mat, int i, int j) {
        int size = mat.length - 1;
        int[][] result = new int[size][size];
        for (int row = 0, newRow = 0; row < mat.length; row++) {
            if (row == i) continue;
            for (int col = 0, newCol = 0; col < mat[0].length; col++) {
                if (col == j) continue;
                result[newRow][newCol++] = mat[row][col];
            }
            newRow++;
        }
        return result;
    }

    public boolean checkSquareMatrix(int[][] mat) {
        for (int[] row : mat) {
            if (row.length != mat.length) return false;
        }
        return true;
    }

    public int findDeterminant(int[][] mat) {
        if (!checkSquareMatrix(mat)) return -1;
        if (mat.length == 2) {
            return (mat[0][0] * mat[1][1] - mat[0][1] * mat[1][0]) % m;
        }
        int det = 0;
        for (int c = 0; c < mat.length; c++) {
            det += ((c % 2 == 0 ? 1 : -1) * mat[0][c] * findDeterminant(removeCopyMatrix(mat, 0, c))) % m;
        }
        return det < 0 ? det + m : det;
    }

    public int[][] findAdjoint(int[][] mat) {
        int size = mat.length;
        int[][] adjoint = new int[size][size];
        if (size == 2) {
            adjoint[0][0] = mat[1][1];
            adjoint[1][1] = mat[0][0];
            adjoint[0][1] = -mat[0][1];
            adjoint[1][0] = -mat[1][0];
            return adjoint;
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int[][] minor = removeCopyMatrix(mat, i, j);
                adjoint[j][i] = ((i + j) % 2 == 0 ? 1 : -1) * findDeterminant(minor) % m;
                if (adjoint[j][i] < 0) adjoint[j][i] += m;
            }
        }
        return adjoint;
    }

    public int[][] scalarMultiply(int scalar, int[][] mat) {
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[i].length; j++) {
                mat[i][j] = (scalar * mat[i][j]) % m;
                if (mat[i][j] < 0) mat[i][j] += m;
            }
        }
        return mat;
    }

    public String encrypt(String plaintext, int[][] keyMatrix) {
        int[][] plaintextMatrix = convertStrMat(plaintext, keyMatrix.length);
        int[][] cipherMatrix = findMatMul(keyMatrix, plaintextMatrix);
        return convertMatStr(cipherMatrix);
    }

    public String decrypt(String ciphertext, int[][] keyMatrix) {
        int det = findDeterminant(keyMatrix);
        if (det == 0 || findGCD(det, m) != 1) {
            throw new IllegalArgumentException("Matrix is not invertible");
        }
        int detInv = extendedEuclids(det, m);  // Use m instead of 1 as the third argument
        int[][] adjointMatrix = findAdjoint(keyMatrix);
        int[][] inverseMatrix = scalarMultiply(detInv, adjointMatrix);
        int[][] ciphertextMatrix = convertStrMat(ciphertext, inverseMatrix.length);
        int[][] plaintextMatrix = findMatMul(inverseMatrix, ciphertextMatrix);
        return convertMatStr(plaintextMatrix).replaceAll("X*$", "");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String alphaList = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        HillCipher hillCipher = new HillCipher(alphaList.length(), alphaList);

        while (true) {
            System.out.println("\nHill Cipher Menu");
            System.out.println("1. Encrypt");
            System.out.println("2. Decrypt");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                System.out.print("Enter the plaintext: ");
                String plaintext = scanner.nextLine().toUpperCase();
                System.out.print("Enter the key matrix size (e.g., 2 for 2x2, 3 for 3x3): ");
                int keyMatrixSize = scanner.nextInt();
                int[][] keyMatrix = new int[keyMatrixSize][keyMatrixSize];
                System.out.println("Enter the key matrix row-wise:");
                for (int i = 0; i < keyMatrixSize; i++) {
                    for (int j = 0; j < keyMatrixSize; j++) {
                        keyMatrix[i][j] = scanner.nextInt();
                    }
                }
                scanner.nextLine();  // Consume newline
                String ciphertext = hillCipher.encrypt(plaintext, keyMatrix);
                System.out.println("Encrypted text: " + ciphertext);

            } else if (choice.equals("2")) {
                System.out.print("Enter the ciphertext: ");
                String ciphertext = scanner.nextLine().toUpperCase();
                System.out.print("Enter the key matrix size (e.g., 2 for 2x2, 3 for 3x3): ");
                int keyMatrixSize = scanner.nextInt();
                int[][] keyMatrix = new int[keyMatrixSize][keyMatrixSize];
                System.out.println("Enter the key matrix row-wise:");
                for (int i = 0; i < keyMatrixSize; i++) {
                    for (int j = 0; j < keyMatrixSize; j++) {
                        keyMatrix[i][j] = scanner.nextInt();
                    }
                }
                scanner.nextLine();
                try {
                    String plaintext = hillCipher.decrypt(ciphertext, keyMatrix);
                    System.out.println("Decrypted text: " + plaintext);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }

            } else if (choice.equals("3")) {

                break;

            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
}

