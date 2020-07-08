package com.incomm.cclp.account.domain.event.publisher;

import java.util.List;

import com.incomm.cclp.account.domain.event.DomainEvent;
import com.incomm.cclp.account.domain.event.subscriber.DomainEventSubscriber;

public class TestDomainEventSubscriber implements DomainEventSubscriber {

	@Override
	public boolean isSubscribed(DomainEvent domainEventClass) {
		// empty method for test
		return false;
	}

	@Override
	public void handle(List<DomainEvent> events) {
		// empty method for test

	}

	@Override
	public void handle(DomainEvent events) {
		// empty method for test
	}

}
