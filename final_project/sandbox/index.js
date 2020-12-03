const BWT = require('./bwt');
const SA = require('./sa');

const fs = require('fs');

const main = () => {
  // const str = 'MISSISSIPPI$';

  // const sa = new SA(str);
  // const bwt = new BWT(str, sa);


  // let range = bwt.bitvectorBackwardSearch('I');
  // console.log(range);
  // for (let i = range[0]; i <= range[1]; i++) {
  //   console.log(sa.saSortedIndexToSaIndex(i) + ': ' + sa.querySaSorted(i));
  // }

  // range = bwt.waveletTreeBackwardSearch('I');
  // console.log(range);
  // for (let i = range[0]; i <= range[1]; i++) {
  //   console.log(sa.saSortedIndexToSaIndex(i) + ': ' + sa.querySaSorted(i));
  // }
  // // }
  // let S = fs.readFileSync('moby_dick.txt', 'utf8');
  // let P = fs.readFileSync('pattern.txt', 'utf8');
  // let S = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc pellentesque pharetra nibh quis dignissim. Donec egestas sed dolor et tincidunt. In orci tortor, posuere quis sapien vel, tempor euismod ante. In placerat, purus ut gravida porta, risus orci vehicula mi, eu ultrices ligula ante a ligula. Praesent tellus lacus, sodales non consectetur sed, laoreet id tellus. Donec ut augue est. Sed eu molestie arcu. Aliquam ullamcorper nunc ante, nec laoreet leo tempor quis. Phasellus ut tellus nec mauris fringilla tincidunt. Donec vitae ullamcorper augue. Donec gravida in purus vel maximus. Cras venenatis viverra mi, in efficitur ante gravida non. Mauris ultricies id lectus sit amet scelerisque.$';
  // let P = 'Phasellus ';

  // let S = 'aabbdcddccdafbed$';
  // let P = 'ed';

  let S = 'MISSISSIPPI';
  let P = 'ISS';

  S = S.toUpperCase().replace(/\W/ig, '').replace(/[0-9]/g, '') + '$';
  //console.log(S);
  P = P.toUpperCase().replace(/\W/ig, '').replace(/[0-9]/g, '');
  //console.log(P);
  const sa = new SA(S);
  //console.log(SA.sa.length);
  let startTime = new Date().getTime();
  const bwt = new BWT(S, sa);
  let endTime = new Date().getTime();
  let totalTime = (endTime - startTime);
  console.log('bwt construction: ' + totalTime);

  startTime = new Date().getTime();
  let range = bwt.bitvectorBackwardSearch(P);
  endTime = new Date().getTime();
  console.log(range);
  // for (let i = range[0]; i <= range[1]; i++) {
  //   //console.log(sa.sa[sa.saSorted[i].index].suffix.substring(0, P.length));
  // }
  totalTime = (endTime - startTime);
  console.log('running time no wavelet tree: ' + totalTime);


  startTime = new Date().getTime();
  range = bwt.waveletTreeBackwardSearch(P);
  endTime = new Date().getTime();
  console.log(range);
  for (let i = range[0]; i <= range[1]; i++) {
    console.log(sa.sa[sa.saSorted[i].index].suffix.substring(0, P.length));
  }
  totalTime = (endTime - startTime);
  console.log('running time with wavelet tree: ' + totalTime);
}
main();