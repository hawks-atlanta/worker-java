package worker.file;

import capyfile.rmi.DownloadFileArgs;
import capyfile.rmi.DownloadFileRes;
import com.healthmarketscience.rmiio.GZIPRemoteInputStream;
import com.healthmarketscience.rmiio.RemoteInputStreamServer;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.InvalidParameterException;
import java.util.stream.IntStream;
import worker.config.Config;
import worker.services.UtilLog;

public class DownloadFile
{
	public static DownloadFileRes downloadFile (DownloadFileArgs args) throws Exception
	{
		// volume exists

		if (!IntStream.of (Config.getAvailableVolumes ()).anyMatch (x -> x == args.volume)) {
			throw new InvalidParameterException ();
		}

		RemoteInputStreamServer istream = null;

		try {

			File file = new File (String.format (
				"%s/files/volume%d/%s", Config.getVolumeBasePath (), args.volume,
				args.uuid.toString ()));

			// fallback to backup
			// NOTE: This could have problems in NFS

			if (!file.isFile ()) {

				file = new File (String.format (
					"%s/backups/volume%d/%s", Config.getVolumeBasePath (), args.volume,
					args.uuid.toString ()));

				// bad luck couldn't find backup
				if (!file.isFile ()) {
					throw new FileNotFoundException ();
				}
			}

			istream =
				new GZIPRemoteInputStream (new BufferedInputStream (new FileInputStream (file)));

			DownloadFileRes res = new DownloadFileRes (args.uuid, file.length (), istream.export());
			System.err.println (UtilLog.format ("File download ready to stream"));

			// ignore "Resource leak" warning
			// client will closed it when it finishes reading
			// if not, the finally block will take care of it.

			istream = null;
			return res;

		} catch (Exception e) {
			System.err.println (UtilLog.format (e.toString ()));
		} finally {
			if (istream != null)
				istream.close ();
		}

		throw new FileNotFoundException (UtilLog.format ("Couldn't find file"));
	}
}
