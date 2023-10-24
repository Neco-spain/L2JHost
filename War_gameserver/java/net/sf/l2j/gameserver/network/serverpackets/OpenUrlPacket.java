package net.sf.l2j.gameserver.network.serverpackets;

public class OpenUrlPacket extends L2GameServerPacket
{
	private final String _url;
	
	public OpenUrlPacket(String url)
	{
		_url = url;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x70);
		writeS(_url);
	}	
}
