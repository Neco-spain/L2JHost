package l2jhost.droplist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import l2jhost.Util.Tokenizercustom;
import net.sf.l2j.commons.pool.ConnectionPool;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;

/**
 * @author BAN - L2JDEV
 */
public class HanlderDropList
{
	
	public void handleSetAddDrop(Player player, Npc monster, Tokenizercustom tokenizer)
	{
	    int itemId = tokenizer.getAsInteger(2, 0);
	    int category = tokenizer.getAsInteger(3, 0);
	    int Min = tokenizer.getAsInteger(4, 0);
	    int Max = tokenizer.getAsInteger(5, 0);
	    int chance = tokenizer.getAsInteger(6, 0);

	    if (category != 0 && category != 1)
	    {
	        player.sendMessage("Invalid category. Please use 0 for Drop or 1 for Spoil.");
	    }
	    else if (chance < 1 || chance > 100)
	    {
	        player.sendMessage("Invalid chance. Please use a value between 1 and 100.");
	    }
	    else if (itemId == 0 || Min == 0 || Max == 0)
	    {
	        player.sendMessage("Please fill in all fields correctly in the HTML.");
	    }
	    else
	    {
	        setDataBase(player, monster, itemId, category, Min, Max, chance);
	    }

	    HtmlDropListInfo.getInstance().monsterNpcAddDrop(player, monster);
	}

	
	void setDataBase(Player player, Npc monster, int itemId, int category, int min, int max, int chance)
	{
		try (Connection con = ConnectionPool.getConnection())
		{
			int mobId = monster.getNpcId();
			
			try (PreparedStatement checkStatement = con.prepareStatement("SELECT * FROM droplist WHERE mobId = ? AND itemId = ? AND category = ?"))
			{
				checkStatement.setInt(1, mobId);
				checkStatement.setInt(2, itemId);
				checkStatement.setInt(3, category);
				try (ResultSet resultSet = checkStatement.executeQuery())
				{
					if (resultSet.next())
					{
						try (PreparedStatement updateStatement = con.prepareStatement("UPDATE droplist SET min = ?, max = ?, chance = ? WHERE mobId = ? AND itemId = ? AND category = ?"))
						{
							updateStatement.setInt(1, min);
							updateStatement.setInt(2, max);
							updateStatement.setInt(3, chance);
							updateStatement.setInt(4, mobId);
							updateStatement.setInt(5, itemId);
							updateStatement.setInt(6, category);
							
							int rowsUpdated = updateStatement.executeUpdate();
							
							if (rowsUpdated > 0)
							{
								player.sendMessage("Successfully updated data in droplist table.");
								if (HtmlEditDropListInfo.getInstance().getActive())
								{
									HtmlEditDropListInfo.getInstance().setActive(false);
									HtmlEditDropListInfo.getInstance().monsterNpcEditDropList(player, monster, itemId, 1);
								}
								else if (HtmlEditSpoilListInfo.getInstance().getActive())
								{
									HtmlEditSpoilListInfo.getInstance().setActive(false);
									HtmlEditSpoilListInfo.getInstance().monsterNpcEditSpoilList(player, monster, itemId, 1);
								}
								else
									HtmlDropListInfo.getInstance().monsterNpcAddDrop(player, monster);
							}
							else
							{
								player.sendMessage("Failed to update data in droplist table.");
							}
						}
					}
					else
					{
						try (PreparedStatement insertStatement = con.prepareStatement("INSERT INTO droplist (mobId, itemId, min, max, category, chance) VALUES (?, ?, ?, ?, ?, ?)"))
						{
							insertStatement.setInt(1, mobId);
							insertStatement.setInt(2, itemId);
							insertStatement.setInt(3, min);
							insertStatement.setInt(4, max);
							insertStatement.setInt(5, category);
							insertStatement.setInt(6, chance);
							
							int rowsInserted = insertStatement.executeUpdate();
							
							if (rowsInserted > 0)
							{
								player.sendMessage("Data successfully inserted into the droplist table.");
								HtmlDropListInfo.getInstance().monsterNpcAddDrop(player, monster);
							}
							else
							{
								player.sendMessage("Failed to insert data into droplist table.");
							}
						}
					}
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			player.sendMessage("Error accessing droplist table: " + e.getMessage());
		}
	}
	
	public static HanlderDropList getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final HanlderDropList INSTANCE = new HanlderDropList();
	}
}
