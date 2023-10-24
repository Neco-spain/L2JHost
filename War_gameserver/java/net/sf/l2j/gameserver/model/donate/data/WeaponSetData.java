package net.sf.l2j.gameserver.model.donate.data;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.commons.data.StatSet;
import net.sf.l2j.commons.data.xml.IXmlReader;

import net.sf.l2j.gameserver.model.donate.holder.WeaponSet;

import org.w3c.dom.Document;

public class WeaponSetData implements IXmlReader
{
	private final Map<Integer, WeaponSet> weaponSets = new HashMap<>();
	
	protected WeaponSetData()
	{
		load();
	}
	
	public void reload()
	{
		weaponSets.clear();
		load();
	}
	
	@Override
	public void load()
	{
		parseFile("./data/donate/weaponSets.xml");
		LOGGER.info("Donate Loaded {} weapon list.", weaponSets.size());
	}
	
	@Override
	public void parseDocument(Document doc, Path path)
	{
		forEach(doc, "list", listNode -> forEach(listNode, "weaponset", weaponSetNode ->
		{
			final StatSet set = parseAttributes(weaponSetNode);
			int weaponId = set.getInteger("weaponId");
			
			int princeId = set.getInteger("priceId");
			int count = set.getInteger("princeCount");
			int enchant = set.getInteger("enchantLevel");
			
			String weaponName = set.getString("weaponName");
			weaponSets.put(weaponId, new WeaponSet(weaponName, new int[]
			{
				weaponId
			}, princeId, count, enchant));
		}));
	}
	
	public WeaponSet getSet(int weaponId)
	{
		return weaponSets.get(weaponId);
	}
	
	public Collection<WeaponSet> getSets()
	{
		return weaponSets.values();
	}
	
	public static WeaponSetData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final WeaponSetData INSTANCE = new WeaponSetData();
	}
}