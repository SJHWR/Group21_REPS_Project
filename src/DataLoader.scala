object DataLoader {
  def loadCsv(filePath: String): Array[Array[String]] = {
    val bufferedSource = scala.io.Source.fromFile(filePath)
    val data = bufferedSource.getLines().map(_.split(",")).toArray
    bufferedSource.close()
    data
  }
  
  def printCsvData(filePath: String, rowsToShow: Int, columnsToExclude: List[Int] = List()): Unit = {
    val data = loadCsv(filePath)
    if (data.isEmpty) {
      println("No data available.")
      return
    }
    
    val filteredData = data.map(row => row.zipWithIndex.filterNot { case (_, index) => columnsToExclude.contains(index) }.map(_._1))

    val colWidths = filteredData.transpose.map { col =>
      col.map(_.length).max
    }
    
    val header = filteredData.head
    val rows = filteredData.tail.take(rowsToShow)

    println(header.zip(colWidths).map { case (cell, width) =>
      cell.padTo(width, ' ')
    }.mkString(" | "))
    println("-" * colWidths.map(_ + 3).sum)

    rows.foreach { row =>
      println(row.zip(colWidths).map { case (cell, width) =>
        cell.padTo(width, ' ')
      }.mkString(" | "))
    }
  }
}
