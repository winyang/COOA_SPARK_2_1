package com.aithink.winyang.COOA

import breeze.linalg.{*, DenseMatrix, DenseVector}
import breeze.numerics.abs

import scala.util.Random

/**
  * Created by winyang on 3/6/17.
  */
class CollectiveThinking {
  //思考者个数,原则上每个思考着有一个idea
  private var currentIdeas = Array[DenseVector[Double]]()
  private var myFitnessFun: (DenseVector[Double]) => Double = _
  private var NT: Int = _

  private var A: DenseMatrix[Double] = _
  private var proMaxtrix: DenseMatrix[Double] = _

  def setCurrentIdeasAndNP(currentIdeas: Array[DenseVector[Double]]): Unit = {
    setCurrentIdeas(currentIdeas)
    this.NT = this.currentIdeas.length
  }

  private def setCurrentIdeas(currentIdeas: Array[DenseVector[Double]]): Unit = {
    this.currentIdeas = currentIdeas
  }
  private def changeCurrentIdea(newIdea:DenseVector[Double],indexOfThinker:Int):Unit={
    this.currentIdeas(indexOfThinker-1) = newIdea
  }

  def setMyFitnessFun(f: (DenseVector[Double]) => Double): Unit = {
    this.myFitnessFun = f
  }

  def getCurrentIdeas: Array[DenseVector[Double]] = {
    this.currentIdeas
  }

  private def fitnessFun(idea: DenseVector[Double]): Double = {
    myFitnessFun(idea)
  }

  private def getFitnessValues: DenseVector[Double] = {
    DenseVector(currentIdeas.map(idea => fitnessFun(idea)))
  }

  private def getProMaxtrix: DenseMatrix[Double] = {
    this.proMaxtrix
  }

  private def computeInfluenceMatrix: DenseMatrix[Double] = {
    val fitnessValues = getFitnessValues
    (DenseMatrix(DenseVector.ones[Double](NT)).reshape(NT, 1) * DenseMatrix(fitnessValues) -
      DenseMatrix(fitnessValues).reshape(NT, 1) * DenseMatrix(DenseVector.ones[Double](NT))) / (abs(DenseVector.ones[Double](NT) + 1e-100) dot fitnessValues)
  }

  private def getMaxtrixA: DenseMatrix[Double] = {
    this.A
  }

  private def setMaxtrixA(): Unit = {
    val A: DenseMatrix[Double] = computeInfluenceMatrix :/ (DenseMatrix(DenseVector.ones[Double](NT)).reshape(NT, 1) * (DenseMatrix(DenseVector.ones[Double](NT)).reshape(1, NT) * abs(computeInfluenceMatrix)))
    this.A = A
  }

  private def setProMaxtrix(): Unit = {
    this.proMaxtrix = abs(A)
  }

  //对于每个思考者,需要选择3个非自己的学习对象,返回的是一个矩阵(NT,3),每一行都是一个思考者的学习集合,可以再做优化
  private def chooseLearningObject: DenseMatrix[Int] = {
    DenseMatrix.tabulate(NT, 3) { case (i, _) => randomOneResult(getProMaxtrix(i, ::).t) }
  }

  //选取好了学习对象之后就开始学习了,学习结果是直接改变当前每个idea做修正
  def learning(): Unit = {
    val LearningObject = chooseLearningObject
    //不然的话改动原数列会改动后面赋值的数列的值(不使用clone的话)
    val oldIdeas = getCurrentIdeas.clone()
    (1 to NT).foreach(x => howToLearning(x,oldIdeas,LearningObject(x,::).t))
  }

  //学习使用学习对象向量,去学习,学完了就去修正自己idea,由于修改了自己的idea那么学习前就需要保留一份
  //indexOfThinker是标号从1开始
  private def howToLearning(indexOfThinker: Int, oldIdeas: Array[DenseVector[Double]], learningObject: DenseVector[Int]): Unit = {
    var result = DenseVector[Double]()
    val Ia = oldIdeas(learningObject(0))
    val Ib = oldIdeas(learningObject(1))
    val Ic = oldIdeas(learningObject(2))
    val parameter_a = getMaxtrixA(learningObject(0), indexOfThinker - 1)
    val parameter_b = getMaxtrixA(learningObject(1), indexOfThinker - 1)
    val parameter_c = getMaxtrixA(learningObject(2), indexOfThinker - 1)
    if (parameter_a > 0) {
      result = Ia + parameter_a * (Ia - oldIdeas(indexOfThinker - 1)) + (parameter_b - parameter_c) * (Ib - Ic)
    } else {
      result = oldIdeas(indexOfThinker - 1) + parameter_a * (Ia - oldIdeas(indexOfThinker - 1)) + (parameter_b - parameter_c) * (Ib - Ic)
    }

    //检查通过的话就更改自己当前的idea
    if(check(result,indexOfThinker)){
      changeCurrentIdea(result,indexOfThinker)
    }
  }

  //  //给定一个概率向量(和为1),随机出3个下标(随机出来的下标的概率为给定向量位置的值),下标由一个向量返回,可以重复！
  //  private  def randSrc(proVector: DenseVector[Double]):DenseVector[Int]={
  //    DenseVector(1 to 3).map(_=>randomOneResult(proVector))
  //  }

  //拿到一组值,就去随机一个0,1之间的数,然后根据随机的数去取相应下标
  private def randomOneResult(myVector: DenseVector[Double]): Int = {
    var random = Random.nextDouble()
    var result: Int = 0
    myVector.foreach(x => {
      random -= x
      if (random > 0) {
        result += 1
      }
    })
    result
  }

  //indexOfThinker从1开始的
  def check(idea: DenseVector[Double], indexOfThinker: Int): Boolean = {
    //边界限定
    val myIdea = TestFun.sigmodFun(idea)
    if (TestFun.getFitnessFun(myIdea) <= getFitnessValues(indexOfThinker - 1)) {
      true
    }else{
      false
    }
  }

}
