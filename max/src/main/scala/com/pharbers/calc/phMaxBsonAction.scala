package com.pharbers.calc

import com.pharbers.common.algorithm.max_path_obj
import com.pharbers.delivery.util.ConfigMongo
import com.pharbers.driver.PhRedisDriver
import com.pharbers.pactions.actionbase._
import com.pharbers.sercuity.Sercurity

object phMaxBsonAction {
    def apply[T](args: pActionArgs = NULLArgs): pActionTrait = new phMaxBsonAction[T](args)
}

class phMaxBsonAction[T](override val defaultArgs: pActionArgs) extends pActionTrait with ConfigMongo {
    override val name: String = "max_bson_action"

    override def perform(prMap: pActionArgs): pActionArgs = {

        val max_result = prMap.asInstanceOf[MapArgs].get("max_calc_action").asInstanceOf[DFArgs].get
        val panelName = defaultArgs.asInstanceOf[StringArgs].get
        val result_location = max_path_obj.p_maxPath + panelName
        val redisDriver = new PhRedisDriver()
        //TODO : uid暂时写死,供测试
        val uid = "uid"
        val company = redisDriver.getMapValue(uid, "company")
        val ym = redisDriver.getMapValue(panelName,"ym")
        val mkt = redisDriver.getMapValue(panelName,"mkt")
        val userJobsKey = Sercurity.md5Hash(s"$uid$company")
        val singleJobKey = Sercurity.md5Hash(s"$uid$company$ym$mkt")
        redisDriver.addSet(userJobsKey,singleJobKey)
        redisDriver.addMap(singleJobKey, "max_path", result_location)
        val max_count = max_result.count()
        redisDriver.addMap(singleJobKey, "max_count", max_count)
        max_result.write
                .format("csv")
                .option("header", value = true)
                .option("delimiter", 31.toChar.toString)
                .option("codec", "org.apache.hadoop.io.compress.GzipCodec")
                .save(result_location)

        max_result.write.format("com.mongodb.spark.sql.DefaultSource").mode("overwrite")
            .option("uri", s"mongodb://$mongodbHost:$mongodbPort/")
            .option("database", "Max_Test")
            .option("collection", singleJobKey)
            .save()

        StringArgs(panelName)
    }
}