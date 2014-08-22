package wade.project.asupqueuing.business;

import java.util.Map;


public abstract class MessageProcessor{

	public abstract int processMessage(Map<Long, String> deliveryMap) ;
	
}
