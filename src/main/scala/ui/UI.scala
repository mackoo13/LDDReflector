package ui

import javax.swing.filechooser.FileNameExtensionFilter
import java.io.{File, IOException}

import parse.LXFMLFile
import utils.RegisterPoints

import scala.swing._
import scala.swing.FileChooser.Result
import scala.xml._

class UI extends MainFrame {

  val labelOut = new Label("<html></html>") {}
  labelOut.horizontalAlignment = Alignment.Left
  labelOut.verticalAlignment = Alignment.Top

  val labelFooter = new Label("Toltomeja 2016, Brickware Licence") {minimumSize=new Dimension(650, 80)}

  val logger = new Logger(labelOut)
  var startDir: File = new File("C:/Users/Maciek/lddr/LDDReflector/in1.lxfml")
  val registerPoints = new RegisterPoints("registerPoints.csv", logger)

  //GUI PROPERTIES
  preferredSize = new Dimension(800, 600)
  resizable = false
  title = "LDD Reflector"

  logger.printInfo("Welcome to LDDReflector!")

  //GUI LAYOUT
  contents = new GridBagPanel {
    def constraints(x: Int, y: Int,
                    gridwidth: Int = 1, gridheight: Int = 1,
                    weightx: Double = 1.0, weighty: Double = 1.0,
                    fill: GridBagPanel.Fill.Value = GridBagPanel.Fill.None)
    :Constraints = {
      val c = new Constraints
      c.gridx = x
      c.gridy = y
      c.gridwidth = gridwidth
      c.gridheight = gridheight
      c.weightx = weightx
      c.weighty = weighty
      c.fill = fill
      c
    }

    add(Button("Load file") {loadFile()}, constraints(0, 0, gridwidth=3, fill=GridBagPanel.Fill.Both))
    add(new ScrollPane(labelOut) {val dim=new Dimension(650, 460); preferredSize=dim; maximumSize=dim; minimumSize=dim},
      constraints(0, 1, gridwidth=3, fill=GridBagPanel.Fill.Both))
    add(labelFooter, constraints(0, 2, fill=GridBagPanel.Fill.Both, weightx=0.5))
    add(Button("Flickr") {goToURL("http://www.flickr.com")}, constraints(1, 2, weightx=0.25))
    add(Button("Github") {goToURL("http://www.github.com")}, constraints(2, 2, weightx=0.25))
  }


  def loadFile() = try {
    val chooser = new FileChooser(startDir)
    chooser.title = "Select file"
    chooser.fileFilter = new FileNameExtensionFilter("LXFML files", "lxfml")
    val result: Result.Value = chooser.showDialog(null, "Load")
    logger.printInfo("")
    logger.printInfo("Loading file...")
    if (result == Result.Approve) {
      val filePath = chooser.selectedFile.toString
      logger.printInfo("File loaded: "+filePath)
      startDir = chooser.selectedFile.getParentFile

      transformFile(filePath)
//      moveAllToZeroInFile(filePath)
    }
  } catch {
    //TODO nazwy ze spacjami
    case e: IOException => Dialog.showMessage(null, "An error occured when trying to open the file. "+e.getMessage, title="Loading error")
    case e: IllegalStateException => Dialog.showMessage(null, e.getMessage, title="Loading error")
    case e: IllegalArgumentException => Dialog.showMessage(null, e.getMessage, title="Loading error")
    case e: SAXParseException => Dialog.showMessage(null, e.getMessage, title="SAXParseException")
    case e: Throwable => println("shit happens "+e.getMessage)
  }


  def transformFile(filePath: String) = {
    val file = new LXFMLFile(filePath, registerPoints)
    file.parse()
    XML.save(filePath.dropRight(6)+"_reflected.lxfml", file.outReflected)
    XML.save(filePath.dropRight(6)+"_remaining.lxfml", file.outRemaining)
    logger.printInfo("Finished reflecting")
    logger.printInfo(file.reflectedParts+" bricks reflected")
    logger.printInfo(file.remainingParts+" bricks remaining")
  }


  def moveAllToZeroInFile(filePath: String) = {
    val file = new LXFMLFile(filePath, registerPoints)
    file.moveAllPartsToZero()
    XML.save(filePath.dropRight(6)+"_zero.lxfml", file.source)
  }

  def goToURL(url: String) = {
    println(url)
  }


}

object LDDReflector {
  def main(args: Array[String]) {
    val ui = new UI
    ui.centerOnScreen()
    ui.visible = true

  }
}