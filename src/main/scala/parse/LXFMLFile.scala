package parse
import utils.TransformationMatrix

import scala.xml.{Attribute, _}
import scala.xml.transform.{RewriteRule, RuleTransformer}

class LXFMLFile(val filename: String) {
  println("open "+filename)
//  val source = <lol><bla t="1,2,3">1</bla><bla t="2,4,6">2</bla><fuj>fuj</fuj></lol>
  val source = XML.loadFile(filename)
}


object HelloWorld {
  def main(args: Array[String]): Unit = {

    def bla(n: NodeSeq): NodeSeq = n match {
      case e: Elem => {
        val matrix = new TransformationMatrix((e \ "@transformation").text)
        e % Attribute(null, "transformation", matrix.reflectedMatrix, Null)
      }
      case _ => n
    }

    val file = new LXFMLFile("in1.lxfml")

    val parts = file.source \\ "Bone"
    println(parts)
    println(parts.map(bla(_)).mkString("\n"))

  }
}