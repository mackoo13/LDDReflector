package parse
import utils.{RegisterPoints, TransformationMatrix}

import scala.xml.{Attribute, _}

class LXFMLFile(val filename: String, val registerPoints: RegisterPoints) {
  var source: Node = XML.loadFile(filename)

  def parse(): Unit = {
    source = parseNode(source)
  }

  def parseNode(n: Node): Node = n match {
    case e: Elem if e.label=="Bone" =>
      val matrix = new TransformationMatrix((e \ "@transformation").text, registerPoints)
      e % Attribute(null, "transformation", matrix.reflectedMatrix, Null)
    case e: Elem if e.label =="RigidSystems" => e.copy(child = Seq.empty[Node])
    case e: Elem => e.copy(child = e.child.map(parseNode(_)))
    case _ => n
  }
}

object HelloWorld {
  def main(args: Array[String]): Unit = {

    val registerPoints = new RegisterPoints("registerPoints.csv")
    val file = new LXFMLFile("in1.lxfml", registerPoints)
    file.parse()
    XML.save("out1.lxfml", file.source)
    println(file.source)

  }
}