package com.incomm.cclp.account.domain.event.subscriber;

import java.util.List;

import com.incomm.cclp.account.domain.event.DomainEvent;

public interface DomainEventSubscriber {

	public boolean isSubscribed(DomainEvent domainEventClass);

	public void handle(List<DomainEvent> events);

	public void handle(DomainEvent events);

}
