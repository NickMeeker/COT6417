import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

public class BetterSuffixArray {
    Suffix[] SA;
    String S;
    BetterSuffixArray(String S) {
        this.S = S;
        int n = S.length();
        this.SA = new Suffix[n];

        int done = -1;

        for(int i = 0; i < n; i++) {
            // create all the suffixes with their initial buckets
            SA[i] = new Suffix(i, S.charAt(i) - 'A', 0);
        }

        // handle the buckets of the next elements
        for(int i = 0; i < n; i++) {
            if(i + 1 < n) {
                SA[i].nextBucket = SA[i + 1].bucket;
            } else {
                SA[i].nextBucket = done; // done!
            }
        }

        // sort based on initial bucket
        Arrays.sort(SA, (s1, s2) -> {
            if (s1.bucket != s2.bucket)
                return Integer.compare(s1.bucket, s2.bucket);
            return Integer.compare(s1.nextBucket, s2.nextBucket);
        });

        // this is where we'll map the original index of each suffix to its index in SA
        int[] indices = new int[n];
        for(int bucketSize = 4; bucketSize < 2 * n; bucketSize *= 2) {
            int bucket = 0;

            // drop first suffix into bucket and track the index
            int previousBucket = SA[0].bucket;
            SA[0].bucket = 0;
            indices[SA[0].index] = 0;

            for(int i = 1; i < n; i++) {
                if(SA[i].bucket == previousBucket && SA[i].nextBucket == SA[i - 1].nextBucket) {
                    // this suffix is in the same bucket
                    SA[i].bucket = bucket;
                } else {
                    // not in the same bucket - we need to increment the bucket number
                    previousBucket = SA[i].bucket;
                    SA[i].bucket = ++bucket;
                }
                // track the new index
                indices[SA[i].index] = i;
            }
            for(int i = 0; i < n; i++) {
                // need to update the bucket number of the suffix that is bucketSize / 2 away
                int next = SA[i].index + bucketSize / 2;
                if(next < n) {
                    SA[i].nextBucket = SA[indices[next]].bucket;
                } else {
                    SA[i].nextBucket = done;
                }
            }
            // now sort based on new bucket
            Arrays.sort(SA, (s1, s2) -> {
                if (s1.bucket != s2.bucket)
                    return Integer.compare(s1.bucket, s2.bucket);
                return Integer.compare(s1.nextBucket, s2.nextBucket);
            });
        }
    }

    class Suffix
    {
        public int index;
        int bucket;
        int nextBucket;

        public Suffix(int index, int bucket, int nextBucket)
        {
            this.index = index;
            this.bucket = bucket;
            this.nextBucket = nextBucket;
        }
    }

}
