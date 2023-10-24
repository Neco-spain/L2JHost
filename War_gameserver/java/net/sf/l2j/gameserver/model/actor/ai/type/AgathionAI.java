package net.sf.l2j.gameserver.model.actor.ai.type;

import java.util.concurrent.ScheduledFuture;

import net.sf.l2j.commons.pool.ThreadPool;
import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.data.manager.SpawnManager;
import net.sf.l2j.gameserver.data.xml.NpcData;
import net.sf.l2j.gameserver.enums.LootRule;
import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.enums.items.ArmorType;
import net.sf.l2j.gameserver.enums.items.WeaponType;
import net.sf.l2j.gameserver.geoengine.GeoEngine;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.Summon;
import net.sf.l2j.gameserver.model.actor.instance.Adventurer;
import net.sf.l2j.gameserver.model.actor.instance.Agathion;
import net.sf.l2j.gameserver.model.actor.instance.Auctioneer;
import net.sf.l2j.gameserver.model.actor.instance.CastleWarehouseKeeper;
import net.sf.l2j.gameserver.model.actor.instance.Chest;
import net.sf.l2j.gameserver.model.actor.instance.ClanHallDoorman;
import net.sf.l2j.gameserver.model.actor.instance.ClanHallManagerNpc;
import net.sf.l2j.gameserver.model.actor.instance.ClanHallManagerNpcSiege;
import net.sf.l2j.gameserver.model.actor.instance.ClassMaster;
import net.sf.l2j.gameserver.model.actor.instance.Door;
import net.sf.l2j.gameserver.model.actor.instance.Folk;
import net.sf.l2j.gameserver.model.actor.instance.Gatekeeper;
import net.sf.l2j.gameserver.model.actor.instance.GrandBoss;
import net.sf.l2j.gameserver.model.actor.instance.Guard;
import net.sf.l2j.gameserver.model.actor.instance.Merchant;
import net.sf.l2j.gameserver.model.actor.instance.Monster;
import net.sf.l2j.gameserver.model.actor.instance.Pet;
import net.sf.l2j.gameserver.model.actor.instance.RaidBoss;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.group.Party;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.model.spawn.Spawn;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.taskmanager.ItemsOnGroundTaskManager;

public class AgathionAI
{
	private final Creature _agathion;
	
	private ScheduledFuture<?> _task;
	private long resurrectionSkillLastUsedTime = 0;
	
	public AgathionAI(Creature agathion)
	{
		_agathion = agathion;
		
	}
	
	public void start(Player player)
	{
		if (_task == null)
		{
			_task = ThreadPool.scheduleAtFixedRate(() -> executeRoutine(player), 500, 500);
		}
	}
	
	public void PickUp(Player player)
	{
		
		WorldObject monster = _agathion.getTarget() != null ? (WorldObject) _agathion.getTarget() : null;
		if (monster != null && !GeoEngine.getInstance().canSeeTarget(_agathion, monster))
			monster = null;
		
		monster = thinkPickUp();
		tryToPickUpItem(player);
		
	}
	
	public void tryToPickUpItem(Player player)
	{
		WorldObject itemObject = thinkPickUp();
		if (itemObject instanceof ItemInstance)
		{
			ItemInstance item = (ItemInstance) itemObject;
			if (_agathion.isIn2DRadius(item, 15))
			{
				
				synchronized (item)
				{
					if (!item.isVisible())
						return;
					
					if (!player.getInventory().validateCapacity(item))
					{
						player.sendPacket(SystemMessageId.YOUR_PET_CANNOT_CARRY_ANY_MORE_ITEMS);
						return;
					}
					
					if (item.getOwnerId() != 0 && !player.isLooterOrInLooterParty(item.getOwnerId()))
					{
						if (item.getItemId() == 57)
							player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.FAILED_TO_PICKUP_S1_ADENA).addNumber(item.getCount()));
						else if (item.getCount() > 1)
							player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.FAILED_TO_PICKUP_S2_S1_S).addItemName(item.getItemId()).addNumber(item.getCount()));
						else
							player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.FAILED_TO_PICKUP_S1).addItemName(item.getItemId()));
						
					}
					
					if (item.hasDropProtection())
						item.removeDropProtection();
					
					final Party party = player.getParty();
					if (party != null && party.getLootRule() != LootRule.ITEM_LOOTER)
						party.distributeItem(player, item);
					else
						item.pickupMe(_agathion);
					
					ItemsOnGroundTaskManager.getInstance().remove(item);
					
				}
				
				SystemMessage sm;
				
				if (item.getItemType() instanceof ArmorType || item.getItemType() instanceof WeaponType)
				{
					if (item.getEnchantLevel() > 0)
						sm = SystemMessage.getSystemMessage(SystemMessageId.ATTENTION_S1_PET_PICKED_UP_S2_S3).addCharName(player).addNumber(item.getEnchantLevel()).addItemName(item.getItemId());
					else
						sm = SystemMessage.getSystemMessage(SystemMessageId.ATTENTION_S1_PET_PICKED_UP_S2).addCharName(player).addItemName(item.getItemId());
					
					_agathion.broadcastPacketInRadius(sm, 1400);
				}
				
				if (item.getItemId() == 57)
					sm = SystemMessage.getSystemMessage(SystemMessageId.PET_PICKED_S1_ADENA).addItemNumber(item.getCount());
				else if (item.getEnchantLevel() > 0)
					sm = SystemMessage.getSystemMessage(SystemMessageId.PET_PICKED_S1_S2).addNumber(item.getEnchantLevel()).addItemName(item.getItemId());
				else if (item.getCount() > 1)
					sm = SystemMessage.getSystemMessage(SystemMessageId.PET_PICKED_S2_S1_S).addItemName(item.getItemId()).addItemNumber(item.getCount());
				else
					sm = SystemMessage.getSystemMessage(SystemMessageId.PET_PICKED_S1).addItemName(item.getItemId());
				
				player.sendPacket(sm);
				
				player.getInventory().addItem("Pickup", item, player, _agathion);
				InventoryUpdate playerIU1111 = new InventoryUpdate();
				playerIU1111.addItem(item);
				player.sendPacket(playerIU1111);
				
				ThreadPool.schedule(() -> _agathion.setIsParalyzed(false), 800);
				_agathion.setIsParalyzed(true);
				
			}
		}
	}
	
	public WorldObject thinkPickUp()
	{
		WorldObject itemObject = null;
		for (WorldObject toTest : _agathion.getKnownTypeInRadius(WorldObject.class, 800))
		{
			// Avoid target raidbosses, grandbosses and chests.
			if (toTest instanceof RaidBoss || toTest instanceof GrandBoss || toTest instanceof Chest)
				continue;
			
			if (toTest instanceof Player || toTest instanceof Pet || toTest instanceof Summon)
				continue;
			
			if (toTest instanceof Monster || toTest instanceof Door || toTest instanceof Guard || toTest instanceof Gatekeeper)
				continue;
			
			if (toTest instanceof Adventurer || toTest instanceof Folk || toTest instanceof Agathion)
				continue;
			
			if (toTest instanceof Auctioneer || toTest instanceof CastleWarehouseKeeper || toTest instanceof ClanHallDoorman)
				continue;
			
			if (toTest instanceof ClanHallManagerNpc || toTest instanceof Merchant || toTest instanceof ClanHallManagerNpcSiege || toTest instanceof ClassMaster)
				continue;
			
			if (toTest instanceof Npc || toTest instanceof Creature)
				continue;
			
			if (itemObject == null || _agathion.distance2D(itemObject) > _agathion.distance2D(toTest))
				itemObject = toTest;
			
			_agathion.getAI().tryToMoveTo(itemObject.getPosition(), null);
		}
		
		return itemObject;
	}
	
	public void executeRoutine(Player player)
	{
		if (player.getPlayerAgathion())
		{
			if (player.isTeleporting())
			{
				((Npc) _agathion).teleportTo(player.getPosition().clone(), 25);
			}
			int x, y, z;
			x = player.getX() + 15;
			y = player.getY() - 30;
			z = player.getZ();
			
			Location loc = new Location(x, y, z);
			_agathion.getAI().tryToMoveTo(loc, null);
			
			((Npc) _agathion).onRandomAnimation(Rnd.get(8));
			
			if (player.getAgathionPickUp())
				PickUp(player);
			
			executeSkill(player);
		}
		else
		{
			player.setPlayerAgathion(false);
			// ((Npc) _agathion).broadcastNpcSay(player.getName() + " left the game.");
			stop(player);
		}
		
	}
	
	private long lastAgathionBufferTime = 0;
	
	public void applyAgathionBuffer(Player player)
	{
		
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastAgathionBufferTime < Config.DRESS_ME_BUFFER_SKILL_TIME * 60 * 1000)
		{
			return;
		}
		
		if (!player.getPlayerAgathion())
		{
			return;
		}
		
		boolean hasAgathionBuffer = false;
		for (AbstractEffect effect : player.getAllEffects())
		{
			if (effect.getSkill().getId() == Config.DRESS_ME_BUFFER_SKILL_ID)
			{
				hasAgathionBuffer = true;
				break;
			}
		}
		if (hasAgathionBuffer)
		{
			return;
		}
		
		L2Skill skill = SkillTable.getInstance().getInfo(Config.DRESS_ME_BUFFER_SKILL_ID, 1);
		if (skill != null)
		{
			skill.getEffects(player, player);
			lastAgathionBufferTime = currentTime;
		}
	}
	
	public void executeSkill(Player player)
	{
		if (Config.DRESS_ME_ACTIVE_SKILLS)
		{
			applyAgathionBuffer(player);
			
			// Happen only when owner's HPs < 50%
			if (player.getStatus().getHpRatio() < 0.5)
			{
				for (final L2Skill skill : ((Npc) _agathion).getTemplate().getSkills(NpcSkillType.HEAL))
				{
					switch (skill.getSkillType())
					{
						case HEAL:
						case HOT:
						case BALANCE_LIFE:
						case HEAL_PERCENT:
						case HEAL_STATIC:
							((Npc) _agathion).getAI().tryToCast(player, skill);
							return;
					}
				}
			}
			
			// Happen only when owner's MPs < 50%
			if (player.getStatus().getMpRatio() < 0.5)
			{
				for (final L2Skill skill : ((Npc) _agathion).getTemplate().getSkills(NpcSkillType.HEAL))
				{
					switch (skill.getSkillType())
					{
						case MANARECHARGE:
						case MANAHEAL_PERCENT:
							((Npc) _agathion).getAI().tryToCast(player, skill);
							return;
					}
				}
			}
		}
		
		if (Config.DRESS_ME_ACTIVE_RESURRECT)
			executeDeath(player);
		
	}
	
	public void executeDeath(Player player)
	{
		if (player.isDead())
		{
			
			long currentTime = System.currentTimeMillis();
			long elapsedTime = currentTime - resurrectionSkillLastUsedTime;
			long resurrectionSkillReuseTime = 60000 * Config.DRESS_ME_RESS_SKILL_TIME;
			
			if (elapsedTime >= resurrectionSkillReuseTime)
			{
				L2Skill skill = SkillTable.getInstance().getInfo(1016, 9);
				
				player.broadcastPacket(new MagicSkillUse(_agathion, player, skill.getId(), 1, 1, 1));
				
				ThreadPool.schedule(() -> endRess(player), 1000 * 2);
				resurrectionSkillLastUsedTime = currentTime;
			}
		}
	}
	
	public void endRess(Player player)
	{
		L2Skill skill = SkillTable.getInstance().getInfo(1016, 9);
		player.reviveRequest(player, skill, false);
	}
	
	public void stop(Player player)
	{
		player.stopSkillEffects(Config.DRESS_ME_BUFFER_SKILL_ID);
		
		if (_task != null)
		{
			_task.cancel(false);
			_task = null;
			
			if (_agathion instanceof Agathion)
			{
				Agathion a = (Agathion) _agathion;
				
				final NpcTemplate npcTemplate = NpcData.getInstance().getTemplate(a.getNpcId());
				try
				{
					final Spawn spawn = new Spawn(npcTemplate);
					spawn.getNpc().deleteMe();
					spawn.setRespawnState(false);
					SpawnManager.getInstance().deleteSpawn(spawn);
					
				}
				catch (Exception e)
				{
					
				}
	
			}

			_agathion.deleteMe();
		}
	}
	
	public boolean isActive()
	{
		return _task != null;
	}
	
	public final Creature getAgathion()
	{
		return _agathion;
	}
	
}

