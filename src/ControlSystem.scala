import java.time.{LocalDate, LocalDateTime}
import java.time.format.DateTimeFormatter

object ControlSystem {
  val solarOutputThreshold = 500.0
  val windOutputThreshold = 100.0
  val hydroOutputThreshold = 4.0
  val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/M/d H:mm")
  var issuesList = List[(String, Double)]()
  var currentEnergyType: String = "solar"

  def filterAndControl(data: Array[Array[String]], specificDate: LocalDate, threshold: Double, energyType: String): Unit = {
    issuesList = List.empty[(String, Double)]
    currentEnergyType = energyType
    println(s"Filtering and controlling $currentEnergyType data for $specificDate with threshold $threshold kW")
    data.tail.foreach { row =>
      val dateTime = LocalDateTime.parse(row(0), dateTimeFormatter).toLocalDate
      val output = row(2).toDouble
      if (dateTime.isEqual(specificDate) && output < threshold) {
        issuesList = issuesList :+ (row(0), output)
      }
    }
    if (issuesList.nonEmpty) {
      println(s"${issuesList.size} issue(s) detected.")
      issuesMenu()
    } else {
      println("No issues detected.")
    }
  }

  def issuesMenu(): Unit = {
    issuesList.zipWithIndex.foreach { case ((time, output), index) =>
      println(s"$index: Issue at $time with output $output kW")
    }
    println(s"${issuesList.size}: Return to Main Menu")
    println("Select an issue to handle:")
    val choice = scala.io.StdIn.readInt()
    if (choice >= 0 && choice < issuesList.size) {
      handleIssue(issuesList(choice))
    }
  }

  def handleIssue(issue: (String, Double)): Unit = {
    println(s"Handling issue at ${issue._1} with output ${issue._2} kW")
    currentEnergyType match {
      case "solar" =>
        println("1: Adjust panel angle")
        println("2: Check photovoltaic cells")
        println("3: Send maintenance alert")
      case "wind" =>
        println("1: Adjust turbine pitch")
        println("2: Lubricate bearings")
        println("3: Send maintenance alert")
      case "hydro" =>
        println("1: Check turbine flow")
        println("2: Inspect valves")
        println("3: Send maintenance alert")
    }
    val action = scala.io.StdIn.readInt()
    action match {
      case 1 => println("Adjusting settings...")
      case 2 => println("Performing component check or lubrication...")
      case 3 => println("Sending maintenance alert...")
      case _ => println("Invalid action.")
    }
  }
}
