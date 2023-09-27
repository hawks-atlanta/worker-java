package worker;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Random;

public class TestUtil
{
	public static byte[] randomBytes (int size)
	{
		byte[] buff = new byte[size];
		(new Random ()).nextBytes (buff);
		return buff;
	}

	@SuppressWarnings ({ "unchecked" })
	public static void updateEnv (String name, String val) throws ReflectiveOperationException
	{
		Map<String, String> env = System.getenv ();
		Field field = env.getClass ().getDeclaredField ("m");
		field.setAccessible (true);
		((Map<String, String>)field.get (env)).put (name, val);
	}
}
