package worker.file;
import capyfile.rmi.interfaces.UploadFileArgs;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ThreadUploadFile extends Thread
{
	UploadFileArgs args;

	public ThreadUploadFile (UploadFileArgs args) { this.args = args; }

	public void run ()
	{
		System.out.println ("This code is running in a thread");

		// TODO: check file is not empty
		// TODO: check file is not too large?
		// TODO: pick place to save file

		String basePath = "/tmp/store/";
		String path = basePath + this.args.uuid;

		// write file

		File outputFile = new File (path);

		try (FileOutputStream outputStream = new FileOutputStream (outputFile)) {
			outputStream.write (this.args.contents);
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println (e);
		}

		// let metada know the file is ready
	}
}
