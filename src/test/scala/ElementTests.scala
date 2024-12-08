import dashboard.UIElements.DataAnalysisTools.PortfolioPieChart.getPieChart
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalafx.scene.chart.PieChart

import scala.collection.mutable

class ElementsTests extends AnyFlatSpec with Matchers:

  "The function getPieChart" should "return a PieChart" in {
    val metaData = getPieChart("portfolio1")
    type expectedType = PieChart

    metaData shouldBe a[expectedType]
  }
