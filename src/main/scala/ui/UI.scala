package ui

import javax.swing.filechooser.FileNameExtensionFilter
import java.io.{File, IOException}

import parse.LXFMLFile
import utils.RegisterPoints

import scala.swing._
import scala.swing.FileChooser.Result
import scala.xml.XML

class UI extends MainFrame {

//  var startDir: File = new FileChooser().selectedFile
  var startDir: File = new File("C:/Users/Maciek/lddr/LDDReflector/in1.lxfml")

  //GUI PROPERTIES
  preferredSize = new Dimension(800, 600)
  resizable = false
  title = "LDD Reflector"


  def loadFile() = try {
    val chooser = new FileChooser(startDir)
    chooser.title_=("Select image")
    chooser.fileFilter_=(new FileNameExtensionFilter("LXFML files", "lxfml"))
    val result: Result.Value = chooser.showDialog(null, "Load")
    if (result == Result.Approve) {
      val filePath = chooser.selectedFile.toString
      println(filePath)
      startDir = chooser.selectedFile.getParentFile
    }
  } catch {
    case e: IOException => Dialog.showMessage(null, "An error occured when trying to open the file.", title="Loading error")
    case e: IllegalStateException => Dialog.showMessage(null, e.getMessage, title="Loading error")
    case e: IllegalArgumentException => Dialog.showMessage(null, e.getMessage, title="Loading error")
  }


  //GUI LAYOUT
  contents = new GridBagPanel {
    def constraints(x: Int, y: Int,
                    gridwidth: Int = 1, gridheight: Int = 1,
                    weightx: Double = 0.0, weighty: Double = 0.0,
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

    add(Button("Load file") {loadFile()},constraints(0, 0, fill=GridBagPanel.Fill.Horizontal))
  }

}

object PietInterpreter {
  def main(args: Array[String]) {
    val ui = new UI
    ui.centerOnScreen()
    ui.visible = true

    val registerPoints = new RegisterPoints("registerPoints.csv")
    val file = new LXFMLFile("sym5_slopes.lxfml", registerPoints)
    //    file.parseReflectible()
    file.moveAllPartsToZero()
    XML.save("symout5_slopes.lxfml", file.source)
    //    XML.save("sym1_bricks.lxfml", file.outReflected)
    //    XML.save("sym1_bricks.lxfml", file.outRemaining)
    println(file.source)
  }
}