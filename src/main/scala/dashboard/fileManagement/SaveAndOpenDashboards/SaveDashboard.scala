package dashboard.fileManagement.SaveAndOpenDashboards

import dashboard.UIElements.DataAnalysisTools.Tile.*
import dashboard.UIElements.FunctionalityElements.Alerts.DashboardOpeningAlert.updateDashboardOpeningChoiseBox
import dashboard.UIElements.FunctionalityElements.LeftSplit.getHiddenElements
import dashboard.UIElements.FunctionalityElements.RightSplit.getPane
import dashboard.fileManagement.SaveAndOpenDashboards.OpenDashboard.getSaveFileMap
import javafx.scene.Node
import javafx.scene.chart.{BarChart, LineChart, PieChart, ScatterChart}
import javafx.scene.layout.VBox
import scalafx.Includes.jfxVBox2sfx
import scalafx.collections.CollectionIncludes.observableList2ObservableBuffer
import scalafx.collections.ObservableBuffer
import scalafx.scene.SceneIncludes.jfxLabel2sfx
import ujson.Value

import java.io.FileWriter
import scala.collection.immutable.Map
import scala.io.Source

object SaveDashboard:

  val saveFilePath = "src/main/scala/dashboard/data/saveFiles/saveFile.json"


  /**
   * Saves current dashboard to a save file.
   * The function does the following steps:
   *    1) creates a new save map from the dashboard
   *    2) reads the previously saved dashboards from save file
   *    3) adds the new dashboard to the map of the previously saved dashboards
   *    4) overwrites the previous map in the save file with the new map
   * @param name the name of the dashboard
   */
  def saveDashboard(name: String) =
    val newData = Map(
      name -> createNewSaveMap
    )
    var savedData = getSaveFileMap
    savedData = savedData ++ newData

    // update file opening alert
    updateDashboardOpeningChoiseBox(savedData.keys)

    val file = saveFilePath
    val fw = new FileWriter(file, false)

    val JSONString = upickle.default.write(savedData)

    fw.write(JSONString)
    fw.close()

  def createNewSaveMap: Map[String, Array[Map[String, String]]] =
    val shownComponents = getShownComponentsMap
    val hiddenComponents = getHiddenComponentsMap

    Map(
      "shown" -> shownComponents,
      "hidden" -> hiddenComponents
    )

  def getShownComponentsMap: Array[Map[String, String]] =
    val shownComponents = getPane.children
    var shownComponentsArray: Array[Map[String, String]] = Array()

    for component <- shownComponents do
      getComponentData(component) match
        case Some(component) =>
          shownComponentsArray = shownComponentsArray :+ component
        case None =>

    shownComponentsArray


  private def getHiddenComponentsMap =
    val hiddenComponents = getHiddenElements
    var hiddenComponentsArray: Array[Map[String, String]] = Array()

    for component <- hiddenComponents do
      getComponentData(component) match
        case Some(component) =>
          hiddenComponentsArray = hiddenComponentsArray :+ component
        case None =>

    hiddenComponentsArray

  private def getComponentData(node: Node): Option[Map[String, String]] =
    var componentData: Option[Map[String, String]] = None

    node match
      case pieChart: PieChart =>
        var map: Map[String, String] = Map()
        val name = pieChart.getTitle.split(" ")(3)
        map += "type" -> "pie chart"
        map += "portfolio" -> name
        componentData = Some(map)

      case scatterPlot: ScatterChart[Number, Number] =>
        val year = scatterPlot.getTitle.split(" ")(5)
        var companyNames = Array[String]()
        val companyData = scatterPlot.getData
        for serie <- companyData do
          val companyName = serie.getName.split(" ").head
          companyNames = companyNames :+ companyName

        var map: Map[String, String] = Map()
        map += "type" -> "scatter plot"
        map += "company1" -> companyNames(0)
        map += "company2" -> companyNames(1)
        map += "year" -> year
        componentData = Some(map)

      case xyChart: LineChart[String, Number] =>
        val name = xyChart.getTitle.split(" ")(0)
        val color = xyChart.getStylesheets.getFirst.split("#")(1).take(8)

        var map: Map[String, String] = Map()
        map += "type" -> "xy chart"
        map += "company" -> name
        map += "color" -> color
        componentData = Some(map)

      case barChart: BarChart[String, Number] =>
        val name = barChart.getTitle.split(" ")(0)
        val color = barChart.getStylesheets.getFirst.split("#")(1).take(8)

        var map: Map[String, String] = Map()
        map += "type" -> "bar chart"
        map += "company" -> name
        map += "color" -> color
        componentData = Some(map)

      case tile: VBox =>
        val name = tile.children.head.asInstanceOf[javafx.scene.control.Label].text()
        val tileElementCount = tile.children.length

        var map: Map[String, String] = Map()
        if tileElementCount == 2 then
          map += "type" -> "portfolio tile"
          map += "portfolio" -> name
        else if tileElementCount == 5 then
          map += "type" -> "stock tile"
          map += "company" -> name
        componentData = Some(map)

      case _ =>

    componentData

