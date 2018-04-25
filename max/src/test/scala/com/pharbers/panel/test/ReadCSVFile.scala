package com.pharbers.panel.test

import org.apache.spark.sql.SQLContext
import com.pharbers.spark.phSparkDriver

object ReadCSVFile{
  def getMaxResult ={
    lazy val sparkDriver: phSparkDriver = phSparkDriver()
    val sqlContext = new SQLContext(sparkDriver.sc)
    val data =sqlContext.read.format("com.databricks.spark.csv")
      .option("header","true") //这里如果在csv第一行有属性的话，没有就是"false"
      .option("inferSchema",true.toString)//这是自动推断属性列的数据类型。
      .option("delimiter",31.toChar.toString)
      .load("/mnt/config/Result")//文件的路径
    val hosptalCount = data.select("ID").rdd.distinct().count()
    val sumArray = data.select("Units","Sales").agg(Map("Units" -> "sum", "Sales" -> "sum")).collect()
    val sales = sumArray(0).get(1).formatted("%.1f").toDouble
    val units = sumArray(0).get(0)
    List(hosptalCount, sales, units)
  }
//  a.foreach(println(_))
//  val df = phSparkDriver().csv2RDD(a.asInstanceOf[StringArgs].get, delimiter = 31.toChar.toString)
}
