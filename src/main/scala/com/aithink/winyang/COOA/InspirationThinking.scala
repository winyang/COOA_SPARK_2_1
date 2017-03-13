package com.aithink.winyang.COOA

/**
  * Created by winyang on 3/6/17.
  */
class InspirationThinking {
  //连续尝试3次就会触发一次跳跃性思维,也就是验证成功就会返回正确(触发),否则错误(不触发)
  //这应该也是一个参数
  private val maxTry: Int = 3

  private def verifyInspirationThinkingCondition(numOfTry: Int): Boolean = {
    if (numOfTry >= maxTry) {
      //触发
      true
    } else {
      false
    }
  }

  def getNewSigemaNow(sigemaNow: Double, sigemaMax: Double,numOfTry: Int): Double = {
      if(verifyInspirationThinkingCondition(numOfTry)){
        sigemaMax
      }else{
        sigemaNow
      }
  }
}
