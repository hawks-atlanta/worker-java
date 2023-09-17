package worker.config;

public class Config
{
	private static String volumeBasePath = "";
	private static int volumeCount = 3;

	public static String getVolumeBasePath () { return volumeBasePath; }
	public static int getVolumeCount () { return volumeCount; }

	public static void initializeFromEnv ()
	{
		volumeBasePath = System.getenv ().getOrDefault ("VOLUME_BASE_PATH", "/tmp/store");
		volumeCount = Integer.parseInt(System.getenv ().getOrDefault ("VOLUME_COUNT", "1"));
	}
}
