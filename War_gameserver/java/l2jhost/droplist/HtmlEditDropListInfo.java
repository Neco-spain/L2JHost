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
public class HtmlEditDropListInfo
{
	private boolean _active;
	
	public void handleEditDropPage(Player player, Tokenizercustom tokenizer)
	{
		int itemId = tokenizer.getAsInteger(2, 0);
		int numPages = tokenizer.getAsInteger(3, 0);
		final WorldObject targetWorldObject = player.getTarget();
		if (targetWorldObject instanceof Npc)
		{
			final Npc targetNpc = (Npc) targetWorldObject;
			monsterNpcEditDropList(player, targetNpc, itemId, numPages);
			
		}
		
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
	
	public void handleDeleteItemDrop(Player player, Tokenizercustom tokenizer)
	{
		int itemId = tokenizer.getAsInteger(2, 0);
		final WorldObject targetWorldObject = player.getTarget();
		if (targetWorldObject instanceof Npc)
		{
			final Npc targetNpc = (Npc) targetWorldObject;
			
			deleteItemFromDropList(player, targetNpc, targetNpc.getNpcId(), itemId);
		}
		
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
	
	
	final DecimalFormat PERCENT = new DecimalFormat("#.###");
	
	public void monsterNpcEditDropList(Player player, Npc monster, int itemId, int page)
	{
		List<EditItemHolder> dropList = getMonsterDropList(monster.getNpcId(), itemId);
		
		final NpcHtmlMessage content = new NpcHtmlMessage(0);
		content.setFile(player.isLang() + "admin/dropList/editDrop.htm");
		
		final StringBuilder sb = new StringBuilder();
		
		Pagination<EditItemHolder> pagination = new Pagination<>(dropList.stream(), page, 1);
		int startIndex = (page - 1) * 1;
		int endIndex = Math.min(startIndex + 1, dropList.size());
		int row = 0;
		for (int i = startIndex; i < endIndex; i++)
		{
			EditItemHolder drop = dropList.get(i);
			
			Item item = ItemData.getInstance().getTemplate(drop.getItemId());
			
			sb.append((row % 2 == 0) ? "<table><tr>" : "<table><tr>");
			
			sb.append("<td height=\"40\" width=\"40\"><img src=\"" + item.getIcon() + "\" width=\"32\" height=\"32\"></td>");
			String itemName = item.getName().length() > 22 ? item.getName().substring(0, 22) + "..." : item.getName();
			itemName = "<font color=\"0099FF\">" + itemName + "</font>";
			
			final String chanceString = PERCENT.format(drop.getChanceDrop());
			
			final String min = PERCENT.format(drop.getMinCount());
			final String max = PERCENT.format(drop.getMaxCount());
			
			sb.append("<td width=190 align=center>" + itemName + "<br1>Min: " + min + " Max: " + max + " Chance: " + chanceString + "</td>");
			
			sb.append("<td height=\"40\" width=\"40\"></td>");
			
			sb.append("</tr></table>");
			sb.append("<img src=\"L2UI.SquareGray\" width=\"300\" height=\"1\"/>");
			row++;
			
		}
		
		int a = (pagination.size());
		// Adicione a tabela ao conteúdo HTML
		content.replace("%edit_table%", sb.toString());
		content.replace("%npcName%", monster.getName());
		content.replace("%npcId%", monster.getNpcId());
		content.replace("%itemId%", itemId);
		content.replace("%category%", 0);
		content.replace("%a%", a);
		setActive(true);
		// Envie a mensagem HTML ao jogador
		player.sendPacket(content);
	}
	
	public List<EditItemHolder> getMonsterDropList(int monsterId, int itemId)
	{
	    List<EditItemHolder> dropList = new ArrayList<>();
	    
	    try (Connection con = ConnectionPool.getConnection();
	        PreparedStatement ps = con.prepareStatement("SELECT * FROM droplist WHERE mobId = ? AND category = 0 AND itemId = ?"))
	    {
	        ps.setInt(1, monsterId);
	        ps.setInt(2, itemId);
	        
	        try (ResultSet rs = ps.executeQuery())
	        {
	            while (rs.next())
	            {
	                int min = rs.getInt("min");
	                int max = rs.getInt("max");
	                int category = rs.getInt("category");
	                int chance = rs.getInt("chance");
	                
	                EditItemHolder drop = new EditItemHolder(monsterId, itemId, min, max, category, chance);
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
	
	public void deleteItemFromDropList(Player player, Npc monster, int monsterId, int itemId)
	{
	    try (Connection con = ConnectionPool.getConnection();
	         PreparedStatement ps = con.prepareStatement("DELETE FROM droplist WHERE mobId = ? AND itemId = ? AND category = 0"))
	    {
	        ps.setInt(1, monsterId);
	        ps.setInt(2, itemId);
	        
	        int rowsDeleted = ps.executeUpdate();
	        
	        if (rowsDeleted > 0)
	        {
	            // Item deletado com sucesso
	        	player.sendMessage("Successfully updated data in droplist table.");
	        	HtmlDropListInfo.getInstance().monsterNpcDropList(player, monster, 1);
	        }
	        else
	        {
	            // Item não encontrado na lista de drop
	        	player.sendMessage("Failed to update data in droplist table.");
	        	HtmlDropListInfo.getInstance().HomeHtm(player, monster, 1);
	        }
	    }
	    catch (SQLException e)
	    {
	        e.printStackTrace();
	    }
	}

	public boolean getActive()
	{
		return _active;
	}
	public void setActive(boolean set)
	{
		_active = set;
	}
	public static HtmlEditDropListInfo getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final HtmlEditDropListInfo INSTANCE = new HtmlEditDropListInfo();
	}
}
