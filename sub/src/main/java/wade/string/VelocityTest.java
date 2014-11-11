package wade.string;

import java.io.FileWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class VelocityTest {

	public static void main(String[] args) throws Exception {
		/* create a context and add data */
		VelocityContext context = new VelocityContext();
		context.put("node", "####1");
		context.put("packageName", "####2");
		context.put("packageFileName", "####3");
		context.put("paramsFileName", "####4");
		context.put("runTime", "####5");
		context.put("jobInsId", "####6");
		/* now render the template into a StringWriter */
		Writer writer = new StringWriter();
		VelocityUtils.generatePPfileContent("x", context, writer);
		
		System.out.println(writer.toString());
		
	}

}
