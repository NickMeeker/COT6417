const process = require('process');
class Suffix {
  constructor(suffix, index) {
    this.suffix = suffix;
    this.index = index;
  }
}

class SA {
  constructor(S) {
    this.S = S;
    this.sa = [];
    let startTime = new Date().getTime();
    for (const [i] of this.S.split('').entries()) {
      const suffix = new Suffix(this.S.substring(i, this.S.length), i)
      console.log(process.memoryUsage());
      this.sa.push(suffix);
    }


    let endTime = new Date().getTime();
    let totalTime = (endTime - startTime);
    console.log('running time sa construction: ' + totalTime);

    //console.log(this.sa);
    startTime = new Date().getTime();
    this.saSorted = [...this.sa].sort((a, b) => {
      if (a.suffix > b.suffix) {
        return 1;
      } else if (b.suffix > a.suffix) {
        return -1;
      } else {
        return 0;
      }
    });

    endTime = new Date().getTime();
    totalTime = (endTime - startTime);
    console.log('running time sorted sa o(n^2 lg n) construction: ' + totalTime);
    // this.saSortedToSa = new Map();
    // for (const [i, value] of this.saSorted.entries()) {
    //   this.saSortedToSa.set(i, this.sa.findIndex(suffix => suffix === value));
    // }
  }

  // querySaSorted(i) {
  //   return this.saSorted[i];
  // }

  // saSortedIndexToSaIndex(i) {
  //   return this.saSortedToSa.get(i);
  // }
}

module.exports = SA;