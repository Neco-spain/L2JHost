package net.sf.l2j.gameserver.model.actor.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.serverpackets.AbstractNpcInfo.NpcInfo;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.ServerObjectInfo;
import net.sf.l2j.gameserver.skills.L2Skill;

public class Agathion extends Folk
{
	protected static final Logger _logPet = Logger.getLogger(Agathion.class.getName());
	private static Player _owner;
	private static Npc npc;
	public static List<Agathion> _character;
	
	public Agathion(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		
		// Set the Player owner.
		npc = this;
		
		_character = new ArrayList<>();
		_character.add(this);
	}
	
	public static void setOwner(Player owner, Npc agathion)
	{
		owner = _owner;
		agathion = npc;
	}
	
	public Player getOwner()
	{
		return _owner;
	}
	
	public static Npc getNpcOwner()
	{
		
		return npc;
	}
	
	@Override
	public void onTeleported()
	{
		super.onTeleported();
		
		// Need it only for "crests on summons" custom.
		if (Config.SHOW_SUMMON_CREST)
			sendPacket(new NpcInfo(this, _owner));
	}
	
	@Override
	public void setWalkOrRun(boolean value)
	{
		super.setWalkOrRun(value);
		
		getStatus().broadcastStatusUpdate();
	}
	
	@Override
	public void updateAbnormalEffect()
	{
		sendPacket(new NpcInfo(this, _owner));
	}
	
	public void updateAndBroadcastStatus(int val)
	{
		if (isVisible())
		{
			for (Player player : getKnownType(Player.class))
			{
				if (player == _owner)
					continue;
				
				sendPacket(new NpcInfo(this, _owner));
			}
		}
	}
	
	@Override
	public boolean hasRandomAnimation()
	{
		return Config.MAX_MONSTER_ANIMATION > 0 && !isRaidRelated();
	}
	
	@Override
	public void deleteMe()
	{
		_character.clear();
		super.deleteMe();
		
	}
	
	@Override
	public void onSpawn()
	{
		// Need it only for "crests on summons" custom.
		if (Config.SHOW_SUMMON_CREST)
			sendPacket(new NpcInfo(this, _owner));
		
		getAI().getFollowStatus();
		super.onSpawn();
	}
	
	@Override
	public int getSkillLevel(int skillId)
	{
		for (final List<L2Skill> list : getTemplate().getSkills().values())
		{
			for (final L2Skill skill : list)
				if (skill.getId() == skillId)
					return skill.getLevel();
		}
		return 0;
	}
	
	@Override
	public L2Skill getSkill(int skillId)
	{
		for (final List<L2Skill> list : getTemplate().getSkills().values())
		{
			for (final L2Skill skill : list)
				if (skill.getId() == skillId)
					return skill;
		}
		return null;
	}
	
	@Override
	public void onInteract(Player player)
	{
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
	
	@Override
	public void sendInfo(Player player)
	{
		player.sendPacket((getStatus().getMoveSpeed() == 0) ? new ServerObjectInfo(this, player) : new NpcInfo(this, player));
	}
	
	@Override
	public void onAction(Player player, boolean isCtrlPressed, boolean isShiftPressed)
	{
		if (isCtrlPressed)
			player.sendPacket(ActionFailed.STATIC_PACKET);
		
		if (isShiftPressed)
			player.sendPacket(ActionFailed.STATIC_PACKET);
		
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
}
