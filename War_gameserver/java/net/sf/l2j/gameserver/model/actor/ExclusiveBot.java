package net.sf.l2j.gameserver.model.actor;

import net.sf.l2j.commons.math.MathUtil;
import net.sf.l2j.commons.pool.ThreadPool;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.manager.BufferManager;
import net.sf.l2j.gameserver.data.xml.MapRegionData;
import net.sf.l2j.gameserver.data.xml.MapRegionData.TeleportType;
import net.sf.l2j.gameserver.enums.BotSpellType;
import net.sf.l2j.gameserver.enums.ShortcutType;
import net.sf.l2j.gameserver.enums.TeamType;
import net.sf.l2j.gameserver.enums.skills.SkillTargetType;
import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.geoengine.GeoEngine;
import net.sf.l2j.gameserver.handler.itemhandlers.ItemSkills;
import net.sf.l2j.gameserver.model.Shortcut;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.instance.Chest;
import net.sf.l2j.gameserver.model.actor.instance.GrandBoss;
import net.sf.l2j.gameserver.model.actor.instance.Monster;
import net.sf.l2j.gameserver.model.actor.instance.RaidBoss;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.network.serverpackets.ExAutoSkillShot;
import net.sf.l2j.gameserver.network.serverpackets.ExServerPrimitive;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.L2Skill;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

public class ExclusiveBot
{
	private final Player _actor;
	private ScheduledFuture<?> _task;
	
	private int LOW_LIFE_PERCENTAGE = 70;
	private int CHANCE_PERCENTAGE = 70;
	
	private int RADIUS = 1200;
	private int HP_POTION_PERCENTAGE = 80;
	private int MP_POTION_PERCENTAGE = 80;
	
	private boolean TOGGLE_NO_BUFF_PROTECTION = true;
	protected int iterationsOnDeath = 0;
	private final int toVillageIterationsOnDeath = 12;
	
	public ExclusiveBot(Player actor)
	{
		_actor = actor;
	}

	public void SelecionarAlvo() {
		if (_actor.isViwerRandiosDOA()) {
			final ExServerPrimitive debug = _actor.getDebugPacket("ZONE");
			debug.reset();

			debug.addCircle("Radius", Color.YELLOW, false, _actor.getX(), _actor.getY(), _actor.getZ(), getRadius());

			debug.sendTo(_actor);
		}

		Monster target = _actor.getTarget() instanceof Monster ? (Monster) _actor.getTarget() : null;

		if (target != null && !GeoEngine.getInstance().canSeeTarget(_actor, target)) {
			target = null;
		}

		if (!_actor.getMemos().getBool("partyAssist")) {
			target = selectTarget();
			_actor.setTarget(target);
		} else {
			if (_actor.getParty() != null) {
				for (Player pm : _actor.getParty().getMembers()) {
					Player leader = pm.getParty().getLeader();

					Monster targetLeader = (Monster) leader.getTarget();
					if (targetLeader != null && _actor.isIn3DRadius(targetLeader, leader.getBot().getRadius())) {
						_actor.setTarget(targetLeader);
					}
				}
			}
		}
	}
	
	public void helpSummonAttack()
	{
		final Monster target = (Monster) _actor.getTarget();
		
		final Summon summon = _actor.getSummon();
		if (summon != null)
			summon.getAI().tryToAttack(target);
		
	}
	
	public void thinking()
	{
		SelecionarAlvo();
		
		if (_actor.isDead())
		{
			handleDeath();
			return;
		}
		
		// Avoid continue if player is already acting or its unable to act.
		if (_actor.isMoving() || _actor.getCast().isCastingNow() || _actor.isMovementDisabled() || _actor.isOutOfControl() || forceStopSystem())
			return;
		
		if (_actor.getMemos().getBool("potion"))
			calculatePotions();
		
		if (_actor.getMemos().getBool("PetAttack"))
			helpSummonAttack();
		
		if (calculateSweep())
			return;
		
		Monster monster = _actor.getTarget() instanceof Monster ? (Monster) _actor.getTarget() : null;
		if (monster != null && !GeoEngine.getInstance().canSeeTarget(_actor, monster))
			monster = null;
		
		L2Skill skill = null;
		if (percentageHpIsLessThan(LOW_LIFE_PERCENTAGE))
			skill = findSkill(BotSpellType.LOW_LIFE);
		
		if (skill == null && Rnd.get(100) < CHANCE_PERCENTAGE) // Check chance skill with 15% success.
			skill = findSkill(BotSpellType.CHANCE);
		
		if (skill == null)
			skill = findSkill(BotSpellType.COMMON);
		
		if (skill != null && skill.getTargetType() == SkillTargetType.CORPSE_MOB && monster != null && monster.isDead())
			doSkill(skill, false);
		else
		{
			monster = selectTarget();
			_actor.setTarget(monster);
			
		}
		
		if (skill != null && !_actor.isSkillDisabled(skill))
			doSkill(skill, (skill.isHeal() && percentageHpIsLessThan(95)) || skill.getTargetType() == SkillTargetType.SELF);
		
		else if (_actor.getMemos().getBool("forceAttack"))
		{
			if (haveActionOnShortcutF10(2))
			{
				ItemInstance weapon = _actor.getActiveWeaponInstance();
				if (weapon == null)
				{
					_actor.abortAll(true);
					return;
				}
				
				_actor.getAI().tryToAttack(monster);
			}
			else if (haveActionOnShortcutF1(2))
			{
				ItemInstance weapon = _actor.getActiveWeaponInstance();
				if (weapon == null)
				{
					_actor.abortAll(true);
					return;
				}
				_actor.getAI().tryToAttack(monster);
			}
		}
	}
	
	protected void handleDeath()
	{
		if (_actor.isDead())
		{
			if (iterationsOnDeath >= toVillageIterationsOnDeath)
			{
				_actor._savedLocation.set(_actor.getPosition());
				toVillageOnDeath();
			}
			iterationsOnDeath++;
			return;
		}
		
		iterationsOnDeath = 0;
	}
	
	protected void returnDeath()
	{
		_actor.teleportTo(_actor.getSavedLocation(), 0);
		// Clear the location.
		_actor._savedLocation.clean();
	}
	
	protected void checkItems()
	{
		checkItem(728);
		checkItem(1539);
	}
	
	private void checkItem(int itemId)
	{
		ItemInstance item = _actor.getInventory().getItemByItemId(itemId);
		if (item == null || item.getCount() <= 20)
		{
			_actor.getInventory().addItem("", itemId, 50, _actor, null);
		}
	}
	
	protected void toVillageOnDeath()
	{
		Location loc = null;
		
		loc = MapRegionData.getInstance().getLocationToTeleport(_actor, TeleportType.TOWN);
		
		_actor.setIsIn7sDungeon(false);
		
		_actor.doRevive();
		
		Summon summon = _actor.getSummon();
		
		_actor.teleportTo(loc, 20);
		
		if (summon != null)
		{
			if (summon.isDead())
				summon.doRevive();
			
			summon.teleportTo(loc, 25);
		}
		
		ThreadPool.schedule(() -> returnDeath(), 1000 * 15);
	}

	/**
	 * Start the auto farming process.
	 */
	public void start() {
		// Only start if the task is not already running
		if (_task == null) {
			// Schedule the task to run every 500 milliseconds
			_task = ThreadPool.scheduleAtFixedRate(() -> thinking(), 500, 500);

			// Send a message to the actor
			_actor.sendMessage("Auto farming just started.");

			// Set the team based on the actor's class
			_actor.setTeam(_actor.isMageClass() ? TeamType.BLUE : TeamType.RED);

			// Broadcast the character information
			_actor.broadcastCharInfo();

			// Broadcast the user information
			_actor.broadcastUserInfo();

			// Check if the bot range is greater than 1
			if (_actor.getMemos().getInteger("botRange") > 1) {
				// Set the RADIUS based on the actor's range
				RADIUS = _actor.isbotRangeDOA();
			}
		}
	}
	
	public void stop()
	{
		if (_task != null)
		{
			_task.cancel(false);
			_task = null;
			_actor.sendMessage("Auto farming has been stoped.");
			_actor.setTeam(TeamType.NONE);
			_actor.abortAll(true);
			_actor.broadcastCharInfo();
			_actor.broadcastUserInfo();
			_actor.setTarget(null);
			for (final L2Skill skill : _actor.getSkills().values())
				_actor.sendPacket(new ExAutoSkillShot(skill, 0));
			
			final ExServerPrimitive debug = _actor.getDebugPacket("ZONE");
			debug.reset();
			debug.sendTo(_actor);
		}
		
	}
	
	public boolean isActive()
	{
		return _task != null;
	}
	
	public void setLowLifePercentage(int value)
	{
		LOW_LIFE_PERCENTAGE = MathUtil.limit(value, 5, 95);
	}
	
	public int getLowLifePercentage()
	{
		return LOW_LIFE_PERCENTAGE;
	}
	
	public void setChancePercentage(int value)
	{
		CHANCE_PERCENTAGE = MathUtil.limit(value, 5, 95);
	}
	
	public int getChancePercentage()
	{
		return CHANCE_PERCENTAGE;
	}
	
	public void setRadius(int value)
	{
		RADIUS = MathUtil.limit(value, 100, 2500);
		
	}
	
	public int getRadius()
	{
		return RADIUS;
	}
	
	public void setHpPotionPercentage(int value)
	{
		HP_POTION_PERCENTAGE = MathUtil.limit(value, 0, 95);
	}
	
	public int getHpPotionPercentage()
	{
		return HP_POTION_PERCENTAGE;
	}
	
	public void setMpPotionPercentage(int value)
	{
		MP_POTION_PERCENTAGE = MathUtil.limit(value, 0, 95);
	}
	
	public int getMpPotionPercentage()
	{
		return MP_POTION_PERCENTAGE;
	}
	
	public void setNoBuffProtection(boolean val)
	{
		TOGGLE_NO_BUFF_PROTECTION = val;
	}
	
	public boolean isNoBuffProtected()
	{
		return TOGGLE_NO_BUFF_PROTECTION;
	}
	
	private boolean forceStopSystem()
	{
		if (!_actor.isbot())
		{
			stop();
			return true;
		}
		
		final boolean force = isActive() && TOGGLE_NO_BUFF_PROTECTION && _actor.getAllEffects().length <= 8 && _actor.getTarget() == null;
		if (force)
		{
			if (_actor.getMemos().getBool("autobuffer"))
			{
				final Map<String, ArrayList<L2Skill>> schemes = BufferManager.getInstance().getPlayerSchemes(_actor.getObjectId());
				if (schemes == null || schemes.isEmpty())
				{
					
				}
				else
				{
					for (Map.Entry<String, ArrayList<L2Skill>> scheme : schemes.entrySet())
					{
						if (_actor.isSelectBufferDOA())
							BufferManager.getInstance().applySchemeEffects(null, _actor, _actor.getObjectId(), scheme.getKey());
						
					}
				}
				
			}
		}
		return force;
	}
	
	public Monster selectTarget()
	{
		Monster monster = null;
		for (Monster toTest : _actor.getKnownTypeInRadius(Monster.class, RADIUS))
		{
			if (toTest == null || toTest.isDead() || !GeoEngine.getInstance().canSeeTarget(_actor, toTest) || !toTest.isAttackableWithoutForceBy(_actor))
				continue;
			
			if (!_actor.getCast().isCastingNow() && !_actor.getAttack().isAttackingNow())
				if (!GeoEngine.getInstance().canMoveToTarget(_actor, toTest))
					continue;
				
			// Avoid target raidbosses, grandbosses and chests.
			if (toTest instanceof RaidBoss || toTest instanceof GrandBoss || toTest instanceof Chest || toTest.isRaidRelated())
				continue;
			
			if (monster == null || _actor.distance2D(monster) > _actor.distance2D(toTest))
				monster = toTest;
		}
		return monster;
	}

	private boolean isNecessarySkill(L2Skill skill) {
		if (skill == null) {
			return false;
		}

		final WorldObject target = _actor.getTarget();
		if (target instanceof Monster) {
			final Monster monster = (Monster) target;
			if (skill.getSkillType() == SkillType.SPOIL && monster.getSpoilState().isSpoiled()) {
				return false;
			}

			List<AbstractEffect> effects = Arrays.stream(monster.getAllEffects())
					.filter(e -> e.getSkill().isDebuff())
					.collect(Collectors.toList());
			if (effects != null && !effects.isEmpty() &&
					effects.stream().anyMatch(e -> e.getSkill().getId() == skill.getId())) {
				return false;
			}

			if (!monster.isDead() && skill.getTargetType() == SkillTargetType.CORPSE_MOB) {
				return false;
			}

			return true;
		}

		return false;
	}
	
	private void doSkill(L2Skill skill, boolean isSelfSkill)
	{
		final WorldObject target = _actor.getTarget();
		if (skill == null || !(target instanceof Creature))
			return;
		
		if (isNecessarySkill(skill))
			_actor.getAI().tryToCast(isSelfSkill ? _actor : (Creature) target, skill);
	}
	
	private L2Skill findSkill(BotSpellType type)
	{
		L2Skill skill = null;
		try
		{
			for (int id : Arrays.stream(_actor.getShortcutList().getShortcuts()).filter(s -> s.getPage() == 9 && s.getType() == ShortcutType.SKILL && type.getSlots().contains(s.getSlot())).map(Shortcut::getId).collect(Collectors.toList()))
			{
				if (skill != null && !_actor.isSkillDisabled(skill) && !skill.isToggle() && isNecessarySkill(skill))
					continue;
				
				skill = _actor.getSkill(id);
				
				_actor.sendPacket(new ExAutoSkillShot(_actor.getSkill(id), 1));
				
			}
		}
		catch (Exception e)
		{
			return skill;
		}
		return isNecessarySkill(skill) ? skill : null;
	}
	
	private boolean haveActionOnShortcutF10(int id)
	{
		return Arrays.stream(_actor.getShortcutList().getShortcuts()).anyMatch(shortcut -> shortcut.getPage() == 9 && shortcut.getType() == ShortcutType.ACTION && shortcut.getId() == id);
	}
	
	private boolean haveActionOnShortcutF1(int id)
	{
		return Arrays.stream(_actor.getShortcutList().getShortcuts()).anyMatch(shortcut -> shortcut.getPage() == 0 && shortcut.getType() == ShortcutType.ACTION && shortcut.getId() == id);
	}
	
	private boolean percentageHpIsLessThan(double val)
	{
		return (_actor.getStatus().getHp() * 100.0f / _actor.getStatus().getMaxHp()) < val;
	}
	
	private boolean percentageMpIsLessThan(double val)
	{
		return (_actor.getStatus().getMp() * 100.0f / _actor.getStatus().getMaxMp()) < val;
	}
	
	private void forceUseItem(int itemId)
	{
		checkItems();
		ItemSkills itemSkills = new ItemSkills();
		
		if (_actor.getInventory().getItemByItemId(itemId) == null)
			return;
		
		itemSkills.useItem(_actor, _actor.getInventory().getItemByItemId(itemId), true);
		
	}
	
	private void forceUseItemSkill(int itemId)
	{
		checkItems();
		ItemSkills itemSkills = new ItemSkills();
		if (_actor.getInventory().getItemByItemId(itemId) == null)
			return;
		itemSkills.useItem(_actor, _actor.getInventory().getItemByItemId(itemId), true);
		
	}
	
	private boolean calculateSweep()
	{
		final L2Skill sweep = _actor.getSkills().get(42);
		if (sweep == null)
			return false;
		
		final Monster monster = _actor.getKnownTypeInRadius(Monster.class, RADIUS).stream().filter(x -> x.isDead() && x.getSpoilState() != null && x.getSpoilState().getSpoilerId() == _actor.getObjectId()).findFirst().orElse(null);
		if (monster != null)
		{
			_actor.getAI().tryToCast(monster, sweep);
			return true;
		}
		return false;
	}
	
	private void calculatePotions()
	{
		if (HP_POTION_PERCENTAGE != 0 && percentageHpIsLessThan(HP_POTION_PERCENTAGE))
			forceUseItemSkill(1539);
		
		if (MP_POTION_PERCENTAGE != 0 && percentageMpIsLessThan(MP_POTION_PERCENTAGE))
			forceUseItem(728);
	}
	
	public final Player getActor()
	{
		return _actor;
	}
}
