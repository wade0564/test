package com.emc.controller.fee;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.core.util.MyBeanUtils;

import com.emc.entity.fee.WadeEntity;
import com.emc.service.fee.WadeServiceI;

/**   
 * @Title: Controller
 * @Description: 帅的不行
 * @author zhangdaihao
 * @date 2014-08-21 01:02:55
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/wadeController")
public class WadeController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(WadeController.class);

	@Autowired
	private WadeServiceI wadeService;
	@Autowired
	private SystemService systemService;
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * 帅的不行列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "wade")
	public ModelAndView wade(HttpServletRequest request) {
		return new ModelAndView("com/emc/fee/wadeList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(WadeEntity wade,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(WadeEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, wade, request.getParameterMap());
		this.wadeService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除帅的不行
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(WadeEntity wade, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		wade = systemService.getEntity(WadeEntity.class, wade.getId());
		message = "帅的不行删除成功";
		wadeService.delete(wade);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加帅的不行
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(WadeEntity wade, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(wade.getId())) {
			message = "帅的不行更新成功";
			WadeEntity t = wadeService.get(WadeEntity.class, wade.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(wade, t);
				wadeService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "帅的不行更新失败";
			}
		} else {
			message = "帅的不行添加成功";
			wadeService.save(wade);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 帅的不行列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(WadeEntity wade, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(wade.getId())) {
			wade = wadeService.getEntity(WadeEntity.class, wade.getId());
			req.setAttribute("wadePage", wade);
		}
		return new ModelAndView("com/emc/fee/wade");
	}
}
