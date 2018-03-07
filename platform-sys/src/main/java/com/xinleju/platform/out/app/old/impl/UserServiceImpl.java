package com.xinleju.platform.out.app.old.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.xinleju.erp.flow.flowutils.bean.FlowResult;
import com.xinleju.erp.flow.flowutils.bean.PageBean;
import com.xinleju.erp.flow.service.api.extend.UserSerivce;
import com.xinleju.erp.flow.service.api.extend.dto.CtrlDTO;
import com.xinleju.erp.flow.service.api.extend.dto.DataAuthDTO;
import com.xinleju.erp.flow.service.api.extend.dto.FieldDTO;
import com.xinleju.erp.flow.service.api.extend.dto.FuncDTO;
import com.xinleju.erp.flow.service.api.extend.dto.OpDTO;
import com.xinleju.erp.flow.service.api.extend.dto.RoleDTO;
import com.xinleju.erp.flow.service.api.extend.dto.ScopeDTO;
import com.xinleju.erp.flow.service.api.extend.dto.UserDTO;
import com.xinleju.platform.tools.data.JacksonUtils;

public class UserServiceImpl implements UserSerivce {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 获取用户列表
	 * 
	 * @param orgnId
	 *            组织机构 ID
	 * @param name
	 *            用户姓名或者登录名
	 * @param start
	 *            从start开始
	 * @param limit
	 *            查多少条
	 * @param includeAllSubOrgns
	 *            是否包含所有子孙的
	 * @param extParm
	 *            扩展参数（暂时未使用）
	 * @return
	 */
	@Override
	public FlowResult<PageBean<UserDTO>> getUserList(String orgnId, String name, Integer start, Integer limit, Boolean includeAllSubOrgns, Map<String, Object> extParm) {
		List<UserDTO> list = null;
		List<Map<String, Object>> listResult = new ArrayList<Map<String, Object>>();
		FlowResult result = new FlowResult();
		String sql = "";
		String sqlCount = "";
		// 判断是否包含子集
		try {
			if (includeAllSubOrgns) {
				String orgSql = "select id from pt_sys_org_orgnazation t where t.prefix_id like '%" + orgnId + "%'";
				RowMapper<String> rowMapperOrg = new BeanPropertyRowMapper<String>(String.class);
				// 查询包括自己的所有下级组织机构Id
				listResult = jdbcTemplate.queryForList(orgSql);
				// List<String> listOrgs =
				// jdbcTemplate.query(orgSql,rowMapperOrg);
				String orgids = "";
				if (listResult.size() > 0) {
					for (int i = 0; i < listResult.size(); i++) {
						listResult.get(i).get("id");
						orgids += (String) listResult.get(i).get("id") + "','";
					}
				}
				if (orgids.length() > 0) {
					orgids = orgids.substring(0, orgids.length() - 3);
				}

				// String orgids = StringUtils.join(listOrgs, "','");

				sql = "select u.id id,u.login_name loginName,u.real_name realName,u.mobile mobile,u.`status` status,u.email email " + " FROM " + " pt_sys_org_user u " + " WHERE " + " u.id IN ( " + " SELECT " + " pu.user_id " + " FROM " + " pt_sys_org_post_user pu " + " WHERE " + " pu.post_id IN ( "
						+ " SELECT " + " p.id " + " FROM " + " pt_sys_org_post p " + " WHERE " + " p.ref_id in ('" + orgids + "') " + " ) " + " ) ";

				sqlCount = "select count(*) " + " FROM " + " pt_sys_org_user u " + " WHERE " + " u.id IN ( " + " SELECT " + " pu.user_id " + " FROM " + " pt_sys_org_post_user pu " + " WHERE " + " pu.post_id IN ( " + " SELECT " + " p.id " + " FROM " + " pt_sys_org_post p " + " WHERE "
						+ " p.ref_id in ('" + orgids + "') " + " ) " + " ) ";

				if (null != name && !"".equals(name)) {
					String sqlName = " and (u.login_name like '%" + name + "%' or u.real_name like '%" + name + "%')  ";
					sql = sql + sqlName;
					sqlCount = sqlCount + sqlName;
				}

				if (null != start) {
					String sqlLimit = " LIMIT " + start + "," + limit + "  ";
					sql = sql + sqlLimit;
				}
				// 查询总数
				int total = jdbcTemplate.queryForObject(sqlCount, Integer.class);

				RowMapper<UserDTO> rowMapper = new BeanPropertyRowMapper<UserDTO>(UserDTO.class);
				// 查询结果集
				list = jdbcTemplate.query(sql, rowMapper);

				PageBean pb = new PageBean(start, limit, total, list);
				pb.setList(list);
				result.setSuccess(true);
				result.setResult(pb);
			}
			else {
				sql = "select u.id id,u.login_name loginName,u.real_name realName,u.mobile mobile,u.`status` status,u.email email " + " FROM " + " pt_sys_org_user u " + " WHERE " + " u.id IN ( " + " SELECT " + " pu.user_id " + " FROM " + " pt_sys_org_post_user pu " + " WHERE " + " pu.post_id IN ( "
						+ " SELECT " + " p.id " + " FROM " + " pt_sys_org_post p " + " WHERE " + " p.ref_id = '" + orgnId + "' " + " ) " + " ) ";

				sqlCount = "select count(*) " + " FROM " + " pt_sys_org_user u " + " WHERE " + " u.id IN ( " + " SELECT " + " pu.user_id " + " FROM " + " pt_sys_org_post_user pu " + " WHERE " + " pu.post_id IN ( " + " SELECT " + " p.id " + " FROM " + " pt_sys_org_post p " + " WHERE "
						+ " p.ref_id = '" + orgnId + "' " + " ) " + " ) ";

				if (null != name && !"".equals(name)) {
					String sqlName = " and (u.login_name like '%" + name + "%' or u.real_name like '%" + name + "%')  ";
					sql = sql + sqlName;
					sqlCount = sqlCount + sqlName;
				}

				if (null != start) {
					String sqlLimit = " LIMIT " + start + "," + limit + "  ";
					sql = sql + sqlLimit;
				}
				// 查询总数
				int total = jdbcTemplate.queryForObject(sqlCount, Integer.class);

				RowMapper<UserDTO> rowMapper = new BeanPropertyRowMapper<UserDTO>(UserDTO.class);
				// 查询结果集
				list = jdbcTemplate.query(sql, rowMapper);

				PageBean pb = new PageBean(start, limit, total, list);
				pb.setList(list);
				result.setSuccess(true);
				result.setResult(pb);
			}
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}
		return result;
	}

	/**
	 * 根据公司ID获取公司下用户
	 * 
	 * @param companyId
	 * @return
	 */
	@Override
	public FlowResult<List<UserDTO>> getUserListByCompanyId(String companyId) {
		List<UserDTO> list = null;
		FlowResult result = new FlowResult();
		try {
			String sql = " select u.id id,u.login_name loginName,u.real_name realName,u.mobile mobile,u.`status` status,u.email email	" + " FROM	" + " pt_sys_org_user u	" + " WHERE	" + " u.id IN (	" + " SELECT	" + " pu.user_id	" + " FROM	" + " pt_sys_org_post_user pu	" + " WHERE	"
					+ " pu.post_id IN (	" + " SELECT	" + " p.id	" + " FROM	" + " pt_sys_org_post p	" + " WHERE	" + " p.ref_id = '" + companyId + "' " + " )	" + " )	";

			RowMapper<UserDTO> rowMapper = new BeanPropertyRowMapper<UserDTO>(UserDTO.class);
			list = jdbcTemplate.query(sql, rowMapper);
			result.setSuccess(true);
			result.setResult(list);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}

		return result;
	}

	/**
	 * 根据部门ID获取部门下用户
	 * 
	 * @param deptId
	 * @return
	 */
	@Override
	public FlowResult<List<UserDTO>> getUserListByDeptId(String deptId) {
		List<UserDTO> list = null;
		FlowResult result = new FlowResult();
		try {
			String sql = " select u.id id,u.login_name loginName,u.real_name realName,u.mobile mobile,u.`status` status,u.email email	" + " FROM	" + " pt_sys_org_user u	" + " WHERE	" + " u.id IN (	" + " SELECT	" + " pu.user_id	" + " FROM	" + " pt_sys_org_post_user pu	" + " WHERE	"
					+ " pu.post_id IN (	" + " SELECT	" + " p.id	" + " FROM	" + " pt_sys_org_post p	" + " WHERE	" + " p.ref_id = '" + deptId + "' " + " )	" + " )	";

			RowMapper<UserDTO> rowMapper = new BeanPropertyRowMapper<UserDTO>(UserDTO.class);
			list = jdbcTemplate.query(sql, rowMapper);
			result.setSuccess(true);
			result.setResult(list);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}

		return result;
	}

	/**
	 * 根据部门ID获取部门下用户
	 * 
	 * @param deptIds
	 * @return
	 */
	@Override
	public FlowResult<List<UserDTO>> getUserListByDeptIds(String[] deptIds) {
		List<UserDTO> list = null;
		FlowResult result = new FlowResult();
		try {
			String ids = StringUtils.join(deptIds, "','");
			String sql = " select u.id id,u.login_name loginName,u.real_name realName,u.mobile mobile,u.`status` status,u.email email	" + " FROM	" + " pt_sys_org_user u	" + " WHERE	" + " u.id IN (	" + " SELECT	" + " pu.user_id	" + " FROM	" + " pt_sys_org_post_user pu	" + " WHERE	"
					+ " pu.post_id IN (	" + " SELECT	" + " p.id	" + " FROM	" + " pt_sys_org_post p	" + " WHERE	" + " p.ref_id in( '" + ids + "' )" + " )	" + " )	";

			RowMapper<UserDTO> rowMapper = new BeanPropertyRowMapper<UserDTO>(UserDTO.class);
			list = jdbcTemplate.query(sql, rowMapper);
			result.setSuccess(true);
			result.setResult(list);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}

		return result;
	}

	/**
	 * 根据岗位ID获取用户
	 * 
	 * @param postId
	 * @return
	 */
	@Override
	public FlowResult<List<UserDTO>> getUserListByPostId(String postId) {
		List<UserDTO> list = null;
		FlowResult result = new FlowResult();
		try {
			String sql = " select u.id id,u.login_name loginName,u.real_name realName,u.mobile mobile,u.`status` status,u.email email	 " + " FROM " + " pt_sys_org_user u,pt_sys_org_orgnazation o " + " WHERE " + " u.belong_org_id = o.id and u.delflag=0 " + " AND " + " u.id IN ( " + " SELECT "
					+ " user_id " + " FROM " + " pt_sys_org_post_user " + " WHERE " + " delflag = 0 and  " + " post_id = '" + postId + "' " + " ) " + " ORDER BY " + " u.belong_org_id, " + " u.sort ";
			RowMapper<UserDTO> rowMapper = new BeanPropertyRowMapper<UserDTO>(UserDTO.class);
			list = jdbcTemplate.query(sql, rowMapper);
			result.setSuccess(true);
			result.setResult(list);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}

		return result;
	}

	/**
	 * 根据岗位ID获取用户
	 * 
	 * @param postId
	 * @return
	 */
	@Override
	public FlowResult<List<UserDTO>> getUserListByPostIds(String[] postIds) {
		List<UserDTO> list = null;
		FlowResult result = new FlowResult();
		try {
			String ids = StringUtils.join(postIds, "','");
			String sql = " select u.id id,u.login_name loginName,u.real_name realName,u.mobile mobile,u.`status` status,u.email email	 " + " FROM " + " pt_sys_org_user u,pt_sys_org_orgnazation o " + " WHERE " + " u.belong_org_id = o.id and u.delflag=0 " + " AND " + " u.id IN ( " + " SELECT "
					+ " user_id " + " FROM " + " pt_sys_org_post_user " + " WHERE " + " delflag = 0 and  " + " post_id in ( '" + ids + "' )" + " ) " + " ORDER BY " + " u.belong_org_id, " + " u.sort ";
			RowMapper<UserDTO> rowMapper = new BeanPropertyRowMapper<UserDTO>(UserDTO.class);
			list = jdbcTemplate.query(sql, rowMapper);
			result.setSuccess(true);
			result.setResult(list);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}

		return result;
	}

	@Override
	public FlowResult<List<UserDTO>> getUserListByProjectId(Long projectId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowResult<List<UserDTO>> getUserListByProjectBranchId(Long companyId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowResult<List<UserDTO>> getUserListByStandardRoleId(Long standardRoleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowResult<List<UserDTO>> getUserListByStandardRoleIds(String[] standardRoleIds) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 根据公司ID和标准角色ID获取用户
	 * 
	 * @param companyId
	 * @param standardRoleId
	 * @return
	 */
	@Override
	public FlowResult<List<UserDTO>> getUserListByCompanyIdAndStandardRoleId(String companyId, String standardRoleId) {
		List<UserDTO> list = null;
		FlowResult result = new FlowResult();
		try {
//			String sql = " select u.id id,u.login_name loginName,u.real_name realName,u.mobile mobile,u.`status` status,u.email email	" + " FROM	" + " pt_sys_org_user u	" + " WHERE	" + " u.id IN (	" + " SELECT	" + " pu.user_id	" + " FROM	" + " pt_sys_org_post_user pu	" + " WHERE	"
//					+ " pu.post_id IN (	" + " SELECT	" + " p.id	" + " FROM	" + " pt_sys_org_post p	" + " WHERE	" + " p.ref_id = '" + companyId + "' " + " AND p.role_id = '" + standardRoleId + "' " + " )	" + " )	";
//			String sql = "SELECT DISTINCT u.id id, u.login_name loginName, u.real_name realName, u.mobile mobile, u.`status` STATUS, u.email email FROM "
//					+ "pt_sys_org_user u "
//					+ "INNER JOIN pt_sys_org_post_user pu ON u.id = pu.user_id "
//					+ "INNER JOIN pt_sys_org_post p ON pu.post_id = p.id "
//					+ "INNER JOIN pt_sys_org_orgnazation po ON p.ref_id = po.id "
//					+ "WHERE po.prefix_id LIKE '%"+companyId+"%' AND p.role_id = '"++"'";
			String sql="SELECT DISTINCT u.id id, u.login_name loginName, u.real_name realName, u.mobile mobile, u.`status` STATUS, u.email email, pu2.id mainRoleId "
					+ "FROM pt_sys_org_user u "
					+ "INNER JOIN pt_sys_org_post_user pu ON u.id = pu.user_id "
					+ "INNER JOIN pt_sys_org_post p ON pu.post_id = p.id "
					+ "INNER JOIN pt_sys_org_orgnazation po ON p.ref_id = po.id "
					+ "LEFT JOIN pt_sys_org_post_user pu2 ON u.id = pu2.user_id AND pu2.is_default = 1 "
					+ "WHERE po.prefix_id LIKE '%"+companyId+"%' AND p.role_id = '"+standardRoleId+"'";
			RowMapper<UserDTO> rowMapper = new BeanPropertyRowMapper<UserDTO>(UserDTO.class);
			list = jdbcTemplate.query(sql, rowMapper);
			result.setSuccess(true);
			result.setResult(list);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}

		return result;
	}

	/**
	 * 根据部门ID和标准角色ID获取用户
	 * 
	 * @param deptId
	 * @param standardRoleId
	 * @return
	 */
	@Override
	public FlowResult<List<UserDTO>> getUserListByDeptIdAndStandardRoleId(String deptId, String standardRoleId) {
		List<UserDTO> list = null;
		FlowResult result = new FlowResult();
		try {
			String sql = " select u.id id,u.login_name loginName,u.real_name realName,u.mobile mobile,u.`status` status,u.email email	" + " FROM	" + " pt_sys_org_user u	" + " WHERE	" + " u.id IN (	" + " SELECT	" + " pu.user_id	" + " FROM	" + " pt_sys_org_post_user pu	" + " WHERE	"
					+ " pu.post_id IN (	" + " SELECT	" + " p.id	" + " FROM	" + " pt_sys_org_post p	" + " WHERE	" + " p.ref_id = '" + deptId + "' " + " AND p.role_id = '" + standardRoleId + "' " + " )	" + " )	";

			RowMapper<UserDTO> rowMapper = new BeanPropertyRowMapper<UserDTO>(UserDTO.class);
			list = jdbcTemplate.query(sql, rowMapper);
			result.setSuccess(true);
			result.setResult(list);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}

		return result;
	}

	/**
	 * 根据项目ID和标准角色ID获取用户
	 * 
	 * @param projectBranchId
	 * @param standardRoleId
	 * @return
	 */
	@Override
	public FlowResult<List<UserDTO>> getUserListByProjectBranchIdAndStandardRoleId(String projectBranchId, String standardRoleId) {
		List<UserDTO> list = null;
		FlowResult result = new FlowResult();
		try {
			String sql = " select u.id id,u.login_name loginName,u.real_name realName,u.mobile mobile,u.`status` status,u.email email	" + " FROM	" + " pt_sys_org_user u	" + " WHERE	" + " u.id IN (	" + " SELECT	" + " pu.user_id	" + " FROM	" + " pt_sys_org_post_user pu	" + " WHERE	"
					+ " pu.post_id IN (	" + " SELECT	" + " p.id	" + " FROM	" + " pt_sys_org_post p	" + " WHERE	" + " p.ref_id = '" + projectBranchId + "' " + " AND p.role_id = '" + standardRoleId + "' " + " )	" + " )	";

			RowMapper<UserDTO> rowMapper = new BeanPropertyRowMapper<UserDTO>(UserDTO.class);
			list = jdbcTemplate.query(sql, rowMapper);
			result.setSuccess(true);
			result.setResult(list);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}

		return result;
	}

	/**
	 * 根据用户ID获取用户
	 * 
	 * @param userId
	 * @return
	 */
	@Override
	public FlowResult<UserDTO> getUserByUserId(String userId) {
		FlowResult result = new FlowResult();
		try {
			String sql = "select u.id id,u.login_name loginName,u.real_name realName,u.mobile mobile,u.`status` status,u.email email,pu.post_id mainRoleId ,r.`name` position" + " from pt_sys_org_user u " + " LEFT JOIN pt_sys_org_post_user pu on pu.user_id = u.id and pu.is_default = 1"
					+ " LEFT JOIN pt_sys_org_post p on pu.post_id = p.id" + " LEFT JOIN pt_sys_org_standard_role r on p.role_id = r.id" + " where u.id = '" + userId + "' ";
			RowMapper<UserDTO> rowMapper = new BeanPropertyRowMapper<UserDTO>(UserDTO.class);
			// UserDTO userDto = jdbcTemplate.queryForObject(sql,rowMapper);
			List<UserDTO> list = jdbcTemplate.query(sql, rowMapper);
			if (null != list && list.size() > 0) {
				result.setSuccess(true);
				result.setResult(list.get(0));
			}
			else {
				result.setSuccess(true);
				result.setResult(new UserDTO());
			}

		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}

		return result;
	}

	/**
	 * 根据用户ID获取用户
	 * 
	 * @param userIds
	 * @return
	 */
	@Override
	public FlowResult<List<UserDTO>> getUserListByUserIds(String[] userIds) {
		List<UserDTO> list = new ArrayList<UserDTO>();
		FlowResult result = new FlowResult();
		try {
			String ids = StringUtils.join(userIds, "','");
			String sql = "select u.id id,u.login_name loginName,u.real_name realName,u.mobile mobile,u.`status` status,u.email email,pu.post_id mainRoleId ,r.`name` position" + " from pt_sys_org_user u " + " LEFT JOIN pt_sys_org_post_user pu on pu.user_id = u.id and pu.is_default = 1"
					+ " LEFT JOIN pt_sys_org_post p on pu.post_id = p.id" + " LEFT JOIN pt_sys_org_standard_role r on p.role_id = r.id" + " where u.id in ('" + ids + "')";
			RowMapper<UserDTO> rowMapper = new BeanPropertyRowMapper<UserDTO>(UserDTO.class);
			list = jdbcTemplate.query(sql, rowMapper);
			result.setSuccess(true);
			result.setResult(list);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}

		return result;
	}

	/**
	 * 根据用户登录名获取用户
	 * 
	 * @param userLoginName
	 * @return
	 */
	@Override
	public FlowResult<UserDTO> getUserByUserLoginName(String userLoginName) {
		FlowResult result = new FlowResult();
		try {
			String sql = "select u.id id,u.login_name loginName,u.real_name realName,u.mobile mobile,u.`status` status,u.email email,pu.post_id mainRoleId ,r.`name` position" + " from pt_sys_org_user u " + " LEFT JOIN pt_sys_org_post_user pu on pu.user_id = u.id and pu.is_default = 1"
					+ " LEFT JOIN pt_sys_org_post p on pu.post_id = p.id" + " LEFT JOIN pt_sys_org_standard_role r on p.role_id = r.id" + " where u.login_name = '" + userLoginName + "'";
			RowMapper<UserDTO> rowMapper = new BeanPropertyRowMapper<UserDTO>(UserDTO.class);
			UserDTO userDto = jdbcTemplate.queryForObject(sql, rowMapper);
			result.setSuccess(true);
			result.setResult(userDto);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}

		return result;
	}

	/**
	 * 根据用户登录名获取用户
	 * 
	 * @param userLoginName
	 * @return
	 */
	@Override
	public FlowResult<List<UserDTO>> getUserListByLoginNames(String[] userLoginNames) {
		FlowResult result = new FlowResult();
		List<UserDTO> list = new ArrayList<UserDTO>();
		String sqlLoginNames = "";
		if (userLoginNames.length > 0) {
			for (int i = 0; i < userLoginNames.length; i++) {
				sqlLoginNames += " u.login_name = '" + userLoginNames[i] + "' or ";
			}
		}
		else {
			result.setSuccess(false);
			result.setResult("传递的参数为空");
			return result;
		}
		if (sqlLoginNames.length() > 0) {
			sqlLoginNames = sqlLoginNames.substring(0, sqlLoginNames.length() - 3);
		}

		try {
			String sql = "select u.id id,u.login_name loginName,u.real_name realName,u.mobile mobile,u.`status` status,u.email email,pu.post_id mainRoleId ,r.`name` position" + " from pt_sys_org_user u " + " LEFT JOIN pt_sys_org_post_user pu on pu.user_id = u.id and pu.is_default = 1"
					+ " LEFT JOIN pt_sys_org_post p on pu.post_id = p.id" + " LEFT JOIN pt_sys_org_standard_role r on p.role_id = r.id" + " where  ";
			sql = sql + sqlLoginNames;
			RowMapper<UserDTO> rowMapper = new BeanPropertyRowMapper<UserDTO>(UserDTO.class);
			list = jdbcTemplate.query(sql, rowMapper);
			result.setSuccess(true);
			result.setResult(list);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}

		return result;
	}

	/**
	 * 根据登录ID获取标准角色
	 * 
	 * @param userId
	 * @return
	 */
	@Override
	public FlowResult<List<RoleDTO>> getStandardRolesByUserId(String userId) {
		List<RoleDTO> listRoleDTO = new ArrayList<RoleDTO>();
		List<Map<String, Object>> resultList = null;
		FlowResult result = new FlowResult();
		try {
			String queryRoleSql = "SELECT " + " r.id,r.name,r.prefix_name,'1' type,r.catalog_id pId,r.`code`,r.prefix_sort " + " FROM pt_sys_org_standard_role r ,pt_sys_org_post p ,pt_sys_org_post_user pu ,pt_sys_org_user u  " + " where r.id = p.role_id and r.delflag = 0 and r.status = 1  "
					+ " and  p.id = pu.post_id and pu.user_id = u.id and u.id = '" + userId + "'  " + " and pu.delflag = 0 and p.delflag = 0 and p.status = 1 " + " UNION " + " SELECT " + " r.id,r.name,r.prefix_name,'1' type,r.catalog_id pId,r.`code`,r.prefix_sort "
					+ " FROM pt_sys_org_standard_role r ,pt_sys_org_role_user ru ,pt_sys_org_user u  " + " where r.id = ru.role_id and ru.user_id = u.id and u.id = '" + userId + "' and r.delflag = 0 and r.status = 1 ";
			resultList = jdbcTemplate.queryForList(queryRoleSql);
			for (Map map : resultList) {
				RoleDTO roleDTO = new RoleDTO();
				roleDTO.setId((String) map.get("id"));
				roleDTO.setName((String) map.get("name"));
				roleDTO.setNamefix((String) map.get("prefix_name"));
				roleDTO.setType((String) map.get("type"));
				roleDTO.setParentId((String) map.get("pId"));
				roleDTO.setCode((String) map.get("code"));
				listRoleDTO.add(roleDTO);
			}
			result.setSuccess(true);
			result.setResult(listRoleDTO);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(listRoleDTO);
		}

		return result;
	}

	/**
	 * 根据标准角色Id获取标准角色
	 * 
	 * @param standardRoleId
	 * @return
	 */
	@Override
	public FlowResult<RoleDTO> getStandardRolesById(String standardRoleId) {
		RoleDTO roleDTO = new RoleDTO();
		FlowResult result = new FlowResult();
		List<Map<String, Object>> resultList = null;
		try {
			String queryResultSql1 = " select * from ( " + " select id,name,prefix_name,'0' type,parent_id pId,`code`,prefix_sort from pt_sys_org_role_catalog " 
		     + " UNION select id,name,prefix_name,'1' type,catalog_id pId,`code`,prefix_sort from pt_sys_org_standard_role "
		     + " UNION SELECT p.id, r.`name`, r.prefix_name, '1' type, r.catalog_id pId, r.`code`, r.prefix_sort FROM "
		     + "pt_sys_org_post p, pt_sys_org_standard_role r WHERE p.role_id = r.id "
					+ " ) o  where o.id='" + standardRoleId + "'" + " ORDER BY o.prefix_sort ";
			resultList = jdbcTemplate.queryForList(queryResultSql1);
			if (!resultList.isEmpty() && resultList.size() > 0) {
				Map<String, Object> map = resultList.get(0);
				roleDTO.setId((String) map.get("id"));
				roleDTO.setName((String) map.get("name"));
				roleDTO.setNamefix((String) map.get("prefix_name"));
				roleDTO.setType((String) map.get("type"));
				roleDTO.setParentId((String) map.get("pId"));
				roleDTO.setCode((String) map.get("code"));
			}
			result.setSuccess(true);
			result.setResult(roleDTO);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}
		return result;
	}

	/**
	 * 获取所有标准角色
	 * 
	 * @return
	 */
	@Override
	public FlowResult<List<RoleDTO>> getAllStandardRoles() {
		List<RoleDTO> listRoleDTO = new ArrayList<RoleDTO>();
		List<Map<String, Object>> resultList = null;
		FlowResult result = new FlowResult();
		try {
			String queryResultSql = " select * from ( " + " select id,name,prefix_name,'0' type,parent_id pId,`code`,prefix_sort from pt_sys_org_role_catalog " + " UNION " + " select id,name,prefix_name,'1' type,catalog_id pId,`code`,prefix_sort from pt_sys_org_standard_role " + " ) o  "
					+ " ORDER BY o.prefix_sort ";
			resultList = jdbcTemplate.queryForList(queryResultSql);
			for (Map map : resultList) {
				RoleDTO roleDTO = new RoleDTO();
				roleDTO.setId((String) map.get("id"));
				roleDTO.setName((String) map.get("name"));
				roleDTO.setNamefix((String) map.get("prefix_name"));
				roleDTO.setType((String) map.get("type"));
				roleDTO.setParentId((String) map.get("pId"));
				roleDTO.setCode((String) map.get("code"));
				listRoleDTO.add(roleDTO);
			}
			result.setSuccess(true);
			result.setResult(listRoleDTO);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}
		return result;
	}

	/**
	 * 获取标准角色根节点
	 * 
	 * @return
	 */
	@Override
	public FlowResult<List<RoleDTO>> getRootStandardRoles() {
		List<RoleDTO> listRoleDTO = new ArrayList<RoleDTO>();
		List<Map<String, Object>> resultList = null;
		FlowResult result = new FlowResult();
		try {
			String queryResultSql = " select id,name,prefix_name,'0' type,parent_id pId,`code`,prefix_sort from pt_sys_org_role_catalog  where ( parent_id IS NULL OR parent_id = '' ) AND type = 1 ";
			resultList = jdbcTemplate.queryForList(queryResultSql);
			for (Map map : resultList) {
				RoleDTO roleDTO = new RoleDTO();
				roleDTO.setId((String) map.get("id"));
				roleDTO.setName((String) map.get("name"));
				roleDTO.setNamefix((String) map.get("prefix_name"));
				roleDTO.setType((String) map.get("type"));
				roleDTO.setParentId((String) map.get("pId"));
				roleDTO.setCode((String) map.get("code"));
				listRoleDTO.add(roleDTO);
			}
			result.setSuccess(true);
			result.setResult(listRoleDTO);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}
		return result;
	}

	/**
	 * 根据父节点ID获取标准角色
	 * 
	 * @param parentId
	 * @return
	 */
	@Override
	public FlowResult<List<RoleDTO>> getSubStandardRoles(String parentId) {
		List<RoleDTO> listRoleDTO = new ArrayList<RoleDTO>();
		List<Map<String, Object>> resultList = null;
		FlowResult result = new FlowResult();
		try {
			String queryResultSql = " select * from ( " + " select id,name,prefix_name,'0' type,parent_id pId,`code`,prefix_sort from pt_sys_org_role_catalog where parent_id = '" + parentId + "' " + " UNION "
					+ " select id,name,prefix_name,'1' type,catalog_id pId,`code`,prefix_sort from pt_sys_org_standard_role where catalog_id = '" + parentId + "' " + " ) o  " + " ORDER BY o.prefix_sort ";
			resultList = jdbcTemplate.queryForList(queryResultSql);
			for (Map map : resultList) {
				RoleDTO roleDTO = new RoleDTO();
				roleDTO.setId((String) map.get("id"));
				roleDTO.setName((String) map.get("name"));
				roleDTO.setNamefix((String) map.get("prefix_name"));
				roleDTO.setType((String) map.get("type"));
				roleDTO.setParentId((String) map.get("pId"));
				roleDTO.setCode((String) map.get("code"));
				listRoleDTO.add(roleDTO);
			}
			result.setSuccess(true);
			result.setResult(listRoleDTO);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}
		return result;
	}

	/**
	 * 获取按钮
	 * 
	 * @param standardRoleId
	 * @param moduleCode
	 * @param funcCode
	 * @return
	 */
	@Override
	public FlowResult<List<OpDTO>> getOpAuthByStandardRoleId(String standardRoleId, String moduleCode, String funcCode) {
		List<OpDTO> listOpDTO = new ArrayList<OpDTO>();
		List<Map<String, Object>> roleList = null;
		List<Map<String, Object>> resultList = null;
		FlowResult result = new FlowResult();
		try {
			String queryResultSql = " select t.id,t.name,t.code,t.resource_id,t.url from pt_sys_res_operation t  " + " LEFT JOIN pt_sys_res_func_permission tt on tt.operation_id = t.id " + " LEFT JOIN pt_sys_res_resource r on t.resource_id = r.id "
					+ " left JOIN pt_sys_res_app app on t.app_id = app.id " + " where tt.role_id = '" + standardRoleId + "' ";

			if (null != moduleCode && !"".equals(moduleCode)) {
				queryResultSql = queryResultSql + " AND app.code = '" + moduleCode + "' ";
			}
			if (null != funcCode && !"".equals(funcCode)) {
				queryResultSql = queryResultSql + " and r.`code` = '" + funcCode + "' ";
			}
			queryResultSql = queryResultSql + " GROUP BY t.id ";
			resultList = jdbcTemplate.queryForList(queryResultSql);
			for (Map map : resultList) {
				String id = (String) map.get("id");
				String name = (String) map.get("name");
				String code = (String) map.get("code");
				String resource_id = (String) map.get("resource_id");
				String url = (String) map.get("url");
				OpDTO opDTO = new OpDTO();
				opDTO.setName(name);
				opDTO.setCode(code);
				opDTO.setFuncModuleId(resource_id);
				opDTO.setButtonUrl(url);
				listOpDTO.add(opDTO);
			}
			result.setSuccess(true);
			result.setResult(listOpDTO);

		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}
		return result;
	}

	/**
	 * 获取菜单
	 * 
	 * @param standardRoleId
	 * @param moduleCode
	 * @return
	 */
	@Override
	public FlowResult<List<FuncDTO>> getFuncAuthByStandardRoleId(String standardRoleId, String moduleCode) {
		List<FuncDTO> listFuncDTO = new ArrayList<FuncDTO>();
		List<Map<String, Object>> resultList = null;
		FlowResult result = new FlowResult();
		try {
			String queryResultSql = " select t.id,t.name,t.code,t.resource_id,t.url,r.id resId,r.name reaName,r.`code` resCode,r.url resUrl,r.parent_id resParentId,r.app_id appId,app.`code` appCode from pt_sys_res_operation t  " + " LEFT JOIN pt_sys_res_func_permission tt on tt.operation_id = t.id "
					+ " LEFT JOIN pt_sys_res_resource r on t.resource_id = r.id " + " left JOIN pt_sys_res_app app on t.app_id = app.id " + " where tt.role_id = '" + standardRoleId + "' ";
			if (null != moduleCode && !"".equals(moduleCode)) {
				queryResultSql = queryResultSql + " AND app.`code` = '" + moduleCode + "' ";
			}

			queryResultSql = queryResultSql + " GROUP BY r.id ";
			resultList = jdbcTemplate.queryForList(queryResultSql);
			for (Map map : resultList) {
				FuncDTO funcDTO = new FuncDTO();
				funcDTO.setId((String) map.get("resId"));
				funcDTO.setCode((String) map.get("resCode"));
				funcDTO.setName((String) map.get("reaName"));
				funcDTO.setUrl((String) map.get("resUrl"));
				funcDTO.setParentId((String) map.get("resParentId"));
				funcDTO.setSystemCode((String) map.get("appCode"));
				listFuncDTO.add(funcDTO);
			}
			result.setSuccess(true);
			result.setResult(listFuncDTO);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}
		return result;
	}

	/**
	 * 获取用户按钮权限
	 * 
	 * @author hongbin
	 * @param loginName
	 *            用户登录名
	 * @param moduleCode
	 *            模块编码
	 * @param funcCode
	 *            功能编码
	 * @return
	 */
	@Override
	public FlowResult<List<OpDTO>> getOpAuth(String loginName, String moduleCode, String funcCode) {
		List<OpDTO> listOpDTO = new ArrayList<OpDTO>();
		List<Map<String, Object>> roleList = null;
		List<Map<String, Object>> resultList = null;
		FlowResult result = new FlowResult();
		try {
			// String queryRoleSql = "SELECT "
			// +" r.id AS id, "
			// +" r.NAME AS NAME, "
			// +" r.CODE AS CODE "
			// +" FROM pt_sys_org_standard_role r ,pt_sys_org_post p
			// ,pt_sys_org_post_user pu ,pt_sys_org_user u "
			// +" where r.id = p.role_id and r.delflag = 0 and r.status = 1 "
			// +" and p.id = pu.post_id and pu.user_id = u.id and u.login_name =
			// '"+loginName+"' "
			// +" and pu.delflag = 0 and p.delflag = 0 and p.status = 1 "
			// +" UNION "
			// +" SELECT "
			// +" r.id AS id, "
			// +" r.NAME AS NAME, "
			// +" r.CODE AS CODE "
			// +" FROM pt_sys_org_standard_role r ,pt_sys_org_role_user ru
			// ,pt_sys_org_user u "
			// +" where r.id = ru.role_id and ru.user_id = u.id and u.login_name
			// = '"+loginName+"' and r.delflag = 0 and r.status = 1 ";
			String queryRoleSql = " SELECT  " + " r.id AS id,  " + " r.NAME AS NAME,  " + " r.CODE AS CODE   " + " FROM pt_sys_org_standard_role r ,pt_sys_org_post p ,pt_sys_org_post_user pu ,pt_sys_org_user u   " + " where r.id = p.role_id and r.delflag = 0 and r.status = 1   "
					+ " and  p.id = pu.post_id and pu.user_id = u.id and u.login_name = '" + loginName + "'   " + " and pu.delflag = 0 and p.delflag = 0 and p.status = 1  " + " UNION " + " SELECT " + " r.id AS id, " + " r.NAME AS NAME, " + " r.CODE AS CODE "
					+ " FROM pt_sys_org_standard_role r ,pt_sys_org_role_user ru ,pt_sys_org_user uu  " + " where r.id = ru.role_id and ru.user_id = uu.id and uu.login_name ='" + loginName + "' and r.delflag = 0 and r.status = 1 " + " UNION " + " select r.id AS id, " + " r.NAME AS NAME, "
					+ " r.CODE AS CODE " + " FROM pt_sys_org_standard_role r   " + " LEFT JOIN pt_sys_org_role_user ru on ru.role_id=r.id " + " LEFT JOIN pt_sys_org_post p on p.id=ru.post_id AND IFNULL(ru.user_id,'')='' " + " LEFT JOIN pt_sys_org_post_user pu on pu.post_id=p.id "
					+ " LEFT JOIN pt_sys_org_user uu on pu.user_id = uu.id " + " WHERE uu.login_name ='" + loginName + "' and p.delflag=0 " + " AND r.delflag = 0 " + " AND r. STATUS = 1 " + " UNION " + " SELECT " + " p.id AS id, " + " p.CODE AS CODE, " + " r.name as name " + " FROM "
					+ " PT_SYS_ORG_POST p ,pt_sys_org_standard_role r ,pt_sys_org_post_user pu , pt_sys_org_orgnazation oo ,pt_sys_org_user uu " + " where r.id = p.role_id  and oo.id = p.ref_id and p.delflag = 0  and r.delflag = 0 and r.`status` = 1 "
					+ " and p.id = pu.post_id and pu.user_id = uu.id and uu.login_name ='" + loginName + "' and pu.delflag = 0 " + " UNION " + " select r.id as id ,r.real_name as name,r.login_name as code from pt_sys_org_user r " + " where r.login_name ='" + loginName
					+ "' and r.delflag =0 and r.`status` = 1 ";

			roleList = jdbcTemplate.queryForList(queryRoleSql);

			List<String> roleListString = null;
			String roleString = "";
			if (null != roleList && roleList.size() > 0) {
				for (Map map : roleList) {
					roleString += "'" + (String) map.get("id") + "',";
				}
			}

			if (roleString.length() > 0) {
				roleString = roleString.substring(0, roleString.length() - 1);
			}
			else {
				result.setSuccess(false);
				result.setResult(listOpDTO);
				return result;
			}

			String queryResultSql = " select t.id,t.name,t.code,t.resource_id,t.url from pt_sys_res_operation t  " + " LEFT JOIN pt_sys_res_func_permission tt on tt.operation_id = t.id " + " LEFT JOIN pt_sys_res_resource r on t.resource_id = r.id "
					+ " left JOIN pt_sys_res_app app on t.app_id = app.id " + " where tt.role_id in (" + roleString + ") ";

			if (null != moduleCode && !"".equals(moduleCode)) {
				queryResultSql = queryResultSql + " AND app.code = '" + moduleCode + "' ";
			}
			if (null != funcCode && !"".equals(funcCode)) {
				queryResultSql = queryResultSql + " and r.`code` = '" + funcCode + "' ";
			}
			queryResultSql = queryResultSql + " GROUP BY t.id ";
			resultList = jdbcTemplate.queryForList(queryResultSql);
			for (Map map : resultList) {
				String id = (String) map.get("id");
				String name = (String) map.get("name");
				String code = (String) map.get("code");
				String resource_id = (String) map.get("resource_id");
				String url = (String) map.get("url");
				OpDTO opDTO = new OpDTO();
				opDTO.setName(name);
				opDTO.setCode(code);
				opDTO.setFuncModuleId(resource_id);
				opDTO.setButtonUrl(url);
				listOpDTO.add(opDTO);
			}
			result.setSuccess(true);
			result.setResult(listOpDTO);

		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}
		return result;

	}

	/**
	 * 
	 * 查询某个APP所有的按钮权限配置
	 * 
	 * @param moduleCode
	 *            模块编码
	 * @return
	 */
	@Override
	public FlowResult<List<OpDTO>> getOpAuthByModuleCode(String moduleCode) {
		List<OpDTO> listOpDTO = new ArrayList<OpDTO>();
		List<Map<String, Object>> resultList = null;
		FlowResult result = new FlowResult();
		try {

			String queryResultSql = " select t.id,t.name,t.code,t.resource_id,t.url from pt_sys_res_operation t  " + " left JOIN pt_sys_res_app app on t.app_id = app.id " + " where app.code = '" + moduleCode + "' ";

			resultList = jdbcTemplate.queryForList(queryResultSql);
			for (Map map : resultList) {
				String id = (String) map.get("id");
				String name = (String) map.get("name");
				String code = (String) map.get("code");
				String resource_id = (String) map.get("resource_id");
				String url = (String) map.get("url");
				OpDTO opDTO = new OpDTO();
				opDTO.setName(name);
				opDTO.setCode(code);
				opDTO.setFuncModuleId(resource_id);
				opDTO.setButtonUrl(url);
				listOpDTO.add(opDTO);
			}
			result.setSuccess(true);
			result.setResult(listOpDTO);

		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}
		return result;
	}

	/**
	 * 
	 * 获取用户功能权限
	 * 
	 * @param loginName
	 *            用户登录名
	 * @param moduleCode
	 *            模块编码
	 * @return
	 */
	@Override
	public FlowResult<List<FuncDTO>> getFuncAuth(String loginName, String moduleCode) {
		List<FuncDTO> listFuncDTO = new ArrayList<FuncDTO>();
		List<Map<String, Object>> roleList = null;
		List<Map<String, Object>> resultList = null;
		FlowResult result = new FlowResult();
		try {
			String queryRoleSql = "SELECT " + " r.id AS id, " + " r.NAME AS NAME, " + " r.CODE AS CODE  " + " FROM pt_sys_org_standard_role r ,pt_sys_org_post p ,pt_sys_org_post_user pu ,pt_sys_org_user u  " + " where r.id = p.role_id and r.delflag = 0 and r.status = 1  "
					+ " and  p.id = pu.post_id and pu.user_id = u.id and u.login_name = '" + loginName + "'  " + " and pu.delflag = 0 and p.delflag = 0 and p.status = 1 " + " UNION " + " SELECT " + " r.id AS id, " + " r.NAME AS NAME, " + " r.CODE AS CODE "
					+ " FROM pt_sys_org_standard_role r ,pt_sys_org_role_user ru ,pt_sys_org_user u  " + " where r.id = ru.role_id and ru.user_id = u.id and u.login_name = '" + loginName + "' and r.delflag = 0 and r.status = 1 ";

			roleList = jdbcTemplate.queryForList(queryRoleSql);

			List<String> roleListString = null;
			String roleString = "";
			if (null != roleList && roleList.size() > 0) {
				for (Map map : roleList) {
					roleString += "'" + (String) map.get("id") + "',";
				}
			}

			if (roleString.length() > 0) {
				roleString = roleString.substring(0, roleString.length() - 1);
			}
			else {
				result.setSuccess(false);
				result.setResult(listFuncDTO);
				return result;
			}
			// '02171fcbc4264f17953af8f09aa57ecf','80ffdb603c7741db86bede84522a91b6','890aadd5a36d465b9d545fccc934b5ca','fa5e056a9b86457ba3430fde679921c2'
			// String queryScopeSql = " select
			// t.id,t.item_id,t.CODE,t.NAME,tt.role_id ,GROUP_CONCAT(ttt.val)
			// val from pt_sys_res_data_point t "
			// +" LEFT JOIN pt_sys_res_data_permission tt on t.id =
			// tt.data_point_id "
			// +" LEFT JOIN pt_sys_res_data_point_permission_val ttt on
			// ttt.data_permission_id = tt.id "
			// +" where tt.role_id in ("+roleString+") "
			// +" GROUP BY t.id,tt.role_id ";

			String queryResultSql = " select t.id,t.name,t.code,t.resource_id,t.url,r.id resId,r.name reaName,r.`code` resCode,r.url resUrl,r.parent_id resParentId,r.app_id appId,app.`code` appCode from pt_sys_res_operation t  " + " LEFT JOIN pt_sys_res_func_permission tt on tt.operation_id = t.id "
					+ " LEFT JOIN pt_sys_res_resource r on t.resource_id = r.id " + " left JOIN pt_sys_res_app app on t.app_id = app.id " + " where tt.role_id in (" + roleString + ") ";
			if (null != moduleCode && !"".equals(moduleCode)) {
				queryResultSql = queryResultSql + " AND app.`code` = '" + moduleCode + "' ";
			}

			queryResultSql = queryResultSql + " GROUP BY r.id ";
			resultList = jdbcTemplate.queryForList(queryResultSql);
			for (Map map : resultList) {
				FuncDTO funcDTO = new FuncDTO();
				funcDTO.setId((String) map.get("resId"));
				funcDTO.setCode((String) map.get("resCode"));
				funcDTO.setName((String) map.get("reaName"));
				funcDTO.setUrl((String) map.get("resUrl"));
				funcDTO.setParentId((String) map.get("resParentId"));
				funcDTO.setSystemCode((String) map.get("appCode"));
				listFuncDTO.add(funcDTO);
			}
			result.setSuccess(true);
			result.setResult(listFuncDTO);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}
		return result;
	}

	/**
	 * 获取用户数据授权
	 * 
	 * @param loginName
	 *            用户登录名
	 * @return
	 */
	@Override
	public FlowResult<DataAuthDTO> getDataAuth(String loginName) {
		DataAuthDTO dataAuthDTO = new DataAuthDTO();
		List<Map<String, Object>> roleList = null;
		List<Map<String, Object>> resultList = null;
		FlowResult result = new FlowResult();
		try {
			String queryRoleSql = "SELECT " + " r.id AS id, " + " r.NAME AS NAME, " + " r.CODE AS CODE  " + " FROM pt_sys_org_standard_role r ,pt_sys_org_post p ,pt_sys_org_post_user pu ,pt_sys_org_user u  " + " where r.id = p.role_id and r.delflag = 0 and r.status = 1  "
					+ " and  p.id = pu.post_id and pu.user_id = u.id and u.login_name = '" + loginName + "'  " + " and pu.delflag = 0 and p.delflag = 0 and p.status = 1 " + " UNION " + " SELECT " + " r.id AS id, " + " r.NAME AS NAME, " + " r.CODE AS CODE "
					+ " FROM pt_sys_org_standard_role r ,pt_sys_org_role_user ru ,pt_sys_org_user u  " + " where r.id = ru.role_id and ru.user_id = u.id and u.login_name = '" + loginName + "' and r.delflag = 0 and r.status = 1 ";

			roleList = jdbcTemplate.queryForList(queryRoleSql);

			List<String> roleListString = null;
			String roleString = "";
			if (null != roleList && roleList.size() > 0) {
				for (Map map : roleList) {
					roleString += "'" + (String) map.get("id") + "',";
				}
			}

			if (roleString.length() > 0) {
				roleString = roleString.substring(0, roleString.length() - 1);
			}
			else {
				result.setSuccess(false);
				result.setResult(dataAuthDTO);
				return result;
			}
			// '02171fcbc4264f17953af8f09aa57ecf','80ffdb603c7741db86bede84522a91b6','890aadd5a36d465b9d545fccc934b5ca','fa5e056a9b86457ba3430fde679921c2'
			// String queryScopeSql = " select
			// t.id,t.item_id,t.CODE,t.NAME,tt.role_id ,GROUP_CONCAT(ttt.val)
			// val from pt_sys_res_data_point t "
			// +" LEFT JOIN pt_sys_res_data_permission tt on t.id =
			// tt.data_point_id "
			// +" LEFT JOIN pt_sys_res_data_point_permission_val ttt on
			// ttt.data_permission_id = tt.id "
			// +" where tt.role_id in ("+roleString+") "
			// +" GROUP BY t.id,tt.role_id ";

			String queryResultSql = " select t.id,t.item_id itemId,app.id appId,app.`code` appCode,app.`name` appName ,di.item_code itemCode,di.item_name itemName,t.code ,t.name,tt.role_id ,GROUP_CONCAT(ttt.val) val from pt_sys_res_data_point t "
					+ " LEFT JOIN pt_sys_res_data_permission tt on t.id = tt.data_point_id  " + " LEFT JOIN pt_sys_res_data_point_permission_val ttt on ttt.data_permission_id = tt.id  " + " LEFT JOIN pt_sys_res_data_item di on t.item_id = di.id  "
					+ " LEFT JOIN pt_sys_res_app app on di.app_id = app.id  " + " where tt.role_id in (" + roleString + ") " + " GROUP BY t.id,tt.role_id ";
			resultList = jdbcTemplate.queryForList(queryResultSql);

			Map<CtrlDTO, Map<FieldDTO, List<ScopeDTO>>> detail = new HashMap<CtrlDTO, Map<FieldDTO, List<ScopeDTO>>>();

			Map<FieldDTO, List<ScopeDTO>> fdetail = new HashMap<FieldDTO, List<ScopeDTO>>();

			// List<ScopeDTO> listScope = new ArrayList<ScopeDTO>();

			for (Map map : resultList) {
				String appId = (String) map.get("appId");

				boolean isexistCtrl = false;
				Set<CtrlDTO> setCtrl = detail.keySet();
				for (CtrlDTO key : setCtrl) {
					if (key.getId().equals(appId)) {
						isexistCtrl = true;
					}
				}
				if (isexistCtrl) {
					continue;
				}
				String appCode = (String) map.get("appCode");
				String appName = (String) map.get("appName");
				CtrlDTO ctrlDTO = new CtrlDTO();
				ctrlDTO.setId(appId);
				ctrlDTO.setCode(appCode);
				ctrlDTO.setName(appName);
				for (Map map1 : resultList) {
					String itemId = (String) map1.get("itemId");
					boolean isexistField = false;
					Set<FieldDTO> set = fdetail.keySet();
					for (FieldDTO key : set) {
						if (key.getId().equals(itemId)) {
							isexistField = true;
						}
					}
					if (isexistField) {
						continue;
					}
					String itemCode = (String) map1.get("itemCode");
					String itemName = (String) map1.get("itemName");
					FieldDTO fieldDTO = new FieldDTO();
					fieldDTO.setId(itemId);
					fieldDTO.setCode(itemCode);
					fieldDTO.setName(itemName);
					List<ScopeDTO> listScope = new ArrayList<ScopeDTO>();
					for (Map map2 : resultList) {
						String itemIdNew = (String) map2.get("itemId");
						if (itemIdNew.equals(itemId)) {
							ScopeDTO scopeDTO = new ScopeDTO();
							scopeDTO.setId((String) map2.get("id"));
							scopeDTO.setCode((String) map2.get("code"));
							scopeDTO.setName((String) map2.get("name"));
							scopeDTO.setValue((String) map2.get("val"));
							listScope.add(scopeDTO);
						}
					}
					fdetail.put(fieldDTO, listScope);

				}
				detail.put(ctrlDTO, fdetail);
			}

			dataAuthDTO.setDetail(detail);
			result.setSuccess(true);
			result.setResult(dataAuthDTO);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}

		return result;
	}

	/**
	 * 获取用户数据授权
	 * 
	 * @param loginName
	 * @param moduleCode
	 * @return
	 */
	@Override
	public FlowResult<DataAuthDTO> getDataAuth(String loginName, String moduleCode) {
		DataAuthDTO dataAuthDTO = new DataAuthDTO();
		List<Map<String, Object>> roleList = null;
		List<Map<String, Object>> resultList = null;
		FlowResult result = new FlowResult();
		try {
			String queryRoleSql = "SELECT " + " r.id AS id, " + " r.NAME AS NAME, " + " r.CODE AS CODE  " + " FROM pt_sys_org_standard_role r ,pt_sys_org_post p ,pt_sys_org_post_user pu ,pt_sys_org_user u  " + " where r.id = p.role_id and r.delflag = 0 and r.status = 1  "
					+ " and  p.id = pu.post_id and pu.user_id = u.id and u.login_name = '" + loginName + "'  " + " and pu.delflag = 0 and p.delflag = 0 and p.status = 1 " + " UNION " + " SELECT " + " r.id AS id, " + " r.NAME AS NAME, " + " r.CODE AS CODE "
					+ " FROM pt_sys_org_standard_role r ,pt_sys_org_role_user ru ,pt_sys_org_user u  " + " where r.id = ru.role_id and ru.user_id = u.id and u.login_name = '" + loginName + "' and r.delflag = 0 and r.status = 1 ";

			roleList = jdbcTemplate.queryForList(queryRoleSql);
			List<String> roleListString = null;
			String roleString = "";
			if (null != roleList && roleList.size() > 0) {
				for (Map map : roleList) {
					roleString += "'" + (String) map.get("id") + "',";
				}
			}

			if (roleString.length() > 0) {
				roleString = roleString.substring(0, roleString.length() - 1);
			}
			else {
				result.setSuccess(false);
				result.setResult(dataAuthDTO);
				return result;
			}
			// '02171fcbc4264f17953af8f09aa57ecf','80ffdb603c7741db86bede84522a91b6','890aadd5a36d465b9d545fccc934b5ca','fa5e056a9b86457ba3430fde679921c2'
			// String queryScopeSql = " select
			// t.id,t.item_id,t.CODE,t.NAME,tt.role_id ,GROUP_CONCAT(ttt.val)
			// val from pt_sys_res_data_point t "
			// +" LEFT JOIN pt_sys_res_data_permission tt on t.id =
			// tt.data_point_id "
			// +" LEFT JOIN pt_sys_res_data_point_permission_val ttt on
			// ttt.data_permission_id = tt.id "
			// +" where tt.role_id in ("+roleString+") "
			// +" GROUP BY t.id,tt.role_id ";

			String queryResultSql = " select t.id,t.item_id itemId,app.id appId,app.`code` appCode,app.`name` appName ,di.item_code itemCode,di.item_name itemName,t.code ,t.name,tt.role_id ,GROUP_CONCAT(ttt.val) val from pt_sys_res_data_point t "
					+ " LEFT JOIN pt_sys_res_data_permission tt on t.id = tt.data_point_id  " + " LEFT JOIN pt_sys_res_data_point_permission_val ttt on ttt.data_permission_id = tt.id  " + " LEFT JOIN pt_sys_res_data_item di on t.item_id = di.id  "
					+ " LEFT JOIN pt_sys_res_app app on di.app_id = app.id  " + " where tt.role_id in (" + roleString + ") ";
			if (null != moduleCode && !"".equals(moduleCode)) {
				queryResultSql = queryResultSql + " AND app.code = '" + moduleCode + "' ";
			}
			queryResultSql = queryResultSql + " GROUP BY t.id,tt.role_id ";

			resultList = jdbcTemplate.queryForList(queryResultSql);

			Map<CtrlDTO, Map<FieldDTO, List<ScopeDTO>>> detail = new HashMap<CtrlDTO, Map<FieldDTO, List<ScopeDTO>>>();

			Map<FieldDTO, List<ScopeDTO>> fdetail = new HashMap<FieldDTO, List<ScopeDTO>>();

			// List<ScopeDTO> listScope = new ArrayList<ScopeDTO>();

			for (Map map : resultList) {
				String appId = (String) map.get("appId");

				boolean isexistCtrl = false;
				Set<CtrlDTO> setCtrl = detail.keySet();
				for (CtrlDTO key : setCtrl) {
					if (key.getId().equals(appId)) {
						isexistCtrl = true;
					}
				}
				if (isexistCtrl) {
					continue;
				}
				String appCode = (String) map.get("appCode");
				String appName = (String) map.get("appName");
				CtrlDTO ctrlDTO = new CtrlDTO();
				ctrlDTO.setId(appId);
				ctrlDTO.setCode(appCode);
				ctrlDTO.setName(appName);
				for (Map map1 : resultList) {
					String itemId = (String) map1.get("itemId");
					boolean isexistField = false;
					Set<FieldDTO> set = fdetail.keySet();
					for (FieldDTO key : set) {
						if (key.getId().equals(itemId)) {
							isexistField = true;
						}
					}
					if (isexistField) {
						continue;
					}
					String itemCode = (String) map1.get("itemCode");
					String itemName = (String) map1.get("itemName");
					FieldDTO fieldDTO = new FieldDTO();
					fieldDTO.setId(itemId);
					fieldDTO.setCode(itemCode);
					fieldDTO.setName(itemName);
					List<ScopeDTO> listScope = new ArrayList<ScopeDTO>();
					for (Map map2 : resultList) {
						String itemIdNew = (String) map2.get("itemId");
						if (itemIdNew.equals(itemId)) {
							ScopeDTO scopeDTO = new ScopeDTO();
							scopeDTO.setId((String) map2.get("id"));
							scopeDTO.setCode((String) map2.get("code"));
							scopeDTO.setName((String) map2.get("name"));
							scopeDTO.setValue((String) map2.get("val"));
							listScope.add(scopeDTO);
						}
					}
					fdetail.put(fieldDTO, listScope);

				}
				detail.put(ctrlDTO, fdetail);
			}

			dataAuthDTO.setDetail(detail);
			result.setSuccess(true);
			result.setResult(dataAuthDTO);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}

		return result;
	}

	/**
	 * 获取数据权限
	 * 
	 * @param moduleCode
	 * @param authUserLoginName
	 * @param ctrId
	 * @param fieldId
	 * @return
	 */
	@Override
	public FlowResult<List<ScopeDTO>> getAuthScope(String moduleCode, String authUserLoginName, String ctrId, String fieldId) {
		List<ScopeDTO> listResult = new ArrayList<ScopeDTO>();
		List<Map<String, Object>> roleList = null;
		List<Map<String, Object>> resultList = null;
		FlowResult result = new FlowResult();
		try {
			String queryRoleSql = "SELECT " + " r.id AS id, " + " r.NAME AS NAME, " + " r.CODE AS CODE  " + " FROM pt_sys_org_standard_role r ,pt_sys_org_post p ,pt_sys_org_post_user pu ,pt_sys_org_user u  " + " where r.id = p.role_id and r.delflag = 0 and r.status = 1  "
					+ " and  p.id = pu.post_id and pu.user_id = u.id and u.login_name = '" + authUserLoginName + "'  " + " and pu.delflag = 0 and p.delflag = 0 and p.status = 1 " + " UNION " + " SELECT " + " r.id AS id, " + " r.NAME AS NAME, " + " r.CODE AS CODE "
					+ " FROM pt_sys_org_standard_role r ,pt_sys_org_role_user ru ,pt_sys_org_user u  " + " where r.id = ru.role_id and ru.user_id = u.id and u.login_name = '" + authUserLoginName + "' and r.delflag = 0 and r.status = 1 ";
			roleList = jdbcTemplate.queryForList(queryRoleSql);

			List<String> roleListString = null;
			String roleString = "";
			if (null != roleList && roleList.size() > 0) {
				for (Map map : roleList) {
					roleString += "'" + (String) map.get("id") + "',";
				}
			}

			if (roleString.length() > 0) {
				roleString = roleString.substring(0, roleString.length() - 1);
			}
			else {
				result.setSuccess(false);
				result.setResult(listResult);
				return result;
			}
			String queryResultSql = " select t.id,t.item_id itemId,app.id appId,app.`code` appCode,app.`name` appName ,di.item_code itemCode,di.item_name itemName,t.code ,t.name,tt.role_id ,GROUP_CONCAT(ttt.val) val from pt_sys_res_data_point t   "
					+ " LEFT JOIN pt_sys_res_data_permission tt on t.id = tt.data_point_id   " + " LEFT JOIN pt_sys_res_data_point_permission_val ttt on ttt.data_permission_id = tt.id   " + " LEFT JOIN pt_sys_res_data_item di on t.item_id = di.id   "
					+ " LEFT JOIN pt_sys_res_app app on di.app_id = app.id   " + " where tt.role_id in (" + roleString + ")  ";
			if (null != moduleCode && !"".equals(moduleCode)) {
				queryResultSql = queryResultSql + " AND app.code = '" + moduleCode + "' ";
			}

			if (null != fieldId && !"".equals(fieldId)) {
				// queryResultSql = queryResultSql + " and t.item_id =
				// '"+fieldId+"' ";
				queryResultSql = queryResultSql + " and di.item_code = '" + fieldId + "' ";
			}

			queryResultSql = queryResultSql + " GROUP BY t.id,tt.role_id ";

			resultList = jdbcTemplate.queryForList(queryResultSql);

			for (Map map2 : resultList) {
				ScopeDTO scopeDTO = new ScopeDTO();
				scopeDTO.setId((String) map2.get("id"));
				scopeDTO.setCode((String) map2.get("code"));
				scopeDTO.setName((String) map2.get("name"));
				scopeDTO.setValue((String) map2.get("val"));
				listResult.add(scopeDTO);
			}
			result.setSuccess(true);
			result.setResult(listResult);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}

		return result;
	}

	/**
	 * 
	 * 查询某个APP所有的模块权限配置
	 * 
	 * @param moduleCode
	 *            模块编码
	 * @return
	 */
	@Override
	public FlowResult<List<FuncDTO>> getFuncAuthByModuleCode(String moduleCode) {
		List<FuncDTO> listFuncDTO = new ArrayList<FuncDTO>();
		List<Map<String, Object>> resultList = null;
		FlowResult result = new FlowResult();
		try {

			String queryResultSql = " select t.id resId,t.name reaName,t.`code` resCode,t.url resUrl,t.parent_id resParentId,t.app_id appId,app.`code` appCode from pt_sys_res_resource t  " + " left JOIN pt_sys_res_app app on t.app_id = app.id " + " where app.`code` = '" + moduleCode + "' ";

			resultList = jdbcTemplate.queryForList(queryResultSql);
			for (Map map : resultList) {
				FuncDTO funcDTO = new FuncDTO();
				funcDTO.setId((String) map.get("resId"));
				funcDTO.setCode((String) map.get("resCode"));
				funcDTO.setName((String) map.get("reaName"));
				funcDTO.setUrl((String) map.get("resUrl"));
				funcDTO.setParentId((String) map.get("resParentId"));
				funcDTO.setSystemCode((String) map.get("appCode"));
				listFuncDTO.add(funcDTO);
			}
			result.setSuccess(true);
			result.setResult(listFuncDTO);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}
		return result;
	}

	/**
	 * 根据名字模糊查询用户
	 * 
	 * @param realName
	 * @return
	 */
	@Override
	public FlowResult<List<UserDTO>> getUserByRealName(String realName) {

		List<UserDTO> list = null;
		FlowResult result = new FlowResult();
		try {
			String sql = "select u.id id,u.login_name loginName,u.real_name realName,u.mobile mobile,u.`status` status,u.email email,pu.post_id mainRoleId ,r.`name` position" + " from pt_sys_org_user u " + " LEFT JOIN pt_sys_org_post_user pu on pu.user_id = u.id and pu.is_default = 1"
					+ " LEFT JOIN pt_sys_org_post p on pu.post_id = p.id" + " LEFT JOIN pt_sys_org_standard_role r on p.role_id = r.id" + " where u.real_name like '%" + realName + "%' ";
			RowMapper<UserDTO> rowMapper = new BeanPropertyRowMapper<UserDTO>(UserDTO.class);
			list = jdbcTemplate.query(sql, rowMapper);
			result.setSuccess(true);
			result.setResult(list);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}

		return result;

	}

	@Override
	public void saveOrUpdateUser(UserDTO userDto) {
		// TODO Auto-generated method stub

	}

}