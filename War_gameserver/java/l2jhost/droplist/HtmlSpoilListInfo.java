package l2jhost.droplist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import l2jhost.Util.Tokenizercustom;
import net.sf.l2j.commons.data.Pagination;
import net.sf.l2j.commons.pool.ConnectionPool;
import net.sf.l2j.gameserver.data.xml.ItemData;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * @author BAN - L2JDEV
 */
public class HtmlSpoilListInfo
{
	public void handleSpoilPage(Player player, Tokenizercustom tokenizer)
	{
		int numPages = tokenizer.getAsInteger(2, 0);
		
		final WorldObject targetWorldObject = player.getTarget();
		if (targetWorldObject instanceof Npc)
		{
			final Npc targetNpc = (Npc) targetWorldObject;
			monsterNpcSpoilList(player, targetNpc, numPages);
			
		}
		
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
	
	final DecimalFormat PERCENT = new DecimalFormat("#.###");

	public void monsterNpcSpoilList(Player player, Npc monster, int page)
	{
		List<SpoilListHolder> dropList = getMonsterSpoilList(monster.getNpcId());
		
		final NpcHtmlMessage content = new NpcHtmlMessage(0);
		content.setFile(player.isLang() + "admin/dropList/spoil.htm");
		
		final StringBuilder sb = new StringBuilder();
		
		Pagination<SpoilListHolder> pagination = new Pagination<>(dropList.stream(), page, 4);
		int startIndex = (page - 1) * 4;
		int endIndex = Math.min(startIndex + 4, dropList.size());
		int row = 0;
		for (int i = startIndex; i < endIndex; i++)
		{
			SpoilListHolder drop = dropList.get(i);
			Item item = ItemData.getInstance().getTemplate(drop.getItemId());
			
			sb.append((row % 2 == 0) ? "<table><tr>" : "<table><tr>");
			
			sb.append("<td height=\"40\" width=\"40\"><img src=\"" + item.getIcon() + "\" width=\"32\" height=\"32\"></td>");
			String itemName = item.getName().length() > 22 ? item.getName().substring(0, 22) + "..." : item.getName();
			itemName = "<font color=\"0099FF\">" + itemName + "</font>";
			
			final String chanceString = PERCENT.format(drop.getChanceDrop());
			final String min = PERCENT.format(drop.getMinCount());
			final String max = PERCENT.format(drop.getMaxCount());
			
			
			sb.append("<td width=190 align=center>" + itemName + "<br1>Min: " + min + " Max: " + max + " Chance: " + chanceString + "</td>");
			
			sb.append("<td height=\"40\" width=\"40\"><button action=\"bypass command set_edit_spoilitem " + drop.getItemId() + " " + page + "\" width=32 height=32 back=\"exclusive.edit_Down\" fore=\"exclusive.edit_normal\"></td>");
			
			sb.append("</tr></table>");
			sb.append("<img src=\"L2UI.SquareGray\" width=\"300\" height=\"1\"/>");
			row++;
			
		}
		
		{
			sb.append("<table width=\"300\" height=\"15\"><tr>");
			if (page > 1)
				sb.append("<td align=left width=70><a action=\"bypass command spoil_list_page " + String.valueOf(page - 1) + "\">Previous</a></td>");
			else
				sb.append("<td align=left width=70>Previous</td>");
			
			sb.append("<td align=center width=128> Page: " + page + "</td>");
			if (pagination.size() > page)
				sb.append("<td align=right width=70><a action=\"bypass command spoil_list_page " + String.valueOf(page + 1) + "\">Next</a></td>");
			else
				sb.append("<td align=right width=70>Next</td>");
			
			sb.append("</tr></table>");
			sb.append("<img src=\"L2UI.SquareGray\" width=\"300\" height=\"1\"/>");
		}
		
		// Adicione a tabela ao conteúdo HTML
		content.replace("%spoil_table%", sb.toString());
		content.replace("%npcName%", monster.getName());
		content.replace("%npcId%", monster.getNpcId());
		
		// Envie a mensagem HTML ao jogador
		player.sendPacket(content);
	}
	
	public List<SpoilListHolder> getMonsterSpoilList(int monsterId)
	{
		List<SpoilListHolder> dropList = new ArrayList<>();
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM droplist WHERE mobId = ? AND category = 1"))
		{
			ps.setInt(1, monsterId);
			
			try (ResultSet rs = ps.executeQuery())
			{
				while (rs.next())
				{
					int itemId = rs.getInt("itemId");
					int min = rs.getInt("min");
					int max = rs.getInt("max");
					int category = rs.getInt("category");
					int chance = rs.getInt("chance");
					
					SpoilListHolder drop = new SpoilListHolder(monsterId, itemId, min, max, category, chance);
					dropList.add(drop);
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return dropList;
	}
	
	public static HtmlSpoilListInfo getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final HtmlSpoilListInfo INSTANCE = new HtmlSpoilListInfo();
	}
}
