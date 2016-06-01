package utils

import java.io.{File, FileNotFoundException, PrintWriter}
import java.net.{MalformedURLException, UnknownHostException}
import ui.Logger
import scala.io.BufferedSource


class RegisterPoints(val filename: String, val logger: Logger, val urlFilename: String = "registerPointsUpdateURL.txt") {

  private var data = Map[Int, Array[Double]]()
  private var symmetryAxes = Map[Int, Int]()
  private var symmetricalParts = Map[Int, Int]()
  //if(!updateData()) loadData()
  loadData()

  def updateData(): Boolean = {
    var source: BufferedSource = null
    try {
      val url = io.Source.fromFile(urlFilename).mkString
      logger.printInfo("Loading file from "+url)
      source = io.Source.fromURL(url)
      for (line <- source.getLines) {
        val cols = line.split(",").map(_.trim)
        val partNo = cols(0).toInt
        data += (partNo -> cols.slice(1,4).map(_.toDouble))
        symmetryAxes += (partNo -> cols(4).toInt)
        symmetricalParts += (partNo -> (if(cols.length>=6) cols(5).toInt else partNo))
      }
      saveData(source.mkString)
      true
    } catch {
      case e: MalformedURLException => logger.printInfo("Wrong URL format in file "+urlFilename); false
      case e: FileNotFoundException => logger.printInfo("File "+urlFilename+" not found"); false
      case e: UnknownHostException => logger.printInfo("Unable to connect to github.com"); false
    } finally if(source!=null) source.close
  }

  def loadData() = {
    logger.printInfo("Loading file: "+filename)
    var source: BufferedSource = null
    try {
      source = io.Source.fromFile(filename)
      for (line <- source.getLines) {
        val cols = line.split(",").map(_.trim)
        val partNo = cols(0).toInt
        data += (partNo -> cols.slice(1,4).map(_.toDouble))
        symmetryAxes += (partNo -> cols(4).toInt)
        symmetricalParts += (partNo -> (if(cols.length>=6) cols(5).toInt else partNo))
      }
    } catch {
      case e: FileNotFoundException => logger.printInfo("File "+filename+" not found"); false
    } finally if(source!=null) source.close
  }

  def saveData(content: String) = {
    logger.printInfo("Saving new list to "+filename)
    val writer = new PrintWriter(new File(filename))
    try writer.write(content)
    finally writer.close()
  }

  def mid(partNo: Int): Array[Double] = data(partNo)

  def getSymmetryAxis(partNo: Int) = symmetryAxes(partNo)

  def getSymmetricalPart(partNo: Int) = symmetricalParts(partNo)

  def contains(partNo: Int): Boolean = data.contains(partNo)

}
