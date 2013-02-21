package groovyrabbit.exchange

import spock.lang.Timeout

import groovyrabbit.test.BaseSpecification
import groovyrabbit.util.*

class ExchangeSpec extends BaseSpecification{

	@Timeout(20)
	def "Create an exchange"(){
		setup: "Create an exchange and 5 queues"
			def exchangeName = 'exchange1'
			def message = getClass().simpleName.toLowerCase() << "createanexchange"
			def channel = newBuilder.build{
				exchange(name: exchangeName,type:'fanout'){
					queue(name:"queue1",routingKey:"example")
				}
			}
		when: "Creating a sender"
			def sender = new Sender(channel:channel,exchangeName:exchangeName,routingKey:"example")
		and: "Creating a receiver per each channel"
			def receivers = [
				new Receiver(channel:channel,queueName:"queue1",routingKey:"example")
			]
		and: "Publishing 5 messages"
			sender.publish(message,5)
		then: "We should receive the same message in all queues"
			receivers.every{r-> r.message == message.toString()}
	}
}
