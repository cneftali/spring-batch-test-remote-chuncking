package fr.cneftali.spring.batch.slave.conf;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.batch.core.step.item.SimpleChunkProcessor;
import org.springframework.batch.integration.chunk.ChunkProcessorChunkHandler;
import org.springframework.batch.item.support.PassThroughItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jms.connection.CachingConnectionFactory;

import fr.cneftali.spring.batch.common.entities.Request;
import fr.cneftali.spring.batch.common.items.TestItemWriter;

@Configuration
@ImportResource({ "classpath:fr/cneftali/spring/batch/slave/conf/slave-integration-context.xml" })
public class Slave1BatchConfiguration {

	@Bean
	public ActiveMQConnectionFactory amqConnectionFactory() {		
		return new ActiveMQConnectionFactory("tcp://localhost:61616");
	}
	
	@Bean
	public PooledConnectionFactory amqPoolConnectionFactory() {
		return new PooledConnectionFactory(amqConnectionFactory());
	}
	
	// A cached connection to wrap the ActiveMQ connection
	@Bean
	public CachingConnectionFactory connectionFactory() {
		 final CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(amqPoolConnectionFactory());
		 cachingConnectionFactory.setSessionCacheSize(100);
		 cachingConnectionFactory.setCacheProducers(true);
		 cachingConnectionFactory.setCacheConsumers(true);
		 return cachingConnectionFactory;
	}

	@Bean
	public SimpleChunkProcessor<Request, Request> simpleChunkProcessor() {
		final SimpleChunkProcessor<Request, Request> simpleChunkProcessor = new SimpleChunkProcessor<>(new PassThroughItemProcessor<Request>(), writer());
		return simpleChunkProcessor;
	}
	
	@Bean(name = "chunkHandler")
	public ChunkProcessorChunkHandler<Request> chunkHandler() {
		final ChunkProcessorChunkHandler<Request> chunkHandler = new ChunkProcessorChunkHandler<>();
		chunkHandler.setChunkProcessor(simpleChunkProcessor());
		return chunkHandler;
	}
	
	@Bean
	public TestItemWriter writer() {
		final TestItemWriter writer = new TestItemWriter();
		writer.setWriterName("remote-slave-1-writer");
		return writer;
	}
}
