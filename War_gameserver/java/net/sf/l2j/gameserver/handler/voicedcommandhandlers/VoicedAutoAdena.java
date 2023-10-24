package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.taskmanager.AutoAdena;

/**
 * @author zeina
 */
public class VoicedAutoAdena implements IVoicedCommandHandler
{
	
	private static final String AUTOGB_COMMAND = "autoAdena";
	
	@Override
	public boolean useVoicedCommand(String command, Player player, String target)
	{
		if (command.startsWith(AUTOGB_COMMAND))
		{
			toggleAutoGoldBar(player);
			Menu.showHtm(player);
			return true;
		}
		return false;
	}
	
	/**
	 * Ativa ou desativa a funcionalidade AutoGoldBar para o jogador.
	 * @param player O jogador em questão.
	 */
	private void toggleAutoGoldBar(Player player)
	{
		if (player.isAutoAdena())
		{
			player.sendMessage("AUTO ADENA OFF");
			player.setAutoAdena(false);
			AutoAdena.getInstance().remove(player);
		}
		else
		{
			player.sendMessage("AUTO ADENA ON");
			player.setAutoAdena(true);
			AutoAdena.getInstance().add(player);
		}
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return new String[]
		{
			AUTOGB_COMMAND
		};
	}
}
