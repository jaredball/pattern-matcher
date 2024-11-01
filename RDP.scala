import Token.*

object RDP {

  // Main parse function - converts string to Expression tree
  def parse(s: String): Exp = {
    val tokens = tokenize(s)
    val (exp, remainingTokens) = parse_E(tokens)
    remainingTokens match {
      case Tok_End :: Nil => exp
      case _ => throw new Exception("Unexpected tokens at end of input")
    }
  }

  // Grammar Rules:

  // E → T ('|' E)?     (Expression)
  private def parse_E(tokens: List[Token]): (Exp, List[Token]) = {
    val (left, remainingAfterT) = parse_T(tokens)
    remainingAfterT match {
      case Tok_OR :: rest =>
        val (right, remainingAfterE) = parse_E(rest)
        (Alternation(left, right), remainingAfterE)
      case _ => (left, remainingAfterT)
    }
  }

  // T → F T'          (Term)
  private def parse_T(tokens: List[Token]): (Exp, List[Token]) = {
    val (first, remainingAfterF) = parse_F(tokens)
    remainingAfterF match {
      case next :: _ if isStartOfFactor(next) =>
        val (second, remainingAfterT) = parse_T(remainingAfterF)
        (Concat(first, second), remainingAfterT)
      case _ => (first, remainingAfterF)
    }
  }

  // F → Char | '(' E ')' | E '?'    (Factor)
  private def parse_F(tokens: List[Token]): (Exp, List[Token]) = tokens match {
    case Tok_Char(c) :: Tok_Q :: rest =>
      (Optional(C(c)), rest)
    case Tok_Char(c) :: rest =>
      (C(c), rest)
    case Tok_LPAREN :: rest =>
      val (exp, afterExp) = parse_E(rest)
      afterExp match {
        case Tok_RPAREN :: Tok_Q :: remaining =>
          (Optional(exp), remaining)
        case Tok_RPAREN :: remaining =>
          (exp, remaining)
        case _ => throw new Exception("Expected closing parenthesis")
      }
    case _ => throw new Exception("Expected character or opening parenthesis")
  }

  // Check if token can start a factor
  private def isStartOfFactor(token: Token): Boolean = token match {
    case Tok_Char(_) | Tok_LPAREN => true
    case _ => false
  }

  // Functions for token handling
  def lookahead(l: List[Token]): Token = l match {
    case Nil => throw new Exception("No tokens available")
    case head :: _ => head
  }

  def matchToken(tok: Token, tokList: List[Token]): List[Token] = tokList match {
    case head :: tail if tok == head => tail
    case _ => throw new Exception(s"Expected $tok but found $tokList")
  }

}
