package dashboard.UIElements.FunctionalityElements.Alerts

import dashboard.UI.stage
import dashboard.fileManagement.SaveAndOpenDashboards.OpenDashboard.getDashboardNames
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Pos
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, ChoiceBox}
import scalafx.scene.layout.VBox

object DashboardOpeningAlert:
  
  private val dashboardChoiseBox = new ChoiceBox[String]
  def getDashboardOpeningAlert =
    val dashboardNames = ObservableBuffer.from(getDashboardNames)
    dashboardChoiseBox.items = dashboardNames

    val alertContent = new VBox:
      children = Array(dashboardChoiseBox)
      alignment = Pos.Center

    var alert = new Alert(AlertType.Confirmation):
      initOwner(stage)
      title = "Open dashboard"
      headerText = "Choose dashboard to open"
      dialogPane().setContent(alertContent)

    (alert, dashboardChoiseBox)
  def updateDashboardOpeningChoiseBox(dashboardNames: Iterable[String]) =
    dashboardChoiseBox.items = ObservableBuffer.from(dashboardNames)
