package com.aithink.winyang.coreprogram

import com.aithink.winyang.mytrait.StructureStreamingTrait
import org.apache.spark.sql.functions._


/**
  * Created by winyang on 3/1/17.
  */
//流处理
class StructureStreaming extends StructureStreamingTrait{
  def test():Unit={
    val lines = spark.readStream
      .format("socket")
      .option("host", "localhost")
      .option("port", 9999 )
      .load()

    //隐式转换
    import spark.implicits._
    val words = lines.as[String].flatMap(_.split("\n")).filter(_.contains("|")).flatMap(_.split("\\|"))
    val wordCounts = words.groupBy("value").count()

    // Start running the query that prints the running counts to the console
    val query = wordCounts.writeStream
      .outputMode("complete")
      .format("console")
      .start()

    query.awaitTermination()
  }

}
