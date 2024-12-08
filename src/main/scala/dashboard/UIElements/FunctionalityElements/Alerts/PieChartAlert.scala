package dashboard.UIElements.FunctionalityElements.Alerts

import dashboard.UI.stage
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Pos
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, ChoiceBox}
import scalafx.scene.layout.VBox



object PieChartAlert:
  
  private val portfolioChoises = ObservableBuffer("Portfolio1")
  
  def getPieChartAlert =

    val portfolioCoiceBox = new ChoiceBox[String]:
      items = portfolioChoises
      value = portfolioChoises.head

    val alertContent = new VBox:
      children = Array(portfolioCoiceBox)
      alignment = Pos.Center
    
    var alert = new Alert(AlertType.Confirmation):
      initOwner(stage)
      title = "Pie chart customization"
      headerText = "Choose portfolio"
      dialogPane().setContent(alertContent)

    (alert, portfolioCoiceBox)
