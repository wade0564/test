package cn.edu.ustc.wade.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
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
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class LoginChsi {
	
	
	public static void main(String[] args) throws Exception, IOException {
		
		
		// 默认的client类。
		HttpClient client = HttpClients.createDefault();  
			
		
		HttpPost httpPost = new HttpPost("https://account.chsi.com.cn/passport/login?service=http%3A%2F%2Fmy.chsi.com.cn%2Farchive%2Fj_spring_cas_security_check");
		
		CookieStore cookieStore = new BasicCookieStore();
		HttpContext httpContext = new BasicHttpContext();
		httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", "18326118706"));
		params.add(new BasicNameValuePair("password", "5711667"));
		params.add(new BasicNameValuePair("lt", "_c775006DE-3A87-5D5B-5987-A5C0A2A2DC4B_k8107F743-B7FC-13A8-13DC-C377C03111AE"));
		params.add(new BasicNameValuePair("_eventId", "submit"));
		params.add(new BasicNameValuePair("submit", "登  录"));

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
        
        System.out.println(statusCode);
        
        System.out.println( IOUtils.toString( response.getEntity().getContent()));
        
        
        
//        if(statusCode== HttpStatus.SC_MOVED_TEMPORARILY){
//            System.out.println("==============Response header Location===============");
//
//        	Header header = response.getFirstHeader("Location");
//        	response.getEntity().consumeContent();
//        	System.out.println(header.getValue());
//        	
//        	HttpGet httpGet2 =  new HttpGet(header.getValue());
//        	
//         HttpResponse execute = client.execute(httpGet2);
//         
//        	
//        }
        
        
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
