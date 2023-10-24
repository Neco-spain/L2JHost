package net.sf.l2j.gameserver.protection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HwidProtectionKeyManager
{
	private static final String SITE_URL = "https://suportel2jhost.000webhostapp.com/verification/index.php";

	public static boolean checkUrl(String ipAddress)
	{
		if (loadUrl(ipAddress))
		{
			printLoadedMessage();
			return true;
		}
		else
		{
			printFailedMessage();
			return false;
		}
	}

	private static boolean loadUrl(String ipAddress)
	{
		try
		{
			URL siteUrl = new URL(SITE_URL + "?ip=" + ipAddress);
			HttpURLConnection connection = (HttpURLConnection) siteUrl.openConnection();
			connection.setRequestMethod("GET");

			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK)
			{
				try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream())))
				{
					String response = in.readLine();
					return response.equals("valid");
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return false;
	}

	private static void printLoadedMessage()
	{
		System.out.println("                                                                            ");
		System.out.println("----------------------------------------------------------------------------");
		System.out.println("                    Hwid Protect IP : " + ProtectionConfig.FIND_HOSTNAME + "");
		System.out.println("----------------------------------------------------------------------------");
		System.out.println("                                                                            ");
	}

	private static void printFailedMessage()
	{
		System.out.println("                                                                            ");
		System.out.println("----------------------------------------------------------------------------");
		System.out.println("------- Falha na conexao : nao foi possivel conectar-se ao servidor --------");
		System.out.println("----------------------------------------------------------------------------");
		System.out.println("                                                                            ");
		System.exit(2);
	}
}