const { WaveletTree, WaveletTreeNode } = require('./wavelet_tree');
const BitvectorOcc = require('./bitvector_occ');

class BWT {
  constructor(S, SA) {
    this.S = S;

    // construct the alphabet
    this.alphabet = new Set();
    this.S.split('').forEach(letter => {
      if (letter !== '$') this.alphabet.add(letter);
    });

    // construct the actual BWT and store it in a 2D array
    // this.bwt = [];
    // for (const [i] of this.S.split('').entries()) {
    //   this.bwt.push(this.S.substring(i, this.S.length) + this.S.substring(0, i));
    // }
    // this.bwt.sort();
    // console.log(this.bwt);

    // this.L = [];
    // this.bwt.forEach(rotation => {
    //   this.L.push(rotation.charAt(rotation.length - 1));
    // });
    // console.log(this.L);
    this.L = [];
    for (let i = 0; i < S.length; i++) {
      let j = SA.saSorted[i].index - 1;
      if (j < 0) {
        j = j + S.length;
      }
      this.L.push(S.charAt(j));
    }
    console.log(this.L);

    this.initializeBitvectorOcc();
    this.initializeWaveletTree();
  }

  initializeBitvectorOcc() {
    // occ object handles all bitvector occ queries
    this.bitvectorOcc = new BitvectorOcc(this.L, this.alphabet);
    this.bitvectorOcc.buildBcArray();
    this.bitvectorOcc.bitvectorPreprocessing();

    // construct C mapping
    // O(sigma * log(m))
    this.C = new Map();
    this.frequency = new Map();

    this.alphabet.forEach(c => {
      this.frequency.set(c, 0);
    });
    this.L.forEach(c => {
      if (this.frequency.has(c)) { // Skipping '$'
        this.frequency.set(c, this.frequency.get(c) + 1);
      }
    });
    let runningTotal = 0;
    Array.from(this.frequency.keys()).sort().forEach(c => {
      this.C.set(c, runningTotal);
      runningTotal += this.frequency.get(c);
    });
    //console.log(this.C);

    // console.log(this.bitvectorOcc.bitvectorOccQuery(6, 'P'));
    // console.log(this.bitvectorOcc.bitvectorOccQuery(10, 'P'));
    // console.log(this.bitvectorOcc.bitvectorOccQuery(10, 'I'));
    // console.log(this.bitvectorOcc.bitvectorOccQuery(11, 'I'));
  }

  initializeWaveletTree() {
    this.waveletTree = new WaveletTree(this.L);
    this.waveletTreeRoot = this.waveletTree.buildWaveletTree(this.waveletTree.L.join(''), Array.from(this.waveletTree.alphabet).sort());

    this.waveletTree.printLevelOrder(this.waveletTreeRoot);
  }

  bitvectorBackwardSearch(P) {
    let i = P.length - 1;
    let sp = 0;
    let ep = this.L.length - 1;
    while (sp <= ep && i >= 0) {
      const c = P.charAt(i);
      //if (c !== '$') {

      sp = this.C.get(c) + this.bitvectorOcc.bitvectorOccQuery(sp - 1, c) + 1;
      ep = this.C.get(c) + this.bitvectorOcc.bitvectorOccQuery(ep, c);
      console.log([sp, ep]);
      //}
      i--;
    }
    if (ep < sp) {
      return [-1, -1];
    }
    return [sp, ep];
  }

  waveletTreeBackwardSearch(P) {
    let i = P.length - 1;
    let sp = 0;
    let ep = this.L.length - 1;
    while (sp <= ep && i >= 0) {
      const c = P.charAt(i);
      sp = this.C.get(c) + this.waveletTree.occ(this.waveletTreeRoot, sp - 1, c) + 1;
      ep = this.C.get(c) + this.waveletTree.occ(this.waveletTreeRoot, ep, c);
      console.log(sp + " " + ep);
      console.log([sp, ep]);
      i--;
    }
    if (ep < sp) {
      return [-1, -1];
    }
    return [sp - 1, ep - 1];
  }
}

module.exports = BWT;