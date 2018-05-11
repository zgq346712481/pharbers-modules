package com.pharbers.calc

import java.util.UUID

import com.pharbers.common.algorithm.max_path_obj
import com.pharbers.common.excel.input.PhExcelXLSXCommonFormat
import com.pharbers.pactions.actionbase.pActionTrait
import com.pharbers.pactions.generalactions.{csv2DFAction, jarPreloadAction, saveCurrenResultAction, xlsxReadingAction}
import com.pharbers.pactions.jobs.{sequenceJob, sequenceJobWithMap}

/**
  * Created by jeorch on 18-5-3.
  */
object phMaxJobForPfizerCNS_R {
    def apply(arg_panel_name: String, universe_file_name: String) : phMaxJobForPfizerCNS_R = {
        new phMaxJobForPfizerCNS_R {
            override lazy val panel_name: String = arg_panel_name
            override lazy val universe_name: String = universe_file_name
        }
    }
}

trait phMaxJobForPfizerCNS_R extends sequenceJobWithMap {
    override val name: String = "phMaxCalcJob"

    val panel_name: String
    val universe_name: String

    val panel_file: String = max_path_obj.p_panelPath + panel_name
    val universe_file: String = max_path_obj.p_matchFilePath + universe_name
    val temp_dir: String = max_path_obj.p_cachePath + panel_name + "/"
    val temp_universe_name: String = UUID.randomUUID().toString

    /// 留做测试
    val temp_panel_name: String = UUID.randomUUID().toString

    // 1. load panel data
    val loadPanelData = new sequenceJob {
        override val name: String = "panel_data"
        override val actions: List[pActionTrait] = csv2DFAction(panel_file) :: Nil
    }

    /// 留做测试
    // 1. load panel data of xlsx
    val loadPanelDataOfExcel = new sequenceJob {
        override val name = "panel_data"
        override val actions: List[pActionTrait] =
            xlsxReadingAction[PhExcelXLSXCommonFormat](panel_file, temp_panel_name) ::
                saveCurrenResultAction(temp_dir + temp_panel_name) ::
                csv2DFAction(temp_dir + temp_panel_name) :: Nil
    }


    // 2. read universe file
    val readUniverseFile = new sequenceJob {
        override val name = "universe_data"
        override val actions: List[pActionTrait] =
            xlsxReadingAction[PhExcelXLSXCommonFormat](universe_file, temp_universe_name) ::
                saveCurrenResultAction(temp_dir + temp_universe_name) ::
                csv2DFAction(temp_dir + temp_universe_name) :: Nil
    }


    override val actions: List[pActionTrait] = jarPreloadAction() ::
        loadPanelData ::
//            loadPanelDataOfExcel ::
        readUniverseFile ::
        phMaxSplitAction() ::
        phMaxGroupAction() ::
        phMaxCalcActionForCNS_R() ::
        phMaxBsonAction() ::
        Nil
}