package net.sf.l2j.gameserver.scripting.quest;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q50015_IslandPass extends Quest
{
	private static final String QUEST_NAME = "Q50015_IslandPass";

	// Item
	private static final int PASSE_MATEIRAL = 9901;

	// Rewards
	private static final int PASS_ILHA = 9900;

	public Q50015_IslandPass()
	{
		super(50015, "IslandPass");

		setItemsIds(PASSE_MATEIRAL);

		addQuestStart(50015); // IslandPass
		addTalkId(50015);
		addMyDying(50016); // Mobs
	}

	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return event;

		if (event.equalsIgnoreCase("50015-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		return event;
	}

	@Override
	public String onTalk(Npc npc, Player player)
	{
		String htmltext = getNoQuestMsg();
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;

		switch (st.getState())
		{
			case CREATED:
				htmltext = (player.getStatus().getLevel() < 80) ? "50015-01.htm" : "50015-02.htm";
				break;

			case STARTED:
				final int count = player.getInventory().getItemCount(PASSE_MATEIRAL);
				if (count < 50)
					htmltext = "50015-04.htm";
				else
				{
					htmltext = "50015-05.htm";
					takeItems(player, PASSE_MATEIRAL, -1);

					final int n = Rnd.get(100);
					if (n == 0)
					{
						giveItems(player, PASS_ILHA, 1);
						playSound(player, SOUND_JACKPOT);
					}
					st.exitQuest(true);
				}
				break;
		}

		return htmltext;
	}

	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();

		final QuestState st = checkPlayerCondition(player, npc, 1);
		if (st == null)
			return;

		if (npc.getNpcId() == 50016)
		{
			if (dropItems(player, PASSE_MATEIRAL, Rnd.nextBoolean() ? 1 : 1, 100, 1000000))
				st.setCond(2);
		}
		else if (dropItemsAlways(player, PASSE_MATEIRAL, (Rnd.get(5) < 4) ? 1 : 2, 50))
			st.setCond(2);
	}
}