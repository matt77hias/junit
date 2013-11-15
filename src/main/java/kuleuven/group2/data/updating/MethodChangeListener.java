package kuleuven.group2.data.updating;

import java.lang.reflect.Method;
import java.util.Date;

public interface MethodChangeListener {

	public void changed(Method method, Date time);
	
}
