package fr.cneftali.spring.batch.master;

import java.util.Date;

import org.apache.activemq.broker.BrokerService;
import org.junit.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import fr.cneftali.spring.batch.master.conf.MasterBatchConfiguration;

public class MasterBatchTestITG {

	@Configuration
	@Import(MasterBatchConfiguration.class)
	static class JavaConfig {

		@Bean(destroyMethod = "stop", initMethod = "start")
		public BrokerService broker() throws Exception {
			final BrokerService broker = new BrokerService();
			broker.setBrokerName("localhost");
			broker.setUseJmx(false);
			broker.setUseShutdownHook(false);
			broker.setPersistent(false);
			broker.setTransportConnectorURIs(new String[] { "tcp://localhost:61616" });
			return broker;
		}
	}

	@Test
	public void test() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		final AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(JavaConfig.class);
			annotationConfigApplicationContext.registerShutdownHook();
			
			
			final JobLauncher jobLauncher = annotationConfigApplicationContext.getBean("jobLauncher", JobLauncher.class);
			final Job job = annotationConfigApplicationContext.getBean("testJob", Job.class);
			
			
			final JobParametersBuilder builder = new JobParametersBuilder();
			builder.addDate("startTime", new Date());
			jobLauncher.run(job, builder.toJobParameters());
		
	}
}
