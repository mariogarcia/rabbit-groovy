package groovyrabbit.dsl

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Connection
import com.rabbitmq.client.Channel

/**
 * This builder is just for learning purposes, it doesn't pretend to 
 * be a serious implementation.
 *
 * The builder allows you to create queues and exchanges or both if you
 * want to link them each other.
 *
 * @author mariogarcia
 *
**/
class RabbitBuilder{

	def conf
	def connectionFactory
	def connection
	def channel
	def queueName
	def exchangeName

	/**
  	 * This class builds standalone queues or queues
	 * related to a given exchange
	**/
	static class QueueBuilder{

		def channel
		def exchange 

		QueueBuilder(ch,ex){
			channel = ch 
			exchange = ex
		}

		/**
		 * This method creates/connects to a queue. The queue
		 * may be related to a given exchange
		 * 
		 * @param params A map containing the required parameters
		 * @return the channel
		**/
		def queue(params){
			this.channel.queueDeclare(
				params.name,
				params.durable?:true,
				params.exclusive?:true,
				params.autodelete?:true,
				params.arguments?:[:]
			)
		 /* If this node is nested inside an exchange, we will be 
			using the exchange data to link the queue to it */
			if (exchange){
				this.channel.queueBind(
					params.name,
					exchange,
					params.routingKey
				)
			}
			this.channel
		}

	}

	/**
	 * The builder needs to know where is RabbitMQ running
	 * 
	 * @param conf A map with just one key:value --> host:"hostname"
	**/
	def RabbitBuilder(conf){
		this.conf = conf
		this.connectionFactory = new ConnectionFactory(host:conf.host)
		this.connection = connectionFactory.newConnection()
		this.channel = connection.createChannel()
	}

	/**
	 * This method builds an exchange with a possible nested queue
	 * 
	 * @param params A map of params (name,type)
	 * @param closure A closure containing a queue 
	 * @return channel
	**/
	def exchange(params,closure){
	 /* We cannot declare an exchange if we declared a queue previously with 
		the same builder */
		if (queueName){
			throw new Exception("Can't declare the exchange after declaring the queue")
		}
		this.channel.exchangeDeclare(params.name,params.type)
		def queueBuilder = new QueueBuilder(channel,params.name)
		queueBuilder.with(closure)
		this.channel
	}
	
	/**
	 * This method builds an exchange
	 * 
	 * @param params A map of params (name,type)
	 * @param closure A closure containing a queue 
	 * @return channel
	**/
	def exchange(params){
		this.exchangeName = params.name
		this.channel.exchangeDeclare(params.name,params.type)
		this
	}

	/**
	 * This method creates or connects to a given queue
	**/
	def queue(params){
		new QueueBuilder(channel,null).queue(params)
		this.channel
	}

	/**
	 * Entry of the builder
	 *
	 * @param closure
	 * @return channel
	 *
	**/
	def build(Closure closure){
		this.with(closure)
		this.channel
	}

}
