package net.sf.l2j.gameserver.model.entity.events.tournament;

import net.sf.l2j.gameserver.enums.SayType;
import net.sf.l2j.gameserver.enums.TeamType;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;

public class Tournament extends AbstractTournament
{
	public Tournament(int size)
	{
		super(size);
	}
	
	@Override
	public void onDisconnect(Player player)
	{
		super.onDisconnect(player);
		
		if (_state == TournamentState.STARTED)
		{
			TeamType winner = player.getTeam() == TeamType.RED ? TeamType.BLUE : TeamType.RED;
			
			getAllPlayer().forEach(p -> p.sendPacket(new CreatureSay(SayType.TRADE, "[Tournament]", player.getName() + " left the event. " + winner.name() + " team won the match!")));
			
			stopFight(winner);
			cancel();
		}
	}
	
	@Override
	public void onDie(Player player)
	{
		if (player.getTeam() == TeamType.RED)
		{
			_redLives--;
		}
		else if (player.getTeam() == TeamType.BLUE)
		{
			_blueLives--;
		}
		
		if (_redLives <= 0)
		{
			TeamType winner = TeamType.BLUE;
			
			getAllPlayer().forEach(p -> p.sendPacket(new CreatureSay(SayType.TRADE, "[Tournament]", "Event finish. Team (" + winner.name() + ") won the match. Congratulations!")));
			
			stopFight(winner);
			cancel();
		}
		else if (_blueLives <= 0)
		{
			TeamType winner = TeamType.RED;
			
			getAllPlayer().forEach(p -> p.sendPacket(new CreatureSay(SayType.TRADE, "[Tournament]", "Event finish. Team (" + winner.name() + ") won the match. Congratulations!")));
			
			stopFight(winner);
			cancel();
		}
	}
	
	@Override
	public void startFight()
	{
		onStart();
		
		super.startFight();
	}
	
	@Override
	public void onEnd()
	{
		if (_state == TournamentState.STARTED)
		{
			if (_tick <= 0)
			{
				getAllPlayer().forEach(s -> s.sendPacket(new CreatureSay(SayType.TRADE, "[Tournament]", "Time is up! The event has ended with both teams tied.")));
				stopFight(null);
				cancel();
				return;
			}
			
			_tick--;
		}
	}
}