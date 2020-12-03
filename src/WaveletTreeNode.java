import java.util.*;

public class WaveletTreeNode {
    List<Integer> B;
    List<Integer> rank1;
    Map<Character, Integer> alphabetMap;

    WaveletTreeNode left = null;
    WaveletTreeNode right = null;

    public WaveletTreeNode(String s, LinkedHashSet<Character> leftAlphabet) {
        this.B = new ArrayList<>();
        this.rank1 = new ArrayList<>();
        this.alphabetMap = new HashMap<>();

        for(char c : s.toCharArray()) {
            this.B.add(leftAlphabet.contains(c) ? 0 : 1);
        }

        int rank = 0;
        for(int bit : this.B) {
            this.rank1.add(bit == 1 ? ++rank : rank);
        }
    }

    int rank1Query(int i) {
        return this.rank1.get(i - 1);
    }

    int occ(WaveletTreeNode node, int i, char c) {
        if(i <= 0) return 0;

        if(node == null) {
            return i;
        }

        return node.alphabetMap.get(c) == 1 ?
                node.occ(node.right, node.rank1Query(i), c) :
                node.occ(node.left, i - node.rank1Query(i), c);
    }
}
