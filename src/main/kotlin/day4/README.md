# Mistake
I misread part 2 of this days problem. I read it to mean that the extra rule was "There must not be a group of adjacent matching digits larger than 2".

- `123444` does not meat the criteria because there is a group of 3 4s
- `123445` meets the criteria
- `111122` does not meat the criteria because there is a group of 4 1s. HOWEVER, according to the real rules, it does.

I'm saving the code for counting the passwords with the misread criteria, because I spent a lot of time on it and it turned out good. The answer was 758 with the input 172930-683082. It can be accessed through the `countValidPart2PasswordsFail()` function.
The correct problem is in the `problem` file.