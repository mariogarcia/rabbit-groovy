package groovyrabbit.util

class Sender extends ChannelAware{

	def send(message){
        channel.basicPublish("",queueName,null,message?.toString()?.getBytes())
	}

	def publish(message,howManyTimes){
			channel.basicPublish(exchangeName,routingKey,null,message?.toString()?.getBytes())
	}

}
