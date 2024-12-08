import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable

class ApiTests extends AnyFlatSpec with Matchers:

  "The function getMetaData" should "return a Map[String, String]" in {
    val metaData = getMetaData("Apple")
    type expectedType = mutable.HashMap[String, String]

    metaData shouldBe a[expectedType]
  }

  "The function getTimeSeries" should "return a Map[String, Map[String, Double]]" in {
    val metaData = getTimeSeries("Apple")
    type expectedType = mutable.HashMap[String, mutable.HashMap[String, Double]]

    metaData shouldBe a[expectedType]
  }
