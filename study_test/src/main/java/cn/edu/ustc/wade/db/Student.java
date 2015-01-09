package cn.edu.ustc.wade.db;

import java.sql.Date;

public class Student {
	// 注意这里需要保证有一个无参构造方法，因为包括Hibernate在内的映射都是使用反射的，如果没有无参构造可能会出现问题
	private int id;
	private String name;
	private Date birth;
	private float score;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "id=" + id + "\tname=" + name + "\tmajor=" + birth + "\tscore="
				+ score + "\n";
	}
}