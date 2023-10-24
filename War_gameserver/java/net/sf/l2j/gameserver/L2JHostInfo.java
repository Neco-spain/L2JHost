package net.sf.l2j.gameserver;
import java.util.Objects;
import java.util.stream.Stream;

import net.sf.l2j.commons.logging.CLogger;

public class L2JHostInfo {
	   private static final CLogger LOG = new CLogger(L2JHostInfo.class.getName());
	   private static final String[] INFO_HEADER = {

		  "________________________________________________________________________________",
	      "                             								                   ",		
		  "                  #     ##### #####     #   # ##### ##### #####                 ",
		  "                  #         #   #       #   # #   # #       #                   ",
		  "                  #      ###    #  ###  ##### #   # #####   #                   ",
		  "                  #     #     # #       #   # #   #     #   #                   ",
		  "                  ##### ##### ###       #   # ##### #####   #                   ",
		  "________________________________________________________________________________",
	      "                           								                       ",		
		  "                          Copiled version L2BLAZING.COM.BR                      ",
		  "________________________________________________________________________________",
		  "                           								                       ",
		  "                          The Chaotic Throne Interlude Project                  ",
		  "                              Desenvolved by BrDevelopers                       ",
		  "                                    WWW.L2JHOST.COM                             ",
		  "________________________________________________________________________________",
	      " "
	   };
   public static final void showInfo() {
      Stream<String> infoStream = Stream.of(INFO_HEADER);
      Objects.requireNonNull(LOG);
      infoStream.forEach(LOG::info);
   }
}
