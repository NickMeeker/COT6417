# COT6417

This is an implementation of the FM-Index technique. In the src directory, classes of interest are:

- **BetterSuffixArray:** Constructs the suffix array for the input string (read from the corpuses) in O(n lgn lgn) time. 
- **FMIndex:** Computes BWT from the SA, computes C, and initializes the Wavelet Tree and Boundaryrank structures. The function to recursively build the Wavelet Tree, as well as the backwardSearch() function, lives here.
- **WaveletTreeNode:** Everything related to wavelet tree nodes, including occ()
- **Boundaryrank:** Everything related to the boundaryrank bitvector data structure, including initialization and O(1) occ() queries

Classes to ignore:
- **SuffixArray:** This is a simple implementation I used for testing, it is not part of the final algorithm
- **Driver:** This really just reads the input, builds the suffix array, the fmindex, and then queries for P. Lots of printing metrics and stuff, too.
- **BuildGiantCorpus:** This is a script I used for generating data up to 2MB
- **Mode:** backwardSearch() takes this to know if it should check the wavelet tree or the boundaryrank table for occ(i,c)
