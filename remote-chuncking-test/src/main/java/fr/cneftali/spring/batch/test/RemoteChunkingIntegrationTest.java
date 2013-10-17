package fr.cneftali.spring.batch.test;

import static junit.framework.Assert.assertEquals;
import static org.springframework.batch.core.BatchStatus.COMPLETED;

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
		@SuppressWarnings("unused")
		final BrokerContext broker = (BrokerContext) new BrokerContext().start();
		final MasterBatchContext masterBatchContext = new MasterBatchContext();
		final Slave1BatchContext slaveBatchContext1 = new Slave1BatchContext();
		final Slave2BatchContext slaveBatchContext2 = new Slave2BatchContext();
		
		masterBatchContext.start();
		
		slaveBatchContext1.start();
		slaveBatchContext2.start();

		BatchJobTestHelper.waitForJobTopComplete(masterBatchContext);

		final BatchStatus batchStatus = masterBatchContext.getBatchStatus();
		LOGGER.info("job finished with status: " + batchStatus);
		assertEquals("Batch Job Status", COMPLETED, batchStatus);
		LOGGER.info("slave 1 chunks written: " + slaveBatchContext1.writtenCount() );
		LOGGER.info("slave 2 chunks written: " + slaveBatchContext2.writtenCount() );
	}
}
