@main
def main(): Unit = {

  // Read pattern
  val pattern = scala.io.StdIn.readLine("pattern? ")
  val compiledPattern = RDP.parse(pattern)

  // Loop - Read strings and check matches
  while (true) {
    val input = scala.io.StdIn.readLine("string? ")
    if (input == "exit") {
      println("Exiting...")
      return
    }
    val result = Exp.matcher(compiledPattern, input)
    println(if (result) "match" else "no match")
  }

}
