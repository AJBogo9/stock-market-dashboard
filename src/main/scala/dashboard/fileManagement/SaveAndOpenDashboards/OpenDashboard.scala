package dashboard.fileManagement.SaveAndOpenDashboards

import dashboard.UIElements.DataAnalysisTools.PortfolioPieChart.getPieChart
import dashboard.UIElements.DataAnalysisTools.ReturnScatterPlot.getScatterPlot
import dashboard.UIElements.DataAnalysisTools.Tile.{getPortfolioTile, getStockTile}
import dashboard.UIElements.DataAnalysisTools.TimeSeriesChart.getTimeSeriesChart
import dashboard.UIElements.DataAnalysisTools.VolumeBarChart.getVolumeBarChart
import dashboard.UIElements.FunctionalityElements.LeftSplit.{clearLeftSplit, hideComponent}
import dashboard.UIElements.FunctionalityElements.RightSplit.{addElementToPane, clearRightSplit}
import dashboard.fileManagement.SaveAndOpenDashboards.SaveDashboard.saveFilePath
import ujson.Value

import scala.collection.immutable.Map
import scala.io.Source

object OpenDashboard:
  
  def openDashboard(name: String) =
    val data = getSaveFileMap(name)

    clearLeftSplit()
    clearRightSplit()

    val shownComponents = data("shown")
    for component <- shownComponents do
      val element = createComponent(component)
      addElementToPane(element)

    val hiddenComponents = data("hidden")
    for component <- hiddenComponents do
      val element = createComponent(component)
      hideComponent(element)

  private def createComponent(data: Map[String, String]) =
    data("type") match
      case "pie chart" => getPieChart(data("portfolio"))
      case "xy chart" => getTimeSeriesChart(data("company"), data("color"))
      case "scatter plot" => getScatterPlot(Array(data("company1"), data("company2")), data("year").toInt)
      case "portfolio tile" => getPortfolioTile(data("portfolio"))
      case "stock tile" => getStockTile(data("company"))
      case "bar chart" => getVolumeBarChart(data("company"), data("color"))
  
  def getSaveFileMap =
    val src = saveFilePath
    val bufferedSource = Source.fromFile(src)
    val rawText = bufferedSource.mkString
    bufferedSource.close()

    val data = ujson.read(rawText).obj
    var retMap: Map[String, Map[String, Array[Map[String, String]]]] = Map()

    for key <- data.keys do
      val shownComponents = data(key)("shown").arr.map(
        component => component.obj.map(
          (key, value) => key -> value.str
        ).toMap
      ).toArray

      val hiddenComponents = data(key)("hidden").arr.map(
        component => component.obj.map(
          (key, value) => key -> value.str
        ).toMap
      ).toArray

      val dashboardMap = Map(
        key -> Map(
          "shown" -> shownComponents,
          "hidden" -> hiddenComponents
        )
      )

      retMap = retMap ++ dashboardMap


    retMap

  def getDashboardNames = getSaveFileMap.keys.toArray
