import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @author wade
 * @version Dec 11, 2014 11:01:00 AM
 */
public class HttpTest {

	public static void main(String[] args) throws ClientProtocolException,
			IOException {
		HttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet("http://localhost:8080/hermes-agent/job/version");
		// HttpGet get = new
		// HttpGet("http://10.32.105.240:8080/hermes-agent/job/version");
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();
		String version = EntityUtils.toString(entity);
		System.out.println(version);
	}

}
