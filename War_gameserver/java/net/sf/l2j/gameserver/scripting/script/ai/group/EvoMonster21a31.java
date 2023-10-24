package net.sf.l2j.gameserver.scripting.script.ai.group;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.enums.EventHandler;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.scripting.script.ai.AttackableAIScript;

public class EvoMonster21a31 extends AttackableAIScript
{
	private static final Map<Integer, Integer> EVOMONSTER = new HashMap<>(5);
	
	static
	{
		EVOMONSTER.put(21, 22);
		EVOMONSTER.put(22, 23);
		EVOMONSTER.put(23, 24);
		EVOMONSTER.put(24, 25);
		EVOMONSTER.put(25, 26);
		EVOMONSTER.put(26, 27);
		EVOMONSTER.put(27, 28);
		EVOMONSTER.put(28, 29);
		EVOMONSTER.put(29, 30);
	}
	
	public EvoMonster21a31()
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