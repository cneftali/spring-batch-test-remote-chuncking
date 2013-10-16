package fr.cneftali.spring.batch.slave.conf;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import fr.cneftali.spring.batch.common.conf.CommonBatchInfrastructure;

@Configuration
@Import({ CommonBatchInfrastructure.class })
@ImportResource({ "classpath:fr/cneftali/spring/batch/slave/conf/slave-integration-context.xml" })
public class SlaveBatchConfiguration {

	@Bean
	public ActiveMQConnectionFactory connectionFactory() {		
		return new ActiveMQConnectionFactory("tcp://localhost:61616");
	}

}
