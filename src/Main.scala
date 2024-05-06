import Analysis._
import Alerts._
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.util.Random

object Main extends App {
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy/M/d")

  def mainMenu(): Unit = {
    println("Main Menu:")
    println("1. Analyze Data")
    println("2. View Data")
    println("3. Alerts")
    println("4. Filter and Control for a Specific Day")
    println("5. Exit")
    val choice = scala.io.StdIn.readInt()
    choice match {
      case 1 => analysisMenu()
      case 2 => viewData()
      case 3 => alertsMenu()
      case 4 => filterAndControlMenu()
      case 5 => println("Exiting the program.")
      case _ => {
        println("Invalid choice, please try again.")
        mainMenu()
      }
    }
  }

  def analysisMenu(): Unit = {
    println("Data Analysis Menu:")
    println("1. Analyze Solar Data")
    println("2. Analyze Wind Data")
    println("3. Analyze Hydro Data")
    val choice = scala.io.StdIn.readInt()
    choice match {
      case 1 => Analysis.analyzeSolar()
      case 2 => Analysis.analyzeWind()
      case 3 => Analysis.analyzeHydro()
      case _ => {
        println("Invalid choice, please try again.")
        analysisMenu()
      }
    }
    mainMenu()
  }

  def getSimulatedStorageCapacity(): Double = {
    5000 + Random.nextDouble() * 1000
  }

  def viewData(): Unit = {
    println("Select Data to View:")
    println("1. Solar Data and Storage Capacity")
    println("2. Wind Data and Storage Capacity")
    println("3. Hydro Data and Storage Capacity")
    println("4. Back to Main Menu")
    val choice = scala.io.StdIn.readInt()
    choice match {
      case 1 => {
        println("How many rows do you want to view?")
        val rows = scala.io.StdIn.readInt()
        println("Viewing Solar Data:")
        DataLoader.printCsvData(Analysis.sunDataPath, rows, List(1))
        println(s"Storage Capacity: ${getSimulatedStorageCapacity()} kWh")
      }
      case 2 => {
        println("How many rows do you want to view?")
        val rows = scala.io.StdIn.readInt()
        println("Viewing Wind Data:")
        DataLoader.printCsvData(Analysis.windDataPath, rows, List(1))
        println(s"Storage Capacity: ${getSimulatedStorageCapacity()} kWh")
      }
      case 3 => {
        println("How many rows do you want to view?")
        val rows = scala.io.StdIn.readInt()
        println("Viewing Hydro Data:")
        DataLoader.printCsvData(Analysis.hydroDataPath, rows, List(1))
        println(s"Storage Capacity: ${getSimulatedStorageCapacity()} kWh")
      }
      case 4 => mainMenu()
      case _ => {
        println("Invalid choice, please try again.")
        viewData()
      }
    }
    mainMenu()
  }

  def alertsMenu(): Unit = {
    println("Detecting and handling issues...")
    Alerts.detectAndHandleIssues(Analysis.sunDataPath, Analysis.windDataPath, Analysis.hydroDataPath)
    mainMenu()
  }

  def filterAndControlMenu(): Unit = {
    println("Filter and Control for a Specific Day:")
    println("Enter the specific date (yyyy/M/d):")
    val specificDate = LocalDate.parse(scala.io.StdIn.readLine(), dateFormatter)
    println("Enter the minimum output threshold (in kW):")
    val threshold = scala.io.StdIn.readDouble()
    println("1. Filter Solar Data")
    println("2. Filter Wind Data")
    println("3. Filter Hydro Data")
    val dataChoice = scala.io.StdIn.readInt()
    val (dataPath, dataType) = dataChoice match {
      case 1 => (Analysis.sunDataPath, "solar")
      case 2 => (Analysis.windDataPath, "wind")
      case 3 => (Analysis.hydroDataPath, "hydro")
      case _ => {
        println("Invalid choice.")
        mainMenu()
        return
      }
    }
    ControlSystem.filterAndControl(DataLoader.loadCsv(dataPath), specificDate, threshold, dataType)
    mainMenu()
  }
  mainMenu()
}
