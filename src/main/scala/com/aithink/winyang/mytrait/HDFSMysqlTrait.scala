package com.aithink.winyang.mytrait

import com.aithink.winyang.jobinit.Conf
import com.aithink.winyang.myutil.{HDFSUtil, MyReducer, TransformDataToMysql}

/**
  * Created by winyang on 3/1/17.
  */
class HDFSMysqlTrait extends Conf{
    protected val HDFSTool = new HDFSUtil
    protected val reducerTool = new MyReducer
    protected val mysqlTool = new TransformDataToMysql
}
