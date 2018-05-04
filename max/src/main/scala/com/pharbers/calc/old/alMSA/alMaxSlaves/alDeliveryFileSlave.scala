package com.pharbers.calc.old.alMSA.alMaxSlaves

import akka.pattern.ask

import scala.concurrent.duration._
import com.pharbers.calc.old.alMSA.alClusterLister.alAgentIP.masterIP
import akka.actor.SupervisorStrategy.Restart
import akka.actor.{Actor, ActorLogging, OneForOneStrategy, Props, SupervisorStrategy}
import akka.util.Timeout
import com.pharbers.calc.old.alCalcHelp.alLog.alTempLog
import com.pharbers.calc.old.alMSA.alCalcAgent.alPropertyAgent.takeNodeForRole
import com.pharbers.calc.old.alMSA.alCalcMaster.alCalcMsg.generateDeliveryFile.{generateDeliveryFileHand, generateDeliveryFileImpl}

import scala.concurrent.Await

object alDeliveryFileSlave {
	def props: Props = Props[alDeliveryFileSlave]
	def name = "delivery-file-slave"
}

class alDeliveryFileSlave extends Actor with ActorLogging {
	override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
		case _ => Restart
	}
	
	def delivery: Receive = {
		case generateDeliveryFileHand() =>
			implicit val t: Timeout = Timeout(2 seconds)
			val a = context.actorSelection("akka.tcp://calc@"+ masterIP +":2551/user/agent-reception")
			val f = a ? takeNodeForRole("splitdeliveryslave")
			if (Await.result(f, t.duration).asInstanceOf[Boolean])
				sender ! generateDeliveryFileHand()
			else Unit
			
		case generateDeliveryFileImpl(uid, company, listJob) =>
			val counter = context.actorOf(alCommonErrorCounter.props)
			val cur = context.actorOf(alDeliveryFileComeo.props(uid, listJob, counter))
			cur.tell(generateDeliveryFileImpl(uid, company, listJob), sender)
			
		case msg => alTempLog(s"Warning! Message not delivered. alDeliveryFileSlave.received_msg=$msg")
	}
	
	override def receive: Receive = delivery
}
