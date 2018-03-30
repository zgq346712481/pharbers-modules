package com.pharbers.panel

import com.pharbers.baseModules.PharbersInjectModule

/**
  * Created by clock on 17-9-21.
  */
object panel_path_obj extends PharbersInjectModule {
    override val id: String = "panel_config"
    override val configPath: String = "pharbers_config/panel_config.xml"
    override val md = "base_path" :: "client_file_path" ::
                    "product_match_file" :: "markets_match_file" :: "universe_file" ::
                    "not_published_hosp_file" :: "fill_hos_data_file" ::
                    "source_dir" :: "output_dir" :: "cache_dir" :: Nil

    val p_base_path: String = config.mc.find(p => p._1 == "base_path").get._2.toString
    val p_client_path: String = config.mc.find(p => p._1 == "client_file_path").get._2.toString

    val p_product_match_file: String = config.mc.find(p => p._1 == "product_match_file").get._2.toString
    val p_markets_match_file: String = config.mc.find(p => p._1 == "markets_match_file").get._2.toString
    val p_universe_file: String = config.mc.find(p => p._1 == "universe_file").get._2.toString
    val p_not_published_hosp_file: String = config.mc.find(p => p._1 == "not_published_hosp_file").get._2.toString
    val p_fill_hos_data_file: String = config.mc.find(p => p._1 == "fill_hos_data_file").get._2.toString

    val p_source_dir: String = config.mc.find(p => p._1 == "source_dir").get._2.toString
    val p_output_dir: String = config.mc.find(p => p._1 == "output_dir").get._2.toString
    val p_cache_dir: String = config.mc.find(p => p._1 == "cache_dir").get._2.toString
}