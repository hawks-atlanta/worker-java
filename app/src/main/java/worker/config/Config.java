package worker.config;

import java.util.Arrays;
import worker.services.UtilLog;

public class Config
{
	private static String metadataBaseUrl = "";
	private static String volumeBasePath = "";
	private static int[] availableVolumes = {};

	public static String getMetadataBaseUrl () { return metadataBaseUrl; }
	public static String getVolumeBasePath () { return volumeBasePath; }
	public static int[] getAvailableVolumes () { return availableVolumes; }
	private static String getEnv (String env, String def)
	{
		return System.getenv ().getOrDefault (env, def);
	}

	public static void initializeFromEnv ()
	{
		Config.metadataBaseUrl = getEnv ("METADATA_BASEURL", "http://127.0.0.1:8082/api/v1");
		Config.volumeBasePath = getEnv ("VOLUME_BASE_PATH", "/var/capy/store");
		String aVols = getEnv ("AVAILABLE_VOLUMES", "");

		try {
			Config.availableVolumes =
				Arrays.stream (aVols.split (",")).mapToInt (Integer::parseInt).toArray ();
			if (availableVolumes.length == 0) {
				throw new RuntimeException (
					UtilLog.format ("Invalid var AVAILABLE_VOLUMES: No volumes specified"));
			}
		} catch (Exception e) {
			throw new RuntimeException (
				UtilLog.format ("Invalid var AVAILABLE_VOLUMES: Couldn't parse"));
		}
	}
}
