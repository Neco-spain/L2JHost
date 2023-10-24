package net.sf.l2j.gameserver.scripting.script.ai.group;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.enums.EventHandler;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.scripting.script.ai.AttackableAIScript;

public class EvoMonster11a15 extends AttackableAIScript
{
	private static final Map<Integer, Integer> EVOMONSTER = new HashMap<>(5);
	
	static
	{
		EVOMONSTER.put(11, 12);
		EVOMONSTER.put(12, 13);
		EVOMONSTER.put(13, 14);
		EVOMONSTER.put(14, 15);
	}
	
	public EvoMonster11a15()
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