package net.sf.l2j.gameserver.scripting.script.ai.group;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.enums.EventHandler;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.scripting.script.ai.AttackableAIScript;

public class EvoMonster40a60 extends AttackableAIScript
{
	private static final Map<Integer, Integer> EVOMONSTER = new HashMap<>(5);
	
	static
	{
		EVOMONSTER.put(40, 41);
		EVOMONSTER.put(41, 42);
		EVOMONSTER.put(42, 43);
		EVOMONSTER.put(43, 44);
		EVOMONSTER.put(44, 45);
		EVOMONSTER.put(45, 46);
		EVOMONSTER.put(46, 47);
		EVOMONSTER.put(47, 48);
		EVOMONSTER.put(48, 49);
		EVOMONSTER.put(49, 50);
		EVOMONSTER.put(50, 51);
		EVOMONSTER.put(51, 52);
		EVOMONSTER.put(52, 53);
		EVOMONSTER.put(53, 54);
		EVOMONSTER.put(54, 55);
		EVOMONSTER.put(55, 56);	
		EVOMONSTER.put(56, 57);
		EVOMONSTER.put(57, 58);
		EVOMONSTER.put(58, 59);
		EVOMONSTER.put(59, 60);
	}
	
	public EvoMonster40a60()
	{
		super("ai/group");
	}
	
	@Override
	protected void registerNpcs()
	{
		addEventIds(EVOMONSTER.keySet(), EventHandler.MY_DYING);
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Npc angel = addSpawn(EVOMONSTER.get(npc.getNpcId()), npc, false, 0, false);
		angel.forceAttack(killer, 200);
		
		super.onMyDying(npc, killer);
	}
}