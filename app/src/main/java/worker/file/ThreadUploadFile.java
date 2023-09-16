package worker.file;
import capyfile.rmi.interfaces.UploadFileArgs;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ThreadUploadFile extends Thread
{
	String filename;
	byte[] contents;

	public ThreadUploadFile (String filename, byte[] contents)
	{
		this.filename = filename;
		this.contents = contents;
	}

	public void run ()
	{
		System.out.println ("This code is running in a thread");

		// TODO: check file is not empty
		// TODO: check file is not too large?
		// TODO: pick place to save file

		String basePath = "/tmp/store";
		int volume = 1;
		String filePath =
			String.format ("%1$s/files/volume%2$d/%3$s", basePath, volume, this.filename);
		String backupPath =
			String.format ("%1$s/backups/volume%2$d/%3$s", basePath, volume, this.filename);

		// write

		writeFile (filePath);
		writeFile (backupPath);

		// let metadata know the file is ready
	}

	private boolean writeFile (String path)
	{
		// write file

		File outputFile = new File (path);

		try (FileOutputStream outputStream = new FileOutputStream (outputFile)) {
			outputStream.write (this.contents);
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println (e);
			return false;
		}

		return true;
	}
}
