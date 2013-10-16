package fr.cneftali.spring.batch.test;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
public class RemoteChunkingIntegrationTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(RemoteChunkingIntegrationTest.class);

	@Test
	public void testRemoteChunking() throws Exception {
		 final BrokerContext broker = new BrokerContext();
		 final MasterBatchContext masterBatchContext = new MasterBatchContext();
		 final SlaveBatchContext slaveBatchContext1 = new SlaveBatchContext();
		 final SlaveBatchContext slaveBatchContext2 = new SlaveBatchContext();
		 broker.start();
		 masterBatchContext.start();
		 slaveBatchContext1.start();
		 slaveBatchContext2.start();
		 
		 BatchJobTestHelper.waitForJobTopComplete(masterBatchContext);

         final BatchStatus batchStatus = masterBatchContext.getBatchStatus();
         LOGGER.info("job finished with status: " + batchStatus);
         assertEquals("Batch Job Status", BatchStatus.COMPLETED, batchStatus);
         LOGGER.info("slave 1 chunks written: " + slaveBatchContext1.writtenCount() );
         LOGGER.info("slave 2 chunks written: " + slaveBatchContext2.writtenCount() );
	}
}
