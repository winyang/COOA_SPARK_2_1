package com.aithink.winyang.jobinit

import org.apache.spark.SparkContext
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * Created by winyang on 3/1/17.
  */
trait Conf {
  //这样写即使是代码尝试去创建多个SparkSession也不会出问题,因为是getOrCreate
  val spark:SparkSession = SparkSession.builder.appName("winyangApp").master("local").getOrCreate()
  //只能这样？
  val sc:SparkContext = spark.sparkContext
  //简化代码
  protected def sql(sqlText:String):DataFrame = spark.sql(sqlText)
}

