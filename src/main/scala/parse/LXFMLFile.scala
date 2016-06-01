package parse
import utils.{RegisterPoints, TransformationMatrix}

import scala.xml.{Attribute, _}

@throws(classOf[SAXParseException])
class LXFMLFile(val filename: String, val registerPoints: RegisterPoints) {
  var outReflected: Node = null
  var outRemaining: Node = null
  var reflectedParts = 0
  var remainingParts = 0
  var partCounter = 0
  var source: Node = XML.loadFile(filename)


  def parse(): Unit = {
    outReflected = parseNode(source, transformReflectible)
    outRemaining = parseNode(source, removeReflectible)
    reflectedParts = countBricks(outReflected)
    remainingParts = countBricks(outRemaining)
  }

  def parseNode(n: Node, fun: Seq[Node]=>Seq[Node]): Node = n match {
    case e: Elem if e.label=="Bricks" => e.copy(child = fun(e.child))
    case e: Elem if e.label =="RigidSystems" => e.copy(child = Seq.empty[Node])
    case e: Elem => e.copy(child = e.child.map(parseNode(_, fun)))
    case _ => n
  }

  def transformReflectible(child: Seq[Node]): Seq[Node] = child.filter(isReflectible(_)).map(transformBrick(_))

  def removeReflectible(child: Seq[Node]): Seq[Node] = child.filter(!isReflectible(_))

  def countBricks(n: Node): Integer = (n \\ "Brick").length

  def isReflectible(n: Node): Boolean = n match {
    case e: Elem if e.label=="Brick" => registerPoints.contains((e\"@designID").text.toInt)
    case _ => false
  }

  def transformBrick(n: Node): Node = n match {
    case e: Elem if e.label=="Brick" =>
      val partNo = (e \ "@designID").text
      val symmetricalPartNo = registerPoints.getSymmetricalPart(partNo.toInt).toString
      e.copy(child = e.child.map(transformBrick(_))) % Attribute(null, "designID", symmetricalPartNo, Null)
    case e: Elem if e.label=="Part" =>
      val partNo = (e \ "@designID").text
      val symmetricalPartNo = registerPoints.getSymmetricalPart(partNo.toInt).toString
      e.copy(child = e.child.map(transformBone(_, partNo.toInt))) % Attribute(null, "designID", symmetricalPartNo, Null)
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