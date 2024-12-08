package dashboard.UIElements.DataAnalysisTools

import dashboard.UIElements.FunctionalityElements.RightSplit.componentWidthAndHeigth
import dashboard.fileManagement.Api.ReadApiData.{getPortfolioData, getTimeSeries}
import scalafx.geometry.Pos
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox
import scalafx.scene.text.{Font, FontWeight}

import scala.math.sqrt

object Tile:
  
  def getStockTile(stockName: String) =
    val stockTimeSeries = getTimeSeries(stockName)
    val stockOpenValuesMap = stockTimeSeries.keys.map(date => date -> stockTimeSeries(date)("1. open")).toMap

    val maxValue = stockOpenValuesMap.maxBy(_._2)
    val minValue = stockOpenValuesMap.minBy(_._2)

    var index = 1
    val returnCount = stockOpenValuesMap.size - 1

    var returnValues = Array[Double]()
    val sortedDatesAndValues = stockOpenValuesMap.toSeq.sortBy(_._1)
    val values = sortedDatesAndValues.map( _._2 )
    while index < returnCount do
      returnValues = returnValues :+ (values(index) - values(index - 1))
      index += 1
    var sumOfReturns = returnValues.sum

    val meanReturn = sumOfReturns / returnCount


    // calculating stangard deviation
    val meanSubtracted = returnValues.map( value => value - meanReturn )
    val squared = meanSubtracted.map( value => value * value )
    val sumOfSquaredDeviations = squared.sum
    val sumDividedByAmountOfDatapoints = sumOfSquaredDeviations / meanSubtracted.length
    val standardDeviationReturn = sqrt(sumDividedByAmountOfDatapoints)
    
    val stockNameLabel = new Label(s"$stockName"):
      font = Font("System", FontWeight.ExtraBold, 15)

    val formatter = java.text.NumberFormat.getCurrencyInstance

    val (chartWidth, chartHeigth) = componentWidthAndHeigth
    
    val stockTile = new VBox:
      children = Array(
        stockNameLabel,
        Label(s"Min value: ${formatter.format(minValue._2)} (${minValue._1})"),
        Label(s"Max value: ${formatter.format(maxValue._2)} (${maxValue._1})"),
        Label(s"Return per month mean: ${formatter.format(meanReturn)}"),
        Label(s"Return standard deviation: ${formatter.format(standardDeviationReturn)}")
      )
      alignment = Pos.Center
      prefWidth = chartWidth
      prefHeight = chartHeigth

    stockTile


  def getPortfolioTile(portfolioName: String) =

    val portfolioData = getPortfolioData(portfolioName)
    val companyAndQuantityMap = portfolioData.keys.map(company => company -> portfolioData(company)("Quantity")).toMap


    var portfolioValue = 0.0
    for company <- companyAndQuantityMap.keys do
      val companyTimeSeries = getTimeSeries(company)
      val dateAndOpenValueMap = companyTimeSeries.keys.map(date => date -> companyTimeSeries(date)("1. open")).toMap

      val lastDate = dateAndOpenValueMap.max._1
      val quantity = companyAndQuantityMap(company).toDouble
      val stockValue = dateAndOpenValueMap(lastDate)

      portfolioValue += quantity * stockValue

    val portfolioNameLabel = new Label(s"$portfolioName"):
      font = Font("System", FontWeight.ExtraBold, 15)

    val formatter = java.text.NumberFormat.getCurrencyInstance

    val (chartWidth, chartHeigth) = componentWidthAndHeigth

    val portfolioTile = new VBox:
      children = Array(
        portfolioNameLabel,
        Label(s"Portfolio value: ${formatter.format(portfolioValue)}")
      )
      alignment = Pos.Center
      prefWidth = chartWidth
      prefHeight = chartHeigth

    portfolioTile
    