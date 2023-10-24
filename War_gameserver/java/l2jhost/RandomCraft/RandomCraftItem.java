package l2jhost.RandomCraft;

import net.sf.l2j.gameserver.data.xml.ItemData;
import net.sf.l2j.gameserver.model.item.kind.Item;

public class RandomCraftItem
{
   private int id;
   private int cantidad;
   private double probabilidad;
   private boolean announce;
   
   public RandomCraftItem(int id, int cantidad, double probabilidad, boolean announce)
   {
       this.id = id;
       this.cantidad = cantidad;
       this.probabilidad = probabilidad;
       this.announce = announce;
   }
   
   public int getId()
   {
       return id;
   }
   
   public int getCantidad()
   {
       return cantidad;
   }
   
   public double getProbabilidad()
   {
       return probabilidad;
   }
   
   public boolean getAnnounce()
   {
       return announce;
   }
   
   public String getIcon()
   {
       return getItem().getIcon();
   }
   
   public Item getItem()
   {
       return ItemData.getInstance().getTemplate(id);
   }
}