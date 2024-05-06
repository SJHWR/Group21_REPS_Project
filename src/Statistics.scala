object Statistics {
  def mean(values: Array[Double]): Double = values.sum / values.length
  def median(values: Array[Double]): Double = {
    val sorted = values.sorted
    if (sorted.length % 2 == 0)
      (sorted(sorted.length / 2 - 1) + sorted(sorted.length / 2)) / 2
    else
      sorted(sorted.length / 2)
  }
  def mode(values: Array[Double]): Double = values.groupBy(identity).maxBy(_._2.size)._1
  def range(values: Array[Double]): Double = values.max - values.min
  def midrange(values: Array[Double]): Double = (values.max + values.min) / 2

  def analyzeData(data: Array[Array[String]], colIndex: Int): Unit = {
    val values = data.tail.map(row => row(colIndex).toDouble)
    println(s"Mean: ${mean(values)}")
    println(s"Median: ${median(values)}")
    println(s"Mode: ${mode(values)}")
    println(s"Range: ${range(values)}")
    println(s"Midrange: ${midrange(values)}")
  }
}
