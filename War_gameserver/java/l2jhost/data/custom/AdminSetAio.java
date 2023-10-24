package l2jhost.data.custom;

import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import net.sf.l2j.commons.pool.ThreadPool;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.enums.SayType;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.skills.L2Skill;


/**
 * @author jBan
 */
public class AdminSetAio implements IAdminCommandHandler
{
   private static String[] _adminCommands = new String[]
   {
       "admin_setaio",
       "admin_removeaio"
   };
   
   @Override
   public void useAdminCommand(String command, Player activeChar)
   {
       StringTokenizer st = new StringTokenizer(command);
       st.nextToken();
       String player = "";
       int time = 1;
       Player target = null;
       if (st.hasMoreTokens())
       {
           player = st.nextToken();
           target = World.getInstance().getPlayer(player);
           if (st.hasMoreTokens())
           {
               try
               {
                   time = Integer.parseInt(st.nextToken());
               }
               catch (NumberFormatException nfe)
               {
                   activeChar.sendMessage("Invalid number format used: " + nfe);
                   return;
               }
           }
       }
       else if (activeChar.getTarget() != null && activeChar.getTarget() instanceof Player)
           target = (Player) activeChar.getTarget();
       
       if (command.startsWith("admin_setaio"))
      {
           if (target == null && player.equals(""))
           {
               activeChar.sendMessage("Usage: //setaio <char_name> [duration_days]");
               return ;
           }
           if (target != null)
           {
              AdminSetAio.doAio(target, activeChar, time);
               activeChar.sendMessage(target.getName() + " Comando /aio Liberado! ");
               activeChar.sendMessage(target.getName() + " got AIO status for " + time + " day(s).");
           }
       }
       else if (command.startsWith("admin_removeaio"))
       {
           if (target == null && player.equals(""))
           {
               activeChar.sendMessage("Usage: //removeaio <char_name>");
               return ;
           }
           if (target != null)
           {
               if (target.isAio())
               {
                   AdminSetAio.removeAio(target, activeChar);
                   activeChar.sendMessage("Removed the AIO status from " + target.getName() + ".");
               }
               activeChar.sendMessage(target.getName() + " haven't AIO status.");
           }
       }
       return;
   }
   
   public static void doAio(Player target, Player player, int time)
   {
       target.getStatus().addExp(target.getStatus().getExpForLevel(81));
       target.broadcastPacket(new SocialAction(target, 3));
       target.setAio(true);
       
       AioTaskManager.getInstance().add(target);
       long remainingTime = target.getMemos().getLong("aioEndTime", 0);
       if (remainingTime > 0)
       {
           target.getMemos().set("aioEndTime", remainingTime + TimeUnit.DAYS.toMillis(time));
           target.sendPacket(new CreatureSay(0, SayType.HERO_VOICE, "AIO Manager", "Dear " + player.getName() + ", your AIO status has been extended by " + time + " day(s)."));
       }
      else
       {
           target.getMemos().set("aioEndTime", System.currentTimeMillis() + TimeUnit.DAYS.toMillis(time));
           target.sendPacket(new CreatureSay(0, SayType.HERO_VOICE, "AIO Manager", "Dear " + player.getName() + ", you got AIO Status for " + time + " day(s)."));
           
           for (L2Skill skill : target.getSkills().values())
               target.removeSkill(skill.getId(), true);
           
           if (Config.AIO_ITEM_ID != 0)
           {
               target.addItem("Add", Config.AIO_ITEM_ID, 1, target, true);
               target.getInventory().equipItemAndRecord(target.getInventory().getItemByItemId(Config.AIO_ITEM_ID));
          }
           target.addAioSkills();
           target.broadcastUserInfo();
       }
   }
   
   public static void removeAio(Player target, Player player)
   {
       AioTaskManager.getInstance().remove(target);
       target.getMemos().set("aioEndTime", 0);
       target.setAio(false);
       for (L2Skill skill : target.getSkills().values())
           target.removeSkill(skill.getId(), true);
       
       if (Config.AIO_ITEM_ID != 0)
          target.destroyItemByItemId("Destroy", Config.AIO_ITEM_ID, 1, target, true);
       
       target.sendPacket(new CreatureSay(0, SayType.HERO_VOICE, "AIO Manager", "Dear " + player.getName() + ", Your AIO period is over. You will be disconected in 3 seconds."));
       target.broadcastPacket(new SocialAction(target, 13));
       target.sendSkillList();
       target.broadcastUserInfo();
       ThreadPool.schedule(() -> target.logout(false), 3000);
   }
   
   @Override
   public String[] getAdminCommandList()
   {
       return _adminCommands;
   }
}