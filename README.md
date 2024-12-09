# Pattern Matcher

This project implements a pattern matcher that supports:
- Character matching (letters and digits)
- Wildcard matching (using '.')
- Concatenation
- Alternation (using '|')
- Optional patterns (using '?')

The implementation follows a formal grammar and uses recursive descent parsing to build an Abstract Syntax Tree (AST).

## Features

### Supported Operations

- **Character Matching**: Match specific letters and digits
- **Wildcard ('.')**: Match any single letter or digit
- **Concatenation**: Match patterns in sequence
- **Alternation ('|')**: Match either of two patterns
- **Optional ('?')**: Match a pattern zero or one time

### Grammar

The project implements the following unambiguous grammar:

```
S  → E$
E  → T '|' E | T
T  → F T | F
F  → A '?' | A
A  → C | '(' E ')'
C  → Alphanumeric characters plus '.'
```

where '$' represents end-of-string.

## Usage

Run the program and enter patterns and strings to match:

```
pattern? ((h|j)ell. worl?d)|(42)
string? hello world
match
string? jello word
match
string? 42
match
string? 24
no match
```

### Pattern Examples

1. Basic patterns:
   ```
   ab          # Matches "ab"
   a.b         # Matches any three-character string starting with 'a' and ending with 'b'
   a|b         # Matches either "a" or "b"
   ab?         # Matches "a" or "ab"
   ```

2. Complex patterns:
   ```
   (a|b)c     # Matches "ac" or "bc"
   h.llo?     # Matches "hello" or "hallo"
   ```

## Implementation Details

### Components

1. **Scanner (Token.scala)**
   - Converts input strings into tokens
   - Handles basic characters, operators, and parentheses

2. **Parser (RDP.scala)**
   - Implements recursive descent parsing
   - Builds AST from tokens
   - Follows the formal grammar

3. **Pattern Matcher (Exp.scala)**
   - Implements pattern matching logic
   - Uses AST to match input strings

### Data Structures

The AST uses the following case classes:
```scala
trait Exp
case class C(value: Char) extends Exp
case class Concat(left: Exp, right: Exp) extends Exp
case class Optional(left: Exp) extends Exp
case class Alternation(left: Exp, right: Exp) extends Exp
```

## Testing

The project includes test cases for:
- Tokenization
- Parsing
- Pattern matching

Example test cases:
```scala
tokenize("ab") = List(Tok_Char('a'), Tok_Char('b'), Token.Tok_End)
parse("ab") = Concat(C('a'), C('b'))
matcher("ab", "ab") = true
```

## Error Handling

The implementation includes error handling for:
- Invalid characters in patterns
- Mismatched parentheses
- Malformed patterns
- Invalid token sequences
  
