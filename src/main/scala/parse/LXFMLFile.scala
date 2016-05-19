package parse
import utils.TransformationMatrix

import scala.xml.{Elem, Node, NodeSeq}
import scala.xml.transform.{RewriteRule, RuleTransformer}

class LXFMLFile(val filename: String) {
  println("open "+filename)
  val source = <lol><bla t="1,2,3">1</bla><bla t="2,4,6">2</bla><fuj>fuj</fuj></lol>
}


object HelloWorld {
  def main(args: Array[String]): Unit = {

    val file = new LXFMLFile("r1.lxfml")
    println(file.source)
    println(file.source.text)
    println(file.source \ "bla")
    println((file.source \ "bla").map(_.text).mkString("+"))
    println((file.source \ "bla").map(_ \ "@t").mkString("+"))
    println((file.source \ "bla").map(_ \ "@t").map(_.text.split(",").mkString("_")).mkString("="))

    val removeBone = new RewriteRule {
      override def transform(n: Node): NodeSeq = n match {
//        case e: Elem if (e \ "@t").text == "1,2,3" => NodeSeq.Empty
        case e: Elem if e.label == "fuj" => NodeSeq.Empty
        case _ => n
      }
    }
    println(new RuleTransformer(removeBone).transform(file.source))

    //----------------------

    val matrix = new TransformationMatrix("1,0,0,0,1,0,0,0,1,3,5,8")
    matrix.reflectedMatrix


  }
}