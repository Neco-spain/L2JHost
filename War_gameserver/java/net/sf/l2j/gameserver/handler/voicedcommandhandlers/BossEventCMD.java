package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

import net.sf.l2j.gameserver.data.manager.BossEvent;
import net.sf.l2j.gameserver.data.manager.BossEvent.EventState;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.ExShowScreenMessage;

import java.util.concurrent.TimeUnit;

public class BossEventCMD implements IVoicedCommandHandler
{
    @Override
    public boolean useVoicedCommand(String command, Player activeChar, String params)
    {
        if (command.startsWith("bossevent"))
        {
            if (BossEvent.getInstance().getState() != EventState.REGISTRATION)
            {
                activeChar.sendMessage("Boss Event is not running!");
                return false;
            }
            if (!BossEvent.getInstance().isRegistered(activeChar))
            {
                if (BossEvent.getInstance().canRegister(activeChar)) {  // Verifique se o jogador pode se registrar
                    if (BossEvent.getInstance().addPlayer(activeChar))
                    {
                        activeChar.sendPacket(new ExShowScreenMessage("You have successfully registered to Boss Event!", (int) TimeUnit.SECONDS.toMillis(5)));
                    }
                } else {
                    activeChar.sendMessage("Numero maximo de jogadores com o mesmo IP atingido para o evento.");
                }

            }
            else
            {
                if (BossEvent.getInstance().removePlayer(activeChar))
                {
                    activeChar.sendPacket(new ExShowScreenMessage("You have successfully unregistered from Boss Event!", (int) TimeUnit.SECONDS.toMillis(5)));
                }
            }
        }
        return false;
    }


    @Override
    public String[] getVoicedCommandList()
    {

        return new String[]
                {
                        "bossevent"
                };
    }

}
