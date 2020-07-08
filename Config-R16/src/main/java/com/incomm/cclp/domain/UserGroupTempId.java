package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class UserGroupTempId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	ClpUser user;
	
	Group group;

	@ManyToOne
	public ClpUser getUser() {
		return user;
	}

	public void setUser(ClpUser user) {
		this.user = user;
	}

	@ManyToOne
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

        UserGroupTempId that = (UserGroupTempId) obj;
        
        return Objects.equals(user, that.user) && 
                Objects.equals(group, that.group);
    }
	
	@Override
	public int hashCode() {
		return Objects.hash(user, group);
    }

}
