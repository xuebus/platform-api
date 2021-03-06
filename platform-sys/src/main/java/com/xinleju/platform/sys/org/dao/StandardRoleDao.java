package com.xinleju.platform.sys.org.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.org.dto.RoleNodeDto;
import com.xinleju.platform.sys.org.dto.StandardRoleDto;
import com.xinleju.platform.sys.org.entity.StandardRole;

/**
 * @author admin
 *
 */

public interface StandardRoleDao extends BaseDao<String, StandardRole> {
	
	
	/**
	 * 获取目录子节点标准角色
	 * @param catalogId
	 * @return
	 */
	public List<RoleNodeDto> queryRoleListByCataId(String catalogId)  throws Exception;
	
	/**
	 * 根据组织机构获取角色
	 * @param paramater
	 * @return
	 */
	public List<RoleNodeDto> queryRoleListByOrgId(Map<String, Object> paramater)  throws Exception;
	
	/**
	 * 根据用户Id查询角色
	 * @param paramater
	 * @return
	 */
	public List<StandardRoleDto> queryRoleListByUserId(Map<String, Object> paramater)  throws Exception;
	
	/**
	 * 根据用户Id查询通用角色
	 * @param paramater
	 * @return
	 */
	public List<StandardRoleDto> queryCurrencyRoleListByUserId(Map<String, Object> paramater)  throws Exception;
	/**
	 * 根据岗位获取通用角色
	 * @param paramater
	 * @return
	 */
	public List<StandardRoleDto> queryCurrencyRoleListByPostId(Map<String, Object> paramater)  throws Exception;
	/**
	 * 根据岗位获取标准岗位
	 * @param paramater
	 * @return
	 */
	public List<StandardRoleDto> queryStandardPostListByPostId(Map<String, Object> paramater)  throws Exception;
	/**
	 * 根据IDs获取结果集
	 * @param paramater
	 * @return
	 */
	public List<Map<String,String>> queryRolesByIds(Map map)throws Exception;
}
