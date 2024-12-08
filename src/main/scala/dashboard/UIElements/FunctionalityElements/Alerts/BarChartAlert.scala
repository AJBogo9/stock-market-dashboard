package dashboard.UIElements.FunctionalityElements.Alerts

import dashboard.UI.stage
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Pos
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, ChoiceBox, ColorPicker}
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color


object BarChartAlert:
  
  def getBarChartAlert =
    val stockChoises = ObservableBuffer("Apple", "Nvidia", "Microsoft")
    
    val companyChoiceBox = new ChoiceBox[String]:
      items = stockChoises
      value = stockChoises.head

    val colorPicker = new ColorPicker(Color.White)

    val alertContent = new VBox:
      children = Array(companyChoiceBox, colorPicker)
      alignment = Pos.Center

    // Create and display the alert box with the choice box
    var alert = new Alert(AlertType.Confirmation):
      initOwner(stage)
      title = "Bar Chart customization"
      headerText = "Choose company"
      // Set the custom content to be the choice box
      dialogPane().setContent(alertContent)

    (alert, companyChoiceBox, colorPicker)