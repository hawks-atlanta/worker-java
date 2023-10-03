package worker.services;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilLog
{
	public static String date ()
	{
		return new SimpleDateFormat ("yyyy.MM.dd HH:mm:ss").format (new Date ());
	}

	public static String format (String msg)
	{
		return new SimpleDateFormat ("yyyy.MM.dd HH:mm:ss | ").format (new Date ()) + msg;
	}
}
