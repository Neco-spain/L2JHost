package net.sf.l2j.gameserver.enums;

public enum DonateSite
{
	PAYPAL, MERCADOPAGO, NUBANK, DEPOSITO;

	@Override
	public String toString()
	{
		return capitalizeFirst(name().toLowerCase());
	}

	private static String capitalizeFirst(final String str)
	{
		if (str == null || str.isEmpty())
		{
			return str;
		}

		final char[] arr = str.toCharArray();
		final char c = arr[0];

		if (Character.isLetter(c))
		{
			arr[0] = Character.toUpperCase(c);
		}

		return new String(arr);
	}
}
