package wade.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GetTest {
	
	
	public static void main(String[] args) throws Exception, IOException {
		
		
		// 默认的client类。
		HttpClient client = new DefaultHttpClient();
		
		// 设置为get取连接的方式.
//		HttpGet get = new HttpGet("http://www.baidu.com");
		HttpGet get = new HttpGet("http://mail.ustc.edu.cn/");
		
//		get.addHeader("Content-Type", "text/html; charset=GB2312");
		// 得到返回的response.
		HttpResponse response = client.execute(get);
		
		HttpEntity entity = response.getEntity();
//		String html = EntityUtils.toString(entity);
		
//		System.out.println(html);
		
		System.out.println("Response code: "+ response.getStatusLine().getStatusCode());
		System.out.println("Character encode: "+ response.getEntity().getContentEncoding());
		
		
		
		BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"GB2312"));

		
		StringBuffer sb = new StringBuffer();
		char[]  cbuf = new char[1024];
		int n=-1;
		while((n=br.read(cbuf))!=-1){
			
			sb.append(new String(cbuf, 0, n));
//			System.out.println(new String(s.getBytes("utf-8"), "GB2312"));
//			System.out.println(new String(s.getBytes("UTF-8"), "UTF-8"));
//			System.out.println(s);
		}
		
		br.close();
		
		System.out.println(sb.toString());
//	    Document document = Jsoup.parse(sb.toString());
	    
//	    Elements elementsByAttribute = document.getElementsByAttribute("href");
//	    
//	    for (Element element : elementsByAttribute) {
//			
//	    	System.out.println(element.text());
//		}
		
	}
	
	
	public static void print(Object o){
		
		System.out.println(o);
		
	}
	
	

}
