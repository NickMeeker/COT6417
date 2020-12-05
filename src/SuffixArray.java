import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * IMPORTANT: This class is completely unused in the final implementation.
 * It was replaced by BetterSuffixArray because it (this class) is terrible.
 * It constructs the SuffixArray/BWT completely naively: dump all the suffixes
 * into a list, sort the list, compute the entire Burrows-Wheeler matrix,
 * sort the matrix, and get the BWT L string from the sorted matrix.
 *
 * It's only kept here for posterity's sake, and for debugging on simple test cases
 * since it definitely works (in case there's an issue with BetterSuffixArray).
 * */
public class SuffixArray {
    List<Suffix> suffixArray;
    List<Suffix> sortedSuffixArray;
    List<String> burrowsWheelerMatrix;
    String L;
    SuffixArray(String S) {
        this.suffixArray = new ArrayList<>();

        StringBuilder s = new StringBuilder(S);
        for(int i = 0; i < S.length(); i++) {
            this.suffixArray.add(new Suffix(new StringBuilder(s), i));

            s.deleteCharAt(0);
        }

        this.sortedSuffixArray = new ArrayList<>(this.suffixArray);
        //Collections.sort(this.sortedSuffixArray, Comparator.comparing(a -> a.suffix));

        this.burrowsWheelerMatrix = new ArrayList<>();
        String t = S;
        for(int i = 0; i < S.length(); i++) {
            this.burrowsWheelerMatrix.add(t);
            t = t.substring(1) + t.charAt(0);
        }
        Collections.sort(this.burrowsWheelerMatrix);

        StringBuilder l = new StringBuilder();
        for(int i = 0; i < S.length(); i++) {
            l.append(this.burrowsWheelerMatrix.get(i).charAt(S.length() - 1));
        }
        this.L = l.toString();
//        System.out.println();
//        System.out.println(this.L);
    }

    class Suffix {
        StringBuilder suffix;
        int index;
        Suffix(StringBuilder suffix, int index) {
            this.suffix = suffix;
            this.index = index;
        }

        public String toString() {
            return this.index + ": " + this.suffix;
        }
    }
}
