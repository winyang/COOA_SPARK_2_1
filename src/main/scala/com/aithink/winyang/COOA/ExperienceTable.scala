package com.aithink.winyang.COOA

import java.text.NumberFormat

import breeze.numerics. exp
import breeze.linalg.{DenseVector, norm}


//如果这个表还要变大怎么办,需要采用什么数据结构？
import scala.collection.mutable.ArrayBuffer


/**
  * Created by winyang on 3/6/17.
  * 记忆维护模块者,需要决定什么记忆Table的大小,同时控制审核条件
  * 每次申请加入的idea都需要接受审核(或者小于某个数量时候不需要审核,足够多了才审核)
  * 这个记忆模块是每个Thinker都有的
  */
class ExperienceTable {
  //这里的table使用缓冲数组[idea]的形式存储,里面的idea要求不一样(初步起码要求)
  private val eMTable = ArrayBuffer[DenseVector[Double]]()
  private var maxLength: Int = _
  private var sitaSquare: Double = _

  //用于设定记忆表长度
  def setMaxLength(eMTableLength: Int): Unit = {
    this.maxLength = eMTableLength
  }

  def setSiteSqaure(sitaSquare:Double):Unit={
    this.sitaSquare = sitaSquare
  }

  //往记忆表中增加idea
  private def addIdea(idea: DenseVector[Double]): Unit = {
    this.eMTable += idea
  }

  //没找到返回-1
  private def useIdeaGetIdeaIndex(idea: DenseVector[Double]): Int = {
    val result = this.eMTable.zipWithIndex.filter(_._1.equals(idea))
    if (result.nonEmpty) {
      result(0)._2
    } else {
      -1
    }
  }

  //使用idea在记忆表中下标去删除记忆
  private def dropIdea(ideaIndex: Int): Unit = {
    this.eMTable.remove(ideaIndex)
  }

  def getEMTableLength: Int = {
    if(eMTable.nonEmpty){
      this.eMTable.length
    }else{
      0
    }
  }

  //每次thinker给自己记忆模块提交请求时候,条件判断是否提交成功
  //当当前记忆表小于最大记忆表长度的时候,基本没有条件限制
  //但是当记忆表等于最大设置长度的时候就需要来判断选取了
  //每次直接使用最新的淘汰最旧的
  //个人感觉这里还有东西做！
  def judgeAndAppendOrReplace(idea: DenseVector[Double]): Unit = {
    if (getEMTableLength <= maxLength-1) {
      addIdea(idea)
    } else {
      dropIdea(0)
      addIdea(idea)
    }
  }

  //用于过滤决定发散思维和收敛思维的idea是否需要进一步验证的条件,要保证不一样吗(不需要)
  //这里设置的合理性怎么解释？
  def chooseOrThrow(idea: DenseVector[Double]): Boolean = {
    val actSimilarity = eMTable.map(x=>exp(norm(x - idea,2)/(-2*sitaSquare))).sum/getEMTableLength
    val random = getRandom
    if(actSimilarity > getRandom){
//      println(actSimilarity)
//      println(random)
      true
    }else{
//      println(actSimilarity)
//      println(random)
      false
    }
  }

  //用于生成0,1之间的随机数
  private def getRandom:Double={
    val n = NumberFormat.getInstance()
    //下数值是控制位数的
    n.setMaximumFractionDigits(3)
    n.format(Math.random()).toDouble
  }
}
