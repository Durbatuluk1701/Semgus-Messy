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

  /** Sort of confusing (maybe poor choice)
   *  Some(true)  == SAT
   *  Some(false) == UNSAT
   *  None        == Unknown
  */
  def checkSat(constraints: List[lang.SMT.SMTCommand]): Option[Boolean] = {
    val osName = System.getProperty("os.name").toLowerCase()
    var ret: Option[LazyList[String]] = None

    if (osName.contains("windows")) {
      ret = Some((s"powershell -Command \"echo '${constraints.mkString("\n")}' | z3 -T:60 -in\"").lazyLines_!)
      // ret = Some((s"Write-Output ${constraints.mkString("\n")}" #| "z3 -in").lazyLines_!)
    } else if (osName.contains("linux")) {
      ret = Some((s"echo ${constraints.mkString("\n")}" #| "z3 -T:60 -in").lazyLines_!)
    } else {
      throw new Exception("Running on an unsupported operating system")
    } 

    ret match {
      case None => throw new Exception("This should be impossible, we will always have either exited or turned some")
      case Some(value) => { 
        if (value.nonEmpty) {
          value.last match {
            case "sat" => Some(true)
              // println("found sat")
            case "timeout" => None
              // Treat timeout like unknwon == SAT maybe
            case "unknown" => None
              // println("found potential")
            case "unsat" => Some(false)
              // println("found unsat")
            case str => 
              throw new Exception(s"unexpected result from z3: $str")
          }
        } else {
          throw new Exception("Critical Error, Z3 ended without any output")
        }
      }
    }
  }
}
