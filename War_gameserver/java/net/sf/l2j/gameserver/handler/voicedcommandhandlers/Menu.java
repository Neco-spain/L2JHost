package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.handler.skillhandlers.SkinEffect;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.clientpackets.RequestBypassToServer;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.util.CustomMessage;

public class Menu implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
	{
		"menu",
		"mod_menu_",
		"forum",
		"linus"
	
	};
	
	@Override
	public boolean useVoicedCommand(String command, Player player, String target)
	{
		if (command.equals("menu") && Config.ENABLE_MENU)
			showHtm(player);
		else if (command.startsWith("mod_menu_"))
		{
			
			String addcmd = command.substring(9).trim();
			if (addcmd.startsWith("exp"))
			{
				if (player.isStopExp())
				{
					player.setStopExp(false);
					player.sendMessage(new CustomMessage("EXP_OFF"));
				}
				else
				{
					player.setStopExp(true);
					player.sendMessage(new CustomMessage("EXP_ON"));
				}
				showHtm(player);
				return true;
			}
			else if (addcmd.startsWith("skinhair"))
			{
				int flag = Integer.parseInt(addcmd.substring(8).trim());
				if (flag == 0)
				{
					player.setHairSkin(true);
					if (player.isHairSkin())
						RequestBypassToServer.setPart(player, "hairall", player.getSkin());
				}
				else
				{
					if (SkinEffect.dontRemoveHair(player.getSkin()))
						player.sendMessage("You cannot use this skin with an accessory.");
					else
					{
						player.setHairSkin(false);
						RequestBypassToServer.setPart(player, "hairall", "Null");
					}
				}
				
				player.broadcastUserInfo();
				showHtm(player);
				return true;
			}
			else if (addcmd.startsWith("trade"))
			{
				if (player.isTradeRefusal())
				{
					player.setTradeRefusal(false);
					player.sendMessage(new CustomMessage("TRADE_OFF"));
				}
				else
				{
					player.setTradeRefusal(true);
					player.sendMessage(new CustomMessage("TRADE_ON"));
				}
				showHtm(player);
				return true;
			}
			else if (addcmd.startsWith("autoloot"))
			{
				if (player.isAutoLoot())
				{
					player.setAutoLoot(false);
					player.sendMessage(new CustomMessage("AUTO_LOOT_OFF"));
				}
				else
				{
					player.setAutoLoot(true);
					player.sendMessage(new CustomMessage("AUTO_LOOT_ON"));
				}
				
				showHtm(player);
				return true;
			}
			else if (addcmd.startsWith("lang_"))
			{
				player.setLang(addcmd.substring(5).trim());
				showHtm(player);
				return true;
			}
		}
		return true;
	}
	
	public static void showHtm(Player player)
	{
		NpcHtmlMessage htm = new NpcHtmlMessage(0);
		htm.setFile(player.isLang() + "mods/menu/menu.htm");
		
		final String ACTIVED = player.isLangString().equals("en") ? "<font color=00FF00>ON</font>" : "<font color=00FF00>ВКЛ</font>";
		final String DESAСTIVED = player.isLangString().equals("en") ? "<font color=FF0000>OFF</font>" : "<font color=FF0000>ВЫКЛ</font>";
		
		htm.replace("%online%", World.getInstance().getPlayers().size() * Config.FAKE_ONLINE_AMOUNT);
		htm.replace("%gainexp%", player.isStopExp() ? ACTIVED : DESAСTIVED);
		htm.replace("%xp%", player.isStopExp() ? "checked" : "unable");
		
		htm.replace("%trade%", player.isTradeRefusal() ? ACTIVED : DESAСTIVED);
		htm.replace("%troca%", player.isTradeRefusal() ? "checked" : "unable");
		
		htm.replace("%skinhair%", player.isHairSkin() ? ACTIVED : DESAСTIVED);
		htm.replace("%skincheck%", player.isHairSkin() ? "checked" : "unable");
		
		htm.replace("%autoloot%", player.isAutoLoot() ? ACTIVED : DESAСTIVED);
		htm.replace("%loot%", player.isAutoLoot() ? "checked" : "unable");
		
		htm.replace("%status_on_of%", player.isAutoGb() ? ACTIVED : DESAСTIVED);
		htm.replace("%checkGB%", player.isAutoGb() ? "checked" : "unable");
		
		htm.replace("%auto_adena%", player.isAutoAdena() ? ACTIVED : DESAСTIVED);
		htm.replace("%adena_check%", player.isAutoAdena() ? "checked" : "unable");
		
		player.sendPacket(htm);
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}