package groovyrabbit.simple

import spock.lang.Timeout

import groovyrabbit.util.*
import groovyrabbit.test.BaseSpecification

import spock.lang.Specification

class SimpleIntegrationSpec extends BaseSpecification{

	@Timeout(20)
	void "Send and receive a message"(){
		setup: "Creating a sender, receiver and monitor"
			def channel = newBuilder.build{
				queue(name:'test')
			}
		when: "Passing the exchange to both objects"
			def sender = new Sender(channel:channel,queueName:'test')
			def receiver = new Receiver(channel:channel,queueName:'test')
		and: "sending the message"
			sender.send("Yo")
		then: "The message should be the expected"
			receiver.message == 'Yo'
	}
}
