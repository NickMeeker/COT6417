package scripts;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;
import java.util.Scanner;

public class BuildGiantCorpus {
    static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static void main(String[] args) throws Exception {
        String corpusFilenameBase = "corpus_";
        String patternFilenameBase = "pattern_";
        int i = 0;
        StringBuilder corpus = new StringBuilder();
        StringBuilder pattern = new StringBuilder();
        Random random = new Random();
//        while(i <= 2000000) {
//            if(i > 0 && i % 100000 == 0) {
//                String filename = corpusFilenameBase + i + ".txt";
//                BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
//                writer.write(corpus.toString());
//
//                writer.close();
//            }
//
//            corpus.append(alphabet.charAt(random.nextInt(26)));
//            i++;
//        }

        String corpus1000000 =  new Scanner(new File("corpus_" + 1000000 + ".txt")).useDelimiter("\\Z").next();
        int count = 1;
        for(i = 0; i < corpus1000000.length(); i++) {
            if(i > 0 && i % 66666 == 0) /*15 datapoints*/ {
                String filename = patternFilenameBase + (count++) + ".txt";
                BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                writer.write(pattern.toString());

                writer.close();
            }
            pattern.append(corpus1000000.charAt(i));
        }
    }
}
