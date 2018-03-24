package com.pharbers.panel.nhwa

import com.pharbers.panel.common.calcYMAction
import com.pharbers.paction.actionContainer.pActionContainer
import com.pharbers.paction.actionbase.{MapArgs, SingleArgFuncArgs, pActionTrait}
import com.pharbers.paction.format.input.common.PhExcelXLSXCommonFormat
import com.pharbers.paction.funcTrait.{jarPreloadTrait, saveCurrenResultTrait, xlsxReadingTrait}
import com.pharbers.panel.format.input.writable.nhwa.PhXlsxCpaWritable

trait NhwaYMActions extends pActionContainer {
    val cpa_location = "/mnt/config/FileBase/8ee0ca24796f9b7f284d931650edbd4b/Client/171215恩华2017年10月检索.xlsx"
    val cpa_location2 = "/mnt/config/FileBase/8ee0ca24796f9b7f284d931650edbd4b/Client/171115恩华2017年9月检索.xlsx"

    val fy : PhXlsxCpaWritable => String = _.getRowKey("YEAR")
    val fm : PhXlsxCpaWritable => String = _.getRowKey("MONTH")
    val fc : PhXlsxCpaWritable => String = _.getRowKey("HOSPITAL_CODE")
    val m : MapArgs = MapArgs(Map(
        "fy" -> SingleArgFuncArgs[PhXlsxCpaWritable, String](fy),
        "fm" -> SingleArgFuncArgs[PhXlsxCpaWritable, String](fm),
        "fc" -> SingleArgFuncArgs[PhXlsxCpaWritable, String](fc)
    ))

    val file_local_10 = "resource/test-01.xlsx"
    val file_local_01 = "resource/test-03.xlsx"
    val file_local_all = "resource/test-02.xlsx"
    val file_local2 = "/mnt/config/FileBase/8ee0ca24796f9b7f284d931650edbd4b/Client/171215恩华2017年10月检索.xlsx"

    override val actions: List[pActionTrait] = jarPreloadTrait() ::
                xlsxReadingTrait[PhExcelXLSXCommonFormat](file_local_all) ::
                calcYMAction(m) ::
                saveCurrenResultTrait("resource/result") ::
                Nil
} 