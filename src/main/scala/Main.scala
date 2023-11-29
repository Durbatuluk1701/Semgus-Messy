import org.rogach.scallop._

import java.io.InputStream
import lang.Semgus.SemgusFile
import utils.filterNones

object Main {
  private class MainConf(args: Seq[String]) extends ScallopConf(args) {
    val infile = opt[String](required = true)
    val outfile = opt[String](default = Some("out.z3"))
    val nt = opt[Boolean]("nt", required = false)
    val prod = opt[Boolean]("prod", required = false)
  
    validate(nt, prod) {
      case (ntValue, prodValue) if ntValue && prodValue => Left("Both '--nt' and '--prod' options cannot be specified together.")
      case (ntValue, prodValue) if !(ntValue || prodValue) => Left("Either '--nt' or '--prod' must be specified.")
      case _ => Right(())
    }
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

    val ss = if (conf.nt()) semgusJava.NT_Mode_JSON2Semgus2Run(ifName) else semgusJava.Prod_Mode_JSON2Semgus2Run(ifName)

    ss match {
      case None => 
        println(s"This specification is unrealizable!")
      case Some(value) => 
        utils.write2File(ofName)(value.mkString("\n"))
        println(s"Realizable spec with $ofName grammar")
    }
  }
}
