package com.xwj.publish_event;

import org.springframework.context.Lifecycle;
import org.springframework.context.LifecycleProcessor;

public class MyLifecycle implements LifecycleProcessor {

	@Override
	public void start() {
	}

	@Override
	public void stop() {

	}

	@Override
	public boolean isRunning() {
		return false;
	}

	@Override
	public void onRefresh() {

	}

	@Override
	public void onClose() {

	}
}
