import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class CaesarCipher {
    public int k = 0;
    public int kl = 0;
    public int l = 0;
    public int fl = 0;
    private final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private final String Frenchalp = "അആഇഈഉഊഋഎഏഐഒഓഔകഖഗഘങചഛജഝഞടഠഡഢണതഥദധനപഫബഭമയരലവശഷസഹളക്ഷഴറ";
    private final String Alpha = "etaoinshrdlcumwfgypbvkjxqz";

    public String encrypt(String plainText, int shiftKey) {
        plainText = plainText.toLowerCase();
        String cipherText = "";
        for (int i = 0; i < plainText.length(); i++) {
            int charPosition = ALPHABET.indexOf(plainText.charAt(i));
            int keyVal = (shiftKey + charPosition) % 26;
            char replaceVal = this.ALPHABET.charAt(keyVal);
            cipherText += replaceVal;
        }
        return cipherText;
    }

    public String decrypt(String cipherText, int shiftKey) {
        cipherText = cipherText.toLowerCase();
        String plainText = "";
        for (int i = 0; i < cipherText.length(); i++) {
            int charPosition = this.ALPHABET.indexOf(cipherText.charAt(i));
            int keyVal = (charPosition - shiftKey) % 26;
            if (keyVal < 0) {
                keyVal = this.ALPHABET.length() + keyVal;
            }
            char replaceVal = this.ALPHABET.charAt(keyVal);
            plainText += replaceVal;
        }
        return plainText;
    }

    public void bruteforce(String cipherText) {
        String plainText = decrypt(cipherText, k);
        Scanner sc = new Scanner(System.in);
        System.out.println("The plainText is " + plainText + " the key is " + k);
         System.out.println("Enter 1 to continue");
        int choice = sc.nextInt();
        if (choice == 1) {
            k++;
            bruteforce(cipherText);
        }
    }
    public void fnbruteforce(String cipherText) {
        String plainText = decrypt(cipherText, kl);
        Scanner sc = new Scanner(System.in);
        System.out.println("The plainText is " + plainText + " the key is " + kl);
        System.out.println(kl);
        System.out.println("Enter 1 to continue");
        int choice = sc.nextInt();
        if (choice == 1) {
            kl++;
            bruteforce(cipherText);
        }
    }

    public String Fncencrypt(String plainText, int shiftKey) {
        plainText = plainText.toLowerCase();
        String cipherText = "";
        for (int i = 0; i < plainText.length(); i++) {
            int charPosition = Frenchalp.indexOf(plainText.charAt(i));
            int keyVal = (shiftKey + charPosition) % 56;
            char replaceVal = this.Frenchalp.charAt(keyVal);
            cipherText += replaceVal;
        }
        return cipherText;
    }

    public String Fncdecrypt(String cipherText, int shiftKey) {
        cipherText = cipherText.toLowerCase();
        String plainText = "";
        for (int i = 0; i < cipherText.length(); i++) {
            int charPosition = this.Frenchalp.indexOf(cipherText.charAt(i));
            int keyVal = (charPosition - shiftKey) % Frenchalp.length();
            if (keyVal < 0) {
                keyVal = this.Frenchalp.length() + keyVal;
            }
            char replaceVal = this.Frenchalp.charAt(keyVal);
            plainText += replaceVal;
        }
        return plainText;
    }

    public void frequencyAnalysisAttack(String cipherText) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : cipherText.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }

        char mostFrequentChar = ' ';
        int maxFrequency = -1;
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                maxFrequency = entry.getValue();
                mostFrequentChar = entry.getKey();
            }
        }
        int guessedKey = (Alpha.indexOf(mostFrequentChar) - Alpha.indexOf(Alpha.charAt(l)) ) % 26;
        System.out.println("Guessed key based on frequency analysis: " + guessedKey);
        String guessedPlainText = decrypt(cipherText, guessedKey);
        System.out.println("Decrypted text using frequencyAnalysisAttack: " + guessedPlainText);
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter 1 to continue");
        int choice = sc.nextInt();
        if (choice == 1){
            l++;
            frequencyAnalysisAttack(cipherText);
        }
    }
    public void fnfrequencyAnalysisAttack(String cipherText) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : cipherText.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }

        char mostFrequentChar = ' ';
        int maxFrequency = -1;
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                maxFrequency = entry.getValue();
                mostFrequentChar = entry.getKey();
            }
        }
        int guessedKey = (Frenchalp.indexOf(mostFrequentChar) - Frenchalp.indexOf(Frenchalp.charAt(fl)) + 56) % 56;
        System.out.println("Guessed key based on frequency analysis: " + guessedKey);
        String guessedPlainText = Fncdecrypt(cipherText, guessedKey);
        System.out.println("Decrypted text using frequencyAnalysisAttack: " + guessedPlainText);
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter 1 to continue");
        int choice = sc.nextInt();
        if (choice == 1) {
            fl++;
            fnfrequencyAnalysisAttack(cipherText);
        }
    }
}
class CaesarDemo {
    public static void main(String args[]) {
        String plainText = "gokul";
        String fnplainText = "ഗഘങചഛജ";
        int shiftKey = 4;
        CaesarCipher cc = new CaesarCipher();
        String cipherText = cc.encrypt(plainText, shiftKey);
        System.out.println("Your Plain Text :" + plainText);
        System.out.println("Your Malayalam Plain Text :" + fnplainText);
        String cPlainText = cc.decrypt(cipherText, shiftKey);


        cc.bruteforce(cipherText);
        System.out.println("\nFrequency Analysis Attack:");
        cc.frequencyAnalysisAttack(cipherText);

        String FncipherText = cc.Fncencrypt(fnplainText, shiftKey);
        System.out.println("Your Plain Text :" + fnplainText);
        System.out.println("Your Cipher Text :" + FncipherText);
        cc.fnbruteforce(FncipherText);
        String FncPlainText = cc.Fncdecrypt(FncipherText, shiftKey);
        System.out.println("Your Plain Text :" + FncPlainText);
        cc.fnfrequencyAnalysisAttack(FncipherText);
    }
    }