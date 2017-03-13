package com.aithink.winyang.coreprogram

import java.io.{File, PrintWriter}

import com.aithink.winyang.mytrait.HDFSMysqlTrait
import org.apache.spark.sql.DataFrame

import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.recommendation.ALS

/**
  * Created by winyang on 3/2/17.
  */
class CollaborativeFiltering extends HDFSMysqlTrait {
  private def loadTermInfoAndRegiste: Unit = HDFSTool.loadResultParquetFileAndCreateTempView("stationport_termname_description_new")

  private def loadConsumeInfoAndRegiste: Unit = HDFSTool

  def DataPrepare(): DataFrame = {
    loadTermInfoAndRegiste
    sql("select distinct TERMID from  stationport_termname_description_new where avg_offer between 3 and 30")
  }

  //把消费地点数据按平均消费金额来划分,区间为1
  def getTermIdDistributeAndSave: Unit = {
    loadTermInfoAndRegiste
    val result = for (i <- 0 to 200) yield {
      (sql("select distinct TERMID from  stationport_termname_description_new where avg_offer between " + i + " and " + (i + 1)).count, i, i + 1)
    }
    val filePath = "/export/home/winyang/project/resultdata/termid_count_avg_consume_distribute"
    val writer = new PrintWriter(new File(filePath))
    result.foreach(x => writer.println(x._1 + "," + x._2 + "," + x._3))
    writer.close()
  }

  def getExpTeridAndSave(): Unit = {
    loadTermInfoAndRegiste
    val collaborative_filtering_termid_termname = sql("select distinct TERMID,TERMNAME from  stationport_termname_description_new where avg_offer between 1.75 and 17.5")
    collaborative_filtering_termid_termname.write.parquet("/user/winyang/data/collaborative_filtering_termid_termname.parquet")
  }
//介于某个金额之间的消费次数分布情况,这区间取得有问题
  def filterConsumeInfoAndSave(): Unit = {
    val arr = (0 to 200).toArray
    val table = "/user/winyang/data/stu_consume_filter_some_temid.parquet"
    val filteredConsumeTable = spark.read.parquet(table)
    filteredConsumeTable.cache()
    filteredConsumeTable.createOrReplaceTempView("filteredConsumeTable")
    sc.broadcast(filteredConsumeTable)
    val result = arr.map(x => sql("""select count(OFFER) as count ,"""" + x * 0.5 + """" as  loverbound ,"""" + (x * 0.5 + 0.5) +"""" as upperbound from filteredConsumeTable where OFFER > """ + x * 0.5 + " and  OFFER <" + (x * 0.5 + 0.5))).reduce(reducerTool.unionTableReducer).collect()
    val filePath = "/export/home/winyang/project/resultdata/stu_consume_count_distribute"
    val writer = new PrintWriter(new File(filePath))
    result.map(x=>(x(0),x(1),x(2))).foreach(x => writer.println(x._1+","+x._2+","+x._3))
    writer.close()
  }
//每个学生在一个端口的消费次数的分布情况
  def filterTable1Distibute():Unit={
    val filter_table_1 = "/user/winyang/data/outid_termid_count_1.parquet"
    val ratings = spark.read.parquet(filter_table_1)
    ratings.createOrReplaceTempView("filter_table_1")
    val arr = (0 to 420).toArray
    sc.broadcast(ratings)
    val result = arr.map(x => sql("""select count(*) as count ,"""" + x + """" as  bound  from rating where count = """ +x)).reduce(reducerTool.unionTableReducer).collect()
    val filePath = "/export/home/winyang/project/resultdata/stu_termid_distribute"
    val writer = new PrintWriter(new File(filePath))
    result.map(x=>(x(0),x(1))).foreach(x => writer.println(x._1+","+x._2))
    writer.close()
  }




}
