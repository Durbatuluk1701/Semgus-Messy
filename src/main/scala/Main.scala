import org.rogach.scallop._

import java.io.InputStream
import lang.Semgus.SemgusFile
import utils.filterNones

object Main {
  private class MainConf(args: Seq[String]) extends ScallopConf(args) {
    val infile = opt[String](required = true)
    val outfile = opt[String](default = Some("out.z3"))
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
    val ofName = conf.outfile()
    println(s"Translating $ifName to an SMT file")

    val ss = semgusJava.JSON2Semgus2Run(ifName)

    ss match {
      case None => 
        println(s"This specification is unrealizable!")
      case Some(value) => 
        utils.write2File(ofName)(value.mkString("\n"))
        println(s"Realizable spec with $ofName grammar")
    }
  }
}
