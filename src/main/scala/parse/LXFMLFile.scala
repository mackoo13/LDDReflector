package parse
import utils.TransformationMatrix

import scala.xml.{Attribute, _}

class LXFMLFile(val filename: String) {
  var source: Node = XML.loadFile(filename)

  def parse(): Unit = {
    source = parseNode(source)
  }

  def parseNode(n: Node): Node = n match {
    case e: Elem if e.label=="Bone" =>
      val matrix = new TransformationMatrix((e \ "@transformation").text)
      e % Attribute(null, "transformation", matrix.reflectedMatrix, Null)
    case e: Elem if e.label =="RigidSystems" => e.copy(child = Seq.empty[Node])
    case e: Elem => e.copy(child = e.child.map(parseNode(_)))
    case _ => n
  }
}

object HelloWorld {
  def main(args: Array[String]): Unit = {

    val file = new LXFMLFile("in1.lxfml")
    file.parse()
    XML.save("out1.lxfml", file.source)
    println(file.source)

  }
}