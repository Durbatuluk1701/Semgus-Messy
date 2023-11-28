import lang.SMT._

import sys.process._
import scala.language.postfixOps

package object utils {

  def filterNones[A](l: List[Option[A]]): List[A] = l.filter{case None => false; case Some(_) => true}.map{_.get}

  def printMagenta(s: String): Unit = println(Console.MAGENTA + s + Console.RESET)
  def printCyan(s: String): Unit = println(Console.CYAN + s + Console.RESET)
  def printYellow(s: String): Unit = println(Console.YELLOW + s + Console.RESET)
  def printGreen(s: String): Unit = println(Console.GREEN + s + Console.RESET)
  def printRed(s: String): Unit = println(Console.RED + s + Console.RESET)

  def write2File(fname: String)(content: String): Unit = {
    val f = new java.io.File(fname)
    val bw = new java.io.BufferedWriter(new java.io.FileWriter(f))
    bw.write(content)
    bw.close()
  }

  def checkSat(constraints: List[SMTCommand]): Unit = {
    val ret = (s"echo ${constraints.mkString("\n")}" #| "z3 -in").lazyLines_!
    ret.last match {
      case "sat" => println("found sat")
      case "unknown" => println("found potential")
      case "unsat" => println("found unsat")
      case str => println(s"unexpected result from z3: $str")
    }
  }
}
