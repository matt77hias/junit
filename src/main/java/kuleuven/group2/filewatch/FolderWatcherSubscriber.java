package kuleuven.group2.filewatch;

import java.nio.file.Path;

public interface FolderWatcherSubscriber {
	
	public void createEvent(Path filePath);
	
	public void modifyEvent(Path filePath);
	
	public void deleteEvent(Path filePath);

}
