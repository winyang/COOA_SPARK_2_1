package com.aithink.winyang.COOA

import breeze.linalg.{DenseVector, norm}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by winyang on 3/6/17.
  * 发散思维发散的结果此时通过收敛思维来处理
  */
class ConvergenceThinking {

  //所有的发散思维产生的idea都先装载到如下ideas中
  def getNewIdeas(ideas:ArrayBuffer[DenseVector[Double]],currentFitness: Double, ideaNow: DenseVector[Double]): DenseVector[Double] = {
    //求函数极小值,所以是小于
    val candidates = ideas.filter(TestFun.getFitnessFun(_) < currentFitness)
    //弄一个曼哈顿距离最大的
    candidates.reduce((x: DenseVector[Double], y: DenseVector[Double]) => {
      if (manhattanDistance(x, ideaNow) < manhattanDistance(y, ideaNow)) y else x
    })
  }

  private def manhattanDistance(A: DenseVector[Double], B: DenseVector[Double]): Double = {
    norm(A - B, 1)
  }

  private def selfDefinedDistance(A: DenseVector[Double], B: DenseVector[Double]): Double = {
    (A - B).toArray.min
  }



}
