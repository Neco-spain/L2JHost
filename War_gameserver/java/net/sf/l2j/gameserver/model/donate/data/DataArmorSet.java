package net.sf.l2j.gameserver.model.donate.data;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.commons.data.StatSet;
import net.sf.l2j.commons.data.xml.IXmlReader;

import net.sf.l2j.gameserver.model.donate.holder.ArmorSet;

import org.w3c.dom.Document;

public class DataArmorSet implements IXmlReader
{
	private final Map<Integer, ArmorSet> _armorSets = new HashMap<>();
	public DataArmorSet()
	{
		load();
	}
	
	public void reload()
	{
		_armorSets.clear();
		load();
	}
	
	@Override
	public void load()
	{
		parseFile("./data/donate/armorSets.xml");
		LOGGER.info("Donate Loaded {} armor sets.", _armorSets.size());
	}
	
	@Override
	public void parseDocument(Document doc, Path path)
	{
		forEach(doc, "list", listNode -> forEach(listNode, "armorset", armorsetNode ->
		{
			final StatSet set = parseAttributes(armorsetNode);
			_armorSets.put(set.getInteger("chest"), new ArmorSet(set));
		}));
	}
	
	public ArmorSet getSet(int chestId)
	{
		return _armorSets.get(chestId);
	}
	
	public Collection<ArmorSet> getSets()
	{
		return _armorSets.values();
	}
	
	public static DataArmorSet getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final DataArmorSet INSTANCE = new DataArmorSet();
	}
}
