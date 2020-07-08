package com.incomm.cclp.account.domain.model;

import com.incomm.cclp.account.application.command.Command;
import com.incomm.cclp.account.domain.view.DomainView;

import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class AbstractAggregateRoot<V extends DomainView, C extends Command> {

	public abstract V process(C command);

}
