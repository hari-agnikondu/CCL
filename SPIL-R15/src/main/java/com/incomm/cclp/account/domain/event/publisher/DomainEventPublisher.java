package com.incomm.cclp.account.domain.event.publisher;

import java.util.List;

import com.incomm.cclp.account.domain.event.DomainEvent;
import com.incomm.cclp.account.domain.event.subscriber.DomainEventSubscriber;

public interface DomainEventPublisher {

	public void addSubscriber(DomainEventSubscriber subscriber);

	public void removeSubscriber(DomainEventSubscriber subscriber);

	public void raise(DomainEvent event);

	public void raise(List<DomainEvent> events);
}
