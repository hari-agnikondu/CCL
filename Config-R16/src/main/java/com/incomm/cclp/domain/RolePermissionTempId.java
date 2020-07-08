package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Embeddable
public class RolePermissionTempId implements Serializable {

	private static final long serialVersionUID = 1L;

	 Role role;
	
	 Permission permission;

	 @ManyToOne(fetch = FetchType.LAZY) 
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@ManyToOne(fetch = FetchType.LAZY) 
	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}
	
	@Override
	public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        RolePermissionTempId that = (RolePermissionTempId) obj;

        return Objects.equals(role, that.role) &&
        		Objects.equals(permission, that.permission);
    }
	
	@Override
	public int hashCode() {
		return Objects.hash(role, permission);
    }
	
	
	
}
