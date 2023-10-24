package net.sf.l2j.gameserver.protection;

import java.io.File;
import java.io.IOException;

import net.sf.l2j.commons.config.ExProperties;
import net.sf.l2j.commons.logging.CLogger;

public class ProtectionConfig
{
	private static final CLogger LOGGER = new CLogger(ProtectionConfig.class.getName());

	public static final String CONFIG_FILE = "config/HwidProtection.properties";

	public static String FIND_HOSTNAME;
	public static String KEY_HOSTNAME;
	public static boolean loading()
	{
		try
		{
			ExProperties config = load(CONFIG_FILE);

			FIND_HOSTNAME = config.getProperty("FindHostName", "127.0.0.1");
			KEY_HOSTNAME = config.getProperty("HwidName", "x0");

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static ExProperties load(String filename)
	{
		return load(new File(filename));
	}

	public static ExProperties load(File file)
	{
		ExProperties result = new ExProperties();

		try
		{
			result.load(file);
		}
		catch (IOException e)
		{
			LOGGER.error("Error loading config : " + file.getName() + "!", e);
		}

		return result;
	}
}