import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.time.DayOfWeek
import DataLoader._
import Statistics._

object Analysis {
  val windDataPath = "D:\\playground\\DMM2\\REPS_Project\\src\\wind.csv"
  val sunDataPath = "D:\\playground\\DMM2\\REPS_Project\\src\\sun.csv"
  val hydroDataPath = "D:\\playground\\DMM2\\REPS_Project\\src\\hydro.csv"

  def analyzeSolar(): Unit = {
    val sunData = loadCsv(sunDataPath)
    println("Solar Photovoltaic Power")
    selectPeriod(sunData)
  }

  def analyzeWind(): Unit = {
    val windData = loadCsv(windDataPath)
    println("Wind Power")
    selectPeriod(windData)
  }

  def analyzeHydro(): Unit = {
    val hydroData = loadCsv(hydroDataPath)
    println("Hydro Power")
    selectPeriod(hydroData)
  }

  def selectPeriod(data: Array[Array[String]]): Unit = {
    println("Select the period for analysis:")
    println("1. Hourly")
    println("2. Daily")
    println("3. Weekly")
    println("4. Monthly")
    println("5. Yearly")
    val choice = scala.io.StdIn.readInt()
    choice match {
      case 1 => analyzePeriod(data, "Hourly")
      case 2 => analyzePeriod(data, "Daily")
      case 3 => analyzePeriod(data, "Weekly")
      case 4 => analyzePeriod(data, "Monthly")
      case 5 => analyzePeriod(data, "Yearly")
      case _ =>
        println("Invalid choice, please select a valid period.")
        selectPeriod(data)
    }
  }

  def analyzePeriod(data: Array[Array[String]], period: String): Unit = {
    val formatter = DateTimeFormatter.ofPattern("yyyy/M/d H:mm")
    val groupedData = data.tail.groupBy { row =>
      val dateTime = LocalDateTime.parse(row(0), formatter)
      period match {
        case "Hourly" => dateTime.withMinute(0).withSecond(0).withNano(0)
        case "Daily" => dateTime.toLocalDate
        case "Weekly" => dateTime.`with`(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        case "Monthly" => dateTime.withDayOfMonth(1).toLocalDate.atStartOfDay()
        case "Yearly" => dateTime.getYear
      }
    }

    groupedData.foreach { case (time, entries) =>
      println(s"Analysis for $time:")
      analyzeData(entries, 2)
    }
  }

  def analyzeData(data: Array[Array[String]], colIndex: Int): Unit = {
    if (data.isEmpty) {
      println("No data available for this period.")
    } else {
      val values = data.map(row => row(colIndex).toDouble)
      println(s"Mean: ${mean(values)}")
      println(s"Median: ${median(values)}")
      println(s"Mode: ${mode(values)}")
      println(s"Range: ${range(values)}")
      println(s"Midrange: ${midrange(values)}")
    }
  }
}
