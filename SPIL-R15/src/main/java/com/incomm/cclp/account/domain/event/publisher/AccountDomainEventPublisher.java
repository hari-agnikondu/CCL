package com.incomm.cclp.account.domain.event.publisher;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.incomm.cclp.account.domain.event.DomainEvent;
import com.incomm.cclp.account.domain.event.subscriber.DomainEventSubscriber;

@Service("accountDomainEventPublisher")
public class AccountDomainEventPublisher implements DomainEventPublisher {

	private Set<DomainEventSubscriber> domainEventSubscribers;

	public AccountDomainEventPublisher() {
		this.domainEventSubscribers = new HashSet<>();
	}

	@Override
	public void addSubscriber(final DomainEventSubscriber subscriber) {
		this.domainEventSubscribers.add(subscriber);
	}

	@Override
	public void removeSubscriber(final DomainEventSubscriber domainEventSubscriber) {
		this.domainEventSubscribers.remove(domainEventSubscriber);
	}

	@Override
	public void raise(DomainEvent event) {
		this.domainEventSubscribers.forEach(subscriber -> subscriber.handle(event));
	}

	@Override
	public void raise(List<DomainEvent> events) {
		this.domainEventSubscribers.forEach(subscriber -> subscriber.handle(events));
	}

	/*
	 * package private method to aid the unit test of the class.
	 */
	Collection<DomainEventSubscriber> getSubscribers() {
		return Collections.unmodifiableCollection(domainEventSubscribers);
	}

}
