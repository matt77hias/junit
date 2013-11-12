package kuleuven.group2.filewatch;

import java.nio.file.Path;

public interface DirectoryWatchListener {

	public void fileCreated(Path filePath);

	public void fileModified(Path filePath);

	public void fileDeleted(Path filePath);

}
