package fr.cneftali.spring.batch.slave.conf;

import java.util.UUID;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.batch.core.step.item.SimpleChunkProcessor;
import org.springframework.batch.integration.chunk.ChunkProcessorChunkHandler;
import org.springframework.batch.item.support.PassThroughItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import fr.cneftali.spring.batch.common.entities.Request;
import fr.cneftali.spring.batch.common.items.TestItemWriter;

@Configuration
@ImportResource({ "classpath:fr/cneftali/spring/batch/slave/conf/slave-integration-context.xml" })
public class SlaveBatchConfiguration {

	@Bean
	public ActiveMQConnectionFactory connectionFactory() {		
		return new ActiveMQConnectionFactory("tcp://localhost:61616");
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
		writer.setWriterName("remote-slave-" +  UUID.randomUUID() + "-writer");
		return writer;
	}
}
