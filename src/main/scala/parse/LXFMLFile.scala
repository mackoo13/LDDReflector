package parse
import utils.{RegisterPoints, TransformationMatrix}

import scala.xml.{Attribute, _}

class LXFMLFile(val filename: String, val registerPoints: RegisterPoints) {
  var source: Node = XML.loadFile(filename)
  var partCounter = 0

  def parse(): Unit = {
    source = parseNode(source)
  }

  def parseNode(n: Node): Node = n match {
    case e: Elem if e.label=="Part" =>
      val partNo = 4
      if(registerPoints.contains(partNo)) {
        e.copy(child = e.child.map(transformBone(_, (e\"@designID").text.toInt)))
      } else e.copy(child = Seq.empty[Node])
    case e: Elem if e.label =="RigidSystems" => e.copy(child = Seq.empty[Node])
    case e: Elem => e.copy(child = e.child.map(parseNode(_)))
    case _ => n
  }

  def transformBone(n: Node, partNo: Int): Node = n match {
    case e: Elem =>
      val matrix = new TransformationMatrix((e \ "@transformation").text, registerPoints, partNo)
      e % Attribute(null, "transformation", matrix.reflectedMatrix, Null)
    case _ => n
  }

  //only for purpose of checking the register points
  def moveAllPartsToZero() = {
    source = moveToZero(source)
  }

  def moveToZero(n: Node): Node = n match {
    case e: Elem if e.label=="Bone" =>
      partCounter += 1
      e % Attribute(null, "transformation", "1,0,0,0,1,0,0,0,1,0,"+partCounter+",0", Null)
    case e: Elem if e.label =="RigidSystems" => e.copy(child = Seq.empty[Node])
    case e: Elem => e.copy(child = e.child.map(moveToZero(_)))
    case _ => n
  }
}

object HelloWorld {
  def main(args: Array[String]): Unit = {

    val registerPoints = new RegisterPoints("registerPoints.csv")
    val file = new LXFMLFile("in1.lxfml", registerPoints)
    file.parse()
//    file.moveAllPartsToZero()
    XML.save("out1.lxfml", file.source)
    println(file.source)

  }
}