package dashboard.UIElements.DataAnalysisTools

import dashboard.UIElements.FunctionalityElements.RightSplit.componentWidthAndHeigth
import dashboard.fileManagement.Api.ReadApiData.{getPortfolioData, getTimeSeries}
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Side
import scalafx.scene.chart.PieChart
import scala.collection.mutable.Map

object PortfolioPieChart:

  /**
   * takes a portfolio name as a parameter and returns a pie chart object with the portfolios data
   * @param portfolioName name of portfolio file
   * @return PieChart
   */
  def getPieChart(portfolioName: String) =

    // get portfolio data
    val portfolioData: Map[String, Map[String, String]] = getPortfolioData(portfolioName)
    val stockNames = portfolioData.keys

    // first value is company's name and second is the owned stock's present value
    var nameAndValue: Seq[(String, Double)] = Seq()
    for key <- stockNames do
      val timeSeriesData = getTimeSeries(key)
      val latestDate = timeSeriesData.keys.max
      val price: Double = timeSeriesData(latestDate)("1. open")
      nameAndValue = nameAndValue :+ (key, (portfolioData(key)("Quantity").toInt * price))

    // create pie chart
    val (chartWidth, chartHeigth) = componentWidthAndHeigth
    val pieChart = new PieChart:
      title = s"Portfolio pie chart $portfolioName"
      clockwise = false
      legendSide = Side.Bottom
      prefWidth = chartWidth
      prefHeight = chartHeigth
      data = ObservableBuffer.from(nameAndValue.map({
        case (x, y) => PieChart.Data(x, y)
      }))

    pieChart