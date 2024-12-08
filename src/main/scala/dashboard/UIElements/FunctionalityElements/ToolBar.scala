package dashboard.UIElements.FunctionalityElements

import dashboard.UIElements.FunctionalityElements.RightSplit.removeSelectedComponents
import scalafx.scene.control.{Button, Separator, ToggleButton, ToolBar}
import dashboard.UIElements.FunctionalityElements.LeftSplit.hideComponent
import dashboard.fileManagement.Api.SaveApiData.getDataFromAlphavantageAndSave

object ToolBar:

  val (
    selectButton,
    hideButton,
    removeButton,
    refreshButton
  ) = (
    ToggleButton("Select"),
    Button("Hide"),
    Button("Remove"),
    Button("Refresh")
  )

  hideButton.onAction = (event) =>
    val removedComponents = removeSelectedComponents()
    removedComponents.foreach(hideComponent)

  removeButton.onAction = (event) =>
    removeSelectedComponents()
    
  refreshButton.onAction = (event) =>
    getDataFromAlphavantageAndSave("TIME_SERIES_MONTHLY", "Apple")
    getDataFromAlphavantageAndSave("TIME_SERIES_MONTHLY", "Microsoft")
    getDataFromAlphavantageAndSave("TIME_SERIES_MONTHLY", "Nvidia")

  def getToolBarElement: ToolBar =

    val toolBar = new ToolBar:
      items = Array(
        selectButton,
        new Separator,
        hideButton,
        removeButton,
        new Separator,
        refreshButton
      )

    toolBar