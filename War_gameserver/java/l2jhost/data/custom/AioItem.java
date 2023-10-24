package l2jhost.data.custom;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.olympiad.OlympiadManager;
import net.sf.l2j.gameserver.network.SystemMessageId;

/**
 * @author Reborn12
 */
public class AioItem implements IItemHandler
{
	private static final int[] ITEM_IDS = new int[]
	{
		Config.AIO_COIN_ID
	};
	
	@Override
	public void useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		
		if (Config.ENABLE_AIO_COIN)
		{
			if (!(playable instanceof Player))
				return;
			
			Player player = (Player) playable;
			if (player.isAio())
				player.sendMessage("Comando /aio Liberado!");
			
			else if (player.isInOlympiadMode() || OlympiadManager.getInstance().isRegisteredInComp(player))
				player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			else if (player.destroyItemByItemId("aio", Config.AIO_COIN_ID, 1, null, true))
				AdminSetAio.doAio(player, player, Config.AIO_COIN_DAYS);
			AioMenu.mainHtml(player, 0);
		}
	}
	
	public int[] getItemIds()
	{
		return ITEM_IDS;
	}
}
