package net.sf.l2j.gameserver.handler.itemhandlers;

import java.sql.Connection;
import java.sql.PreparedStatement;

import net.sf.l2j.commons.pool.ConnectionPool;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.clientpackets.RequestBypassToServer;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 * @author EDUSZ93
 */
public class Skins implements IItemHandler
{
	@Override
	public void useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		L2Skill skill = null;
		Player player = (Player) playable;
		int itemId = item.getItemId();
		
		if(isSkin(itemId) && !Config.ALLOW_DRESS_ME_SYSTEM)
		{
		    player.sendMessage("Sorry. Admin has disabled skins.");
		    return;
		}
		
		String ns = null;
		
		if(isSkin(itemId))
			ns = Player.getNameSkin(itemId);
		
		if(isEFFECT_WARRIOR(itemId))
			skill = SkillTable.FrequentSkill.ID_VISUAL_EFFECT_WARRIOR.getSkill();
        if(isEFFECT_KNIGHTS(itemId))
			skill = SkillTable.FrequentSkill.ID_VISUAL_EFFECT_KNIGHTS.getSkill();
        if(isEFFECT_WIZARDS(itemId))
        	skill = SkillTable.FrequentSkill.ID_VISUAL_EFFECT_WIZARDS.getSkill();
		
		if(isSkin(itemId) && player.isNoble())
		{
			if(!(playable instanceof Player))
				return;
			
			final String as = player.getSkin();
			
			if(!player.isDressMeEnabled() && isSkin(itemId) || isSkin(itemId) && ns != as)
			{
				try (Connection con = ConnectionPool.getConnection();
					PreparedStatement stmt = con.prepareStatement("UPDATE characters SET skin = ? WHERE obj_Id=?"))
				{
					stmt.setInt(1, itemId);
					stmt.setInt(2, player.getObjectId());
					stmt.execute();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
				if (skill != null)
					player.getAI().tryToCast(player, skill);
			}
			else if(isSkin(itemId) && ns == as)
		    {
		    	try (Connection con = ConnectionPool.getConnection();
		    		PreparedStatement stmt = con.prepareStatement("UPDATE characters SET skin = ? WHERE obj_Id=?"))
				{
		    		stmt.setString(1, null);
		    		stmt.setInt(2, player.getObjectId());
		    		stmt.execute();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
		    	
		    	RequestBypassToServer.setPart(player, "chest", "Null");
				RequestBypassToServer.setPart(player, "legs", "Null");
				RequestBypassToServer.setPart(player, "gloves", "Null");
				RequestBypassToServer.setPart(player, "boots", "Null");
				RequestBypassToServer.setPart(player, "hairall", "Null");
				player.sendMessage("You have disabled the "+ns+" visual skin.");
				player.setDressMeEnabled(false);
				player.broadcastUserInfo();
		    }
		}
		else
			player.sendMessage("Sorry. You don't have noble privileges to use Visual Skin.");
	}
	
    private static boolean isSkin(int Skin)
    {
        return Skin >= 30000 && Skin <= 30034;
    }
    
	private static boolean isEFFECT_WARRIOR(int Skin)
    {
        return Skin == 30000 || Skin == 30001 || Skin == 30002 || Skin == 30003 || Skin == 30004 || Skin == 30005 || Skin == 30011 || Skin == 30012 || Skin == 30013 || Skin == 30014 || Skin == 30015 || Skin == 30016 || Skin == 30018 || Skin == 30021 || Skin == 30032 || Skin == 30034;
    }
   
    private static boolean isEFFECT_KNIGHTS(int Skin)
    {
        return Skin == 30007 || Skin == 30008 || Skin == 30009 || Skin == 30010 || Skin == 30019 || Skin == 30020 || Skin == 30023 || Skin == 30027 || Skin == 30029 || Skin == 30030 || Skin == 30033;
    }
    
    private static boolean isEFFECT_WIZARDS(int Skin)
    {
        return Skin == 30006 || Skin == 30017 || Skin == 30025 || Skin == 30026 || Skin == 30028 || Skin == 30031;
    }
}