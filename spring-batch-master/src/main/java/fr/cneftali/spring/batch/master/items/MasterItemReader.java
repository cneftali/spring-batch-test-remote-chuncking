package fr.cneftali.spring.batch.master.items;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import fr.cneftali.spring.batch.common.entities.Request;

public class MasterItemReader implements ItemReader<Request>{

	private static final Logger logger = LoggerFactory.getLogger(MasterItemReader.class);
	private final List<Request> requests = new ArrayList<>();
	public  volatile int count = 0;
	
	public MasterItemReader() {
		for (int cpt = 0; cpt < 50; cpt ++) {
			final Request request = new Request(cpt, "item " + cpt);
			requests.add(request);
		}
	}
	
	@Override
	public Request read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if (requests.size() > count) {
			final Request request = requests.get(count++);
			logger.info("******* for count " + count + ", reading item: " + request);
			return request;
		} else {
			logger.info("******* returning null for count " + count);
			return null;
		}
	
	}
}
