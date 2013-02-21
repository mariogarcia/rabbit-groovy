package groovyrabbit.util

/**
 * Instances of this class are able to send messages to a queue or to
 * a given exchange 
**/
class Sender extends ChannelAware{

	static final NULL = null
	static final EMPTY = ""

	/**
	 * This method sends the messages passed as a parameter to a given queue
	 * 
	 * @param message
	**/
	def send(message){
        channel.basicPublish(EMPTY,queueName,NULL,message?.toString()?.getBytes())
	}

	/**
	 * This method publishes the messages passed as parameter to a given exchange with
	 * a given routingKey
	 * 
	 * @param message
	 * @param howManyTimes
	**/
	def publish(message,howManyTimes = 1){
		(1..howManyTimes){
			channel.basicPublish(exchangeName,routingKey,NULL,message?.toString()?.getBytes())
		}
	}

}
