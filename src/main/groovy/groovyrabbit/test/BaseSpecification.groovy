package groovyrabbit.test

import spock.lang.Specification
import groovyrabbit.dsl.RabbitBuilder

class BaseSpecification extends Specification{

	def getNewBuilder(){
		def conf = [
			host:"localhost"
		]
		def builder = new RabbitBuilder(conf)
		builder
	}
}
