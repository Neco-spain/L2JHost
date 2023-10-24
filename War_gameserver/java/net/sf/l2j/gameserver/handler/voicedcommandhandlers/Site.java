package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.OpenUrl;

import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;


public class Site implements IVoicedCommandHandler {


    private static final String[] COMMANDS = new String[]{"forum","site"};


    private long _nextUpdate;


    private final Map<Integer, Integer> _lastPage = new ConcurrentHashMap<>();


    public boolean useVoicedCommand(String command, Player player, String params) {
        StringTokenizer st = new StringTokenizer(command);
        st.nextToken();
        if (command.startsWith("forum")) {
            player.sendPacket(new OpenUrl("https://www.l2jone.com/"));
        } else if (command.startsWith("site")) {
            player.sendPacket(new OpenUrl("https://l2jone.com/index.php?/forum/13-linushost/"));
        }
        return false;
    }

    public String[] getVoicedCommandList() {
        return COMMANDS;
    }
}