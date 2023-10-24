package net.sf.l2j.gameserver.model.donate.holder;

import net.sf.l2j.gameserver.data.xml.ItemData;
import net.sf.l2j.gameserver.model.item.kind.Item;

public class WeaponSet
{
	private final String name;
	private final int[] weapons;
	private final int _prince;
	private final int _princeCont;
	private final int _enchant;
	
	public WeaponSet(String name, int[] weapons, int prince, int count, int enchant)
	{
		this.name = name;
		this.weapons = weapons;
		
		this._prince = prince;
		this._princeCont = count;
		this._enchant = enchant;
	
	}
	
	public String getName()
	{
		return name;
	}
	
	public int[] getWeapons()
	{
		return weapons;
	}
	
	public int getPrinceId()
	{
		return _prince;
	}
	
	public int getPrinceCont()
	{
		return _princeCont;
	}
	
	public int getEnchantLevel()
	{
		return _enchant;
	}
	
	public String getIcon()
	{
		int itemId = weapons[0];
		final Item item = ItemData.getInstance().getTemplate(itemId);
		return item.getIcon();
	}
}
