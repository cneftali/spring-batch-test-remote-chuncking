package fr.cneftali.spring.batch.test;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import fr.cneftali.spring.batch.master.conf.MasterBatchConfiguration;

public class MasterBatchContext extends AbstractStartable<BatchStatus> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MasterBatchContext.class);
	private final String JOB_NAME = "testJob";
	private BatchStatus batchStatus;

	@Override
	public BatchStatus call() throws Exception {
		try {
			applicationContext = new AnnotationConfigApplicationContext(MasterBatchConfiguration.class);
			final JobLauncher jobLauncher = (JobLauncher) applicationContext.getBean("jobLauncher");
			final JobParameter jobParam = new JobParameter(UUID.randomUUID().toString());
			final Map<String, JobParameter> params = Collections.singletonMap("param1", jobParam);
			final Job job = applicationContext.getBean(JOB_NAME, Job.class);
			LOGGER.info("**** Master batch context running job " + job);
			final JobExecution jobExecution = jobLauncher.run(job, new JobParameters(params));
			batchStatus = jobExecution.getStatus();
			LOGGER.info("**** Master batch context finished running job: " + batchStatus);
			return batchStatus;
		} catch (final Exception e) {
			LOGGER.error("**** Error initializing master context", e);
			throw new RuntimeException(e);
		}
	}
	
	public BatchStatus getBatchStatus() {
		return batchStatus;
	}
}
