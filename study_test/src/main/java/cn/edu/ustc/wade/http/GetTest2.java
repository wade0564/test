package cn.edu.ustc.wade.http;

import java.io.IOException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class GetTest2 {
	
	
	public static void main(String[] args) throws Exception, IOException {
		
		
		Document loginHtml = Jsoup.parse(new URL("https://account.chsi.com.cn/passport/login") , 60*1000);
		
		String lt = loginHtml.getElementsByAttributeValue("name", "lt").val();
	
		System.out.println(lt);
		
		
	}
	
	
	
	

}
