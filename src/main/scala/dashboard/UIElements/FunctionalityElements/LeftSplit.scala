package dashboard.UIElements.FunctionalityElements

import dashboard.UIElements.DataAnalysisTools.Tile.*
import dashboard.UIElements.DataAnalysisTools.PortfolioPieChart.getPieChart
import dashboard.UIElements.DataAnalysisTools.ReturnScatterPlot.getScatterPlot
import dashboard.UIElements.DataAnalysisTools.TimeSeriesChart.getTimeSeriesChart
import dashboard.UIElements.DataAnalysisTools.VolumeBarChart.getVolumeBarChart
import javafx.scene.chart.{BarChart, Chart, LineChart, PieChart, ScatterChart}
import javafx.scene.Node
import scalafx.scene.control.{Button, Label, Separator}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.text.{Font, FontWeight}
import scalafx.Includes.jfxChart2sfx
import scalafx.scene.SceneIncludes.jfxVBox2sfx
import scalafx.geometry.Orientation
import scalafx.scene.control.ControlIncludes.jfxLabel2sfx
import dashboard.UIElements.FunctionalityElements.RightSplit.addElementToPane
import scalafx.scene.SceneIncludes.jfxNode2sfx
import scalafx.Includes.observableList2ObservableBuffer
import scalafx.collections.ObservableBuffer


object LeftSplit:


  /**
   * Layout of left split:
   *
   *                      leftSideVBox (VBox)
   *                           |
   *     leftSplitHeader                   componentList (HBox)
   *
   *                                             |
   *
   *                 hiddenElementNames      separator        hiddenElementButtons
   *
   *
   *
   */

  private val hiddenElementNames = new VBox(8)
  private val separator = new Separator:
    orientation = Orientation.Vertical
  private val hiddenElementButtons = new VBox
  private val componentList = new HBox(5):
    children = Array(hiddenElementNames, separator, hiddenElementButtons)

  private val leftSplitHeader = new Label(s"Hidden elements:"):
    font = Font("System", FontWeight.ExtraBold, 20)
  private val leftSideVBox = new VBox:
    children = Array(leftSplitHeader, componentList)

  private var hiddenElements: ObservableBuffer[Node] = ObservableBuffer()

  def getHiddenElements = hiddenElements

  def clearLeftSplit() =
    hiddenElementNames.children = Array[scalafx.scene.Node]()
    hiddenElementButtons.children = Array[scalafx.scene.Node]()
    hiddenElements = ObservableBuffer(leftSplitHeader)

  def getLeftSplit = leftSideVBox

  def hideComponent(element: Node): Unit =
    element.style = ""
    addElementToLeftSplit(element)
    hiddenElements.addAll(element)

  private def addElementToLeftSplit(element: Node) =
    element match
      case chart: javafx.scene.chart.Chart =>
        val label = Label(s" - ${chart.title.value}")
        hiddenElementNames.children.add(label)

        val addButton = new Button("Add")
        val duplicateButton = new Button("Duplicate")
        val buttons = new HBox(3):
          children = Array(addButton, duplicateButton)
        hiddenElementButtons.children.add(buttons)

        addButton.onAction = (event) =>
          addElementToPane(element)
          hiddenElementNames.children.remove(label)
          hiddenElementButtons.children.remove(buttons)
          hiddenElements.remove(element)

        duplicateButton.onAction = (event) =>
          duplicateElement(element)



      case tile: javafx.scene.layout.VBox =>
        val name = tile.children.head.asInstanceOf[javafx.scene.control.Label].text()
        val label = Label(s" - $name tile")
        hiddenElementNames.children.add(label)

        val addButton = new Button("Add")
        val duplicateButton = new Button("Duplicate")
        val buttons = new HBox(3):
          children = Array(addButton, duplicateButton)
        hiddenElementButtons.children.add(buttons)

        addButton.onAction = (event) =>
          addElementToPane(element)
          hiddenElementNames.children.remove(label)
          hiddenElementButtons.children.remove(buttons)
          hiddenElements.remove(element)

        duplicateButton.onAction = (event) =>
          duplicateElement(element)


  private def duplicateElement(element: Node) =
    var duplicate: Option[Node] = None

    element match
      case pieChart: PieChart =>
        val name = pieChart.getTitle.split(" ")(3)
        duplicate = Some(getPieChart(name))

      case scatterPlot: ScatterChart[Number, Number] =>
        val year = scatterPlot.getTitle.split(" ")(5).toInt
        var companyNames = Array[String]()
        val companyData = scatterPlot.getData
        for serie <- companyData do
            val companyName = serie.getName.split(" ").head
            companyNames = companyNames :+ companyName
        duplicate = Some(getScatterPlot(companyNames, year))

      case xyChart: LineChart[String, Number] =>
        val name = xyChart.getTitle.split(" ")(0)
        val color = xyChart.getStylesheets.getFirst.split("#")(1).take(8)
        duplicate = Some(getTimeSeriesChart(name, color))

      case barChart: BarChart[String, Number] =>
        val name = barChart.getTitle.split(" ")(0)
        val color = barChart.getStylesheets.getFirst.split("#")(1).take(8)
        duplicate = Some(getVolumeBarChart(name, color))

      case tile: javafx.scene.layout.VBox =>
        val name = tile.children.head.asInstanceOf[javafx.scene.control.Label].text()
        val tileElementCount = tile.children.length
        if tileElementCount == 2 then
          duplicate = Some(getPortfolioTile(name))
        else if tileElementCount == 5 then
          duplicate = Some(getStockTile(name))

      case _ =>

    duplicate match
      case Some(element: javafx.scene.Node) => hideComponent(element)
      case None =>
