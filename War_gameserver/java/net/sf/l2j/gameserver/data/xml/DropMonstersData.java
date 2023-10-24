package net.sf.l2j.gameserver.data.xml;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.commons.data.StatSet;
import net.sf.l2j.commons.data.xml.IXmlReader;

import org.w3c.dom.Document;

import l2jhost.data.custom.DropMonsters;

/**
 * @author Williams
 */
public class DropMonstersData implements IXmlReader
{
	private final Map<Integer, DropMonsters> _data = new HashMap<>();
	
	public DropMonstersData()
	{
		load();
	}
	
	public void reload()
	{
		_data.clear();
		load();
	}
	
	@Override
	public void load()
	{
		parseFile("./data/xml/monstersDrops.xml");
		LOGGER.info("Loaded {} drop data.", _data.size());
	}
	
	@Override
	public void parseDocument(Document doc, Path path)
	{
		forEach(doc, "list", listNode -> forEach(listNode, "drops", monstersNode ->
		{
			final StatSet set = parseAttributes(monstersNode);
			forEach(monstersNode, "settings", setNode -> set.putAll(parseAttributes(setNode)));
			_data.put(set.getInteger("npcId"), new DropMonsters(set));
		}));
	}
	
	public DropMonsters getNpcs(int npcId)
	{
		return _data.get(npcId);
	}
	
	public static DropMonstersData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final DropMonstersData INSTANCE = new DropMonstersData();
	}
}
