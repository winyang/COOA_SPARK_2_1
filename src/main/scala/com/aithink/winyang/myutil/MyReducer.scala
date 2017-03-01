package com.aithink.winyang.myutil

import org.apache.spark.sql.DataFrame

/**
  * Created by winyang on 3/1/17.
  */
class MyReducer {
  def unionTableReducer:(DataFrame,DataFrame)=>DataFrame=(x:DataFrame,y:DataFrame)=>x.union(y)
}
