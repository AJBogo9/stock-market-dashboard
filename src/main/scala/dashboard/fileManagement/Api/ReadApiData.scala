package dashboard.fileManagement.Api

import dashboard.fileManagement.Api.SaveApiData.{companySymbol, timeSeriesDstFolder}
import ujson.Value

import scala.collection.mutable
import scala.collection.mutable.Map
import scala.io.Source

object ReadApiData:
  
  private def readTextFromFile(src: String): String =
    val bufferedSource = Source.fromFile(src)
    val text = bufferedSource.mkString
    bufferedSource.close()
    text

  def getMetaData(company: String): mutable.Map[String, String] =
    val function = "TIME_SERIES_MONTHLY"
    val symbol = companySymbol(company)
    val src = s"$timeSeriesDstFolder/${function}_${symbol}.json"

    val rawText = readTextFromFile(src)
    val metaData: Value = ujson.read(rawText).obj("Meta Data")
    val usableMetaData = metaData.obj
    val ret: Map[String, String] = Map()
    for key <- usableMetaData.keys do
      ret += key -> usableMetaData(key).str
    ret

  def getTimeSeries(company: String): mutable.Map[String, mutable.Map[String, Double]] =
    val function = "TIME_SERIES_MONTHLY"
    val symbol = companySymbol(company)
    val src = s"$timeSeriesDstFolder/${function}_$symbol.json"

    val rawText = readTextFromFile(src)

    val timeSeries: Map[String, Value] = ujson.read(rawText).obj("Monthly Time Series").obj
    var retMap: Map[String, Map[String, Double]] = Map()
    for date <- timeSeries.keys do
      // converts Value type to Double
      val numbers: Map[String, Value] = timeSeries(date).obj
      val newNumbers: Map[String, Double] = Map()
      for key <- numbers.keys do
        newNumbers += key -> numbers(key).str.toDouble
      retMap += date -> newNumbers

    retMap

  def getPortfolioData(portfolioName: String): Map[String, Map[String, String]] =
    val portfolioSrc = s"src/main/scala/dashboard/data/portfolios/$portfolioName.json"
    val rawText = readTextFromFile(portfolioSrc)
    val data = ujson.read(rawText).obj
    var ret: Map[String, Map[String, String]] = Map()
    for key <- data.keys do
      val dataObj: Map[String, Value] = data(key).obj
      val mapped: Map[String, String] = Map()
      for key <- dataObj.keys do
        mapped += key -> dataObj(key).str
      ret += key -> mapped
    ret