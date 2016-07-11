import java.util.*;
import java.io.*;

public class Main {
    public static void main() throws FileNotFoundException {
        File f = new File("5000-Most-Common-English-Lemmas.txt"); //population of words
        Scanner input = new Scanner(f);
        int numOfWords = getNumOfWords(f);
        String[] words = new String[numOfWords];

        for (int i = 0; i < numOfWords; i++) { //each line of information
            input.next(); //ignores rank
            words[i] = input.next().toLowerCase(); //stores word in lowercase
            input.nextLine(); //ignores rest of information
        }

        removeNonLetters(words); //hyphens, apostrophes, etc. don't count
        Arrays.sort(words); //the letter a word start with is most likely independent of its length

        String[] sampleWords = getSampleWords(words);
        PrintStream output = new PrintStream(new File("sample.txt"));

        for (String s: sampleWords) {
            output.println(s);
        }

        double p = getProportionMoreThan5Letters(words); //population proportion
        double p_hat = getProportionMoreThan5Letters(sampleWords); //sample proportion
        double se = getStandardError(p_hat, sampleWords.length);

        System.out.printf("p: %.4f\n", p);
        System.out.printf("p-hat: %.4f\n", p_hat);
        System.out.printf("SE(p-hat): %.4f\n", se);
    }

    public static int getNumOfWords(File f) throws FileNotFoundException {
        Scanner input = new Scanner(f);
        int n = 0;

        while (input.hasNextLine()) {
            n++;
            input.nextLine();
        }

        return n;
    }

    public static void removeNonLetters(String[] words) {
        for (int i = 0; i < words.length; i++) { //each word
            for (int j = 0; j < words[i].length(); j++) { //each letter of the word
                char c = words[i].charAt(j);

                if (c < 'a' || c > 'z') {
                    String s = words[i];
                    words[i] = s.substring(0, j) + s.substring(j + 1, s.length()); //remove non-letter
                    j--;
                }
            }
        }
    }

    public static String[] getSampleWords(String[] words) {
        int sampleSize = (int) (words.length * 0.09); //tries to satisfy 10% Rule
        String[] sampleWords = new String[sampleSize];
        int index = (int) (Math.random() * 11); //randomly sets index to one of the first 11 indices

        for (int i = 0; i < sampleSize; i++) {
            sampleWords[i] = words[index];
            index += 11;
        }

        return sampleWords;
    }

    public static double getProportionMoreThan5Letters(String[] list) {
        int moreThan5Letters = 0;

        for (String s: list) { //check length of each word
            if (s.length() > 5) {
                moreThan5Letters++;
            }
        }

        return 1.0 * moreThan5Letters / list.length;
    }

    public static double getStandardError(double p_hat, int n) {
        return Math.sqrt(p_hat * (1 - p_hat) / n);
    }
}