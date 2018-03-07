package com.xinleju.platform.sys.org.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_ORG_ROLE_USER",desc="用户虚拟角色关联表")
public class RoleUser extends BaseEntity{
	
		
	@Column(value="user_id",desc="用户id")
	private String userId;
    
	@Column(value="target_type",desc="目标类型")
	private String targetType;
	@Column(value="post_id",desc="岗位id")
	private String postId;
	
  		
	@Column(value="role_id",desc="角色")
	private String roleId;
    
  		
	@Column(value="sort",desc="排序")
	private Long sort;
  		
	public String getTargetType() {
		return targetType;
	}
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
  		
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
    
	
}