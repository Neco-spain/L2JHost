package net.sf.l2j.gameserver.model.donate.data;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.commons.data.StatSet;
import net.sf.l2j.commons.data.xml.IXmlReader;

import net.sf.l2j.gameserver.model.donate.holder.JewelSet;

import org.w3c.dom.Document;

public class JewelSetData implements IXmlReader
{
	private final Map<Integer, JewelSet> _jewelSets = new HashMap<>();
	
	protected JewelSetData()
	{
		load();
	}
	
	public void reload()
	{
		_jewelSets.clear();
		load();
	}
	
	@Override
	public void load()
	{
		parseFile("./data/donate/jewelSets.xml");
		LOGGER.info("Donate Loaded {} jewels List.", _jewelSets.size());
	}
	
	@Override
	public void parseDocument(Document doc, Path path)
	{
		forEach(doc, "list", listNode -> forEach(listNode, "jewelset", jewelsetNode ->
		{
			final StatSet set = parseAttributes(jewelsetNode);
			_jewelSets.put(set.getInteger("necklace"), new JewelSet(set));
		}));
	}
	
	public JewelSet getSet(int chestId)
	{
		return _jewelSets.get(chestId);
	}
	
	public Collection<JewelSet> getSets()
	{
		return _jewelSets.values();
	}
	
	public static JewelSetData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final JewelSetData INSTANCE = new JewelSetData();
	}
}
