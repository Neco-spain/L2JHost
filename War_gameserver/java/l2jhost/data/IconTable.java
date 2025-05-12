package l2jhost.data;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class IconTable extends XMLDocument
{
   private static final Map<Integer, String> itemIcons = new HashMap<>();
   
   public IconTable()
   {
       load();
   }
   
   @Override
   protected void load()
   {
       loadDocument("./data/xml/icons.xml");
       LOG.info("Loaded " + itemIcons.size() + " icons.");
   }
   
   @Override
   protected void parseDocument(Document doc, File f)
   {
       // First element is never read.
       final Node n = doc.getFirstChild();
       
       for (Node o = n.getFirstChild(); o != null; o = o.getNextSibling())
       {
           if (!"icon".equalsIgnoreCase(o.getNodeName()))
           {
               continue;
           }
           
           final NamedNodeMap attrs = o.getAttributes();
           final int itemId = Integer.valueOf(attrs.getNamedItem("itemId").getNodeValue());
           final String value = String.valueOf(attrs.getNamedItem("iconName").getNodeValue());
           
           itemIcons.put(itemId, value);
       }
   }
   
   public static String getIcon(int id)
   {
       return itemIcons.get(id) == null ? "icon.noimage" : itemIcons.get(id);
   }
   
   public static IconTable getInstance()
   {
       return SingletonHolder._instance;
   }
   
   private static class SingletonHolder
   {
       protected static final IconTable _instance = new IconTable();
   }
}