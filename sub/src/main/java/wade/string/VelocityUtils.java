package wade.string;

import java.io.Writer;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VelocityUtils {
	public static Logger logger = LoggerFactory.getLogger(VelocityUtils.class);
	private static VelocityEngine ve;
	private static Template upgradeJobTemplate;
	private static Template jobTemplate;

	static {
		/* first, get and initialize an engine */
		ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class",
				ClasspathResourceLoader.class.getName());
		ve.init();
		/* next, get the Template */
		upgradeJobTemplate = ve.getTemplate("UpgradeJob.vm");
		jobTemplate = ve.getTemplate("job.vm");
	}


	public static void generatePPfileContent(String ppType,
			VelocityContext context, Writer writer) throws Exception {
		Template t = null;
		if ("upgrade".equalsIgnoreCase(ppType)) {
//			t = upgradeJobTemplate;
			t = upgradeJobTemplate;
		} else {
			t = jobTemplate;
		}
		t.merge(context, writer);
		writer.flush();
		writer.close();
	}

}
