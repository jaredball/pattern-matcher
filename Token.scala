enum Token:
  case Tok_Char(value: Char)  // Regular character or wildcard
  case Tok_OR                 // | operator
  case Tok_Q                  // ? operator
  case Tok_LPAREN            // Left parenthesis
  case Tok_RPAREN            // Right parenthesis
  case Tok_End               // End of input

def tokenize(inputString: String): List[Token] = {
  
  // Converts input string into tokens, accumulating them in reverse order
  def processChars(remainingChars: List[Char], tokenList: List[Token]): List[Token] = {
    remainingChars match {
      case Nil => tokenList.reverse :+ Token.Tok_End
      case '|' :: restChars => processChars(restChars, Token.Tok_OR :: tokenList)
      case '?' :: restChars => processChars(restChars, Token.Tok_Q :: tokenList)
      case '(' :: restChars => processChars(restChars, Token.Tok_LPAREN :: tokenList)
      case ')' :: restChars => processChars(restChars, Token.Tok_RPAREN :: tokenList)
      // Handle letters, digits, dots, and spaces as regular characters
      case currentChar :: restChars if currentChar.isLetterOrDigit || currentChar == '.' || currentChar == ' ' =>
        processChars(restChars, Token.Tok_Char(currentChar) :: tokenList)
      case invalidChar :: _ => throw new Exception(s"Invalid character: $invalidChar")
    }
  }
  
  // Convert to a list of characters and pass an empty token list
  processChars(inputString.toList, List())
}
