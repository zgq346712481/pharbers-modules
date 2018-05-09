package com.pharbers.panel.common

import com.pharbers.common.algorithm.max_path_obj
import com.pharbers.pactions.actionbase._

object phSavePanelJob  {
    def apply(args: MapArgs) : pActionTrait = new phSavePanelJob(args)
}

class phSavePanelJob(override val defaultArgs: pActionArgs) extends pActionTrait {
    override val name: String = "phSavePanelJob"
    override def perform(pr : pActionArgs): pActionArgs = {

        val panel = pr.asInstanceOf[MapArgs].get("panel").asInstanceOf[DFArgs].get
        val panel_name = defaultArgs.asInstanceOf[MapArgs].get("name").asInstanceOf[StringArgs].get
        val panel_location = max_path_obj.p_resultPath + panel_name

        panel.coalesce(1).write
                .format("csv")
                .option("header", value = true)
                .option("delimiter", 31.toChar.toString)
                .save(panel_location)

        StringArgs(panel_name)
    }

}