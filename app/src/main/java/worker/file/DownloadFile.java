package worker.file;

import capyfile.rmi.DownloadFileArgs;
import capyfile.rmi.DownloadFileRes;
import com.healthmarketscience.rmiio.GZIPRemoteInputStream;
import com.healthmarketscience.rmiio.RemoteInputStreamServer;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.stream.IntStream;
import worker.config.Config;

public class DownloadFile
{
	public static DownloadFileRes downloadFile (DownloadFileArgs args)
	{
		// volume exists

		if (!IntStream.of (Config.getAvailableVolumes ()).anyMatch (x -> x == args.volume)) {
			return null;
		}

		RemoteInputStreamServer istream = null;

		try {

			// TODO: Falldown to backup

			String filePath = String.format (
				"%s/files/volume%d/%s", Config.getVolumeBasePath (), args.volume,
				args.uuid.toString ());
			File file = new File (filePath);

			istream =
				new GZIPRemoteInputStream (new BufferedInputStream (new FileInputStream (file)));

			DownloadFileRes res = new DownloadFileRes (args.uuid, file.length (), istream.export());

			// ignore "Resource leak" warning
			// client will closed it when it finishes reading
			// if not, the finally block will take care of it.

			istream = null;
			return res;

		} catch (Exception e) {
			e.printStackTrace ();
		} finally {
			if (istream != null)
				istream.close ();
		}

		return null;
	}
}
