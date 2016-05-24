package parse
import utils.{RegisterPoints, TransformationMatrix}

import scala.xml.{Attribute, _}

class LXFMLFile(val filename: String, val registerPoints: RegisterPoints) {
  var outReflected: Node = null
  var outRemaining: Node = null
  var source: Node = XML.loadFile(filename)
  var partCounter = 0

  def parseReflectible(): Unit = {
    outReflected = parseNode(source, transformPart)
  }

  def parseRemaining(): Unit = {
    outRemaining = parseNode(source, removePart)
  }

  def parseNode(n: Node, fun: Node=>Node): Node = n match {
    case e: Elem if e.label=="Part" => fun(e)
    case e: Elem if e.label =="RigidSystems" => e.copy(child = Seq.empty[Node])
    case e: Elem => e.copy(child = e.child.map(parseNode(_, fun)))
    case _ => n
  }

  //remove the parts that are not symmetrical
  def removePart(n: Node): Node = n match {
    case e: Elem =>
      val partNo = (e\"@designID").text.toInt
      if(registerPoints.contains(partNo)) {
        e.copy(child = e.child.map(transformBone(_, partNo)))
      } else e.copy(child = Seq.empty[Node])
    case _ => n
  }

  def transformPart(n: Node): Node = n match {
    case e: Elem =>
      val partNo = (e\"@designID").text.toInt
      if(registerPoints.contains(partNo)) {
        e.copy(child = e.child.map(transformBone(_, partNo)))
      } else e.copy(child = Seq.empty[Node])
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
      e % Attribute(null, "transformation", "1,0,0,0,1,0,0,0,1,0,"+5*partCounter+",0", Null)
    case e: Elem if e.label =="RigidSystems" => e.copy(child = Seq.empty[Node])
    case e: Elem => e.copy(child = e.child.map(moveToZero(_)))
    case _ => n
  }
}