package cn.edu.ustc.wade.http;

import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HttpsTest {
	
	public static void main(String[] args) throws Exception {
		
		Document loginHtml = Jsoup.parse(new URL("https://account.chsi.com.cn/passport/login") , 60*1000);
		
		String lt = loginHtml.getElementsByAttributeValue("name", "lt").val();
	
		System.out.println(lt);
		
			SSLContextBuilder builder = new SSLContextBuilder();
		    builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
		    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
		    CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

		    HttpPost httpPost = new HttpPost("https://account.chsi.com.cn/passport/login?service=http%3A%2F%2Fmy.chsi.com.cn%2Farchive%2Fj_spring_cas_security_check");
		    List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", "18326118706"));
			params.add(new BasicNameValuePair("password", "5711667"));
			params.add(new BasicNameValuePair("lt", lt));
			params.add(new BasicNameValuePair("_eventId", "submit"));
			params.add(new BasicNameValuePair("submit", "登  录"));
			setHeader(httpPost);
			httpPost.setEntity(new UrlEncodedFormEntity(params));
		    CloseableHttpResponse response = httpclient.execute(httpPost);
//		    try {
		        System.out.println(response.getStatusLine());
		        int statusCode = response.getStatusLine().getStatusCode();
		        if(statusCode== HttpStatus.SC_MOVED_TEMPORARILY){
		        	response.getEntity().consumeContent();
		        	 System.out.println("==============Response header Location===============");
		             Header[] allheaders = response.getAllHeaders();
		             
		             for (Header header : allheaders) {
		 				System.out.println("======="+header.getName()+"|"+header.getValue());
		 			}
//		        		Header header = response.getFirstHeader("Location");
//		        		System.out.println(header.getValue());
//		        		EntityUtils.consume(response.getEntity());
		        }
		        HttpEntity entity = response.getEntity();
		        EntityUtils.consume(entity);
//		    }
//		    finally {
//		        response.close();
//		    }
		    

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
