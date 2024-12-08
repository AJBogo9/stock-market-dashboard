package dashboard.UIElements.FunctionalityElements.Alerts

import dashboard.UI.stage
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Pos
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, ChoiceBox, ColorPicker}
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color


object XYChartAlert:

  private val stockChoises = ObservableBuffer("Apple", "Nvidia", "Microsoft")

  def getXYChartAlert =

    val companyChoiceBox = new ChoiceBox[String]:
      items = stockChoises
      value = stockChoises.head

    val colorPicker = new ColorPicker(Color.White)

    val alertContent = new VBox:
      children = Array(companyChoiceBox, colorPicker)
      alignment = Pos.Center
    
    var alert = new Alert(AlertType.Confirmation):
      initOwner(stage)
      title = "XY Chart customization"
      headerText = "Choose company"
      dialogPane().setContent(alertContent)

    (alert, companyChoiceBox, colorPicker)