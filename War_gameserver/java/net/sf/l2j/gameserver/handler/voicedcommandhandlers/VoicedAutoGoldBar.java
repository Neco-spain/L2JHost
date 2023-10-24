package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.taskmanager.AutoGoldBar;

/**
 * @author zeina
 */
public class VoicedAutoGoldBar implements IVoicedCommandHandler
{
	
	private static final String AUTOGB_COMMAND = "autogb";
	
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
		if (player.isAutoGb())
		{
			player.sendMessage("AUTO GB OFF");
			player.setAutoGb(false);
			AutoGoldBar.getInstance().remove(player);
		}
		else
		{
			player.sendMessage("AUTO GB ON");
			player.setAutoGb(true);
			AutoGoldBar.getInstance().add(player);
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
