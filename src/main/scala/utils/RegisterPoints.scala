package utils

import sys.process._
import java.net.URL
import java.io.File

class RegisterPoints(val filename: String) {

  try update("http://google.com", filename)
  catch {
    case e: Throwable => println(e.getMessage)
  }

  val source = io.Source.fromFile(filename)
  var data = Map[Int, Array[Double]]()

  for (line <- source.getLines) {
    val cols = line.split(",").map(_.trim)
    data += (cols(0).toInt -> cols.slice(1,4).map(_.toDouble))
  }

  source.close

  def mid(partNo: Int): Array[Double] = data(partNo)

  def contains(partNo: Int): Boolean = data.contains(partNo)

  def update(url: String, filename: String) = {
    println(67)
    new URL(url) #> new File(filename)
  }
}
