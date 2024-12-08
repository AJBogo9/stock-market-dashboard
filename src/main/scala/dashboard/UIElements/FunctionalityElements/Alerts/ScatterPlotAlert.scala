package dashboard.UIElements.FunctionalityElements.Alerts


import dashboard.UI.stage
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Pos
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, ChoiceBox}
import scalafx.scene.layout.VBox

object ScatterPlotAlert:
  private val stockChoises = ObservableBuffer("Apple", "Nvidia", "Microsoft")
  private val years = ObservableBuffer.from((2000 to 2024).reverse)
  def getScatterPlotAlert =
    val companyChoiceBox1 = new ChoiceBox[String]:
      items = stockChoises
      value = stockChoises.head

    val companyChoiceBox2 = new ChoiceBox[String]:
      items = stockChoises
      value = stockChoises.last

    val yearChoiceBox = new ChoiceBox[Int]:
      items = years
      value = years.head

    val alertContent = new VBox:
      children = Array(companyChoiceBox1, companyChoiceBox2, yearChoiceBox)
      alignment = Pos.Center
    
    var alert = new Alert(AlertType.Confirmation):
      initOwner(stage)
      title = "Scatter plot customization"
      headerText = "Choose company and year"
      dialogPane().setContent(alertContent)


    (alert, companyChoiceBox1, companyChoiceBox2, yearChoiceBox)
