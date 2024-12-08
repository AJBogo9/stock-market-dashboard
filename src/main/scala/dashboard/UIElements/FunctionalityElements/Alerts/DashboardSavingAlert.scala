package dashboard.UIElements.FunctionalityElements.Alerts


import dashboard.UI.stage
import dashboard.fileManagement.SaveAndOpenDashboards.OpenDashboard.getDashboardNames
import scalafx.geometry.Pos
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.TextFormatter.Change
import scalafx.scene.control.{Alert, Label, TextField, TextFormatter}
import scalafx.scene.layout.VBox

object DashboardSavingAlert:
  private val errorLabel = new Label(""):
    style = "-fx-text-fill: red"
  private val fileNameTextField = new TextField

  def getDashboardSavingAlert =


    fileNameTextField.textFormatter = textFormatter

    val alertContent = new VBox:
      children = Array(errorLabel, fileNameTextField)
      alignment = Pos.Center

    var alert = new Alert(AlertType.Confirmation):
      initOwner(stage)
      title = "Save dashboard"
      headerText = "Enter dashboard name"
      dialogPane().setContent(alertContent)

    (alert, fileNameTextField)

  def emptyTextField() = fileNameTextField.text = ""

  private def validateText(str: String): Option[ValidationError] =
    val dashboardNames = getDashboardNames
    str match
      case str if str.isEmpty => Some(EmptyField)
      case str if dashboardNames.contains(str) => Some(NameTaken)
      case _ => None

  trait ValidationError
  case object EmptyField extends ValidationError
  case object NameTaken extends ValidationError

  private val textFormatter = TextFormatter(
    filter = (change: Change) =>
      if change.isContentChange then
        val validationResult = validateText(change.controlNewText)
        validationResult match
          case Some(NameTaken) =>
            errorLabel.text = "This name is taken"
            change
          case Some(EmptyField) =>
            errorLabel.text = "Dashboard name can't be empty"
            change
          case None =>
            errorLabel.text = ""
            change
          case _ => null
      else change
  )