package com.incomm.cclp.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "clp_configuration.attribute_definition")
public class AttributeDefinition implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ATTRIBUTE_NAME")
	private String attributeName;

	@Column(name = "ATTRIBUTE_VALUE")
	private String attributeValue;

	@Column(name = "TYPE", updatable = false)
	private String type;

	@Column(name = "DESCRIPTION", updatable = false)
	private String description;

	@Column(name = "ATTRIBUTE_GROUP", updatable = false)
	private String attributeGroup;

	public String getAttributeName() {
		return attributeName;
	}

	public AttributeDefinition() {
	}

	public AttributeDefinition(String attributeName, String attributeValue) {
		this.attributeName = attributeName;
		this.attributeValue = attributeValue;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAttributeGroup() {
		return attributeGroup;
	}

	public void setAttributeGroup(String attributeGroup) {
		this.attributeGroup = attributeGroup;
	}
}
