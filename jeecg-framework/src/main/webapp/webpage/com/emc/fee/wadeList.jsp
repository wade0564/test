<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="wadeList" title="帅的不行" actionUrl="wadeController.do?datagrid" idField="id" fit="true">
   <t:dgCol title="编号" field="id" hidden="false"></t:dgCol>
   <t:dgCol title="年龄" field="age" ></t:dgCol>
   <t:dgCol title="名字" field="name" ></t:dgCol>
   <t:dgCol title="工资" field="salary" ></t:dgCol>
   <t:dgCol title="出生日期" field="createdt" formatter="yyyy-MM-dd hh:mm:ss"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="wadeController.do?del&id={id}" />
   <t:dgToolBar title="录入" icon="icon-add" url="wadeController.do?addorupdate" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="wadeController.do?addorupdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="wadeController.do?addorupdate" funname="detail"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>