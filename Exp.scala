trait Exp

// Cases: single character, concatenation, optional (?), and alternation (|)
case class C(value:Char) extends Exp
case class Concat(left: Exp, right: Exp) extends Exp
case class Optional(left: Exp) extends Exp
case class Alternation(left: Exp, right: Exp) extends Exp

object Exp {
  def matcher(pattern: Exp, s: String): Boolean = {
    
    // Returns a list of the strings that are left over after a successful match
    def matchAt(exp: Exp, str: String): List[String] = exp match {
      
      // Match single character (including '.')
      case C(char) =>
        if (str.nonEmpty && (char == '.' || str.head == char))
          List(str.tail)
        else
          List()

      // Match concatenated patterns
      case Concat(left, right) =>
        for {
          leftMatch <- matchAt(left, str)
          rightMatch <- matchAt(right, leftMatch)
        } yield rightMatch

      // Match optional pattern
      case Optional(inner) =>
        str :: matchAt(inner, str)

      // Match either left or right pattern
      case Alternation(left, right) =>
        matchAt(left, str) ++ matchAt(right, str)
    }

    // Pattern matches if empty string is in results (complete match)
    matchAt(pattern, s).contains("")
  }
}
