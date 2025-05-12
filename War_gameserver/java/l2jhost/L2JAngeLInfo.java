package l2jhost;

import java.util.Objects;
import java.util.stream.Stream;

import net.sf.l2j.commons.logging.CLogger;

public class L2JAngeLInfo 
{
	   private static final CLogger LOG = new CLogger(L2JAngeLInfo.class.getName());
	   private static final String[] INFO_HEADER = 
		   {

		  "____________________________________________________________________",		
	      " #        #####        #       #    #     #  #####  ####### #       ",
		  " #       #     #       #      # #   ##    # #     # #       #       ",
		  " #             #       #     #   #  # #   # #       #       #       ",
		  " #        #####        #    #     # #  #  # #  #### #####   #       ",
		  " #       #       #     #    ####### #   # # #     # #       #       ",
		  " #       #       #     #    #     # #    ## #     # #       #       ",
		  " ####### #######  #####     #     # #     #  #####  ####### ####### ",
		  "____________________________________________________________________",		
		  "                          Copiled version L2ALGEL.NET               ",
		  "____________________________________________________________________",
		  "                      The Chaotic Throne Interlude Project          ",
		  "                         Desenvolved by WaN202                      ",
		  "                                 WWW.L2ALGEL.NET                    ",
		  "____________________________________________________________________",
	      " "
	   };
   public static final void showInfo() 
   {
      Stream<String> infoStream = Stream.of(INFO_HEADER);
      Objects.requireNonNull(LOG);
      infoStream.forEach(LOG::info);
   }
}
