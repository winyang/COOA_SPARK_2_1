package com.aithink.winyang.COOA

import breeze.linalg.DenseVector

import scala.util.Random

/**
  * Created by winyang on 3/6/17.
  * 接收一个idea然后产生发散
  * 拥有发散sigemaSquare_max 与 sigemaSquare_min参数的控制权,以及sfactor因子的 直接设置权
  *外部反馈的是fitness值
  * 内部会拿到这次和上次的fitness值,然后调节sigema从而影响下一次发散思维
  */
class DivergentThinking {
  //初始为 最大与最小的除以2 其实就是最大的一半
  private var sigemaNow: Double = _
  private var sigemaMax: Double = _
  private var sigemaMin: Double = _
  private var sFactor: Double = _
  private var dNum: Int = _
  //最大非法发散思考次数
  private var INum:Int = _

  //初始没有尝试
  private var numOfTry:Int = 0
  private var numOfInvalidThinking:Int = 0

  //行为只能是加或者清零
  private def setNumOfTry(action:String):Unit={
    if(action.equals("plus")){
      numOfTry += 1
    }else if(action.equals("reset")){
      numOfTry = 0
    }else{
      println("undefined action !")
    }
  }

  private def setNumOfInvalidThinking(action:String):Unit={
    if(action.equals("plus")){
      numOfInvalidThinking += 1
    }else if(action.equals("reset")){
      numOfInvalidThinking = 0
    }else{
      println("undefined action !")
    }
  }

  def initParameters(sigemaMax: Double,sigemaMin: Double,sFactor: Double,dNum: Int,Inum: Int):Unit={
    setSigemaMax(sigemaMax)
    setSigemaMin(sigemaMin)
    setSFactor(sFactor)
    setDNum(dNum)
    setINum(Inum)
    //这个初始化定义为最大sigema的一半
    sigemaNowInit()
  }

  private def setSigemaMax(sigemaMax: Double): Unit = {
    this.sigemaMax = sigemaMax
  }

  private def setSigemaMin(sigemaMin: Double): Unit = {
    this.sigemaMin = sigemaMin
  }

  //sigema变化因子
  private def setSFactor(sFactor: Double): Unit = {
    this.sFactor = sFactor
  }

  //发散思维产生idea数量
  private def setDNum(dNum: Int): Unit = {
    this.dNum = dNum
  }

  private def setINum(Inum: Int): Unit = {
    this.INum = INum
  }

  private def setSigemaNow(sigemaNew: Double): Unit = {
    this.sigemaNow = sigemaNew
  }

  //初始化让最大的2分之一作为creative variance,之后也可以通过set方法来set
  private def sigemaNowInit(): Unit = {
    this.sigemaNow = this.sigemaMax / 2
  }

//这些细节的测试抛出get方法是为了实验,可用于监控里面关键参数的变化
  def getSigemaNow: Double = {
    this.sigemaNow
  }

  def getNumOfTry:Int={
    this.numOfTry
  }

  def getNumOfInvalidThinking:Int={
    this.numOfInvalidThinking
  }

//对于外部就是需要拿到发散思维的结果
  def constructDivergenceIdeas(idea: DenseVector[Double]): Array[DenseVector[Double]] = {
    (1 to dNum).toArray.map(_ => getOneNewIdea(idea))
  }

  //拿的时候直接可以拿,但是写的时候要使用set取写！
  //这是调节SigemaNow 的函数,每次发散思维前应该都是改变过得
  //外部只有适应度值传入,然后发散思维根据这个做计算,修改相应值
  def adapterSigemaNow(lastFitnessValue:Double,currentFitnessValue:Double): Unit ={
    if(currentFitnessValue<lastFitnessValue){
      setSigemaNow(sigemaNow/sFactor)
      if(sigemaNow > sigemaMax){
        setSigemaNow(sigemaMax)
      }
    }else{
      setNumOfInvalidThinking("plus")
    }
    if(numOfInvalidThinking == INum ){
      setSigemaNow(sigemaNow*sFactor)
      setNumOfTry("plus")
      if(sigemaNow < sigemaMin){
        setSigemaNow(sigemaMin)
      }
      setNumOfInvalidThinking("reset")
    }
  }


  private def getOneNewIdea(idea: DenseVector[Double]): DenseVector[Double] = {
    idea.map(x => x +getGuassianRandomVariable(sigemaNow))
  }

  //产生均值未0 ,给定方差的随机数
  private def getGuassianRandomVariable(sigema: Double):Double={
    val myrandom = new Random()
    (sigema*sigema)*myrandom.nextGaussian()
  }

}
