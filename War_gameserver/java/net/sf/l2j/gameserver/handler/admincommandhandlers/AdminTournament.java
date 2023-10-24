package net.sf.l2j.gameserver.handler.admincommandhandlers;

import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;

public class AdminTournament implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_tour"
	};
	
	@Override
	public void useAdminCommand(String command, Player activeChar)
	{
		
		if (command.equals("admin_tour"))
		{
	
		}

	}
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}
