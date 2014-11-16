package cn.edu.ustc.wade.http;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class GetTest2 {
	
	
	public static void main(String[] args) throws Exception, IOException {
		
		
		HttpClient client = HttpClientBuilder.create().build();
		
		HttpGet get = new HttpGet("http://mail.ustc.edu.cn/");
		
		HttpResponse response = client.execute(get);
		
		HttpEntity entity = response.getEntity();
		String html = EntityUtils.toString(entity,"GB2312");
		
		System.out.println(html);
		
		
	}
	
	
	
	

}
