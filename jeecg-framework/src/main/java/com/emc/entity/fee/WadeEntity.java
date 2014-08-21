package com.emc.entity.fee;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.SequenceGenerator;

/**   
 * @Title: Entity
 * @Description: 帅的不行
 * @author zhangdaihao
 * @date 2014-08-21 01:02:56
 * @version V1.0   
 *
 */
@Entity
@Table(name = "wade", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class WadeEntity implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**年龄*/
	private java.lang.Integer age;
	/**名字*/
	private java.lang.String name;
	/**工资*/
	private BigDecimal salary;
	/**出生日期*/
	private java.util.Date createdt;
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  id
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,length=36)
	public java.lang.String getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  id
	 */
	public void setId(java.lang.String id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  年龄
	 */
	@Column(name ="AGE",nullable=false,precision=10,scale=0)
	public java.lang.Integer getAge(){
		return this.age;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  年龄
	 */
	public void setAge(java.lang.Integer age){
		this.age = age;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  名字
	 */
	@Column(name ="NAME",nullable=false,length=255)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  名字
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  工资
	 */
	@Column(name ="SALARY",nullable=true,precision=19,scale=2)
	public BigDecimal getSalary(){
		return this.salary;
	}

	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  工资
	 */
	public void setSalary(BigDecimal salary){
		this.salary = salary;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  出生日期
	 */
	@Column(name ="CREATEDT",nullable=true)
	public java.util.Date getCreatedt(){
		return this.createdt;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  出生日期
	 */
	public void setCreatedt(java.util.Date createdt){
		this.createdt = createdt;
	}
}
