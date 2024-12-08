package dashboard.UIElements.FunctionalityElements

import scalafx.scene.control.{ButtonType, Menu, MenuBar, MenuItem}
import dashboard.UIElements.DataAnalysisTools.PortfolioPieChart.getPieChart
import dashboard.UIElements.DataAnalysisTools.ReturnScatterPlot.getScatterPlot
import dashboard.UIElements.DataAnalysisTools.Tile.{getPortfolioTile, getStockTile}
import dashboard.UIElements.DataAnalysisTools.TimeSeriesChart.getTimeSeriesChart
import dashboard.UIElements.DataAnalysisTools.VolumeBarChart.getVolumeBarChart
import dashboard.UIElements.FunctionalityElements.RightSplit.addElementToPane
import dashboard.fileManagement.SaveAndOpenDashboards.OpenDashboard.{getDashboardNames, openDashboard}
import dashboard.fileManagement.SaveAndOpenDashboards.SaveDashboard.saveDashboard
import dashboard.UIElements.FunctionalityElements.Alerts.PieChartAlert.getPieChartAlert
import dashboard.UIElements.FunctionalityElements.Alerts.ScatterPlotAlert.getScatterPlotAlert
import dashboard.UIElements.FunctionalityElements.Alerts.DashboardSavingAlert.{emptyTextField, getDashboardSavingAlert}
import dashboard.UIElements.FunctionalityElements.Alerts.BarChartAlert.getBarChartAlert
import dashboard.UIElements.FunctionalityElements.Alerts.PortfolioTileAlert.getPortfolioTileAlert
import dashboard.UIElements.FunctionalityElements.Alerts.StockTileAlert.getStockTileAlert
import dashboard.UIElements.FunctionalityElements.Alerts.XYChartAlert.getXYChartAlert
import dashboard.UIElements.FunctionalityElements.Alerts.DashboardOpeningAlert.getDashboardOpeningAlert

object MenuElement:

  val (
    barChartMenuItem,
    pieChartMenuItem,
    scatterPlotMenuItem,
    portfolioTileMenuItem,
    stockTileMenuItem,
    xyChartMenuItem,
    openMenuItem,
    saveMenuItem
    ) = (
    MenuItem("Bar Chart"),
    MenuItem("Pie Chart"),
    MenuItem("Scatter Plot"),
    MenuItem("Portfolio tile"),
    MenuItem("Stock tile"),
    MenuItem("XY chart"),
    MenuItem("Open"),
    MenuItem("Save")
  )

  private val (barChartAlert, barChartChoiceBox, barColorPicker) = getBarChartAlert
  barChartMenuItem.onAction = (event) =>
    val result = barChartAlert.showAndWait()
    val company = barChartChoiceBox.value.value
    val color = barColorPicker.value.apply().toString.drop(2)
    val barChart = getVolumeBarChart(company, color)
    result match
      case Some(ButtonType.OK) => addElementToPane(barChart)
      case _ =>

  private val (xyChartAlert, xyChartChoiceBox, xyColorPicker) = getXYChartAlert
  xyChartMenuItem.onAction = (event) =>
    val result = xyChartAlert.showAndWait()
    val company = xyChartChoiceBox.value.value
    val color = xyColorPicker.value.apply().toString.drop(2)
    val xyChart = getTimeSeriesChart(company, color)
    result match
      case Some(ButtonType.OK) => addElementToPane(xyChart)
      case _ =>

  private val (pieChartAlert, portfolioChoiseBoxPieChart) = getPieChartAlert
  pieChartMenuItem.onAction = (event) =>
    val result = pieChartAlert.showAndWait()
    val portfolio = portfolioChoiseBoxPieChart.value.value
    val pieChart = getPieChart(portfolio)
    result match
      case Some(ButtonType.OK) => addElementToPane(pieChart)
      case _ =>

  private val (portfolioTileAlert, portfolioChoiceBoxTile) = getPortfolioTileAlert
  portfolioTileMenuItem.onAction = (event) =>
    val result = portfolioTileAlert.showAndWait()
    val portfolio = portfolioChoiceBoxTile.value.value
    val portfolioTile = getPortfolioTile(portfolio)
    result match
      case Some(ButtonType.OK) => addElementToPane(portfolioTile)
      case _ =>

  private val (stockTileAlert, stockTileChoiseBox) = getStockTileAlert
  stockTileMenuItem.onAction = (event) =>
    val result = stockTileAlert.showAndWait()
    val company = stockTileChoiseBox.value.value
    val stockTile = getStockTile(company)
    result match
      case Some(ButtonType.OK) => addElementToPane(stockTile)
      case _ =>

  private val (scatterPlotAlert, scatterPlotChoiceBox1, scatterPlotChoiceBox2, yearChoiceBox) =
    getScatterPlotAlert
  scatterPlotMenuItem.onAction = (event) =>
    val result = scatterPlotAlert.showAndWait()
    val companies = Array(scatterPlotChoiceBox1.value.value, scatterPlotChoiceBox2.value.value)
    val year = yearChoiceBox.value.value
    val scatterPlot = getScatterPlot(companies, year)
    result match
      case Some(ButtonType.OK) => addElementToPane(scatterPlot)
      case _ =>

  private val (dashboardSavingAlert, dashboardNameTextField) = getDashboardSavingAlert
  saveMenuItem.onAction = (event) =>
    val result = dashboardSavingAlert.showAndWait()
    val dashboardName = dashboardNameTextField.text.apply()
    (result, dashboardName) match
      case (Some(ButtonType.OK), name: String)
        if (name != "" && !getDashboardNames.contains(name)) =>
          saveDashboard(name)
          emptyTextField()
      case _ =>

  private val (dashboardOpeningAlert, dashboardNameSelector) = getDashboardOpeningAlert
  openMenuItem.onAction = (event) =>
    val result = dashboardOpeningAlert.showAndWait()
    val dashboardName = dashboardNameSelector.value.value
    (result, dashboardName) match
      case (Some(ButtonType.OK), name: String) => openDashboard(name)
      case _ =>



  def getMenuElement: MenuBar =

    val fileOperations = new Menu("File"):
      items = Array(
        openMenuItem,
        saveMenuItem
      )

    val createChart = new Menu("New"):
      items = Array(
        barChartMenuItem,
        pieChartMenuItem,
        scatterPlotMenuItem,
        portfolioTileMenuItem,
        stockTileMenuItem,
        xyChartMenuItem
      )

    val menu = new MenuBar:
      menus = Array(fileOperations, createChart)

    menu