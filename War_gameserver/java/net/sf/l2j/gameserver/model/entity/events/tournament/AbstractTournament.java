package net.sf.l2j.gameserver.model.entity.events.tournament;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

import net.sf.l2j.commons.pool.ThreadPool;
import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.LootRule;
import net.sf.l2j.gameserver.enums.SayType;
import net.sf.l2j.gameserver.enums.TeamType;
import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.Summon;
import net.sf.l2j.gameserver.model.group.Party;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.model.instancemanager.Instance;
import net.sf.l2j.gameserver.model.instancemanager.InstanceManager;
import net.sf.l2j.gameserver.model.olympiad.OlympiadManager;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.skills.AbstractEffect;

public abstract class AbstractTournament
{
	protected List<Player> _players = new CopyOnWriteArrayList<>();
	protected int _size;
	protected int _tick;
	protected TournamentState _state = TournamentState.INACTIVE;

	protected int _blueLives;
	protected int _redLives;

	protected Instance _instance;
	
	private ScheduledFuture<?> _schedular;
	
	private ScheduledFuture<?> _task = null;

	public AbstractTournament(int size)
	{
		_size = size;
		_tick = TournamentConfig.RUNNING_TIME;
		_blueLives = size;
		_redLives = size;
		_instance = InstanceManager.getInstance().createInstance();
	}

	public TournamentState getState()
	{
		return _state;
	}

	public int getTeamSize()
	{
		return _size;
	}

	public synchronized void handleRegUnReg(Player player)
	{
		if (getAllPlayer().contains(player))
		{
			if (_state != TournamentState.INACTIVE)
			{
				player.sendPacket(new CreatureSay(SayType.CLAN, "[Tournament]", "You may no unregister now."));
				return;
			}

			getAllPlayer().remove(player);
			player.setTournament(null);
			player.sendPacket(new CreatureSay(SayType.CLAN, "[Tournament]", "You successfuly unregistered."));
			return;
		}

		if (_state != TournamentState.INACTIVE)
		{
			player.sendPacket(new CreatureSay(SayType.CLAN, "[Tournament]", "A battle is starting. Please try again in" + TournamentConfig.TELEPORT_DELAY + " second(s)."));
			return;
		}

		if (OlympiadManager.getInstance().isRegisteredInComp(player))
		{
			player.sendPacket(new CreatureSay(SayType.CLAN, "[Tournament]", "Olympiad participants can't register."));
			return;
		}

		if (player.isCursedWeaponEquipped())
		{
			player.sendPacket(new CreatureSay(SayType.CLAN, "[Tournament]", "Cursed weapon owners are not allowed to participate."));
			return;
		}

		if (player.getKarma() > 0)
		{
			player.sendPacket(new CreatureSay(SayType.CLAN, "[Tournament]", "Chaotic players are not allowed to participate."));
			return;
		}

		if (player.getStatus().getLevel() < TournamentConfig.MIN_LEVEL)
		{
			player.sendPacket(new CreatureSay(SayType.CLAN, "[Tournament]", "Min level to register is " + TournamentConfig.MIN_LEVEL + "."));
			return;
		}

		if (player.getStatus().getLevel() > TournamentConfig.MAX_LEVEL)
		{
			player.sendPacket(new CreatureSay(SayType.CLAN, "[Tournament]", "Max level to register is " + TournamentConfig.MAX_LEVEL + "."));
			return;
		}

		if (TournamentConfig.RESTRICTED_CLASSES.contains(player.getClassId().getId()))
		{
			player.sendPacket(new CreatureSay(SayType.CLAN, "[Tournament]", "Your class cannot participate in tournament."));
			return;
		}

		for (IntIntHolder item : TournamentConfig.REQUIRES)
		{
			if (!player.destroyItemByItemId("", item.getId(), item.getValue(), player, true))
			{
				return;
			}
		}

		getAllPlayer().add(player);
		player.setTournament(this);
		player.sendPacket(new CreatureSay(SayType.CLAN, "[Tournament]", "You have been registered in " + _size + " vs " + _size + "."));

		if (isValidSize())
		{
			checkToStart();
		}
	}
	
	protected List<Player> getAllPlayer()
	{
		return _players;
	}

	protected List<Player> getPlayers(TeamType type)
	{
		return getAllPlayer().stream().filter(s -> s.getTeam() == type).collect(Collectors.toList());
	}

	protected boolean isValidSize()
	{
		return getAllPlayer().size() == _size * 2;
	}

	private synchronized void checkToStart()
	{
		// Set state to REGISTRATION
		_state = TournamentState.REGISTRATION;

		getAllPlayer().forEach(s -> s.sendPacket(new CreatureSay(SayType.CLAN, "[Tournament]", "Teleporting participants to an arena in " + TournamentConfig.TELEPORT_DELAY + " second(s).")));

		if (_task == null)
		{
			_task = ThreadPool.schedule(() ->
			{
				if (isValidSize())
				{
					startFight();
				}
				else
				{
					// Set state INACTIVE
					_state = TournamentState.INACTIVE;
					
					getAllPlayer().forEach(p ->
					{
						p.setTournament(null);
						p.sendPacket(new CreatureSay(SayType.CLAN, "[Tournament]", "Has been aborted due to participation."));
					});
					getAllPlayer().clear();
				}
			}, TournamentConfig.TELEPORT_DELAY * 1000);
		}
	}

	protected void startFight()
	{
		TournamentManager.replaceTournament(_size);
		
		_instance = InstanceManager.getInstance().createInstance();
		
		// Set state to STARTING
		_state = TournamentState.STARTING;

		Collections.shuffle(getAllPlayer());

		List<Player> temp = new ArrayList<>(getAllPlayer());

		int i = 0;
		while (temp.size() != 0)
		{
			i++;
			Player player = temp.get(Rnd.nextInt(temp.size()));
			player.setTeam(i == 1 ? TeamType.BLUE : TeamType.RED);
			player.broadcastUserInfo();
			temp.remove(player);

			if (i == 2)
			{
				i = 0;
			}
		}

		getAllPlayer().forEach(p ->
		{
			if (p.isDead())
			{
				p.doRevive();
			}

			p.getStatus().setMaxCpHpMp();

			final Summon summon = p.getSummon();
			if (summon != null)
			{
				summon.unSummon(p);
			}
			
			if (p.isInParty())
			{
				p.getParty().disband();
			}
				
			
			if (TournamentConfig.REMOVE_BUFFS)
			{
				for (AbstractEffect effect : p.getAllEffects())
					effect.exit();
			}

			p.setInstance(_instance, true);
			p.setIsParalyzed(true);
			p.sendPacket(new CreatureSay(SayType.HERO_VOICE, "[Tournament]", "Match starts in few seconds."));
			p.teleToLocation(p.getTeam() == TeamType.RED ? TournamentConfig.TEAM_RED_LOCATION : TournamentConfig.TEAM_BLUE_LOCATION);
		});

		ThreadPool.schedule(() ->
		{
			// Set state STARTED
			_state = TournamentState.STARTED;

			getAllPlayer().forEach(p ->
			{
				selectParty(p);
				p.sendPacket(new CreatureSay(SayType.HERO_VOICE, "[Tournament]", "Started, go fight!"));
				p.setIsParalyzed(false);
			});

			_tick = TournamentConfig.RUNNING_TIME * 60;
		}, 10000);
	}
	
	protected void stopFight(TeamType winner)
	{
		// Set state INACTIVATING
		_state = TournamentState.INACTIVATING;
		
		getAllPlayer().forEach(p ->
		{
			if (winner != null)
			{
				if (p.getTeam() == winner)
				{
					for (IntIntHolder rh : TournamentConfig.REWARDS)
					{
						p.addItem("", rh.getId(), rh.getValue(), p, true);
					}

					p.sendPacket(new CreatureSay(SayType.HERO_VOICE, "[Tournament]", "You will be teleported back after " + TournamentConfig.TELEPORT_DELAY + " second(s)."));
				}
				else
					p.sendPacket(new CreatureSay(SayType.HERO_VOICE, "[Tournament]", "You will be teleported back after " + TournamentConfig.TELEPORT_DELAY + " second(s)."));
			}

			p.setTournament(null);

			if (p.isDead())
			{
				p.doRevive();
			}
			
			p.getStatus().setMaxCpHpMp();
			p.setIsParalyzed(true);
		});

		ThreadPool.schedule(() ->
		{
			getAllPlayer().forEach(p ->
			{
				if (p.isInParty())
				{
					p.getParty().disband();
				}
				p.setInstance(InstanceManager.getInstance().getInstance(0), true);
				p.setTeam(TeamType.NONE);
				p.broadcastUserInfo();
				
				p.setIsParalyzed(false);
				p.teleToLocation(TournamentConfig.BACK_LOCATION);
			});

			getAllPlayer().clear();
		}, TournamentConfig.TELEPORT_DELAY * 1000);

		// Set state INACTIVE
		_state = TournamentState.INACTIVE;
	}
	
	public final void onStart()
	{
		_schedular = ThreadPool.scheduleAtFixedRate(this::onEnd, 1000, 1000);
	}

	public final void cancel()
	{
		if (_schedular != null)
		{
			_schedular.cancel(false);
			_schedular = null;
		}
	}

	public static boolean onScrollUse(Player player)
	{
		if (player.isInTournament() && !TournamentConfig.SCROLL_ALLOWED)
		{
			return true;
		}

		return false;
	}

	public static boolean canDoAttack(Creature attacker, Creature target)
	{
		if (attacker.getActingPlayer().isInTournament() && target.getActingPlayer().isInTournament() && attacker.getActingPlayer().getTeam() == target.getActingPlayer().getTeam() && !TournamentConfig.TARGET_TEAM_MEMBERS_ALLOWED)
		{
			attacker.sendPacket(ActionFailed.STATIC_PACKET);
			return true;
		}

		return false;
	}

	
	public void selectParty(Player player)
	{
		final int MAX_GROUP_DISTANCE = 2000;
		final int MAX_PARTY_SIZE = 9;
		Party actorParty = player.getParty();
		
		for (Player nearbyPlayer : player.getKnownTypeInRadius(Player.class, MAX_GROUP_DISTANCE))
		{
			
			if (!player.isIn3DRadius(nearbyPlayer, MAX_GROUP_DISTANCE) || nearbyPlayer.isInParty())
			{
				continue;
			}
			
			Party nearbyPlayerParty = nearbyPlayer.getParty();
			if (actorParty == null)
			{
				
				actorParty = new Party(player, nearbyPlayer, LootRule.ITEM_LOOTER);
				player.setParty(actorParty);
				nearbyPlayer.setParty(actorParty);
			}
			else if (actorParty.getMembersCount() < MAX_PARTY_SIZE)
			{
				if (actorParty.getMembers().stream().filter(k -> k.getClassId() == (ClassId.CARDINAL)).count() >= 2)
				{
					return;
				}
				
				actorParty.addPartyMember(nearbyPlayer);
				nearbyPlayer.setParty(actorParty);
			}
			else if (nearbyPlayerParty == null)
			{
				
				Party nearestParty = getNearestParty(nearbyPlayer, MAX_GROUP_DISTANCE);
				if (nearestParty != null)
				{
					if (nearestParty.getMembers().stream().filter(k -> k.getClassId() == (ClassId.CARDINAL)).count() >= 2)
					{
						
						return;
					}
					
					nearestParty.addPartyMember(nearbyPlayer);
					nearbyPlayer.setParty(nearestParty);
				}
				else
				{
					
					Party newParty = new Party(nearbyPlayer, nearbyPlayer, LootRule.ITEM_LOOTER);
					nearbyPlayer.setParty(newParty);
				}
			}
		}
	}
	
	private Party getNearestParty(Player player, int radius)
	{
		Party nearestParty = null;
		double nearestDistance = Double.MAX_VALUE;
		for (Player toTest : player.getKnownTypeInRadius(Player.class, radius))
		{
			if (player != toTest && !player.isIn3DRadius(toTest, 1000) && toTest.isInParty())
			{
				Party party = toTest.getParty();
				double distance = player.distance2D(party.getLeader());
				if (distance < nearestDistance && !party.isFull())
				{
					nearestParty = party;
					nearestDistance = distance;
				}
			}
		}
		return nearestParty;
	}
	
	public void onDisconnect(Player player)
	{
		_players.remove(player);
	}

	public abstract void onDie(Player player);
	
	public abstract void onEnd();
}
