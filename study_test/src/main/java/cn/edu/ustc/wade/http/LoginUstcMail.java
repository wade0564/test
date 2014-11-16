package cn.edu.ustc.wade.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.http.Header;
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

public class LoginUstcMail {
	
	
	public static void main(String[] args) throws Exception, IOException {
		
		
		// 默认的client类。
		HttpClient client = new DefaultHttpClient();
		
//		// 设置为get取连接的方式.
//		HttpGet httpGet1 = new HttpGet("http://mail.ustc.edu.cn");
//		
//		HttpResponse httpResponse1 = client.execute(httpGet1);
//		
//		Header[] headers1 = httpResponse1.getAllHeaders();
//        System.out.println("==============Response headers1===============");
//        for (Header header : headers1) {
//        	System.out.println(header);
//		}
		
		HttpPost httpPost = new HttpPost("http://mail.ustc.edu.cn/coremail/login.jsp");
		
		CookieStore cookieStore = new BasicCookieStore();
		HttpContext httpContext = new BasicHttpContext();
		httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("user", "sa612296@mail.ustc.edu.cn"));
		params.add(new BasicNameValuePair("password", "sa612296"));
		params.add(new BasicNameValuePair("domain", "mail.ustc.edu.cn"));
		params.add(new BasicNameValuePair("face", null));
		params.add(new BasicNameValuePair("Submit", "(unable to decode value)"));
		params.add(new BasicNameValuePair( ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY));
		setHeader(httpPost);
        httpPost.setEntity(new UrlEncodedFormEntity(params));
        System.out.println("==============Request headers===============");
        Header[] requestHeaders = httpPost.getAllHeaders();
        for (Header header : requestHeaders) {
        	System.out.println(header);
		}
        
        HttpResponse response = client.execute(httpPost,httpContext);
        Header[] headers = response.getAllHeaders();
        
        System.out.println("==============Response headers===============");
        for (Header header : headers) {
        	System.out.println(header);
		}
        
        int statusCode = response.getStatusLine().getStatusCode();
        
        if(statusCode== HttpStatus.SC_MOVED_TEMPORARILY){
            System.out.println("==============Response header Location===============");

        	Header header = response.getFirstHeader("Location");
        	response.getEntity().consumeContent();
        	System.out.println(header.getValue());
        	
        	HttpGet httpGet2 =  new HttpGet(header.getValue());
        	
         HttpResponse execute = client.execute(httpGet2);
         
        	
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
