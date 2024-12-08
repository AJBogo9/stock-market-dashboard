package dashboard.UIElements.FunctionalityElements.Alerts


import dashboard.UI.stage
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Pos
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, ChoiceBox}
import scalafx.scene.layout.VBox

object StockTileAlert:
  private val stockChoises = ObservableBuffer("Apple", "Nvidia", "Microsoft")
  def getStockTileAlert =

    val companyChoiceBox = new ChoiceBox[String]:
      items = stockChoises
      value = stockChoises.head

    val alertContent = new VBox:
      children = Array(companyChoiceBox)
      alignment = Pos.Center
    
    var alert = new Alert(AlertType.Confirmation):
      initOwner(stage)
      title = "Stock tile customization"
      headerText = "Choose company"
      dialogPane().setContent(alertContent)

    (alert, companyChoiceBox)
