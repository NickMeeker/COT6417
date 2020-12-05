import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;


public class Driver {
    public static void main(String[] args) throws Exception {
//        String S = "MISSISSIPPI";
//        String P = "ISS";
        PrintStream console = System.out;
        // currently set to run against corpuses 1-15
        for(int bytes = 100000; bytes <= 1500000; bytes += 100000) {
        //for(int bytes = 1; bytes <= 15; bytes += 1) {
            // dont try this at home
            System.gc();
            System.gc();
            System.gc();
            System.gc();
            System.gc();
            System.out.println(bytes / 100000 + " out of " + 20);
            PrintStream o = new PrintStream(new File("corpus_" + bytes + "_preprocessing_metrics_demo.txt"));
            //PrintStream o = new PrintStream(new File("pattern_" + bytes + "_runtime_metrics.txt"));
            System.setOut(o);
            String S = new Scanner(new File("corpus_" + bytes + ".txt")).useDelimiter("\\Z").next();
            //String S = new Scanner(new File("corpus_" + 1000000 + ".txt")).useDelimiter("\\Z").next();
            String P = new Scanner(new File("pattern.txt")).useDelimiter("\\Z").next();
            //String P = new Scanner(new File("pattern_" + bytes + ".txt")).useDelimiter("\\Z").next();

            S = S.replaceAll("[^a-zA-Z]", "").toUpperCase() + '$';
            P = P.replaceAll("[^a-zA-Z]", "").toUpperCase();

            long startTime = System.currentTimeMillis();
            BetterSuffixArray bsa = new BetterSuffixArray(S);
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            System.out.println("suffix array construction time: " + totalTime);
            //startTime = System.currentTimeMillis();
            FMIndex fmIndex = new FMIndex(S, bsa);
            endTime = fmIndex.lConstructEndTime;
            totalTime = endTime - startTime;
            System.out.println("BWT construction time: " + totalTime);

            fmIndex.waveletTreeSearchStartTime = System.currentTimeMillis();
            int[] result = fmIndex.backwardSearch(P, Mode.Wavelet);
            System.out.println(result[0] + " " + result[1]);
            if (result[0] > -1) {
                for (int i = result[0]; i <= result[1]; i++) {
                    System.out.println(bsa.SA[i].index);
                    //System.out.println(S.substring(bsa.SA[i].index));
                    //System.out.println(S.substring(bsa.SA[i].index, bsa.SA[i].index + P.length()));
                }
            }
            fmIndex.waveletTreeSearchEndTime = System.currentTimeMillis();
            System.out.println("wavelet tree search time: " + (fmIndex.waveletTreeSearchEndTime - fmIndex.waveletTreeSearchStartTime));

            fmIndex.boundaryrankSearchStartTime = System.currentTimeMillis();
            result = fmIndex.backwardSearch(P, Mode.Boundaryrank);
            System.out.println(result[0] + " " + result[1]);
            if (result[0] > -1) {
                for (int i = result[0]; i <= result[1]; i++) {
                    System.out.println(bsa.SA[i].index);
                    //System.out.println(S.substring(bsa.SA[i].index));
                    //System.out.println(S.substring(bsa.SA[i].index, bsa.SA[i].index + P.length()));
                }
            }
            fmIndex.boundaryrankSearchEndTime = System.currentTimeMillis();
            System.out.println("boundaryrank search time: " + (fmIndex.boundaryrankSearchEndTime - fmIndex.boundaryrankSearchStartTime));
            System.setOut(console);
        }
    }
}
