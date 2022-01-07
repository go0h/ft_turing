# Turing Machine emulator

### Build
You need to Scala Build Tool 1.5.7. _Not tested on other versions_

To build run command in shell ```sbt turing```

To clean up project folder run ```sbt clear```

### Usage
```
Usage: ft_turing [gen] [options] jsonFile [input]

  jsonFile     json description of the machine
  input        input of the machine
  -h, --help   show this help message and exit
Command: gen
Generate Universal Turing Machine for input jsonFile
```

#### Example of work
```java -jar ft_turing.jar ./resources/unary_add.json '11+11='```
```
[1+1=......] (scanright, 1) -> (scanright, 1, RIGHT)
[1+1=......] (scanright, +) -> (addone, +, RIGHT)
[1+1=......] (addone, 1) -> (writeone, +, LEFT)
[1++=......] (writeone, +) -> (scanright, 1, RIGHT)
[11+=......] (scanright, +) -> (addone, +, RIGHT)
[11+=......] (addone, =) -> (eraseall, ., LEFT)
[11+.......] (eraseall, +) -> (eraseall, ., LEFT)
[11........] (eraseall, 1) -> (HALT, 1, LEFT)
```

### Machine descriptions
All descriptions in ```./resouces``` directory.
1. A machine able to compute an unary addition. ```unary_add.json```
2. A machine able to decide whether its input is a palindrome or not. Before halting, write the result on the tape as a 'n' or a 'y' at the right of the rightmost character of the tape. ```palindrome.json```
3. A machine able to decide if the input is a word of the language 0n1n, for instance the words 000111 or 0000011111. Before halting, write the result on the tape as a 'n' or a 'y' at the right of the rightmost character of the tape. ```language_0n1n.json```
4. A machine able to decide if the input is a word of the language 02n, for instance the words 00 or 0000, but not the words 000 or 00000. Before halting, write the result on the tape as a 'n' or a 'y' at the right of the rightmost character of the tape. ```language_02n.json```
5. A machine able to run the first machine of this list, the one comput- ing an unary addition. The machine alphabet, states, transitions and input ARE the input of the machine you are writing, encoded as you see fit. ```just look down :)```

### Universal Turing Machine generator
To generate UTM for description use command
```java -jar ft_turing.jar gen jsonFile```

Example for ```unary_add.json``` program creates JSON file with description for Universal Turing Machine, and print how to use it.
```
Created Universal Turing Machine - utm_unary_add.json
Usage:
java -jar ft_turing.jar utm_unary_add.json 'A!A[{.A.R}{1A1R}{+C+R}{=H.R}]C[{1B+L}{+C1R}{=D.L}{.C.R}]B[{+A1R}{.B.L}]D[{.D.L}{+D.L}{1H1L}]#input'
```

#### OR You may use utm.json file with next short descriptions:
1. unary_add ```'A!A[{.A.R}{1A1R}{+C+R}{=H.R}]C[{1B+L}{+C1R}{=D.L}{.C.R}]B[{+A1R}{.B.L}]D[{.D.L}{+D.L}{1H1L}]#11+11='```
2. unary_sub ```'A!A[{.A.R}{1A1R}{-A-R}{=B.L}]B[{1C=L}{-H.L}]C[{1C1L}{-D-L}]D[{.D.L}{1A.R}]#111-1='```
3. palindrome ```'A!E[{.F.L}{aEaR}{bEbR}]F[{aC.L}{bDnL}{.HyR}]A[{aE.R}{bG.R}{.H.R}]I[{bC.L}{aDnL}{.HyR}]G[{.I.L}{aGaR}{bGbR}]B[{.A.R}{aBaL}{bBbL}]C[{.HyR}{aBaL}{bBbL}]D[{aD.L}{bD.L}{.H.R}]#aaabbaaa'```
4. language 0n1n ```'A!E[{.B.R}{0E0L}{1E1L}]F[{0F.R}{1F.R}{.HnR}]A[{.A.R}{0C.R}{1F.R}]G[{0G.L}{1G.L}{.H.R}]B[{0C.R}{1F.R}{.HyR}]C[{.D.L}{0C0R}{1C1R}]D[{.F.R}{1E.L}{0GnL}]#000111'```
5. language 0^(2n) ```'A!E[{-E-R}{0D0R}{.B.L}]J[{0J0R}{-J0R}{.HnL}]F[{0F0L}{-F0L}{|G0R}]A[{0C|R}]I[{0I0L}{-I0L}{|J0R}]G[{0G0R}{-G0R}{.HyL}]B[{0B0L}{-B-L}{|C|R}]C[{0E-R}{-C-R}{.F.L}]D[{-D-R}{0E-R}{.I.L}]#0000'```