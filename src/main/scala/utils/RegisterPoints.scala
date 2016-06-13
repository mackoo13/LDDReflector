package utils

import java.io.{File, FileNotFoundException, PrintWriter}
import java.net.{MalformedURLException, UnknownHostException}
import ui.Logger
import scala.io.BufferedSource


class RegisterPoints(val filename: String, val logger: Logger, val urlFilename: String = "registerPointsUpdateURL.txt") {

  private var coords = Map[Int, Array[Double]]()
  private var symmetryAxis = Map[Int, Int]()
  private var symmetricalPart = Map[Int, Int]()
  updateData()
  loadData()

  def updateData(): Boolean = {
    var source: BufferedSource = null
    try {
      val url = io.Source.fromFile(urlFilename).mkString
      logger.printInfo("Loading file from "+url)
      source = io.Source.fromURL(url)
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
        coords += (partNo -> cols.slice(1,4).map(_.toDouble))
        symmetryAxis += (partNo -> cols(4).toInt)
        symmetricalPart += (partNo -> (if(cols(5)!="0") cols(5).toInt else partNo))
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

  def mid(partNo: Int): Array[Double] = coords(partNo)

  def getSymmetryAxis(partNo: Int) = symmetryAxis(partNo)

  def getSymmetricalPart(partNo: Int) = symmetricalPart(partNo)

  def contains(partNo: Int): Boolean = coords.contains(partNo)

}
