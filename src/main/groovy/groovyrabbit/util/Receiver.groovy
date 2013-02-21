package groovyrabbit.util

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Connection
import com.rabbitmq.client.Channel
import com.rabbitmq.client.QueueingConsumer

/**
 * This class consumes messages from a given queue
**/
class Receiver extends ChannelAware{

	def getMessage(){
		def expectedMessage
		def received = false
    	def consumer = new QueueingConsumer(channel)

    	channel.basicConsume(queueName, false, consumer)

	    while (!received) {
    	  QueueingConsumer.Delivery delivery = consumer.nextDelivery()
	      expectedMessage = new String(delivery.getBody())
			received = true
    	}

		expectedMessage	
	}
}

