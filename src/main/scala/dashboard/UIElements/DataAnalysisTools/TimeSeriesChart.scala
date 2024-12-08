package dashboard.UIElements.DataAnalysisTools

import dashboard.UI.stage
import dashboard.UIElements.FunctionalityElements.RightSplit.componentWidthAndHeigth
import dashboard.fileManagement.Api.ReadApiData.getTimeSeries
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Side
import scalafx.scene.chart.*
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType


object TimeSeriesChart:
  def getTimeSeriesChart(company: String, color: String) =
    val timeSeries = getTimeSeries(company)
    var dateValuePairs = timeSeries.map(date => (date._1, date._2("1. open"))).toSeq.sortBy(_._1)

    val (chartWidth, chartHeigth) = componentWidthAndHeigth

    val chart = new LineChart(CategoryAxis("Date"), NumberAxis("Price")):
      title = s"$company stock value"
      legendSide = Side.Bottom
      prefWidth = chartWidth
      prefHeight = chartHeigth

    val series = new XYChart.Series[String, Number]:
      name = company
      data = ObservableBuffer.from(
        dateValuePairs.map( (x, y) => XYChart.Data[String, Number](x, y) )
      )

    chart.getData.add(series)

    series.getData.forEach { data =>
      data.getNode.setOnMouseClicked { event =>
        val alert = new Alert(AlertType.Information):
          initOwner(stage)
          title = "Data point information"
          headerText = s"date: ${data.getXValue}\nprice: ${data.getYValue}"

        alert.showAndWait()
      }
    }

    chart.getStylesheets().setAll(
                    s"""
                    data:text/css,
                    .default-color0.chart-line-symbol { -fx-background-color: #$color, white; }
                    .default-color0.chart-series-line { -fx-stroke: #$color; }
                    """
    )


    chart
