package com.emc.service.impl.fee;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emc.service.fee.WadeServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("wadeService")
@Transactional
public class WadeServiceImpl extends CommonServiceImpl implements WadeServiceI {
	
}