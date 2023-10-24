package net.sf.l2j.gameserver.scripting.script.ai.group;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.enums.EventHandler;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.scripting.script.ai.AttackableAIScript;

public class EvoMonster6a10 extends AttackableAIScript
{
	private static final Map<Integer, Integer> EVOMONSTER = new HashMap<>(5);
	
	static
	{
		EVOMONSTER.put(6, 7);
		EVOMONSTER.put(7, 8);
		EVOMONSTER.put(8, 9);
		EVOMONSTER.put(9, 10);
	}
	
	public EvoMonster6a10()
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