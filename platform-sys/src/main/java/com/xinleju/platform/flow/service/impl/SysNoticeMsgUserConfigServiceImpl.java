package com.xinleju.platform.flow.service.impl;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.flow.dao.SysNoticeMsgUserConfigDao;
import com.xinleju.platform.flow.entity.SysNoticeMsgUserConfig;
import com.xinleju.platform.flow.service.SysNoticeMsgUserConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by luoro on 2017/9/24.
 */
@Service
public class SysNoticeMsgUserConfigServiceImpl extends BaseServiceImpl<String,SysNoticeMsgUserConfig> implements SysNoticeMsgUserConfigService {
    @Autowired
    private SysNoticeMsgUserConfigDao sysNoticeMsgUserConfigDao;

}