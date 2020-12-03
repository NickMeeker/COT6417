import java.util.*;

public class Boundaryrank {
    Map<Character, List<Integer>> bitvector;
    Map<Character, List<List<List<Integer>>>> smallrankMap;
    Map<Character, List<Integer>> boundaryrankMap;
    int t;
    int numBlocks;

    Boundaryrank(String L, int n, Set<Character> alphabet) {
        this.bitvector = new HashMap<>();
        this.smallrankMap = new HashMap<>();
        this.boundaryrankMap = new HashMap<>();
        for(char c : alphabet) {
            this.bitvector.put(c, new ArrayList<>());
            for(char letter : L.toCharArray()) {
                bitvector.get(c).add(letter == c ? 1 : 0);
            }
        }

        // each bitvector will be divided into blocks of size t
        this.t = (int) (log2(n) / 2);
        this.numBlocks = n / this.t;

        for(char c : alphabet) {
            // build the smallrank table - this can answer inner-block rank1 queries
            this.smallrankMap.put(c, new ArrayList<>());
            // the boundary rank answers rank1 queries up till the block we're interested in, then smallrank takes over
            this.boundaryrankMap.put(c, new ArrayList<>());

            int blockStartingIndex = 0;
            int i = 0;
            int boundaryrankValue = 0;
            while(blockStartingIndex < n) {
                int blockEndingIndex = blockStartingIndex + this.t;
                if(blockEndingIndex > n) {
                    blockEndingIndex = n - 1;
                }
                List<List<Integer>> smallrankEntry = this.computeSmallrankEntry(L.substring(blockStartingIndex, blockEndingIndex), c);

                this.boundaryrankMap.get(c).add(boundaryrankValue);
                this.smallrankMap.get(c).add(smallrankEntry);
                if(smallrankEntry.size() > 0)
                    boundaryrankValue += smallrankEntry.get(0).get(smallrankEntry.get(0).size() - 1);
                i++;
                blockStartingIndex = i * this.t;
            }
        }
    }

    List<List<Integer>> computeSmallrankEntry(String s, char c) {
        List<List<Integer>> smallrankEntry = new ArrayList<>();
        for(int i = 0; i < s.length(); i++) {
            List<Integer> block = new ArrayList<>();
            int frequency = 0;
            for(int j = i; j < s.length(); j++) {
                if(s.charAt(j) == c) {
                    frequency++;
                }
                block.add(frequency);
            }
            smallrankEntry.add(block);
        }
        return smallrankEntry;
    }

    int occ(int i, char c) {
        if(i <= 0) return 0;
        i--;
        int blockIndex = i / this.t;
        int indexInBlock = i % this.t;

        return this.boundaryrankMap.get(c).get(blockIndex) + this.smallrankMap.get(c).get(blockIndex).get(0).get(indexInBlock);

    }

    static double log2(int n) {
        return Math.log10(n) / Math.log10(2);
    }
}



