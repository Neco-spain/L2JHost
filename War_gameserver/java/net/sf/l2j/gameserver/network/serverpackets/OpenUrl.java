package net.sf.l2j.gameserver.network.serverpackets;

public class OpenUrl
        extends L2GameServerPacket
{
    private final String _url;

    public OpenUrl(String url) {
      _url = url;
    }



    protected final void writeImpl() {
        writeC(112);
        writeS(_url);
    }
}
