import org.rogach.scallop._

import java.io.InputStream
import lang.Semgus.SemgusFile
import utils.filterNones

object Main {
  private class MainConf(args: Seq[String]) extends ScallopConf(args) {
    val infile = opt[String](required = true)
    verify()
  }

  def getStringfromStream(s: InputStream): String = {
    val barray: Array[Byte] = Array.fill[Byte](1024)(0)
    s.read(barray);
    barray.map(_.toChar).mkString
  }

  def main(args: Array[String]): Unit = {
    val conf = new MainConf(args.toIndexedSeq)
    val ifName = conf.infile()
    println(s"Translating $ifName to an SMT file")

    val ss: List[SemgusFile] = semgusJava.JSON2Semgus(ifName)
    val smts = ss.map((s) => genConstraints.genBasic.semgus2SMT(s))

    var counters = 0
    smts.foreach((smt) => {
      counters += 1
      smt match {
        case Some(value) => utils.write2File(s"out_$counters.z3")(value.mkString("\n"))
        case None => 
      }
    })

    println(s"Translation complete... run a CHC solver on '*.z3' files to solve problem")
  }
}
