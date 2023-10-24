package net.sf.l2j.gameserver.model.donate.holder;

import net.sf.l2j.commons.data.StatSet;

import net.sf.l2j.gameserver.data.xml.ItemData;
import net.sf.l2j.gameserver.enums.Paperdoll;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Item;

public final class JewelSet
{
	private final String name;
	private final int[] set = new int[5];
	private final int _prince;
	private final int _princeCont;
	private final int _enchant;
	
	public JewelSet(StatSet set)
	{
		name = set.getString("name");
		
		this.set[0] = set.getInteger("necklace");
		this.set[1] = set.getInteger("earring");
		this.set[2] = set.getInteger("ring");
		this.set[3] = set.getInteger("bracelet");
		this.set[4] = set.getInteger("belt");
		
		_prince = set.getInteger("priceId", 0);
		_princeCont = set.getInteger("princeCount", 0);
		_enchant = set.getInteger("enchantLevel", 0);
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
	public int[] getSetItemsId()
	{
		return set;
	}
	
	public int getEnchantLevel()
	{
		return _enchant;
	}
	
	public int getPrinceId()
	{
		return _prince;
	}
	
	public int getPrinceCont()
	{
		return _princeCont;
	}
	
	public boolean containsAll(Player player)
	{
		int necklace = 0;
		final ItemInstance necklaceItem = player.getInventory().getItemFrom(Paperdoll.NECK);
		if (necklaceItem != null)
		{
			necklace = necklaceItem.getItemId();
		}
		
		if (set[0] != 0 && set[0] != necklace)
		{
			return false;
		}
		
		int earring = 0;
		final ItemInstance earringItem = player.getInventory().getItemFrom(Paperdoll.LEAR);
		if (earringItem != null)
		{
			earring = earringItem.getItemId();
		}
		
		if (set[1] != 0 && set[1] != earring)
		{
			return false;
		}
		
		int ring = 0;
		final ItemInstance ringItem = player.getInventory().getItemFrom(Paperdoll.REAR);
		if (ringItem != null)
		{
			ring = ringItem.getItemId();
		}
		
		if (set[2] != 0 && set[2] != ring)
		{
			return false;
		}
		
		int bracelet = 0;
		final ItemInstance braceletItem = player.getInventory().getItemFrom(Paperdoll.LFINGER);
		if (braceletItem != null)
		{
			bracelet = braceletItem.getItemId();
		}
		
		if (set[3] != 0 && set[3] != bracelet)
		{
			return false;
		}
		
		int belt = 0;
		final ItemInstance beltItem = player.getInventory().getItemFrom(Paperdoll.RFINGER);
		if (beltItem != null)
		{
			belt = beltItem.getItemId();
		}
		
		if (set[4] != 0 && set[4] != belt)
		{
			return false;
		}
		
		return true;
	}
	
	public boolean containsItem(Paperdoll slot, int itemId)
	{
		switch (slot)
		{
			case NECK:
				return set[0] == itemId;
			
			case LEAR:
				return set[1] == itemId;
			
			case REAR:
				return set[2] == itemId;
			
			case LFINGER:
				return set[3] == itemId;
			
			case RFINGER:
				return set[4] == itemId;
			
			default:
				return false;
		}
	}
	
	public String getIcon()
	{
		int itemId = set[0];
		final Item item = ItemData.getInstance().getTemplate(itemId);
		return item.getIcon();
	}
}
