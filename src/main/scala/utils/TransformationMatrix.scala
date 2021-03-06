package utils

class TransformationMatrix(val input: String, val registerPoints: RegisterPoints, val partNo: Int) {
  val matrix = input.split(",")
  val trans = matrix.slice(0, 9)
  val pos = matrix.slice(9, 12)
  val transDouble = trans.map(_.toDouble)
  val posDouble = pos.map(_.toDouble)

  val reflectionAxis = registerPoints.getSymmetryAxis(partNo)
  val toMiddle = registerPoints.mid(partNo)

  def reflectedMatrix: String = {
    val newTransString = newTransToString()

    val newTransDouble = newTransString.map(_.toDouble).toArray

    //keep register point in the same place
    val currentMidPos = Product(transDouble, posDouble, toMiddle)
    val wantedMidPos = currentMidPos
    wantedMidPos(0) *= -1
    val newMidPos = Product(newTransDouble, Array(0.0, 0.0, 0.0), toMiddle)

    val newPosDouble = Subtraction(wantedMidPos, newMidPos)
    val newPosString = newPosDouble.map(_.toString)

    (newTransString ++ newPosString).mkString(",")
  }

  def newTransToString(): List[String] = reflectionAxis match {
    case 0 => (List("", "-", "-", "-", "", "", "-", "", ""), trans).zipped.map(_ + _).map(removeDoubleMinus(_))
    case 2 => (List("-", "", "", "-", "", "", "", "-", "-"), trans).zipped.map(_ + _).map(removeDoubleMinus(_))
    case _ => List()
  }

  def removeDoubleMinus(s: String): String = if(s.startsWith("--")) s.substring(2) else s

  def Product(trans: Array[Double], pos: Array[Double], coords: Array[Double]): Array[Double] = {
    val x = trans(0)*coords(0) + trans(3)*coords(1) + trans(6)*coords(2) + pos(0)
    val y = trans(1)*coords(0) + trans(4)*coords(1) + trans(7)*coords(2) + pos(1)
    val z = trans(2)*coords(0) + trans(5)*coords(1) + trans(8)*coords(2) + pos(2)
    Array(x, y, z)
  }

  def Subtraction(a: Array[Double], b: Array[Double]) = Array(a(0)-b(0), a(1)-b(1), a(2)-b(2))
}
