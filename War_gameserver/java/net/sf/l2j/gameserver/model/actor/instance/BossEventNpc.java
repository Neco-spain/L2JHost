package net.sf.l2j.gameserver.model.actor.instance;

import net.sf.l2j.gameserver.data.manager.BossEvent;
import net.sf.l2j.gameserver.data.manager.BossEvent.EventState;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * joseina
 */
public class BossEventNpc extends Folk
{
    private final NpcTemplate template;

    /**
     * @param objectId
     * @param template
     */
    public BossEventNpc(int objectId, NpcTemplate template)
    {
        super(objectId, template);
        this.template = template;
    }

    @Override
    public void showChatWindow(Player player, int val)
    {
        NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
        html.setFile(player.isLang() + "/mods/BossEvent.htm");
        html.replace("%objectId%", String.valueOf(getObjectId()));
        html.replace("%npcname%", getName());
        html.replace("%regCount%", String.valueOf(BossEvent.getInstance().eventPlayers.size()));
        player.sendPacket(html);
    }

    @Override
    public void onBypassFeedback(Player activeChar, String command)
    {

        super.onBypassFeedback(activeChar, command);
        if (command.startsWith("register"))
        {
            if (BossEvent.getInstance().getState() != EventState.REGISTRATION)
            {
                activeChar.sendMessage("Boss Event is not running!");
                return;
            }
            if (!BossEvent.getInstance().isRegistered(activeChar))
            {
                if (BossEvent.getInstance().addPlayer(activeChar))
                {
                    activeChar.sendMessage("You have been successfully registered in Boss Event!");
                }

            }
            else
            {
                if (BossEvent.getInstance().removePlayer(activeChar))
                {
                    activeChar.sendMessage("You have been successfully removed of Boss Event!");
                }
            }
        }
    }

}
