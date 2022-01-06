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
