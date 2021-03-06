package com.xinleju.platform.flow.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;

/**
 * @author admin
 * 
 * 
 */
@Table(value = "PT_FLOW_MONITORED_PERSON", desc = "被监控人")
public class MonitoredPerson extends BaseEntity {

	@Column(value = "monitored_name", desc = "被监控人")
	private String monitoredName;

	@Column(value = "monitored_id", desc = "被监控人id")
	private String monitoredId;

	@Column(value = "monitored_type", desc = "被监控人类型")
	private String monitoredType;

	@Column(value = "monitor_setting_id", desc = "监控设置id")
	private String monitorSettingId;

	public String getMonitoredName() {
		return monitoredName;
	}

	public void setMonitoredName(String monitoredName) {
		this.monitoredName = monitoredName;
	}

	public String getMonitoredId() {
		return monitoredId;
	}

	public void setMonitoredId(String monitoredId) {
		this.monitoredId = monitoredId;
	}

	public String getMonitoredType() {
		return monitoredType;
	}

	public void setMonitoredType(String monitoredType) {
		this.monitoredType = monitoredType;
	}

	public String getMonitorSettingId() {
		return monitorSettingId;
	}

	public void setMonitorSettingId(String monitorSettingId) {
		this.monitorSettingId = monitorSettingId;
	}

}
