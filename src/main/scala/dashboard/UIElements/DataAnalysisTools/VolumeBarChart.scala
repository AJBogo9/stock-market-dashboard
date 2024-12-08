package dashboard.UIElements.DataAnalysisTools

import dashboard.UI.stage
import dashboard.UIElements.FunctionalityElements.RightSplit.componentWidthAndHeigth
import dashboard.fileManagement.Api.ReadApiData.getTimeSeries
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Side
import scalafx.scene.chart.{BarChart, CategoryAxis, NumberAxis, XYChart}
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType

object VolumeBarChart:
  def getVolumeBarChart(company: String, color: String) =
    val ApiData = getTimeSeries(company)
    val dates = ApiData.keys.toArray.sorted
    var datesAndVolume: Seq[(String, Double)] = Seq()
    for date <- dates do
      datesAndVolume = datesAndVolume :+ (date, ApiData(date)("5. volume"))


    val (chartWidth, chartHeigth) = componentWidthAndHeigth

    val chart = new BarChart(CategoryAxis("Date"), NumberAxis("Volume")):
      title = s"$company trading volume"
      legendSide = Side.Bottom
      prefWidth = chartWidth
      prefHeight = chartHeigth


    val series = new XYChart.Series[String, Number]:
      name = company
      data = ObservableBuffer.from(
        datesAndVolume.map((x, y) => XYChart.Data[String, Number](x, y))
      )

    chart.getData.add(series)

    series.getData.forEach { data =>
      data.getNode.setOnMouseClicked { event =>
        val formatter = java.text.NumberFormat.getIntegerInstance
        val volume = data.getYValue.intValue()
        
        val alert = new Alert(AlertType.Information):
          initOwner(stage)
          title = "Data point information"
          headerText = s"date: ${data.getXValue}\nvolume: ${formatter.format(volume)}"
        
        alert.showAndWait()
      }
    }

    chart.getStylesheets().setAll(
      s"""
                        data:text/css,
                        .default-color0.chart-bar { -fx-background-color: #$color; }
                        """
    )

    chart