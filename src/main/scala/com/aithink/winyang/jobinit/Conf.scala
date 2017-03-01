package com.aithink.winyang.jobinit

import org.apache.spark.SparkContext
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * Created by winyang on 3/1/17.
  */
trait Conf {
  val spark:SparkSession = SparkSession.builder.appName("winyangApp").getOrCreate()
  //只能这样？
  val sc:SparkContext = spark.sparkContext
  //简化代码
  protected def sql(sqlText:String):DataFrame = spark.sql(sqlText)
}

