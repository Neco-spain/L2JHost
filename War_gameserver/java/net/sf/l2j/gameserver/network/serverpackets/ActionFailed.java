package net.sf.l2j.gameserver.network.serverpackets;

public final class ActionFailed extends L2GameServerPacket
{
	public static final ActionFailed STATIC_PACKET = new ActionFailed();
	private static final String _S__35_ACTIONFAILED = "[S] 25 ActionFailed";
	
	public ActionFailed()
	{
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0x25);
	}

	@Override
	public String getType()
	{
		return _S__35_ACTIONFAILED;
	}
}