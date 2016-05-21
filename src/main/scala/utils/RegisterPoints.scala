package utils

class RegisterPoints(val filename: String) {

  val source = io.Source.fromFile(filename)
  var data = Map[Int, Array[Double]]()

  for (line <- source.getLines) {
    val cols = line.split(",").map(_.trim)
    data += (cols(0).toInt -> cols.slice(1,4).map(_.toDouble))
  }

  source.close
  println(data)


  def mid(partNo: Int): Array[Double] = data(partNo)
}
