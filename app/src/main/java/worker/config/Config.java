package worker.config;

import java.util.Arrays;

public class Config
{
	private static String volumeBasePath = "";
	private static int[] availableVolumes = {};

	public static String getVolumeBasePath () { return volumeBasePath; }
	public static int[] getAvailableVolumes () { return availableVolumes; }

	public static void initializeFromEnv ()
	{
		volumeBasePath = System.getenv ().getOrDefault ("VOLUME_BASE_PATH", "/tmp/store");

		String availableVolumesStr = System.getenv ().getOrDefault ("AVAILABLE_VOLUMES", "1,2,3");
		availableVolumes = Arrays.stream(availableVolumesStr.split(",")).mapToInt(Integer::parseInt).toArray();

		if (availableVolumes.length < 0) {
			throw new RuntimeException("No available volumes");
		}
	}
}
