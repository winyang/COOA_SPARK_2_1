package com.aithink.winyang.COOA

import breeze.linalg.DenseVector

/**
  * Created by winyang on 3/6/17.
  */
object TestFun {
  //目标评价函数
  var myFitnessFun:(DenseVector[Double])=>Double = _

  def setMyFitnessFun(f:(DenseVector[Double])=>Double) :Unit={
    this.myFitnessFun = f
  }

  def getFitnessFun(idea: DenseVector[Double]): Double = {
    myFitnessFun(idea)
  }

  //这个需要塞进去一个边界处理函数
  def sigmodFun(vector:DenseVector[Double]):DenseVector[Double]={
    vector
  }



}
