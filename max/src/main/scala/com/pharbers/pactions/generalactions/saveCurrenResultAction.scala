package com.pharbers.pactions.generalactions

import scala.reflect.ClassTag
import com.pharbers.pactions.actionbase.{RDDArgs, StringArgs, pActionArgs, pActionTrait}

object saveCurrenResultAction {
    def apply[T : ClassTag](arg_path: String,
                            arg_name: String = "saveCurrenResultJob") : pActionTrait =
        new saveCurrenResultAction[T](StringArgs(arg_path), arg_name)
}

class saveCurrenResultAction[T : ClassTag](override val defaultArgs: pActionArgs,
                                           override val name: String) extends pActionTrait {

    override implicit def progressFunc(progress : Double, flag : String) : Unit = {}

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {
        val rdd = args.asInstanceOf[RDDArgs[T]].get
        rdd.repartition(1).saveAsTextFile(defaultArgs.asInstanceOf[StringArgs].get)
        args
    }
}