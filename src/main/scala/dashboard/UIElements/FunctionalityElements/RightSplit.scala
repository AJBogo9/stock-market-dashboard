package dashboard.UIElements.FunctionalityElements

import scalafx.scene.shape.Rectangle
import dashboard.UIElements.FunctionalityElements.LeftSplit.hideComponent
import dashboard.UIElements.FunctionalityElements.ToolBar.selectButton
import scalafx.Includes.jfxNode2sfx
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.Button
import scalafx.scene.layout.Pane
import scalafx.scene.paint.Color
import javafx.scene.Node
import scalafx.scene.shape.Rectangle.sfxRectangle2jfx

import scala.collection.mutable.Buffer


object RightSplit:

  private var paneSlotsOccupied = Buffer(
    Buffer(false, false, false, false),
    Buffer(false, false, false, false)
  )

  private val (width, heigth) = (300.0, 290.0)

  private val selectionRectangle = new Rectangle:
    stroke = Color.Black
    fill = Color.Transparent
    strokeDashArray = List(3d, 3d)
    // visible = false

  // this button is used to ensure that pane reaches over the whole window.
  private val buttonFarAway = new Button("█▄ █ ▀█▀")
  buttonFarAway.layoutX = 9999
  buttonFarAway.layoutY = 9999

  private val pane = new Pane:
    children = Array(buttonFarAway)

  def getPane = pane

  def clearRightSplit() =
    pane.children = Array[scalafx.scene.Node](buttonFarAway)
    paneSlotsOccupied = Buffer(
      Buffer(false, false, false, false),
      Buffer(false, false, false, false)
    )
  
  def getRightSplit =

    var startX: Double = 0
    var startY: Double = 0

    pane.onMousePressed = (event) =>
      if selectButton.selected() then
        pane.children.addAll(selectionRectangle)
        // selectionRectangle.visible = true
        startX = event.getX
        startY = event.getY
        selectionRectangle.x = startX
        selectionRectangle.y = startY
        selectionRectangle.width = 0
        selectionRectangle.height = 0

      // deselect already selected components
      for (child <- pane.children) do
        child match
          case node: javafx.scene.Node =>
            node.style = ""


    pane.onMouseDragged = (event) =>
      if selectButton.selected() then

        var currentX = event.getX
        var currentY = event.getY
        val width = currentX - startX
        val height = currentY - startY

        selectionRectangle.width = Math.abs(width)
        selectionRectangle.height = Math.abs(height)

        selectionRectangle.x = if (width < 0) currentX else startX
        selectionRectangle.y = if (height < 0) currentY else startY

        for (child <- pane.children) do
          child match
            case selectionTool: javafx.scene.shape.Rectangle =>
            case element: javafx.scene.Node =>
              element.style =
                if element.getBoundsInParent.intersects(selectionRectangle.getBoundsInParent) then
                  "-fx-background-color: blue;"
                else ""

    pane.onMouseReleased = (event) =>
      if selectButton.selected() then
        pane.children.removeAll(selectionRectangle)


    pane

  def removeSelectedComponents() =
    val selectedComponents = pane.children.filter(_.style().contains("-fx-background-color: blue;"))
    for component <- selectedComponents do
      val row = (component.getLayoutY / heigth).toInt
      val column = (component.getLayoutX / width).toInt
      paneSlotsOccupied(row)(column) = false
    pane.children.removeAll(selectedComponents)
    selectedComponents

  def componentWidthAndHeigth = (width, heigth)

  def addElementToPane(element: javafx.scene.Node) =

    var firstFreeSlot: Option[(Int, Int)] = None

    var row = 0
    while row < 2 do
      var column = 0
      while column < 4 do
        if !paneSlotsOccupied(row)(column) then
          firstFreeSlot = Some((row, column))
          row = 2
          column = 4
        column += 1
      row += 1

    firstFreeSlot match
      case Some((row, column)) =>
        pane.children.prepend(element)
        element.layoutX = column * width
        element.layoutY = row * heigth
        paneSlotsOccupied(row)(column) = true
      case None =>
        hideComponent(element)

