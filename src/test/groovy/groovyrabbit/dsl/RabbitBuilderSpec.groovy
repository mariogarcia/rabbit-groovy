package groovyrabbit.dsl

import spock.lang.Specification
import groovyrabbit.test.BaseSpecification
import com.rabbitmq.client.Channel

class RabbitBuilderSpec extends BaseSpecification{

	def "Create an exchange"(){
		setup: "Crating the builder instance"
			def conf = [host:"localhost"]
		when: "Building an exchange"
			def channel = newBuilder.build{
				exchange(name:'exchange',type:'direct')
			}
		then: "We need to make sure the exchange has been created properly"
			channel instanceof Channel 
	}
}
