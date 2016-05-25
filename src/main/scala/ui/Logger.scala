package ui
import scala.swing.Label

class Logger(val label: Label) {

  var content = List[String]()

  def printInfo(info: String) = {
    content ::= info
    label.text = "<html>$ %s</html>".format(content.mkString("<br>$ "))
  }

}
