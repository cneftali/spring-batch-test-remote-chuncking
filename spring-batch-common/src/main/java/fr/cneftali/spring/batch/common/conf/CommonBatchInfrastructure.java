package fr.cneftali.spring.batch.common.conf;

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class CommonBatchInfrastructure implements BatchConfigurer {

	public static final String NOOP_TRANSACTION_MANAGER = "noopTransactionManager";
	private static final String ISOLATION_LEVEL_FOR_CREATE = "ISOLATION_DEFAULT";
	
	@Bean
	public MapJobRepositoryFactoryBean jobRepositoryFactoryBean() throws Exception {
		final MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean(getTransactionManager());
		factory.setIsolationLevelForCreate(ISOLATION_LEVEL_FOR_CREATE);
		factory.afterPropertiesSet();
		return factory;
	}

	@Bean
	public JobRepository getJobRepository() throws Exception {
		return (JobRepository) jobRepositoryFactoryBean().getObject();
	}

	@Bean(name = NOOP_TRANSACTION_MANAGER)
	public PlatformTransactionManager getTransactionManager() throws Exception {
		return new ResourcelessTransactionManager();
	}

	@Bean
	public MapJobExplorerFactoryBean jobExplorer() throws Exception {
		final MapJobExplorerFactoryBean factoryBean = new MapJobExplorerFactoryBean(jobRepositoryFactoryBean());
		factoryBean.afterPropertiesSet();
		return factoryBean;
	}

	@Bean
	public MapJobRegistry jobRegistry() {
		return new MapJobRegistry();
	}

	@Bean
	public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() throws Exception {
		final JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
		jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry());
		jobRegistryBeanPostProcessor.afterPropertiesSet();
		return jobRegistryBeanPostProcessor;
	}

	@Bean
	public JobLauncher getJobLauncher() throws Exception {
		final SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(getJobRepository());
		jobLauncher.afterPropertiesSet();
		return jobLauncher;
	}

	@Bean
	public JobOperator jobOperator() throws Exception {
		final SimpleJobOperator simpleJobOperator = new SimpleJobOperator();
		simpleJobOperator.setJobExplorer((JobExplorer) jobExplorer().getObject());
		simpleJobOperator.setJobRepository(getJobRepository());
		simpleJobOperator.setJobRegistry(jobRegistry());
		simpleJobOperator.setJobLauncher(getJobLauncher());
		simpleJobOperator.afterPropertiesSet();
		return simpleJobOperator;
	}
}
