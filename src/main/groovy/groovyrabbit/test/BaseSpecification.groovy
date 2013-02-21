package groovyrabbit.test

import spock.lang.Specification
import groovyrabbit.dsl.RabbitBuilder

/**
 * All specifications that want to use a RabbitBuilder should be inheriting
 * this class and making use of the getNewBuilder() method
**/
class BaseSpecification extends Specification{

	/**
	 * This method builds a new instance of a RabbitBuilder object
	**/
	def getNewBuilder(){
		def conf = [
			host:"localhost"
		]
		def builder = new RabbitBuilder(conf)
		builder
	}
}
