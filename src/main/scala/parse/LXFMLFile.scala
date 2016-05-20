package parse
import utils.TransformationMatrix

import scala.xml.{Attribute, _}

class LXFMLFile(val filename: String) {
  println("open "+filename)
//  val source = <lol><bla t="1,2,3">1</bla><bla t="2,4,6">2</bla><fuj>fuj</fuj></lol>
  val source = XML.loadFile(filename)
}


object HelloWorld {
  def main(args: Array[String]): Unit = {

    def deepCopy(n: Node): Node = n match {
      case e: Elem if e.label=="Bone" =>
        val matrix = new TransformationMatrix((e \ "@transformation").text)
        e % Attribute(null, "transformation", matrix.reflectedMatrix, Null)
      case e: Elem if e.label =="RigidSystems" => e.copy(child = Seq.empty[Node])
      case e: Elem => e.copy(child = e.child.map(deepCopy(_)))
      case _ => n
    }

    val file = new LXFMLFile("in1.lxfml")
    println(deepCopy(file.source))

  }
}