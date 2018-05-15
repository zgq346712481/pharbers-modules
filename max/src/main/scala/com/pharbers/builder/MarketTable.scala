package com.pharbers.builder

trait MarketTable {

    val nhwa_mz = Map(
        "company" -> "5afa53bded925c05c6f69c54",
        "subsidiary" -> "恩华",
        "market" -> "麻醉市场",
        "ymInstance" -> "com.pharbers.panel.nhwa.phNhwaCalcYMJob",
        "panelInstance" -> "com.pharbers.panel.nhwa.phNhwaPanelJob",
        "maxInstance" -> "com.pharbers.calc.phMaxJob",

        "source" -> "cpa",
        "panelArgs" -> "not_published_hosp_file#universe_file#product_match_file#fill_hos_data_file#markets_match_file",
        "not_published_hosp_file" -> "nhwa/2017年未出版医院名单.xlsx",
        "universe_file" -> "nhwa/universe_麻醉市场_online.xlsx",
        "product_match_file" -> "nhwa/nhwa匹配表.xlsx",
        "fill_hos_data_file" -> "nhwa/补充医院.xlsx",
        "markets_match_file" -> "nhwa/通用名市场定义.xlsx"
    )

    val marketTable: List[Map[String, String]] = nhwa_mz :: Nil

}
