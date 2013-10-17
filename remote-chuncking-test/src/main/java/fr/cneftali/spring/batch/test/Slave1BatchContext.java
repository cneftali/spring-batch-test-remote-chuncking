package fr.cneftali.spring.batch.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import fr.cneftali.spring.batch.common.items.TestItemWriter;
import fr.cneftali.spring.batch.slave.conf.Slave1BatchConfiguration;

public class Slave1BatchContext extends AbstractStartable<Integer> {

	private static final Logger LOGGER = LoggerFactory.getLogger(Slave1BatchContext.class);

	@Override
	public Integer call() throws Exception {

		try {
			applicationContext = new AnnotationConfigApplicationContext(Slave1BatchConfiguration.class);
			return 0;
		} catch (final Exception e) {
			LOGGER.error("**** Error initializing Slave context", e);
			throw new RuntimeException(e);
		}
	}

	public int writtenCount() {
		final TestItemWriter writer = applicationContext.getBean("writer", TestItemWriter.class);
		return writer.getChunkCount();
	}
}
