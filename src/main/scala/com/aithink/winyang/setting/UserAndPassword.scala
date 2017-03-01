package com.aithink.winyang.setting

import java.util.Properties

/**
  * Created by winyang on 3/1/17.
  */
class UserAndPassword {

  //类型
  private case class sqlInfo(user: String, password: String, port: String, dataBase: String, ip: String, setting: String)

  //组合 url and setting
  private def constructMysqlUrlAndSetting(mysqlInfo: sqlInfo): String = "jdbc:mysql://" + mysqlInfo.ip + ":" + mysqlInfo.port + "/" + mysqlInfo.dataBase + "?" + mysqlInfo.setting

  //组合 prop
  private def constructMysqlProp(mysqlInfo: sqlInfo): Properties = {
    val prop = new Properties
    prop.setProperty("user", mysqlInfo.user)
    prop.setProperty("password", mysqlInfo.password)
    prop
  }

  //配置表
  private val mysqlConfigures = Map(
    "ecustStudent_ecustStudent" -> sqlInfo("ecustStudent", "winyang1994", "3306", "bbt_1", "c0108", "useUnicode=true&characterEncoding=utf8&useSSL=false")
  )

  private var mysqlConfigure: sqlInfo = mysqlConfigures("ecustStudent_ecustStudent")

  //选择Mysql配置
  def setMysqlConfigure(dbOptionName: String): Unit = {
    mysqlConfigure = mysqlConfigures("dbOptionName")
  }
  //生成的配置
  def mysqlProp: Properties = constructMysqlProp(mysqlConfigure)
  def mysqlUrlAndSetting: String = constructMysqlUrlAndSetting(mysqlConfigure)


}
