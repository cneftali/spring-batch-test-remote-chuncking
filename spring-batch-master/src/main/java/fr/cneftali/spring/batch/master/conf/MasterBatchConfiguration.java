package fr.cneftali.spring.batch.master.conf;

import static fr.cneftali.spring.batch.common.conf.CommonBatchInfrastructure.NOOP_TRANSACTION_MANAGER;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.integration.chunk.ChunkMessageChannelItemWriter;
import org.springframework.batch.integration.chunk.RemoteChunkHandlerFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.core.PollableChannel;
import org.springframework.transaction.PlatformTransactionManager;

import fr.cneftali.spring.batch.common.conf.CommonBatchInfrastructure;
import fr.cneftali.spring.batch.common.entities.Request;
import fr.cneftali.spring.batch.common.items.TestItemWriter;
import fr.cneftali.spring.batch.master.items.MasterItemReader;

@Configuration
@Import({ CommonBatchInfrastructure.class })
@ImportResource({ "classpath:fr/cneftali/spring/batch/master/conf/master-batch-integration.xml" })
public class MasterBatchConfiguration {

	@Autowired
	private StepBuilderFactory stepBuilders;
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	@Qualifier(NOOP_TRANSACTION_MANAGER)
	private PlatformTransactionManager noopTransactionManager;
	
	@Autowired
	private MessagingTemplate messagingGateway;
	
	@Autowired
	private PollableChannel replies;
	
	@Bean
	@StepScope
	public ChunkMessageChannelItemWriter<Request> chunkWriter() {
		final ChunkMessageChannelItemWriter<Request> writer = new ChunkMessageChannelItemWriter<>();
		writer.setMessagingOperations(messagingGateway);
		writer.setThrottleLimit(5);
		writer.setMaxWaitTimeouts(30000);
		writer.setReplyChannel(replies);

		return writer;
	}
	
	
	@Bean(name = "chunkHandler")
	public RemoteChunkHandlerFactoryBean<Request> chunkHandler() {
		final RemoteChunkHandlerFactoryBean<Request> chunkHandler = new RemoteChunkHandlerFactoryBean<>();
		chunkHandler.setChunkWriter(chunkWriter());
		chunkHandler.setStep(step1());
		return chunkHandler;
	}
	
	@Bean
	public TaskletStep step1() {
		return stepBuilders
				.get("step1")
				.transactionManager(noopTransactionManager)
				.<Request, Request>chunk(5)
				.reader(reader())
				.writer(writer())
				.build();
	}
	@Bean(name = "testJob")
	public Job job() {
		return jobBuilderFactory
				.get("testJob")
				.start(step1())
				.build();
	}
	
	@Bean
	@StepScope
	public MasterItemReader reader() {
		final MasterItemReader reader = new MasterItemReader();
		return reader;
	}
	
	@Bean
	@StepScope
	public TestItemWriter writer() {
		final TestItemWriter writer = new TestItemWriter();
		return writer;
	}
}
