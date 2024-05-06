import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.Month

object Alerts {
  import DataLoader._

  def isDaytime(dateTimeStr: String): Boolean = {
    val formatter = DateTimeFormatter.ofPattern("yyyy/M/d H:mm")
    val dateTime = LocalDateTime.parse(dateTimeStr, formatter)
    val month = dateTime.getMonth
    val hour = dateTime.getHour

    (month == Month.DECEMBER || month == Month.JANUARY || month == Month.FEBRUARY) match {
      case true => hour >= 10 && hour < 16
      case false => hour >= 8 && hour < 18
    }
  }

  def detectSolarIssues(solarDataPath: String): Unit = {
    val solarData = DataLoader.loadCsv(solarDataPath)
    val solarOutputThreshold = 500.0

    solarData.tail.foreach { row =>
      val dateTime = row(0)
      val output = row(2).toDouble

      if (isDaytime(dateTime) && output < solarOutputThreshold) {
        println(s"Alert: At $dateTime, solar energy output is below the threshold of $solarOutputThreshold kW.")
        println(row.mkString(" | "))
      }
    }
  }

  def detectWindIssues(windDataPath: String): Unit = {
    val windData = DataLoader.loadCsv(windDataPath)
    val windOutputThreshold = 100.0

    windData.tail.foreach { row =>
      val dateTime = row(0)
      val output = row(2).toDouble

      if (output < windOutputThreshold) {
        println(s"Alert: At $dateTime, wind energy output is below the threshold of $windOutputThreshold kW.")
        println(row.mkString(" | "))
      }
    }
  }

  def detectHydroIssues(hydroDataPath: String): Unit = {
    val hydroData = DataLoader.loadCsv(hydroDataPath)
    val hydroOutputThreshold = 4.0

    hydroData.tail.foreach { row =>
      val dateTime = row(0)
      val output = row(2).toDouble

      if (output < hydroOutputThreshold) {
        println(s"Alert: At $dateTime, hydro energy output is below the threshold of $hydroOutputThreshold kW.")
        println(row.mkString(" | "))
      }
    }
  }

  def detectAndHandleIssues(solarDataPath: String, windDataPath: String, hydroDataPath: String): Unit = {
    println("Checking solar energy issues...")
    detectSolarIssues(solarDataPath)
    println("Checking wind energy issues...")
    detectWindIssues(windDataPath)
    println("Checking hydro energy issues...")
    detectHydroIssues(hydroDataPath)
  }
}
