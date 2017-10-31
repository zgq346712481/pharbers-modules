package com.pharbers.panel.util.excel

import java.util.UUID

import com.pharbers.panel.util.csv.phHandleCsv
import com.pharbers.panel.util.excel.handle.{phReadExcelHandle, phWriteExcelHandle}
import com.pharbers.panel.util.phDataHandle

import scala.collection.immutable.Map

/**
  * Created by clock on 17-10-23.
  */
object phHandleExcel {
    val filterFun: Map[String, String] => Boolean = { _ => true }
    val postFun: Map[String, String] => Option[Map[String, String]] = { tr => Some(tr) }
}

case class phHandleExcel() extends ReadExcelTrait with WriteExcelTrait with ReadExcelToCache

trait ReadExcelTrait extends phDataHandle {
    def getCount(file_local: String): Int = phReadExcelHandle(file_local).getCount

    def readExcel(arg: phExcelData)
                 (implicit filterFun: Map[String, String] => Boolean,
                  postFun: Map[String, String] => Option[Map[String, String]]): List[Map[String, String]] = {
        new phReadExcelHandle(arg.file_local) {
            override val fieldMap = arg.fieldArg
            override val defaultValueMap = arg.defaultValueArg

            override def processFun(): Option[Map[String, String]] = {
                var tr = titleMap.keys.map { t => (titleMap(t), rowMap(t)) }.toMap
                if (filterFun(tr)) {
                    tr.foreach { x =>
                        x._2 match {
                            case s: String if s == "" => tr += x._1 -> getDefaultValue(x._1, tr)
                            case _: String => Unit // 必须有，处理超多后面空数据还持续读取问题
                            case _ => Unit
                        }
                    }
                    tr = postFun(tr).getOrElse(throw new Exception("data parse error => postFun error"))
                    Some(tr)
                } else {
                    None
                }
            }
        }.process(arg.sheetId, arg.sheetName)
    }
}

trait WriteExcelTrait extends phDataHandle {
    def writeByList(content: List[Map[String, Any]], output_file: String,
                    sheet: String = "Sheet1", cellNumArg: Map[String, Int] = Map()): Boolean = {
        def getWriteSeq(row: Map[String, Any]): Map[String, Int] = {
            var i = -1
            row.map { x =>
                i += 1
                x._1 -> i
            }
        }

        implicit val writeSeq = cellNumArg.size match {
            case 0 => getWriteSeq(content.head)
            case _ => cellNumArg
        }
        phWriteExcelHandle(output_file).write(content, sheet)
        true
    }
}

trait ReadExcelToCache extends phDataHandle {
    def readExcelToCache(arg: phExcelData, splitKey: String = "")
                        (implicit cache_base: String,
                         filterFun: Map[String, String] => Boolean,
                         postFun: Map[String, String] => Option[Map[String, String]]): (Map[String, String], List[String]) = {
        var splitMap: Map[String, String] = Map()
        var titleLst: List[String] = Nil

        new phReadExcelHandle(arg.file_local) {
            override val fieldMap = arg.fieldArg
            override val defaultValueMap = arg.defaultValueArg
            implicit val titleSeq = Nil

            override def processFun(): Option[Map[String, String]] = {
                var tr = titleMap.keys.map { t => (titleMap(t), rowMap(t)) }.toMap
                tr.foreach { x =>
                    x._2 match {
                        case s: String if s == "" => tr += x._1 -> getDefaultValue(x._1, tr)
                        case _: String => Unit // 必须有，处理超多后面空数据还持续读取问题
                        case _ => Unit
                    }
                }
                tr = postFun(tr).getOrElse(throw new Exception("parse xlsx error => postFun error"))

                val file_key = tr.get(splitKey) match {
                    case Some(value) => value
                    case None => "NoSplitKey"
                }

                val append_local = splitMap.get(file_key) match {
                    case Some(value) => value
                    case None => cache_base + UUID.randomUUID.toString + ".cache"
                }
                if (filterFun(tr)){
                    phHandleCsv().appendByLine(tr, append_local)
                    splitMap += file_key -> append_local
                    titleLst = tr.keys.toList
                }
                None
            }
        }.process(arg.sheetId, arg.sheetName)

        (splitMap, titleLst)
    }
}