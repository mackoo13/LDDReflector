package utils

import java.io.PrintWriter
import java.net.{MalformedURLException, UnknownHostException}
import java.io.File


class RegisterPoints(val filename: String) {

  var data = Map[Int, Array[Double]]()
  if(!updateData()) loadData()

  def updateData(): Boolean = {
    val urlFilename = "registerPointsUpdateURL.txt"
    try {
      val url = io.Source.fromFile(urlFilename).mkString
      println("Loading file from "+url)
      val source = io.Source.fromURL(url)
      for (line <- source.getLines) {
        val cols = line.split(",").map(_.trim)
        data += (cols(0).toInt -> cols.slice(1,4).map(_.toDouble))
      }
      source.close
      saveData(source.mkString)
      true
    } catch {
      case e: MalformedURLException => println("Wrong URL format in file "+urlFilename); false
      case e: UnknownHostException => println("Unable to connect to github.com"); false
    }
  }

  def loadData() = {
    println("Loading file from "+filename)
    val source = io.Source.fromFile(filename)
    for (line <- source.getLines) {
      val cols = line.split(",").map(_.trim)
      data += (cols(0).toInt -> cols.slice(1,4).map(_.toDouble))
    }
    source.close
  }

  def saveData(content: String) = {
    val writer = new PrintWriter(new File(filename))
    try writer.write(content)
    finally writer.close()
  }

  def mid(partNo: Int): Array[Double] = data(partNo)

  def contains(partNo: Int): Boolean = data.contains(partNo)

}
