package net.sf.l2j.gameserver.model.donate.voiced;

import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.previwer.htm.GenerationDonateMain;

public class VoiceExclusiveDonate implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
	{
		"donate"

	};
	
	@Override
	public boolean useVoicedCommand(String command, Player player, String target)
	{
		if (command.equals("donate"))
			GenerationDonateMain.Main(player);
		
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}
