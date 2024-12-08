package dashboard.fileManagement.Api

import ujson.Value

import java.io.FileWriter
import java.nio.file.{Files, Paths}
import scala.collection.mutable
import scala.collection.mutable.Map
import scala.io.Source

object SaveApiData:

  /**
   * Reads API key from apiKey.txt whitch is located in the root of the project with build.sbt etc.
   * A personal API key can be claimed from https://www.alphavantage.co/support/#api-key
   */
  private val apiKey: String =
    val apiKeyFile = "apiKey.txt"
    val source = Source.fromFile(apiKeyFile)
    val key = source.getLines().mkString
    source.close()
    key

  val timeSeriesDstFolder = "src/main/scala/dashboard/data/timeSeries"
  private val symbols = Map("Apple" -> "AAPL", "Nvidia" -> "NVDA", "Microsoft" -> "MSFT")

  /**
   * Does an API call to https://www.alphavantage.co/documentation/ and saves the data
   * into a file for later use
   *
   * @param function    The program is only tested with the "TIME_SERIES_MONTHLY" function
   * @param companyName Company name starting with a capital letter
   * @return
   */
  def getDataFromAlphavantageAndSave(function: String, companyName: String): String =
    val symbol = companySymbol(companyName)

    // get data from api
    val urlMonthly = s"https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY&symbol=$symbol&apikey=$apiKey"
    val html = Source.fromURL(urlMonthly)
    val text = html.mkString

    // Create directory
    Files.createDirectories(Paths.get(timeSeriesDstFolder))

    // Write to file
    val file = s"$timeSeriesDstFolder/${function}_$symbol.json"
    val fw = new FileWriter(file, false)
    fw.write(text)
    fw.close()

    text

  def companySymbol(company: String): String = symbols(company)