import java.util.*;

public class FMIndex {
    String S;
    String L;
    Set<Character> alphabet;
    Map<Character, Integer> C;
    WaveletTreeNode root;
    Boundaryrank boundaryrank;

    long lConstructEndTime;
    long waveletTreeConstructStartTime;
    long waveletTreeConstructEndTime;
    long boundaryRankConstructStartTime;
    long boundaryRankConstructEndTime;
    long waveletTreeSearchStartTime;
    long waveletTreeSearchEndTime;
    long boundaryrankSearchStartTime;
    long boundaryrankSearchEndTime;
    long waveletTreeStartMem;
    long waveletTreeEndMem;
    long boundaryRankStartMem;
    long boundaryRankEndMem;

    public FMIndex(String S, BetterSuffixArray sa) {
        this.S = S;

        // get alphabet
        this.alphabet = new HashSet<>();
        for(char c : S.toCharArray()) {
            this.alphabet.add(c);
        }

        // construct L
        StringBuilder l = new StringBuilder();
        for(int i = 0; i < S.length(); i++) {
            int j = sa.SA[i].index - 1;
            if(j < 0) {
                j += S.length();
            }
            l.append(S.charAt(j));
        }
        this.L = l.toString();
        this.lConstructEndTime = System.currentTimeMillis();

        // construct C
        this.C = new HashMap<>();
        Map<Character, Integer> frequency = new HashMap<>();
        for(char c : this.L.toCharArray()) {
            frequency.put(c, frequency.getOrDefault(c, 0) + 1);
        }
        int runningTotal = 0;
        List<Character> alphabetList = new ArrayList<>(this.alphabet);
        Collections.sort(alphabetList);
        for(char c : alphabetList) {
            this.C.put(c, runningTotal);
            runningTotal += frequency.get(c);
        }

        // construct wavelet tree
        this.waveletTreeStartMem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        this.waveletTreeConstructStartTime = System.currentTimeMillis();
        this.root = buildWaveletTree(L, alphabetList);
        this.waveletTreeEndMem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        this.waveletTreeConstructEndTime = System.currentTimeMillis();
        System.out.println("wavelet tree construct total time: "  + (this.waveletTreeConstructEndTime - this.waveletTreeConstructStartTime));
        System.out.println("wavelet tree total memory kb: "  + (this.waveletTreeEndMem - this.waveletTreeStartMem) / 1024L);

        // construct boundaryrank tables
        this.boundaryRankStartMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        this.boundaryRankConstructStartTime = System.currentTimeMillis();
        this.boundaryrank = new Boundaryrank(L, L.length(), alphabet);
        this.boundaryRankEndMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        this.boundaryRankConstructEndTime = System.currentTimeMillis();
        System.out.println("boundaryrank construct total time: "  + (this.boundaryRankConstructEndTime - this.boundaryRankConstructStartTime));
        System.out.println("boundaryrank total memory kb: "  + (this.boundaryRankEndMem - this.boundaryRankStartMem) / 1024L);
    }

    WaveletTreeNode buildWaveletTree(String s, List<Character> alphabet) {
        if(alphabet.size() <= 1) {
            return null;
        }

        // split alphabet and string
        int alphabetMidpoint = alphabet.size() / 2;
        LinkedHashSet<Character> leftAlphabet = new LinkedHashSet<>(alphabet.subList(0, alphabetMidpoint));
        LinkedHashSet<Character> rightAlphabet = new LinkedHashSet<>(alphabet.subList(alphabetMidpoint, alphabet.size()));
        StringBuilder leftString = new StringBuilder();
        StringBuilder rightString = new StringBuilder();
        for(char c : s.toCharArray()) {
            if (leftAlphabet.contains(c)) {
                leftString.append(c);
            } else {
                rightString.append(c);
            }
        }
        // build node
        WaveletTreeNode node = new WaveletTreeNode(s, leftAlphabet);
        node.left = buildWaveletTree(leftString.toString(), new ArrayList<>(leftAlphabet));
        node.right = buildWaveletTree(rightString.toString(), new ArrayList<>(rightAlphabet));

        // encode characters
        for(char c : leftAlphabet) node.alphabetMap.put(c, 0);
        for(char c : rightAlphabet) node.alphabetMap.put(c, 1);
        return node;
    }

    int[] backwardSearch(String P, Mode mode) {
        int i = P.length() - 1;
        int sp = 0;
        int ep = this.L.length() - 1;

        while(sp <= ep && i >= 0) {
            char c = P.charAt(i);
            sp = this.C.get(c) + (mode == Mode.Wavelet ? this.root.occ(this.root, sp - 1, c) : this.boundaryrank.occ(sp - 1, c)) + 1;
            ep = this.C.get(c) + (mode == Mode.Wavelet ? this.root.occ(this.root, ep, c) : this.boundaryrank.occ(ep, c));
            i--;
        }

        if(ep < sp) {
            return new int[]{-1, -1};
        }

        return new int[]{sp - 1, ep - 1};
    }

    public void inOrderPrint(WaveletTreeNode node){
        if (node.left != null) {
            inOrderPrint(node.left);
        }

        for(int bit : node.B) {
            System.out.print(bit);
        }

        System.out.println();

        if (node.right != null) {
            inOrderPrint(node.right);
        }
    }
}
