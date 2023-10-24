package net.sf.l2j.gameserver.scripting.script.ai.group;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.enums.EventHandler;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.scripting.script.ai.AttackableAIScript;

public class EvoMonster16a20 extends AttackableAIScript
{
	private static final Map<Integer, Integer> EVOMONSTER = new HashMap<>(5);
	
	static
	{
		EVOMONSTER.put(16, 17);
		EVOMONSTER.put(17, 18);
		EVOMONSTER.put(18, 19);
		EVOMONSTER.put(19, 20);
	}
	
	public EvoMonster16a20()
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