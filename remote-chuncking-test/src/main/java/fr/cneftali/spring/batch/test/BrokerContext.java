package fr.cneftali.spring.batch.test;

import org.apache.activemq.broker.BrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BrokerContext extends AbstractStartable<Integer> {

	private static final Logger LOGGER = LoggerFactory.getLogger(BrokerContext.class);
	
	@Configuration
	static class BrokerConf {
		
		@Bean(name = "brokerService", initMethod = "start", destroyMethod = "stop")
		public BrokerService brokerService() {
			final BrokerService brokerService = new BrokerService();
			brokerService.setBrokerName("localhost");
			brokerService.setUseJmx(false);
			brokerService.setUseShutdownHook(false);
			brokerService.setPersistent(false);
			brokerService.setTransportConnectorURIs(new String[] {"tcp://localhost:61616"});
			return brokerService; 
		}
	}
	
	@Override
	public Integer call() throws Exception {
		
		try {
			applicationContext = new AnnotationConfigApplicationContext(BrokerConf.class);
			return 0;
		} catch (final Exception e) {
			LOGGER.error("**** Error initializing broker context", e);
			throw new RuntimeException(e);
		}
	}
}
