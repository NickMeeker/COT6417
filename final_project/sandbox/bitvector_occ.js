class BitvectorOcc {
  constructor(L, alphabet) {
    this.L = L;

    this.alphabet = alphabet;
    this.Bc = new Map();

    this.smallrank = new Map();
    this.boundaryrank = new Map();
    this.t = -1;
    this.numBlocks = -1;
  }

  buildBcArray() {
    //console.log(this.L);

    this.L.forEach(letter => {
      if (letter !== '$') this.alphabet.add(letter);
    });

    this.alphabet.forEach(c => {
      this.Bc.set(c, []);
      this.L.forEach(letter => {
        this.Bc.get(c).push((letter === c) ? 1 : 0);
      });
    });

    //console.log(this.Bc);
  }

  computeSmallrankEntry(block, c) {
    const smallrankEntry = [];

    for (let i = 0; i < block.length; i++) {
      const arr = [];
      let frequency = 0;
      for (let j = i; j < block.length; j++) {
        if (block[j] == c) {
          frequency++;
        }
        arr.push(frequency);
      }
      smallrankEntry.push(arr);
    }
    return smallrankEntry;
    //console.log(smallrankEntry);
  }

  bitvectorPreprocessing() {
    console.log(Math.log2(this.L.length));
    this.t = Math.round(Math.log2(this.L.length) / 2);
    console.log('t:' + this.t);
    this.numBlocks = this.L.length / this.t;

    // console.log(this.t + ' ' + this.numBlocks);

    Array.from(this.Bc.keys()).forEach(c => {
      this.smallrank.set(c, []);
      this.boundaryrank.set(c, []);

      let blockStartingIndex = 0;
      let i = 0;
      let boundaryrankValue = 0;
      while (blockStartingIndex < this.L.length) {
        const smallrankEntry = this.computeSmallrankEntry(this.L.slice(blockStartingIndex, blockStartingIndex + this.t), c);

        this.boundaryrank.get(c).push(boundaryrankValue);
        this.smallrank.get(c).push(smallrankEntry);
        boundaryrankValue += smallrankEntry[0][this.t - 1];
        i++;
        blockStartingIndex = i * this.t;
      }

      // console.log(c);
      // console.log(this.smallrank.get(c));
      // console.log(this.boundaryrank.get(c));
    });
  }

  bitvectorOccQuery(i, c) {
    if (!this.alphabet.has(c)) {
      return -1;
    }

    // from lecture slides
    if (i < 0) {
      return 0;
    }

    const blockIndex = Math.floor(i / this.t);
    const indexInBlock = i % this.t;
    // console.log(blockIndex);
    // console.log(indexInBlock);
    // console.log(this.boundaryrank.get(c)[blockIndex]);
    // console.log(this.smallrank.get(c)[blockIndex][0][indexInBlock]);
    return this.boundaryrank.get(c)[blockIndex] + this.smallrank.get(c)[blockIndex][0][indexInBlock];
  }
}

module.exports = BitvectorOcc;