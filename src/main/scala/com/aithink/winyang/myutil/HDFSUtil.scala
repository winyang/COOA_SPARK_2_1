package com.aithink.winyang.myutil

import com.aithink.winyang.jobinit.Conf
import org.apache.spark.sql.DataFrame

/**
  * Created by winyang on 3/1/17.
  */
class HDFSUtil extends Conf{
  private var originalDataPath = "/user/fengxiang/ecust_newStatistics/"
  private var testDataPath = "/user/fengxiang/testdata/"
  private var resultDataPath = "/user/fengxiang/data/temp/"

  def setOriginalDataPath(originalDataPath: String): Unit = {
    this.originalDataPath = originalDataPath
  }

  def setTestDataPath(testDataPath: String): Unit = {
    this.testDataPath = testDataPath
  }

  def setResultDataPath(resultDataPath: String): Unit = {
    this.resultDataPath = resultDataPath
  }

  def getOriginalDataPath: String = {
    this.originalDataPath
  }

  def getTestDataPath: String = {
    this.testDataPath
  }

  def getResultDataPath: String = {
    this.resultDataPath
  }

  private def getParquetPath(tableName: String,path: String) = path + tableName + ".parquet"

  def getOriginalParquetPath(tableName:String):String = originalDataPath + tableName + ".parquet"
  def getTestParquetPath(tableName:String):String = testDataPath + tableName + ".parquet"
  def getResultParquetPath(tableName:String):String = resultDataPath + tableName + ".parquet"

  //从相应路径读取指定表忽略后缀名
  private def getParquetFile(tableName: String, path: String): DataFrame = {
    spark.read.parquet(getParquetPath(tableName,path))
  }


  //直接从相应路径读取,只需要表名(不需要后缀)
  def getOriginalParquetFile(tableName:String): DataFrame = {
    getParquetFile(tableName,originalDataPath)
  }

  def getTestParquetFile(tableName:String): DataFrame = {
    getParquetFile(tableName,testDataPath)
  }

  def getResultParquetFile(tableName:String): DataFrame = {
    getParquetFile(tableName,resultDataPath)
  }


  def loadOriginalParquetFileAndCreateTempView(tableName:String):Unit={
    getOriginalParquetFile(tableName).createTempView(tableName)
  }
  def loadTestParquetFileAndCreateTempView(tableName:String):Unit= {
    getTestParquetFile(tableName).createTempView(tableName)
  }
  def loadResultParquetFileAndCreateTempView(tableName:String):Unit= {
    getResultParquetFile(tableName).createTempView(tableName)
  }

  }
