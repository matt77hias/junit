package kuleuven.group2.filewatch;

import java.nio.file.Path;

/**
 * An listener interface that should be implemented by classes that
 * want to know about directory watching.
 * 
 * @author Group2
 * @version 8 November 2013
 */
public interface DirectoryWatchListener {

	public void fileCreated(Path filePath);

	public void fileModified(Path filePath);

	public void fileDeleted(Path filePath);

}
