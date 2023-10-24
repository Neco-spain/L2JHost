package net.sf.l2j.gameserver.handler.skillhandlers;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.clientpackets.RequestBypassToServer;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 * @author EDUSZ93
 */
public class SkinEffect implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.SKIN_EFFECT
	};
	
	@Override
	public void useSkill(Creature activeChar, L2Skill skill, WorldObject[] targets, ItemInstance item)
	{
		if (!(activeChar instanceof Player))
		{
			return;
		}
		
		Player player = (Player) activeChar;
		String skinName = player.getSkin();
		
		if (isArmorEffect(skill.getId()))
		
		{
			RequestBypassToServer.setPart(player, "chest", skinName);
			RequestBypassToServer.setPart(player, "legs", skinName);
			RequestBypassToServer.setPart(player, "gloves", skinName);
			RequestBypassToServer.setPart(player, "boots", skinName);
			if (player.isHairSkin())
			{
				player.getHairSkin();
				RequestBypassToServer.setPart(player, "hairall", skinName);
			}
			else
			{
				if (dontRemoveHair(player.getSkin()))
				{
					player.setHairSkin(true);
				}
				else
				{
					RequestBypassToServer.setPart(player, "hairall", "Null");
				}
			}
			player.sendMessage("You have activated " + skinName + " visual skin.");
		}
		
		player.setDressMeEnabled(true);
		player.broadcastUserInfo();
	}
	
	private static boolean isArmorEffect(int ArmorEffectId)
	{
		return ArmorEffectId >= Config.ID_VISUAL_EFFECT_KNIGHTS && ArmorEffectId <= Config.ID_VISUAL_EFFECT_WIZARDS;
	}
	
	public static boolean dontRemoveHair(String _skinname)
	{
		return _skinname == "Lilith" || _skinname == "Healer";
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
	
}