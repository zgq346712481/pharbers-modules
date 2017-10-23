//package pfizer
//
//import java.text.SimpleDateFormat
//import java.util.Date
//
//import com.pharbers.panel.pfizer.impl.phPfizerHandleImpl
//import org.scalatest.FunSuite
//import play.api.libs.json.{JsObject, JsString, JsValue}
//
//import scala.collection.immutable.Map
//
///**
//  * Created by clock on 17-9-7.
//  */
//class PfizerSuite extends FunSuite {
//    val cpa_file_local = "config/company/Client/CPA/1704 CPA.xlsx"//test.xlsx"//
//    val gycx_file_local = "config/company/Client/GYCX/1704 GYC.xlsx"//test2.xlsx"//
//    val args: Map[String, List[String]] = Map(
//        "company" -> List("company"),
//        "user" -> List("user"),
//        "cpas" -> List(cpa_file_local),
//        "gycxs" -> List(gycx_file_local)
//    )
//
//    test("calc ym") {
//        val data_parse = new phPfizerHandleImpl(args)
//        val yms = data_parse.calcYM.asInstanceOf[JsString].value
//        println(yms)
//    }
//
//    test("generate panel file") {
//        val dateformat1 = new SimpleDateFormat("MM-dd HH:mm:ss")
//        println(s"生成panel测试开始时间" + dateformat1.format(new Date()))
//        val result = new phPfizerHandleImpl(args).getPanelFile("201705" :: Nil)
//        println(result)
//        println(s"生成panel测试结束时间" + dateformat1.format(new Date()))
//    }
//
//    test("Pressure test => 50") {
//        val dateformat1 = new SimpleDateFormat("MM-dd HH:mm:ss")
//        println(s"压力测试开始时间" + dateformat1.format(new Date()))
//
//        for(i <- 1 to 50) {
//            val args: Map[String, List[String]] = Map(
//                "company" -> List("generatePanel"),
//                "user" -> List("user"+i),
//                "cpas" -> List(cpa_file_local),
//                "gycxs" -> List(gycx_file_local)
//            )
//            val parse = new phPfizerHandleImpl(args)
//            val ym = parse.calcYM.asInstanceOf[JsString].value
//            val result = parse.getPanelFile(ym :: Nil)
//            println(s"第$i 个完成结果$result")
//        }
//
//        println(s"压力测试结束时间" + dateformat1.format(new Date()))
//    }
//}
