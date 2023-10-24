package net.sf.l2j.gameserver.model.entity.events.tournament;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.l2j.commons.logging.CLogger;



public class TournamentManager
{
	private static final CLogger LOGGER = new CLogger(TournamentManager.class.getName());

	private static final Map<Integer, AbstractTournament> _tournaments = new ConcurrentHashMap<>();

	public static void init()
	{
		if (!TournamentConfig.init())
		{
			LOGGER.info("[TournamentManager] Failed to load configuration files. Tournaments won't load.");
			return;
		}

		for (int teamSize : TournamentConfig.TOURNAMENTS)
		{
			_tournaments.put(teamSize, new Tournament(teamSize));
		}

		LOGGER.info("[TournamentManager] Initialized " + _tournaments.size() + " tournament holders:");

	}

	public static AbstractTournament getTournament(int size)
	{
		return _tournaments.get(size);
	}

	public static Collection<AbstractTournament> getTournaments()
	{
		return _tournaments.values();
	}
	
	public static void replaceTournament(int size)
	{
		_tournaments.put(size, new Tournament(size));
	}
}

