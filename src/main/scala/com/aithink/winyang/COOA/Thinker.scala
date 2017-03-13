package com.aithink.winyang.COOA

import org.apache.spark.mllib.linalg.Vector

/**
  * Created by winyang on 3/11/17.
  * 每一个thinker维护自己的idea ,选择适当时机来交换idea
  * 每一个思考者都具有收敛,发散,集体,灵感思维以及记忆
  * 前面的思维都是改变自己idea的途径,而记忆代表着群体记忆
  * 每一个Think都可以把自己的idea加入到记忆里面（若审核通过）,
  * 每做一轮都需要把自己向table提交记忆请求,并行计算的时候其实这个Table就是一个Broadcast变量
  *
  *
  */
object Thinker {

  var idea: Vector = _

  //初始化
  def setIdea(idea: Vector): Unit = {
    this.idea = idea
  }


}
