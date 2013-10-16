package fr.cneftali.spring.batch.test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.context.ApplicationContext;

public abstract class AbstractStartable<V> implements Callable<V> {

	private final ExecutorService executorService = Executors.newFixedThreadPool(1);
	Future<V> future;
	protected ApplicationContext applicationContext;

	public void shutdown() {
		executorService.shutdown();
		executorService.shutdownNow();
		if (! executorService.isTerminated() && ! executorService.isShutdown() ) {
			throw new RuntimeException("application context is not shutdown correctly");
		}
	}

	public AbstractStartable<V> start() {
		future = executorService.submit((Callable<V>) this);
		return this;
	}

	public Future<V> getFuture() {
		return future;
	}
}