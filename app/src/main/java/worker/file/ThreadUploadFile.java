package worker.file;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;
import worker.config.Config;
import worker.services.MetadataService;

public class ThreadUploadFile extends Thread
{
	private static final int READY_ATTEMPTS = 5;
	private static final long READY_TIMEOUT = 5000;
	UUID fileUUID;
	byte[] contents;

	public ThreadUploadFile (UUID fileUUID, byte[] contents)
	{
		this.fileUUID = fileUUID;
		this.contents = contents;
	}

	public void run ()
	{
		// pick volume to save file

		int[] vols = Config.getAvailableVolumes ();
		int volume = vols[(int)(Math.random () * vols.length)];

		String filePath = String.format (
			"%1$s/files/volume%2$d/%3$s", Config.getVolumeBasePath (), volume,
			this.fileUUID.toString ());
		String backupPath = String.format (
			"%1$s/backups/volume%2$d/%3$s", Config.getVolumeBasePath (), volume,
			this.fileUUID.toString ());

		// write

		if (!writeFile (filePath) || !writeFile (backupPath)) {
			return; // couldn't write
		}

		// mark file as ready

		for (int i = 0; i < READY_ATTEMPTS; i++) {
			if (MetadataService.fileReady (fileUUID, volume)) {
				break;
			}
			try {
				sleep (READY_TIMEOUT);
			} catch (Exception e) {
				System.err.println (e);
			}
		}
	}

	private boolean writeFile (String path)
	{
		// write file

		File outputFile = new File (path);

		try (FileOutputStream outputStream = new FileOutputStream (outputFile)) {
			outputStream.write (this.contents);
		} catch (Exception e) {
			System.err.println (e);
			return false;
		}

		return true;
	}
}
