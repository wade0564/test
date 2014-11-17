package cn.edu.ustc.wade.spring;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** 
 * @author wade 
 * @version Nov 16, 2014 6:42:41 PM 
 */
@Component
public class T {
	
	
	
	private P p2;
	
	private List<P> ps;
	
	@Autowired
	private T (P t){
		
		System.out.println("==========constructor");
		System.out.println(t);
		
	}
	
	@PostConstruct
	private void postConstruct(){
		
		System.out.println("=============post constructor");
		
	}
	
	
	@Autowired
	private  void foo(){
		
		System.out.println("==============autowired method foo");
		
	}
	
	
	@Autowired
	private P p1;
	
	@Autowired
	private void setP(List<P> ps){
		System.out.println("================set Ps");
		this.ps =ps;
	}
	

}
