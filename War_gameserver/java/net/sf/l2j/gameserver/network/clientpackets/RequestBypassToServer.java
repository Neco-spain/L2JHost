package net.sf.l2j.gameserver.network.clientpackets;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import l2jhost.Util.Tokenizercustom;
import l2jhost.droplist.HanlderDropList;
import l2jhost.droplist.HtmlDropListInfo;
import l2jhost.droplist.HtmlEditDropListInfo;
import l2jhost.droplist.HtmlEditSpoilListInfo;
import l2jhost.droplist.HtmlSpoilListInfo;
import net.sf.l2j.Config;
import net.sf.l2j.commons.data.Pagination;
import net.sf.l2j.commons.lang.StringUtil;
import net.sf.l2j.commons.lang.Tokenizer;
import net.sf.l2j.gameserver.communitybbs.CommunityBoard;
import net.sf.l2j.gameserver.data.DropCalc;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.data.manager.BotsPreventionManager;
import net.sf.l2j.gameserver.data.manager.HeroManager;
import net.sf.l2j.gameserver.data.manager.SpawnManager;
import net.sf.l2j.gameserver.data.sql.BalanceManagerAI;
import net.sf.l2j.gameserver.data.sql.BalanceManagerSkillAI;
import net.sf.l2j.gameserver.data.xml.AdminData;
import net.sf.l2j.gameserver.data.xml.DressMeData;
import net.sf.l2j.gameserver.data.xml.ItemData;
import net.sf.l2j.gameserver.data.xml.PlayerData;
import net.sf.l2j.gameserver.enums.DropType;
import net.sf.l2j.gameserver.enums.FloodProtector;
import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.enums.skills.ElementType;
import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.handler.AdminCommandHandler;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.handler.VoicedCommandHandler;
import net.sf.l2j.gameserver.handler.voicedcommandhandlers.Menu;
import net.sf.l2j.gameserver.handler.voicedcommandhandlers.VoiceExclusiveFarm;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Attackable;
import net.sf.l2j.gameserver.model.actor.ExclusiveBot;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.Summon;
import net.sf.l2j.gameserver.model.actor.container.attackable.AggroList;
import net.sf.l2j.gameserver.model.actor.container.npc.AggroInfo;
import net.sf.l2j.gameserver.model.actor.instance.*;
import net.sf.l2j.gameserver.model.donate.handler.HandlerArmorSet;
import net.sf.l2j.gameserver.model.donate.handler.HandlerJewelList;
import net.sf.l2j.gameserver.model.donate.handler.HandlerPayProcess;
import net.sf.l2j.gameserver.model.donate.handler.HandlerTeleport;
import net.sf.l2j.gameserver.model.donate.handler.HandlerTransfer;
import net.sf.l2j.gameserver.model.donate.handler.HandlerTransferStory;
import net.sf.l2j.gameserver.model.donate.handler.HandlerWeaponList;
import net.sf.l2j.gameserver.model.donate.previwer.HtmlItemRecover;
import net.sf.l2j.gameserver.model.item.DropCategory;
import net.sf.l2j.gameserver.model.item.DropData;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.model.olympiad.OlympiadManager;
import net.sf.l2j.gameserver.model.previwer.htm.GenerationDonateMain;
import net.sf.l2j.gameserver.model.spawn.ASpawn;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.ExServerPrimitive;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.OpenUrlPacket;
import net.sf.l2j.gameserver.scripting.QuestState;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.L2Skill;


public final class RequestBypassToServer extends L2GameClientPacket
{
	private static final Logger GMAUDIT_LOG = Logger.getLogger("gmaudit");
	
	private static final DecimalFormat PERCENT = new DecimalFormat("#.###");
	public static final int PAGE_LIMIT_1 = 1;

	private String _command;
	
	@Override
	protected void readImpl()
	{
		_command = readS();
	}
	
	@Override
	protected void runImpl()
	{
		if (_command.isEmpty())
			return;
		
		if (!getClient().performAction(FloodProtector.SERVER_BYPASS))
			return;
		
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		if (_command.startsWith("command"))
		{
			final WorldObject targetWorldObject = player.getTarget();
			
			if (!(targetWorldObject instanceof Monster) && !(targetWorldObject instanceof RaidBoss) && !(targetWorldObject instanceof GrandBoss))
			{
				player.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			if (targetWorldObject instanceof Npc)
			{
				final Npc targetNpc = (Npc) targetWorldObject;
				
				final Tokenizercustom Tokenizercustom = new Tokenizercustom(_command);
				
				final String param = Tokenizercustom.getToken(1);
				switch (param.toLowerCase())
				{
					case "home":
						HtmlDropListInfo.getInstance().HomeHtm(player, targetNpc, 1);
						break;
					case "npc_add_item":
						HtmlDropListInfo.getInstance().monsterNpcAddDrop(player, targetNpc);
						break;
					case "drop_list":
						HtmlDropListInfo.getInstance().monsterNpcDropList(player, targetNpc, 1);
						break;
					case "drop_list_page":
						HtmlDropListInfo.getInstance().handleDropPage(player, Tokenizercustom);
						break;
					case "spoil_list":
						HtmlSpoilListInfo.getInstance().monsterNpcSpoilList(player, targetNpc, 1);
						break;
					case "spoil_list_page":
						HtmlSpoilListInfo.getInstance().handleSpoilPage(player, Tokenizercustom);
						break;
					case "set_add_item":
						HanlderDropList.getInstance().handleSetAddDrop(player, targetNpc, Tokenizercustom);
						break;
					case "set_edit_item":
						HtmlEditDropListInfo.getInstance().handleEditDropPage(player, Tokenizercustom);
						break;
					case "delete_item_edit":
						HtmlEditDropListInfo.getInstance().handleDeleteItemDrop(player, Tokenizercustom);
						break;
						
					case "set_edit_spoilitem":
						HtmlEditSpoilListInfo.getInstance().handleEditSpoilPage(player, Tokenizercustom);
						break;
					case "delete_spoilitem_edit":
						HtmlEditSpoilListInfo.getInstance().handleDeleteItemSpoil(player, Tokenizercustom);
						break;
			
				}
			}
			
		}

		if (_command.startsWith("link"))
		{
			StringTokenizer st = new StringTokenizer(_command, " ");
			st.nextToken();
			String val = String.valueOf(st.nextToken());
			player.sendPacket(new OpenUrlPacket(val));
		}
		
		if (_command.startsWith("donate"))
		{
			final Tokenizer tokenizer = new Tokenizer(_command);
			
			if (tokenizer.size() == 1)
			{
				GenerationDonateMain.Main(player);
				return;
			}
			
			final String param = tokenizer.getToken(1);
			switch (param.toLowerCase())
			{
				case "purchase":
					GenerationDonateMain.purchase(player);
					break;
					
				case "bank_select":
					GenerationDonateMain.bank_select(player);
					break;
					
				case "trans_coin":
					GenerationDonateMain.transferencia(player);
					break;
					
				case "end_transfer":
					HandlerTransfer.TransferCoins(player, tokenizer);
					break;
					
				case "pay_confirme":
					HandlerPayProcess.saveCache(player, tokenizer);
					break;
					
				case "list_set":
					HandlerArmorSet.handleListSet(player, tokenizer);
					break;
				
				case "create_armor":
					HandlerArmorSet.CreateArmor(player, tokenizer);
					break;
					
				case "list_weapon":
					HandlerWeaponList.handleWeaponList(player, tokenizer);
					break;
				
				case "create_weapon":
					HandlerWeaponList.CreateWeapon(player, tokenizer);
					break;
					
				case "list_jewels":
					HandlerJewelList.handleJewelsListSet(player, tokenizer);
					break;
				
				case "create_jewels":
					HandlerJewelList.CreateJewels(player, tokenizer);
					break;
					
				case "info_pagments":
					HandlerTransferStory.infopagments(player, tokenizer);
					break;
				case "story_end":
					HandlerTransferStory.infopagmentsEnd(player, tokenizer);
					break;
					
				case "recover_list":
					HtmlItemRecover.handleRecoverItemsList(player, tokenizer);
					break;
				case "recover":
					HtmlItemRecover.recover(player, tokenizer);
					break;
			}
		}
		
		if (_command.startsWith("service"))
		{
			ExclusiveBot bot = player.getBot();
			
			final Tokenizer tokenizer = new Tokenizer(_command);
			final ExServerPrimitive debug = player.getDebugPacket("ZONE");
			debug.reset();
			
			final String param = tokenizer.getToken(1);
			switch (param.toLowerCase())
			{
				case "viwer":
					if (player.isViwerRandiosDOA())
					{
						player.setViwerRandiosDOA(false);
						debug.sendTo(player);
					}
					else
					{
						player.setViwerRandiosDOA(true);
						debug.addCircle("Radius", Color.YELLOW, false, player.getX(), player.getY(), player.getZ(), bot.getRadius());
						debug.sendTo(player);
					}
					
					VoiceExclusiveFarm.showHtml(player);
					break;
				
				case "farm_on":
					if (player.isbot())
						bot.start();
					VoiceExclusiveFarm.showHtml(player);
					break;
				case "farm_off":
					if (player.isbot())
						bot.stop();
					VoiceExclusiveFarm.showHtml(player);
					break;
				case "inc_radius":
					if (player.isbot())
						bot.setRadius(bot.getRadius() + 100);
					
					player.setbotRangeDOA(bot.getRadius());
					player.getMemos().set("botRange", bot.getRadius());
					VoiceExclusiveFarm.showHtml(player);
					break;
				case "dec_radius":
					if (player.isbot())
						bot.setRadius(bot.getRadius() - 100);
					
					player.setbotRangeDOA(bot.getRadius());
					player.getMemos().set("botRange", bot.getRadius());
					VoiceExclusiveFarm.showHtml(player);
					break;
				case "buff_protect":
					if (player.isbot())
						bot.setNoBuffProtection(!bot.isNoBuffProtected());
					VoiceExclusiveFarm.showHtml(player);
					break;
				case "potions":
					if (player.isPotionDOA())
					{
						player.setPotionDOA(false);
						player.getMemos().set("potion", false);
					}
					else
					{
						player.setPotionDOA(true);
						player.getMemos().set("potion", true);
					}
					VoiceExclusiveFarm.showHtml(player);
					break;
				case "pethelp":
					if (player.isPetAttackDOA())
					{
						player.setPetAttackDOA(false);
						player.getMemos().set("PetAttack", false);
					}
					else
					{
						player.setPetAttackDOA(true);
						player.getMemos().set("PetAttack", true);
					}
					VoiceExclusiveFarm.showHtml(player);
					break;
				case "assist":
					if (player.isPartyAssistDOA())
					{
						player.setPartyAssistDOA(false);
						player.getMemos().set("partyAssist", false);
					}
					else
					{
						player.setPartyAssistDOA(true);
						player.getMemos().set("partyAssist", true);
					}
					Summon summon = player.getSummon();
					if (summon != null)
						summon.abortAll(true);
					player.abortAll(true);
					VoiceExclusiveFarm.showHtml(player);
					break;
				
				case "tryattak":
					if (player.isForceAttackDOA())
					{
						player.setForceAttackDOA(false);
						player.getMemos().set("forceAttack", false);
					}
					else
					{
						player.setForceAttackDOA(true);
						player.getMemos().set("forceAttack", true);
					}
					player.abortAll(true);
					VoiceExclusiveFarm.showHtml(player);
					break;
				case "buffer":
					if (player.isAutoBufferDOA())
					{
						player.setAutoBufferDOA(false);
						player.getMemos().set("autobuffer", false);
					}
					else
					{
						player.setAutoBufferDOA(true);
						player.getMemos().set("autobuffer", true);
					}
					
					VoiceExclusiveFarm.showHtmlBuffer(player);
					break;
				case "appy":
					
					if (player.isSelectBufferDOA())
					{
						player.setSelectBufferDOA(false);
					}
					else
					{
						player.setSelectBufferDOA(true);
					}
					
					VoiceExclusiveFarm.showHtmlBuffer(player);
					
					break;
				case "givebuffs":
					VoiceExclusiveFarm.giveBuffers(player, tokenizer);
					break;
				case "skill_list":
					VoiceExclusiveFarm.handleSkills(player, tokenizer);
					break;
				case "skill_use":
					VoiceExclusiveFarm.handleUseSkill(player, tokenizer);
					break;
				case "skill_remove":
					VoiceExclusiveFarm.handleRemoveSkill(player, tokenizer);
					break;
				case "teleport":
					HandlerTeleport.handleTeleport(player, tokenizer);
					break;
				
			}
		}
		if (_command.startsWith("admin_"))
		{
			String command = _command.split(" ")[0];
			
			final IAdminCommandHandler ach = AdminCommandHandler.getInstance().getHandler(command);
			if (ach == null)
			{
				if (player.isGM())
					player.sendMessage("The command " + command.substring(6) + " doesn't exist.");
				
				LOGGER.warn("No handler registered for admin command '{}'.", command);
				return;
			}
			
			if (!AdminData.getInstance().hasAccess(command, player.getAccessLevel()))
			{
				player.sendMessage("You don't have the access rights to use this command.");
				LOGGER.warn("{} tried to use admin command '{}' without proper Access Level.", player.getName(), command);
				return;
			}
			
			if (Config.GMAUDIT)
				GMAUDIT_LOG.info(player.getName() + " [" + player.getObjectId() + "] used '" + _command + "' command on: " + ((player.getTarget() != null) ? player.getTarget().getName() : "none"));
			
			ach.useAdminCommand(_command, player);
		}
		
		else if (_command.startsWith("agathionPickup"))
		{
			if (player.getPlayerAgathion())
			{
				if (player.getAgathionPickUp())
				{
					player.setAgathionPickUp(false);
				}
				else
				{
					player.setAgathionPickUp(true);
				}
				Menu.showHtm(player);
			}
			else
			{
				player.sendMessage("" + player.getName() + " You cannot perform this action at the moment..");
				Menu.showHtm(player);
			}
			
		}
		else if (_command.startsWith("player_help "))
		{
			final String path = _command.substring(12);
			if (path.indexOf("..") != -1)
				return;
			
			final StringTokenizer st = new StringTokenizer(path);
			final String[] cmd = st.nextToken().split("#");
			
			final NpcHtmlMessage html = new NpcHtmlMessage(0);
			html.setFile(player.isLang() + "help/" + cmd[0]);
			if (cmd.length > 1)
			{
				final int itemId = Integer.parseInt(cmd[1]);
				html.setItemId(itemId);
				
				if (itemId == 7064 && cmd[0].equalsIgnoreCase("lidias_diary/7064-16.htm"))
				{
					final QuestState qs = player.getQuestList().getQuestState("Q023_LidiasHeart");
					if (qs != null && qs.getCond() == 5 && qs.getInteger("diary") == 0)
						qs.set("diary", "1");
				}
			}
			html.disableValidation();
			player.sendPacket(html);
			
		}
		else if (_command.startsWith("npc_"))
		{
			if (!player.validateBypass(_command))
				return;
			
			int endOfId = _command.indexOf('_', 5);
			String id;
			if (endOfId > 0)
				id = _command.substring(4, endOfId);
			else
				id = _command.substring(4);
			
			try
			{
				final WorldObject object = World.getInstance().getObject(Integer.parseInt(id));
				
				if (object instanceof Npc && endOfId > 0 && player.getAI().canDoInteract(object))
					((Npc) object).onBypassFeedback(player, _command.substring(endOfId + 1));
				
				player.sendPacket(ActionFailed.STATIC_PACKET);
			}
			catch (NumberFormatException nfe)
			{
			}
		}
		// Navigate throught Manor windows
		else if (_command.startsWith("manor_menu_select?"))
		{
			WorldObject object = player.getTarget();
			if (object instanceof Npc)
				((Npc) object).onBypassFeedback(player, _command);
		}
		else if (_command.startsWith("bbs_") || _command.startsWith("_bbs") || _command.startsWith("_friend") || _command.startsWith("_mail") || _command.startsWith("_block"))
		{
			CommunityBoard.getInstance().handleCommands(getClient(), _command);
		}
		else if (_command.startsWith("Quest "))
		{
			if (!player.validateBypass(_command))
				return;
			
			String[] str = _command.substring(6).trim().split(" ", 2);
			if (str.length == 1)
				player.getQuestList().processQuestEvent(str[0], "");
			else
				player.getQuestList().processQuestEvent(str[0], str[1]);
		}
		else if (_command.startsWith("_match"))
		{
			String params = _command.substring(_command.indexOf("?") + 1);
			StringTokenizer st = new StringTokenizer(params, "&");
			int heroclass = Integer.parseInt(st.nextToken().split("=")[1]);
			int heropage = Integer.parseInt(st.nextToken().split("=")[1]);
			int heroid = HeroManager.getInstance().getHeroByClass(heroclass);
			if (heroid > 0)
				HeroManager.getInstance().showHeroFights(player, heroclass, heroid, heropage);
		}
		else if (_command.startsWith("_diary"))
		{
			String params = _command.substring(_command.indexOf("?") + 1);
			StringTokenizer st = new StringTokenizer(params, "&");
			int heroclass = Integer.parseInt(st.nextToken().split("=")[1]);
			int heropage = Integer.parseInt(st.nextToken().split("=")[1]);
			int heroid = HeroManager.getInstance().getHeroByClass(heroclass);
			if (heroid > 0)
				HeroManager.getInstance().showHeroDiary(player, heroclass, heroid, heropage);
		}
		else if (_command.startsWith("arenachange")) // change
		{
			final boolean isManager = player.getCurrentFolk() instanceof OlympiadManagerNpc;
			if (!isManager)
			{
				// Without npc, command can be used only in observer mode on arena
				if (!player.isInObserverMode() || player.isInOlympiadMode() || player.getOlympiadGameId() < 0)
					return;
			}
			
			// Olympiad registration check.
			if (OlympiadManager.getInstance().isRegisteredInComp(player))
			{
				player.sendPacket(SystemMessageId.WHILE_YOU_ARE_ON_THE_WAITING_LIST_YOU_ARE_NOT_ALLOWED_TO_WATCH_THE_GAME);
				return;
			}
			
			final int arenaId = Integer.parseInt(_command.substring(12).trim());
			player.enterOlympiadObserverMode(arenaId);
		}
		else if (_command.startsWith("voiced_"))
		{
			String command = _command.split(" ")[0];
			IVoicedCommandHandler ach = VoicedCommandHandler.getInstance().getHandler(_command.substring(7));
			if (ach == null)
			{
				player.sendMessage("The command " + command.substring(7) + " does not exist!");
				LOGGER.warn("No handler registered for command '" + _command + "'");
				return;
			}
			ach.useVoicedCommand(_command.substring(7), player, null);
		}
		else if (_command.startsWith("report"))
			BotsPreventionManager.getInstance().analyseBypass(_command, player);
		else if (_command.startsWith("QuestGatekeeper"))
		{
			String[] args = _command.substring(16).split(" ");
			
			int loc = Integer.parseInt(args[0]);
			int loc1 = Integer.parseInt(args[1]);
			int loc2 = Integer.parseInt(args[2]);
			int itemid = Integer.parseInt(args[3]);
			int count = Integer.parseInt(args[4]);
			
			if (player.getInventory().getItemByItemId(itemid) == null || player.getInventory().getItemByItemId(itemid).getCount() < count)
			{
				player.sendMessage("Incorrect item count. You need " + count + " " + itemid);
				return;
			}
			
			player.destroyItemByItemId("QuestGatekeeper", itemid, count, player, true);
			player.teleportTo(loc, loc1, loc2, 20);
		}
		else if (_command.startsWith("npcfind_byid"))
		{
			String[] args = _command.substring(13).split(" ");
			final int raidId = Integer.parseInt(args[0]);
			
			// get spawn information of the raid boss
			final ASpawn spawn = SpawnManager.getInstance().getSpawn(raidId);
			if (spawn != null)
				player.getRadarList().addMarker(spawn.getSpawnLocation());
			else
			{
				// spawn information does not exist, try to find living instance
				final Npc raid = World.getInstance().getNpc(raidId);
				if (raid != null)
					player.getRadarList().addMarker(raid.getPosition());
			}
		}
		else if (_command.startsWith("skillbalance"))
		{
			int classId = Integer.parseInt(_command.substring(13));
			NpcHtmlMessage htm = new NpcHtmlMessage(0);
			htm.setFile(player.isLang() + "mods/balance/skill/show_list.htm");
			htm.replace("%classid%", classId);
			htm.replace("%classname%", PlayerData.getInstance().getClassNameById(classId));
			player.sendPacket(htm);
		}
		else if (_command.startsWith("seteBalance"))
		{
			String[] args = _command.substring(12).split(" ");
			int targetClassId = args[0].equals("ALL") ? -1 : Integer.parseInt(args[0]);
			int classId = Integer.parseInt(args[1]);
			NpcHtmlMessage htm = new NpcHtmlMessage(0);
			htm.setFile(player.isLang() + "mods/balance/skill/show_change.htm");
			htm.replace("%classid%", classId);
			htm.replace("%targetclassid%", targetClassId);
			htm.replace("%dmgmod%", BalanceManagerSkillAI.getInstance().getPowerMod(classId, targetClassId));
			for (ClassId ci : ClassId.values())
			{
				if (ci.getId() == classId)
				{
					htm.replace("%classname%", ci.name());
					break;
				}
			}
			if (targetClassId == -1)
				htm.replace("%targetclassname%", "ALL CLASSES");
			else
			{
				for (ClassId ci : ClassId.values())
				{
					if (ci.getId() == targetClassId)
					{
						htm.replace("%targetclassname%", ci.name());
						break;
					}
				}
			}
			player.sendPacket(htm);
		}
		else if (_command.startsWith("vulDamage"))
		{
			String[] args = _command.substring(10).split(" ");
			if (args.length < 3)
				return;
			int targetClassId = args[0].equals("ALL") ? -1 : Integer.parseInt(args[0]);
			int classId = Integer.parseInt(args[1]);
			int mod = Integer.parseInt(args[2]);
			
			BalanceManagerSkillAI.getInstance().addPowerMod(classId, targetClassId, mod);
			
			NpcHtmlMessage htm = new NpcHtmlMessage(0);
			htm.setFile(player.isLang() + "mods/balance/skill/show_change.htm");
			htm.replace("%classid%", classId);
			htm.replace("%targetclassid%", targetClassId);
			htm.replace("%dmgmod%", BalanceManagerSkillAI.getInstance().getPowerMod(classId, targetClassId));
			for (ClassId ci : ClassId.values())
			{
				if (ci.getId() == classId)
				{
					htm.replace("%classname%", ci.name());
					break;
				}
			}
			if (targetClassId == -1)
				htm.replace("%targetclassname%", "ALL CLASSES");
			else
			{
				for (ClassId ci : ClassId.values())
				{
					if (ci.getId() == targetClassId)
					{
						htm.replace("%targetclassname%", ci.name());
						break;
					}
				}
			}
			player.sendPacket(htm);
		}
		else if (_command.startsWith("balance"))
		{
			int classId = Integer.parseInt(_command.substring(8));
			NpcHtmlMessage htm = new NpcHtmlMessage(0);
			htm.setFile(player.isLang() + "mods/balance/show_list.htm");
			htm.replace("%classid%", classId);
			htm.replace("%classname%", PlayerData.getInstance().getClassNameById(classId));
			player.sendPacket(htm);
		}
		else if (_command.startsWith("makeBalance"))
		{
			String[] args = _command.substring(12).split(" ");
			int targetClassId = args[0].equals("ALL") ? -1 : Integer.parseInt(args[0]);
			int classId = Integer.parseInt(args[1]);
			NpcHtmlMessage htm = new NpcHtmlMessage(0);
			htm.setFile(player.isLang() + "mods/balance/show_change.htm");
			htm.replace("%classid%", classId);
			htm.replace("%targetclassid%", targetClassId);
			htm.replace("%dmgmod%", BalanceManagerAI.getInstance().getPowerMod(classId, targetClassId));
			for (ClassId ci : ClassId.values())
			{
				if (ci.getId() == classId)
				{
					htm.replace("%classname%", ci.name());
					break;
				}
			}
			if (targetClassId == -1)
				htm.replace("%targetclassname%", "ALL CLASSES");
			else
			{
				for (ClassId ci : ClassId.values())
				{
					if (ci.getId() == targetClassId)
					{
						htm.replace("%targetclassname%", ci.name());
						break;
					}
				}
			}
			player.sendPacket(htm);
		}
		else if (_command.startsWith("modDamage"))
		{
			String[] args = _command.substring(10).split(" ");
			if (args.length < 3)
				return;
			int targetClassId = args[0].equals("ALL") ? -1 : Integer.parseInt(args[0]);
			int classId = Integer.parseInt(args[1]);
			int mod = Integer.parseInt(args[2]);
			
			BalanceManagerAI.getInstance().addPowerMod(classId, targetClassId, mod);
			
			NpcHtmlMessage htm = new NpcHtmlMessage(0);
			htm.setFile(player.isLang() + "mods/balance/show_change.htm");
			htm.replace("%classid%", classId);
			htm.replace("%targetclassid%", targetClassId);
			htm.replace("%dmgmod%", BalanceManagerAI.getInstance().getPowerMod(classId, targetClassId));
			for (ClassId ci : ClassId.values())
			{
				if (ci.getId() == classId)
				{
					htm.replace("%classname%", ci.name());
					break;
				}
			}
			if (targetClassId == -1)
				htm.replace("%targetclassname%", "ALL CLASSES");
			else
			{
				for (ClassId ci : ClassId.values())
				{
					if (ci.getId() == targetClassId)
					{
						htm.replace("%targetclassname%", ci.name());
						break;
					}
				}
			}
			player.sendPacket(htm);
		}
		else if (_command.equals("main"))
		{
			NpcHtmlMessage htm = new NpcHtmlMessage(0);
			htm.setFile(player.isLang() + "mods/balance/main.htm");
			player.sendPacket(htm);
		}
		else if (_command.startsWith("user_npc_info"))
		{
			final StringTokenizer st = new StringTokenizer(_command, " ");
			st.nextToken(); // skip command
			try
			{
				final var objId = Integer.parseInt(st.nextToken());
				final var wo = World.getInstance().getObject(objId);
				if (wo instanceof Npc)
				{
					var html = new NpcHtmlMessage(0);
					if (!st.hasMoreTokens())
						showNpcStatsInfos(player, (Npc) wo, html);
					else
					{
						var type = st.nextToken();
						switch (type)
						{
							case "aggr":
								showAggrInfo(player, (Npc) wo, html);
								break;
							
							case "drop":
							case "spoil":
								try
								{
									var page = st.hasMoreTokens() ? Integer.parseInt(st.nextToken()) : 1;
									showNpcInfoDrop(player, (Npc) wo, html, page, type.equalsIgnoreCase("drop"));
								}
								catch (Exception e)
								{
									showNpcInfoDrop(player, (Npc) wo, html, 1, true);
								}
								break;
							
							case "skill":
								showSkillInfos(player, (Npc) wo, html);
								break;
							
							case "effects":
								try
								{
									var page = st.hasMoreTokens() ? Integer.parseInt(st.nextToken()) : 1;
									showNpcInfoEffects(player, (Npc) wo, html, page);
								}
								catch (Exception e)
								{
									showNpcInfoEffects(player, (Npc) wo, html, 1);
								}
								break;
						}
					}
					player.sendPacket(html);
				}
			}
			catch (Exception e)
			{
				LOGGER.error("bypass user_npc_info error", e);
			}
		}
		else if (_command.startsWith("mod_menu_"))
		{
			IVoicedCommandHandler vch = VoicedCommandHandler.getInstance().getHandler("mod_menu_");
			vch.useVoicedCommand(_command, player, null);
		}

		// L2SkillSeller
		else if (_command.startsWith("skill")) {
			String b = _command.substring(5);
			int id = 0;
			try {
				id = Integer.parseInt(b);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (id == 0)
				return;

			L2Skill s = SkillTable.getInstance().getInfo(id, 10);
			ItemInstance i = player.getInventory().getItemByItemId(L2SkillSellerInstance.ITEM_ID);

			if (i == null || i.getCount() < L2SkillSellerInstance.ITEM_COUNT) {
				player.sendMessage("You don't have enough gold bars");
				return;
			}

			// Verifique se o jogador já possui a habilidade
			if (player.getSkillLevel(s.getId()) > 0) {
				player.sendMessage("Você já possui essa habilidade!");
				return;
			}

			player.getInventory().destroyItemByItemId("", L2SkillSellerInstance.ITEM_ID, L2SkillSellerInstance.ITEM_COUNT, player, null);
			player.sendMessage("You rewarded successfully with " + s.getName() + " Lvl:10, 5 Gold Bar disappeared");
			player.addSkill(s, false);

		}
	}
	
	public static void showNpcStatsInfos(Player player, Npc npc, NpcHtmlMessage html)
	{
		html.setFile(player.isLang() + "mods/npcinfo/stat.htm");
		
		html.replace("%objid%", npc.getObjectId());
		html.replace("%hp%", (int) npc.getStatus().getHp());
		html.replace("%hpmax%", npc.getStatus().getMaxHp());
		html.replace("%mp%", (int) npc.getStatus().getMp());
		html.replace("%mpmax%", npc.getStatus().getMaxMp());
		html.replace("%patk%", npc.getStatus().getPAtk(null));
		html.replace("%matk%", npc.getStatus().getMAtk(null, null));
		html.replace("%pdef%", npc.getStatus().getPDef(null));
		html.replace("%mdef%", npc.getStatus().getMDef(null, null));
		html.replace("%accu%", npc.getStatus().getAccuracy());
		html.replace("%evas%", npc.getStatus().getEvasionRate(null));
		html.replace("%crit%", npc.getStatus().getCriticalHit(null, null));
		html.replace("%rspd%", (int) npc.getStatus().getMoveSpeed());
		html.replace("%aspd%", npc.getStatus().getPAtkSpd());
		html.replace("%cspd%", npc.getStatus().getMAtkSpd());
		html.replace("%str%", npc.getStatus().getSTR());
		html.replace("%dex%", npc.getStatus().getDEX());
		html.replace("%con%", npc.getStatus().getCON());
		html.replace("%int%", npc.getStatus().getINT());
		html.replace("%wit%", npc.getStatus().getWIT());
		html.replace("%men%", npc.getStatus().getMEN());
		html.replace("%ele_fire%", npc.getStatus().getDefenseElementValue(ElementType.FIRE));
		html.replace("%ele_water%", npc.getStatus().getDefenseElementValue(ElementType.WATER));
		html.replace("%ele_wind%", npc.getStatus().getDefenseElementValue(ElementType.WIND));
		html.replace("%ele_earth%", npc.getStatus().getDefenseElementValue(ElementType.EARTH));
		html.replace("%ele_holy%", npc.getStatus().getDefenseElementValue(ElementType.HOLY));
		html.replace("%ele_dark%", npc.getStatus().getDefenseElementValue(ElementType.DARK));
	}
	
	private static void showAggrInfo(Player player, Npc npc, NpcHtmlMessage html)
	{
		html.setFile(player.isLang() + "mods/npcinfo/aggr.htm");
		html.replace("%objid%", npc.getObjectId());
		
		if (!(npc instanceof Attackable))
		{
			html.replace("%content%", "This NPC can't build aggro towards targets.<br><button value=\"Refresh\" action=\"bypass -h user_npc_info " + npc.getObjectId() + " aggr\" width=65 height=19 back=\"L2UI_ch3.smallbutton2_over\" fore=\"L2UI_ch3.smallbutton2\">");
			return;
		}
		
		final AggroList aggroList = ((Attackable) npc).getAggroList();
		if (aggroList.isEmpty())
		{
			html.replace("%content%", "This NPC's AggroList is empty.<br><button value=\"Refresh\" action=\"bypass -h user_npc_info " + npc.getObjectId() + " aggr\" width=65 height=19 back=\"L2UI_ch3.smallbutton2_over\" fore=\"L2UI_ch3.smallbutton2\">");
			return;
		}
		
		final StringBuilder sb = new StringBuilder(500);
		sb.append("<button value=\"Refresh\" action=\"bypass -h user_npc_info " + npc.getObjectId() + " aggr\" width=65 height=19 back=\"L2UI_ch3.smallbutton2_over\" fore=\"L2UI_ch3.smallbutton2\"><br><table width=\"280\"><tr><td><font color=\"LEVEL\">Attacker</font></td><td><font color=\"LEVEL\">Damage</font></td><td><font color=\"LEVEL\">Hate</font></td></tr>");
		
		for (AggroInfo ai : aggroList.values().stream().sorted(Comparator.comparing(AggroInfo::getHate, Comparator.reverseOrder())).limit(15).collect(Collectors.toList()))
			StringUtil.append(sb, "<tr><td>", ai.getAttacker().getName(), "</td><td>", ai.getDamage(), "</td><td>", ai.getHate(), "</td></tr>");
		
		sb.append("</table><img src=\"L2UI.SquareGray\" width=277 height=1>");
		
		html.replace("%content%", sb.toString());
	}
	
	private static void showNpcInfoDrop(Player player, Npc npc, NpcHtmlMessage html, int page, boolean isDrop)
	{
		// Load static Htm.
		html.setFile(player.isLang() + "mods/npcinfo/droplist.htm");
		html.replace("%objid%", npc.getObjectId());
		
		int row = 0;
		
		// Generate data.
		final Pagination<DropCategory> list = new Pagination<>(npc.getTemplate().getDropData().stream(), page, PAGE_LIMIT_1, dc -> (isDrop) ? dc.getDropType() != DropType.SPOIL : dc.getDropType() == DropType.SPOIL);
		for (DropCategory category : list)
		{
			double catChance = Math.min(DropCalc.getInstance().calcDropChance(player, npc, category, category.getDropType(), npc.isRaidBoss(), npc instanceof GrandBoss), 100.0);
			double baseCatChance = category.getChance() * category.getDropType().getDropRate(player, npc, npc.isRaidBoss(), npc instanceof GrandBoss);
			double chanceMultiplier = 1;
			double countMultiplier = 1;
			
			if (baseCatChance > 100)
			{
				countMultiplier = baseCatChance / category.getCategoryCumulativeChance();
				chanceMultiplier = baseCatChance / 100d / countMultiplier;
				baseCatChance = 100;
			}
			
			if (Config.ALTERNATE_DROP_LIST)
			{
				list.append("<br></center>Category: ", category.getDropType(), " - Rate: ", PERCENT.format(catChance), "%<center>");
				
				for (DropData drop : category.getAllDrops())
				{
					final double chance = DropCalc.getInstance().calcDropChance(player, npc, drop, category.getDropType(), npc.isRaidBoss(), npc instanceof GrandBoss);
					
					final double normChance = Math.min(99.99, chance);
					
					final double overflowFactor = Math.max(0.0, (chance - 100) / 100);
					final double inverseCategoryChance = (100 - category.getChance()) / 100;
					final double reduceFactor = Math.pow(inverseCategoryChance, 10);
					final double levelFactor = (80.0 - npc.getStatus().getLevel()) / 90;
					int min = drop.getMinDrop();
					int max = drop.getMaxDrop();
					
					min = (int) (min + min * overflowFactor - min * overflowFactor * reduceFactor);
					max = (int) (max + max * overflowFactor - max * overflowFactor * reduceFactor);
					if (category.getDropType() != DropType.CURRENCY)
						min = (int) (min - min * levelFactor);
					min = Math.max(min, drop.getMinDrop());
					if (category.getDropType() != DropType.CURRENCY)
						max = (int) (max - max * levelFactor);
					max = Math.max(max, min);
					
					final String color = (normChance > 80.) ? "90EE90" : (normChance > 5.) ? "BDB76B" : "F08080";
					final String percent = PERCENT.format(normChance);
					final String amount = (min == max) ? min + "" : min + "-" + max;
					final Item item = ItemData.getInstance().getTemplate(drop.getItemId());
					
					String name = item.getName();
					if (name.startsWith("Recipe: "))
						name = "R: " + name.substring(8);
					
					name = StringUtil.trimAndDress(name, 45);
					
					list.append(((row % 2) == 0 ? "<table width=280 bgcolor=000000><tr>" : "<table width=280><tr>"));
					list.append("<td width=44 height=41 align=center><table bgcolor=" + "FFFFFF" + " cellpadding=6 cellspacing=\"-5\"><tr><td><button width=32 height=32 back=" + item.getIcon() + " fore=" + item.getIcon() + "></td></tr></table></td>");
					list.append("<td width=246>&nbsp;", name, "<br1>");
					list.append("<table width=240><tr><td width=80><font color=B09878>Rate:</font> <font color=", color, ">", percent, "%</font></td><td width=160><font color=B09878>Amount: </font>", amount, "</td></tr></table>");
					list.append("</td></tr></table><img src=L2UI.SquareGray width=280 height=1>");
					
					row++;
				}
			}
			else
			{
				list.append("<br></center>Category: ", category.getDropType(), " - Rate: ", PERCENT.format(baseCatChance), "%<center>");
				
				for (DropData drop : category.getAllDrops())
				{
					final double chance = drop.getChance() * chanceMultiplier;
					final String color = (chance > 80.) ? "90EE90" : (chance > 5.) ? "BDB76B" : "F08080";
					final String percent = PERCENT.format(chance);
					final String amount = (drop.getMinDrop() == drop.getMaxDrop()) ? (int) (drop.getMinDrop() * countMultiplier) + "" : (int) (drop.getMinDrop() * countMultiplier) + " - " + (int) (drop.getMaxDrop() * countMultiplier);
					final Item item = ItemData.getInstance().getTemplate(drop.getItemId());
					
					String name = item.getName();
					if (name.startsWith("Recipe: "))
						name = "R: " + name.substring(8);
					
					name = StringUtil.trimAndDress(name, 45);
					
					list.append(((row % 2) == 0 ? "<table width=280 bgcolor=000000><tr>" : "<table width=280><tr>"));
					list.append("<td width=44 height=41 align=center><table bgcolor=" + "FFFFFF" + " cellpadding=6 cellspacing=\"-5\"><tr><td><button width=32 height=32 back=" + item.getIcon() + " fore=" + item.getIcon() + "></td></tr></table></td>");
					list.append("<td width=246>&nbsp;", name, "<br1>");
					list.append("<table width=240><tr><td width=80><font color=B09878>Rate:</font> <font color=", color, ">", percent, "%</font></td><td width=160><font color=B09878>Amount: </font>", amount, "</td></tr></table>");
					list.append("</td></tr></table><img src=L2UI.SquareGray width=280 height=1>");
					
					row++;
				}
			}
		}
		
		list.generateSpace(20);
		list.generatePages("bypass user_npc_info " + npc.getObjectId() + " " + ((isDrop) ? "drop" : "spoil") + " %page%");
		
		html.replace("%content%", list.getContent());
	}
	
	private static void showSkillInfos(Player player, Npc npc, NpcHtmlMessage html)
	{
		html.setFile(player.isLang() + "mods/npcinfo/skills.htm");
		html.replace("%objid%", npc.getObjectId());
		
		if (npc.getTemplate().getSkills().isEmpty())
		{
			html.replace("%content%", "This NPC doesn't hold any skill.");
			return;
		}
		
		final StringBuilder sb = new StringBuilder(500);
		
		NpcSkillType type = null; // Used to see if we moved of type.
		
		// For any type of SkillType
		for (Map.Entry<NpcSkillType, List<L2Skill>> entry : npc.getTemplate().getSkills().entrySet())
		{
			if (type != entry.getKey())
			{
				type = entry.getKey();
				StringUtil.append(sb, "<br><font color=\"LEVEL\">", type.name(), "</font><br1>");
			}
			
			for (L2Skill skill : entry.getValue())
				StringUtil.append(sb, ((skill.getSkillType() == SkillType.NOTDONE) ? ("<font color=\"777777\">" + skill.getName() + "</font>") : skill.getName()), " [", skill.getId(), "-", skill.getLevel(), "]<br1>");
		}
		
		html.replace("%content%", sb.toString());
	}
		
		public static void setPart(Player player, String part, String type)
		{
			if (player.getDressMeData() == null)
			{
				DressMeData dmd = new DressMeData();
				player.setDressMeData(dmd);
			}
			
			switch (part)
			{
				case "chest":
				{
					if (Config.DRESS_ME_CHESTS.keySet().contains(type))
						player.getDressMeData().setChestId(Config.DRESS_ME_CHESTS.get(type));
					break;
				}
				case "hairall":
				{
					if (Config.DRESS_ME_HAIRALL.keySet().contains(type))
						player.getDressMeData().setHairAllId(Config.DRESS_ME_HAIRALL.get(type));
					break;
				}
				case "legs":
				{
					if (Config.DRESS_ME_LEGS.keySet().contains(type))
						player.getDressMeData().setLegsId(Config.DRESS_ME_LEGS.get(type));
					break;
				}
				case "gloves":
				{
					if (Config.DRESS_ME_GLOVES.keySet().contains(type))
						player.getDressMeData().setGlovesId(Config.DRESS_ME_GLOVES.get(type));
					break;
				}
				case "boots":
				{
					if (Config.DRESS_ME_BOOTS.keySet().contains(type))
						player.getDressMeData().setBootsId(Config.DRESS_ME_BOOTS.get(type));
					break;
				}
			}
		}	
	private static void showNpcInfoEffects(Player player, Npc npc, NpcHtmlMessage html, int page)
	{
		final int EFFECTS_PER_LIST = 12;
		final Pagination<AbstractEffect> list = new Pagination<>(Arrays.stream(npc.getAllEffects()), page, EFFECTS_PER_LIST);
		
		// Load static Htm.
		html.setFile(player.isLang() + "mods/npcinfo/effects.htm");
		html.replace("%objid%", npc.getObjectId());
		
		final StringBuilder sb = new StringBuilder(500);
		
		sb.append("<button value=\"Refresh\" action=\"bypass -h user_npc_info " + npc.getObjectId() + " effects\" width=65 height=19 back=\"L2UI_ch3.smallbutton2_over\" fore=\"L2UI_ch3.smallbutton2\">");
		
		if (list.isEmpty())
			sb.append("This NPC's Effects is empty.");
		else
		{
			sb.append("<table width=270><tr><td width=220>Effect</td><td width=50>Time Left</td></tr>");
			
			for (AbstractEffect effect : list)
				StringUtil.append(sb, "<tr><td>", effect.getSkill().getName(), "</td><td>", (effect.getSkill().isToggle()) ? "toggle" : effect.getPeriod() - effect.getTime() + "s", "</td></tr>");
			
			sb.append("</table><br>");
			
			// Build page footer.
			sb.append("<br><img src=\"L2UI.SquareGray\" width=277 height=1><table width=\"100%\" bgcolor=000000><tr>");
		}
		
		html.replace("%content%", sb.toString());
	}
}