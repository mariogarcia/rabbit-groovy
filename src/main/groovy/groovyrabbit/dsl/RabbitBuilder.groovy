package groovyrabbit.dsl

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Connection
import com.rabbitmq.client.Channel

class RabbitBuilder{

	def conf
	def connectionFactory
	def connection
	def channel
	def queueName
	def exchangeName

	static class QueueBuilder{

		def channel
		def exchange 

		QueueBuilder(ch,ex){
			channel = ch 
			exchange = ex
		}

		def queue(params){
			this.channel.queueDeclare(
				params.name,
				params.durable?:true,
				params.exclusive?:true,
				params.autodelete?:true,
				params.arguments?:[:]
			)
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

	def RabbitBuilder(conf){
		this.conf = conf
		this.connectionFactory = new ConnectionFactory(host:conf.host)
		this.connection = connectionFactory.newConnection()
		this.channel = connection.createChannel()
	}

	def exchange(params,closure){
		if (queueName){
			throw new Exception("Can't declare the exchange after declaring the queue")
		}
		this.channel.exchangeDeclare(params.name,params.type)
		def queueBuilder = new QueueBuilder(channel,params.name)
		queueBuilder.with(closure)
		this.channel
	}
	
	def exchange(params){
		this.exchangeName = params.name
		this.channel.exchangeDeclare(params.name,params.type)
		this
	}

	def queue(params){
		new QueueBuilder(channel,null).queue(params)
		this.channel
	}

	def build(Closure closure){
		this.with(closure)
		this.channel
	}

}
