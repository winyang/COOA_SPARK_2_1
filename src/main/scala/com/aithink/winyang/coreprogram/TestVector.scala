package com.aithink.winyang.coreprogram

import org.apache.spark.mllib.linalg._
import com.aithink.winyang.mytrait.HDFSMysqlTrait
import org.apache.spark.mllib.linalg.distributed._
import org.apache.spark.mllib.stat.Statistics

/**
  * Created by winyang on 3/1/17.
  */
class TestVector extends HDFSMysqlTrait{

  def test() = {
    val arr = Array(1.0,2,3,4)
    var vec=Vectors.dense(arr)
    val rdd=sc.makeRDD(Seq(Vectors.dense(arr),Vectors.dense(arr.map(_*10)),Vectors.dense(arr.map(_*100))))
    val mat: RowMatrix = new RowMatrix(rdd)
    val m = mat.numRows()
    val n = mat.numCols()
    val sum=Statistics.colStats(rdd)
    println(sum.mean)
  }
}
