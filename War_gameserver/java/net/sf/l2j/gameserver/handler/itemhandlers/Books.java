package net.sf.l2j.gameserver.handler.itemhandlers;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.handler.voicedcommandhandlers.Menu;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public class Books implements IItemHandler
{
	@Override
	public void useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!(playable instanceof Player))
			return;
		
		final Player player = (Player) playable;
		
		if(Config.ENABLE_MENU_IN_BOOK)
		{
			Menu.showHtm(player);
		}
		else
		{
			final NpcHtmlMessage html = new NpcHtmlMessage(0);
			html.setFile(player.isLang() + "help/" + item.getItemId() + ".htm");
			html.setItemId(item.getItemId());
			player.sendPacket(html);
		}
		
		
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
}