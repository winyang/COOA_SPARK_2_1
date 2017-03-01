package com.aithink.winyang.myutil

import com.aithink.winyang.setting.UserAndPassword
import org.apache.spark.sql.{DataFrame, SaveMode}

/**
  * Created by winyang on 3/1/17.
  */
class TransformDataToMysql {
    private val configureTool = new UserAndPassword

    //目前只是把数据写到Mysql
    def transFormDataIntoMysql(result: DataFrame, tableName: String): Unit = {
      configureTool.setMysqlConfigure("ecustStudent_ecustStudent")
      result.write.mode(SaveMode.Append).jdbc(configureTool.mysqlUrlAndSetting, tableName,configureTool.mysqlProp )
    }


}
