package cn.edu.ustc.wade.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class LoginUstcMailWrong {
	
	
	public static void main(String[] args) throws Exception, IOException {
		
		
		// 默认的client类。
		HttpClient client = new DefaultHttpClient();
		
		// 设置为get取连接的方式.
		HttpGet httpGet1 = new HttpGet("http://mail.ustc.edu.cn/coremail/myip.jsp");
		
		HttpResponse httpResponse1 = client.execute(httpGet1);
		
		Header[] headers1 = httpResponse1.getAllHeaders();
        System.out.println("==============Response headers1===============");
        for (Header header : headers1) {
        	System.out.println(header);
		}
		
        
        
//        System.out.println("Response code: "+ response.getStatusLine().getStatusCode());
//		
//		HttpEntity entity = response.getEntity();
//		
//        List<Cookie> cookies = ((AbstractHttpClient)client).getCookieStore().getCookies();
//        
//        for (Cookie cookie : cookies) {
//			System.out.println(cookie);
//		}
//
//		String html = EntityUtils.toString(entity,"GB2312");
//		System.out.println(html);
		
		
	}
	
	public static void setHeader(HttpPost post) throws Exception {
		
		List<Header> headers  = new ArrayList<Header>();
		
		 headers.add(new BasicHeader("Accept", "text/html, application/xhtml+xml, */*"));
		 headers.add(new BasicHeader("Accept-Language", "zh-CN"));
		 headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)"));
		 headers.add(new BasicHeader("Accept-Encoding", "gzip, deflate"));
		
		post.setHeaders(headers.toArray(new Header[0]));
		
    }
	
	

}
