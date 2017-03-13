/**
  * Created by winyang on 3/11/17.
  * 用于COOA单元测试
  */

import com.aithink.winyang.COOA._
import breeze.linalg._
import breeze.numerics.abs

class COOATest {


}

object COOATest {
  def main(args: Array[String]): Unit = {
    //    test_1()
        test_2()

  }


  def test_1(): Unit = {
    val test = new ExperienceTable
    val haha_1 = DenseVector(0.6, 0.5, 0.4)
    val haha_2 = DenseVector(0.3, 0.4, 0.3)
    val haha_3 = DenseVector(0.1, 0.4, 0.2)
    val haha_4 = DenseVector(0.3, 0.8, 0.04)
    val haha_5 = DenseVector(0.1, 0.9, 0.02)

    test.setMaxLength(3)

    //这个参数是要调试的
    test.setSiteSqaure(1)
    println(test.getEMTableLength)
    test.judgeAndAppendOrReplace(haha_1 / norm(haha_1, 2))
    println(test.getEMTableLength)
    test.judgeAndAppendOrReplace(haha_2 / norm(haha_2, 2))
    println(test.getEMTableLength)
    test.judgeAndAppendOrReplace(haha_3 / norm(haha_3, 2))
    println(test.getEMTableLength)
    test.judgeAndAppendOrReplace(haha_4 / norm(haha_4, 2))
    println(test.getEMTableLength)

    for (_ <- 1 to 100) {
      println(test.chooseOrThrow(haha_5 / norm(haha_5, 2)))
    }
  }

  def test_2(): Unit = {
    //    val A =  DenseVector(0.6, 0.5, 0.4)
    //    val B = DenseVector(0.6, 0.5, 0.4)
    //    val c = DenseMatrix(A).reshape(3,1) * DenseMatrix(B)
    //    c.toArray.foreach(println(_))
        val a = new CollectiveThinking
        a.setCurrentIdeasAndNP(Array(DenseVector(0.6, 0.5, 0.4),DenseVector(0.7, 0.4, 0.3),DenseVector(0.2, 0.1, 0.1)))
        a.setMyFitnessFun((a:DenseVector[Double])=>sum(a))
        val c =  a.chooseLearningObject
    c.foreachValue(x=>println(x))


  }
}
