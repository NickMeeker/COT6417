class WaveletTreeNode {
  constructor(S, alphabet) {
    this.B = [];
    this.alphabetMap = new Map();
    alphabet = new Set(alphabet);
    S.split('').forEach(c => {
      //if (c !== '$') 
      this.B.push(alphabet.has(c) ? 0 : 1);
    });

    this.rank1Query = [];
    let rank = 0;
    this.B.forEach(bit => this.rank1Query.push(bit === 1 ? ++rank : rank));

  }

  setLeftChild(leftChild) {
    this.leftChild = leftChild;
  }

  setRightChild(rightChild) {
    this.rightChild = rightChild;
  }
}

class WaveletTree {
  constructor(L) {
    this.L = L;
    this.alphabet = new Set();
    this.L.forEach(c => {
      //if (c !== '$') 
      this.alphabet.add(c);
    });
  }

  buildWaveletTree(S, alphabet) {
    if (alphabet.length <= 1) {
      return null;
    }

    // if (alphabet.length == 1) {
    //   const node = new WaveletTreeNode(S, alphabet);
    //   node.setLeftChild(null);
    //   node.setRightChild(null);
    //   return node;
    // }

    const alphabetMidpoint = Math.floor(alphabet.length / 2);
    const leftAlphabet = Array.from(alphabet).slice(0, alphabetMidpoint);
    const rightAlphabet = Array.from(alphabet).slice(alphabetMidpoint);

    // console.log(leftAlphabet + ' ' + rightAlphabet);
    // console.log(S);
    let leftString = '';
    let rightString = '';
    S.split('').forEach(c => {
      if (leftAlphabet.includes(c)) {
        leftString += c;
      } else {
        rightString += c;
      }
    });

    const node = new WaveletTreeNode(S, leftAlphabet);
    node.setLeftChild(this.buildWaveletTree(leftString, leftAlphabet));
    node.setRightChild(this.buildWaveletTree(rightString, rightAlphabet));



    leftAlphabet.forEach(c => node.alphabetMap.set(c, 0));
    rightAlphabet.forEach(c => node.alphabetMap.set(c, 1));
    return node;
  }

  // todo: make this faster
  rank1Query(B, i) {
    let rank = 0;
    for (let index = 0; index < i; index++) {
      if (B[index] === 1) {
        rank++;
      }
    }
    return rank;
  }

  occ(node, i, c) {
    if (i < 0) {
      return 0;
    }

    if (node == null /*|| (node.leftChild == null && node.leftChild == null)*/) {
      // console.log(i - 1); 
      // console.log(node.B);
      // console.log(node.rank1Query[i - 1]);
      // console.log(this.rank1Query(node.B, i - 1));
      // return node.alphabetMap.get(c) === 1 ?
      //   node.rank1Query[i - 1] :
      //   i - node.rank1Query[i - 1];
      //console.log(i);
      return i;
    }

    let rank = node.rank1Query[i - 1];

    //console.log(i);
    return node.alphabetMap.get(c) === 1 ?
      this.occ(node.rightChild, rank, c) :
      this.occ(node.leftChild, i - rank, c);

  }

  printLevelOrder(root) {
    const levels = [];
    const levelQueue = [];
    let level = [];
    const queue = [];
    let prev = 0;

    levelQueue.push(prev);
    queue.push(root);
    while (queue.length > 0) {
      const current = levelQueue.shift();
      const node = queue.shift();
      if (current > prev) {
        prev = current;
        levels.push([...level]);
        level = [];
      }
      level.push(node === null ? null : node.B);
      if (node === null) continue;
      queue.push(node.leftChild);
      levelQueue.push(current + 1);
      queue.push(node.rightChild);
      levelQueue.push(current + 1);
    }

    //console.log(levels);
  }
}

module.exports = { WaveletTree, WaveletTreeNode };