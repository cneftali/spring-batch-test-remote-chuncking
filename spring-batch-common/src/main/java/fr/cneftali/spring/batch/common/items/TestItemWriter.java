package fr.cneftali.spring.batch.common.items;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import fr.cneftali.spring.batch.common.entities.Request;

public class TestItemWriter implements ItemWriter<Request> {

	private static final Logger logger = LoggerFactory.getLogger(TestItemWriter.class);

	/**
	 * Counts the number of chunks processed in the handler.
	 */
	public volatile static int count = 0;
	
	private int chunkCount = 0;
	
	private String writerName = "master-writer";
	
	@Override
	public void write(List<? extends Request> items) throws Exception {
		for (final Request request : items) {
			count++;
			logger.info("**** [" + writerName + "] writing item: " + request);
		}
		chunkCount ++;

	}
	
	public int getChunkCount() {
		return chunkCount;
	}
	
	public void setWriterName(String writerName) {
		this.writerName = writerName;
	}
}
