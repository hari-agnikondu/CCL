package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

public class GroupRoleId implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	Role role;
	
	Group group;

	@ManyToOne(fetch=FetchType.LAZY)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
	
	@Override
	public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        GroupRoleId that = (GroupRoleId) obj;
        
        return Objects.equals(role, that.role) && 
                Objects.equals(group, that.group);
    }
	
	@Override
	public int hashCode() {
		return Objects.hash(role, group);
    }
}
