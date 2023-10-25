package net.sf.l2j;

import net.sf.l2j.commons.config.ExProperties;
import net.sf.l2j.commons.logging.CLogger;
import net.sf.l2j.commons.math.MathUtil;
import net.sf.l2j.gameserver.enums.GeoType;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.model.olympiad.enums.OlympiadPeriod;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class contains global server configuration.<br>
 * It has static final fields initialized from configuration files.
 */
public final class Config {
	private static final CLogger LOGGER = new CLogger(Config.class.getName());

	public static final String CHAT_FILTER_FILE = "./config/chatfilter.txt";

	public static final String CLANS_FILE = "./config/clans.properties";
	public static final String EVENTS_FILE = "./config/events.properties";
	public static final String GEOENGINE_FILE = "./config/geoengine.properties";
	public static final String HEXID_FILE = "./config/hexid.txt";
	public static final String LOGINSERVER_FILE = "./config/loginserver.properties";
	public static final String NPCS_FILE = "./config/npcs.properties";
	public static final String OFFLINE_FILE = "./config/offlineshop.properties";
	public static final String PLAYERS_FILE = "./config/players.properties";
	public static final String RATES_FILE = "./config/rates.properties";
	public static final String FILE_L2JHOST = "./config/l2jhost.properties";
	public static final String SERVER_FILE = "./config/server.properties";
	public static final String SIEGE_FILE = "./config/siege.properties";
	public static final String CUSTOMQUESTS = "./config/quests.properties";
	public static final String NEWBIE_FILE = "./config/newbies_system.properties";

	public static final String BALANCE_FILE = "./config/balance.properties";
	public static final String DONATE_FILE = "./config/donate.properties";
	public static final String BOSS_EVENT_INSTANCED = "./config/BossEvent.properties";

	/** Boss Event */
	public static boolean BOSS_EVENT_TIME_ON_SCREEN;
	public static int BOSS_EVENT_TIME_TO_DESPAWN_BOSS;
	public static int BOSS_EVENT_REGISTRATION_NPC_ID;
	public static Map<Integer, Integer> BOSS_EVENT_GENERAL_REWARDS = new HashMap<>();
	public static Map<Integer, Integer> BOSS_EVENT_LAST_ATTACKER_REWARDS = new HashMap<>();
	public static Map<Integer, Integer> BOSS_EVENT_MAIN_DAMAGE_DEALER_REWARDS = new HashMap<>();
	public static boolean BOSS_EVENT_REWARD_MAIN_DAMAGE_DEALER;
	public static boolean BOSS_EVENT_REWARD_LAST_ATTACKER;
	public static List<Location> BOSS_EVENT_LOCATION = new ArrayList<>();
	public static int BOSS_EVENT_REWARD_ID;
	public static int BOSS_EVENT_REWARD_COUNT;
	public static int BOSS_EVENT_MIN_DAMAGE_TO_OBTAIN_REWARD;
	public static List<Integer> BOSS_EVENT_ID = new ArrayList<>();
	public static Location BOSS_EVENT_NPC_REGISTER_LOC;
	public static int BOSS_EVENT_TIME_TO_WAIT;
	public static int BOSS_EVENT_TIME_TO_TELEPORT_PLAYERS;
	public static int BOSS_EVENT_MIN_PLAYERS;
	public static int BOSS_EVENT_REGISTRATION_TIME;
	public static String[] BOSS_EVENT_BY_TIME_OF_DAY;

	public static int BOSS_EVENT_MAX_PLAYERS_PER_IP;

	public static int ID_NPC_SKILL_SELLER;
	public static int ITEM_ID_SKILL_SELLER;
	public static int QUANT_OF_GOLD_ITEM;
	public static final Map<Integer, Integer> SKILLIDSSELLER = new HashMap<>();

	public static final Map<Integer, Integer> SKILLCLASS1 = new HashMap<>();
	public static final Map<Integer, Integer> SKILLCLASS2 = new HashMap<>();
	public static final Map<Integer, Integer> SKILLCLASS3 = new HashMap<>();
	public static final Map<Integer, Integer> SKILLCLASS4 = new HashMap<>();
	public static final Map<Integer, Integer> SKILLCLASS5 = new HashMap<>();
	public static final Map<Integer, Integer> SKILLCLASS6 = new HashMap<>();
	public static final Map<Integer, Integer> SKILLCLASS7 = new HashMap<>();
	public static final Map<Integer, Integer> SKILLCLASS8 = new HashMap<>();
	public static final Map<Integer, Integer> SKILLCLASS9 = new HashMap<>();
	public static final Map<Integer, Integer> SKILLCLASS10 = new HashMap<>();
	public static final Map<Integer, Integer> SKILLCLASS11 = new HashMap<>();
	public static final Map<Integer, Integer> SKILLCLASS12 = new HashMap<>();
	public static final Map<Integer, Integer> SKILLCLASS13 = new HashMap<>();
	public static final Map<Integer, Integer> SKILLCLASS14 = new HashMap<>();
	public static final Map<Integer, Integer> SKILLCLASS15 = new HashMap<>();
	public static final Map<Integer, Integer> SKILLCLASS16 = new HashMap<>();
	public static final Map<Integer, Integer> SKILLCLASS17 = new HashMap<>();
	public static final Map<Integer, Integer> SKILLCLASS18 = new HashMap<>();
	public static final Map<Integer, Integer> SKILLCLASS19 = new HashMap<>();
	public static final Map<Integer, Integer> SKILLCLASS20 = new HashMap<>();

	public static final Map<Integer, Integer> SKILLCLASS21 = new HashMap<>();
	public static final Map<Integer, Integer> SKILLCLASS22 = new HashMap<>();
	public static final Map<Integer, Integer> RANDOM_SKILLS = new HashMap<>();
	/** newbie system */
	public static int NEWBIE_DIST;
	public static int NEWBIE_LADO;
	public static int NEWBIE_ALTURA;
	public static boolean ENABLE_STARTUP;
	public static int[] TELE_TO_LOCATION = new int[3];
	public static int NEWBIE_ITEMS_ENCHANT;
	public static int ENCHANT_CHANCE;

	public static int MAX_PARTY_HEALER_BISHOP;
	public static int MAX_PARTY_ORC_OVER;
	public static int MAX_PARTY_DUELISTA;
	public static int MAX_PARTY_WARLORD;
	/**
	 * Custom Quests
	 */
	// Q619_RelicsOfTheOldEMpire
	public static int IMPERIAL1;
	public static int IMPERIAL2;
	public static int IMPERIAL3;
	public static int IMPERIAL4;
	public static int IMPERIAL5;
	public static int IMPERIAL6;
	public static int IMPERIAL7;
	public static int IMPERIAL8;
	public static int IMPERIAL9;
	public static int IMPERIAL10;
	public static int IMPERIALSTABLE1;
	public static int IMPERIALSTABLE2;
	public static int STABLE_AMOUNT1;
	public static int STABLE_AMOUNT2;
	public static int RANDOM_AMMOUNT;
	public static final boolean ENABLE_GNU_PANEL = true;
	// Q617 GatherTheFlames
	public static int FOG1;
	public static int FOG2;
	public static int FOG3;
	public static int FOG4;
	public static int FOG5;
	public static int FOG6;
	public static int FOG7;
	public static int FOG8;
	public static int FOG9;
	public static int FOG10;
	public static int FOGSTABLE1;
	public static int FOGSTABLE2;
	public static int FOG_STABLE_AMOUNT1;
	public static int FOG_STABLE_AMOUNT2;
	public static int FOG_RANDOM_AMMOUNT;

	public static boolean MONSTERS_DROPS;
	/** L2jBan AIO Settings */
	public static Map<Integer, Integer> AIO_SKILLS;
	public static int AIO_ITEM_ID;
	public static boolean ENABLE_AIO_COIN;
	public static int AIO_COIN_ID;
	public static int AIO_COIN_DAYS;
	public static boolean ANNOUNCE_AIO;

	public static boolean BLOCK_SELL_ITEMS_ADENA;
	public static int RANDOM_CRAFT_ITEM_ID_CONSUME;
	public static int RANDOM_CRAFT_ITEM_CONSUME_REFRESH;
	public static int RANDOM_CRAFT_ITEM_CONSUME_CREATE;
	/** Buffer Manager Settings */
	public static int BUFFS_MASTER_MAX_SKILLS_PER_SCHEME;
	public static int BUFFS_MASTER_STATIC_COST_PER_BUFF;
	public static int BUFFS_MASTER_PAYMENT_ITEM;
	public static String BUFFS_MASTER_PAYMENT_ITEM_NAME;
	public static int[] BUFFS_MASTER_WARRIOR_SCHEME;
	public static int[] BUFFS_MASTER_MYSTIC_SCHEME;
	public static int[] BUFFS_MASTER_HEALER_SCHEME;
	public static int[] BUFFS_MASTER_TANKER_SCHEME;
	public static int BUFFS_MASTER_CP_RESTORE_PRICE;
	public static int BUFFS_MASTER_HP_RESTORE_PRICE;
	public static int BUFFS_MASTER_MP_RESTORE_PRICE;
	public static boolean BUFFS_MASTER_CAN_USE_KARMA;
	public static boolean BUFFS_MASTER_CAN_USE_OUTSIDE_TOWN;
	public static boolean BUFFS_MASTER_CAN_USE_IN_COMBAT;
	/** .dressme */
	public static boolean ALLOW_DRESS_ME_SYSTEM;
	public static Map<String, Integer> DRESS_ME_CHESTS = new HashMap<>();
	public static Map<String, Integer> DRESS_ME_HAIRALL = new HashMap<>();
	public static Map<String, Integer> DRESS_ME_LEGS = new HashMap<>();
	public static Map<String, Integer> DRESS_ME_BOOTS = new HashMap<>();
	public static Map<String, Integer> DRESS_ME_GLOVES = new HashMap<>();
	public static int ID_VISUAL_EFFECT_KNIGHTS;
	public static int ID_VISUAL_EFFECT_WARRIOR;
	public static int ID_VISUAL_EFFECT_WIZARDS;

	public static int ITEM_ZONE;
	public static int BUFFER_ZONE;
	/** Bank system golg bar */
	public static boolean BANKING_SYSTEM_ENABLED;
	public static int BANKING_SYSTEM_GOLDBAR_ID;
	public static int BANKING_SYSTEM_GOLDBARS;
	public static int BANKING_SYSTEM_ADENA;

	public static boolean ENABLE_BALANCE_ATTACK_TYPE;
	public static boolean ENABLE_BALANCE_LOGGER_PHYSICAL;

	public static boolean ENABLE_BALANCE_MAGICAL_TYPE;
	public static boolean ENABLE_BALANCE_LOGGER_MAGICAL;

	public static boolean ENABLE_DONATE_INSTANCE;
	public static int DONATE_BYPASS_TIME;
	public static boolean ENABLE_DONATE_VOICED_COMMAND;
	public static int DONATE_ITEMID;
	public static int DONATE_ITEM_RECOVER_COUNT;

	public static boolean DISABLE_ATTACK_NPC_TYPE;
	public static String ALLOWED_NPC_TYPES;
	public static ArrayList<String> LIST_ALLOWED_NPC_TYPES = new ArrayList<>();

	/** Messages */
	public static boolean ANNOUNCE_VIP_ENTER;
	public static String ANNOUNCE_VIP_ENTER_BY_CLAN_MEMBER_MSG;
	public static String ANNOUNCE_VIP_ENTER_BY_PLAYER_MSG;
	public static boolean ANNOUNCE_HERO_ONLY_BASECLASS;
	public static String ANNOUNCE_HERO_ENTER_BY_CLAN_MEMBER_MSG;
	public static String ANNOUNCE_HERO_ENTER_BY_PLAYER_MSG;

	public static boolean ANNOUNCE_CASTLE_LORDS;
	public static String ANNOUNCE_LORDS_ENTER_BY_CLAN_MEMBER_MSG;

	public static boolean ANNOUNCE_KILL;
	public static String ANNOUNCE_PK_MSG;
	public static String ANNOUNCE_PVP_MSG;

	public static boolean ANNOUNCE_TOP;
	public static String ANNOUNCE_TOP_PVP_ENTER_BY_CLAN_MEMBER_MSG;
	public static String ANNOUNCE_TOP_PVP_ENTER_BY_PLAYER_MSG;
	public static String ANNOUNCE_TOP_PK_ENTER_BY_CLAN_MEMBER_MSG;
	public static String ANNOUNCE_TOP_PK_ENTER_BY_PLAYER_MSG;

	public static boolean ENABLE_BOSS_DEFEATED_MSG;
	public static String RAID_BOSS_DEFEATED_BY_CLAN_MEMBER_MSG;
	public static String RAID_BOSS_DEFEATED_BY_PLAYER_MSG;
	public static String GRAND_BOSS_DEFEATED_BY_CLAN_MEMBER_MSG;
	public static String GRAND_BOSS_DEFEATED_BY_PLAYER_MSG;

	/** Announce Online Players */
	public static boolean ALLOW_ANNOUNCE_ONLINE_PLAYERS;
	public static int ANNOUNCE_ONLINE_PLAYERS_DELAY;

	/** MMO settings */
	public static int MMO_SELECTOR_SLEEP_TIME = 20; // default 20
	public static int MMO_MAX_SEND_PER_PASS = 80; // default 80
	public static int MMO_MAX_READ_PER_PASS = 80; // default 80
	public static int MMO_HELPER_BUFFER_COUNT = 20; // default 20

	// --------------------------------------------------
	// Clans settings
	// --------------------------------------------------

	/** Clans */
	public static int CLAN_JOIN_DAYS;
	public static int CLAN_CREATE_DAYS;
	public static int CLAN_DISSOLVE_DAYS;
	public static int ALLY_JOIN_DAYS_WHEN_LEAVED;
	public static int ALLY_JOIN_DAYS_WHEN_DISMISSED;
	public static int ACCEPT_CLAN_DAYS_WHEN_DISMISSED;
	public static int CREATE_ALLY_DAYS_WHEN_DISSOLVED;
	public static int MAX_NUM_OF_CLANS_IN_ALLY;
	public static int CLAN_MEMBERS_FOR_WAR;
	public static int CLAN_WAR_PENALTY_WHEN_ENDED;
	public static boolean MEMBERS_CAN_WITHDRAW_FROM_CLANWH;

	/** Manor */
	public static int MANOR_REFRESH_TIME;
	public static int MANOR_REFRESH_MIN;
	public static int MANOR_APPROVE_TIME;
	public static int MANOR_APPROVE_MIN;
	public static int MANOR_MAINTENANCE_MIN;
	public static int MANOR_SAVE_PERIOD_RATE;

	/** Clan Hall function */
	public static long CH_TELE_FEE_RATIO;
	public static int CH_TELE1_FEE;
	public static int CH_TELE2_FEE;
	public static long CH_SUPPORT_FEE_RATIO;
	public static int CH_SUPPORT1_FEE;
	public static int CH_SUPPORT2_FEE;
	public static int CH_SUPPORT3_FEE;
	public static int CH_SUPPORT4_FEE;
	public static int CH_SUPPORT5_FEE;
	public static int CH_SUPPORT6_FEE;
	public static int CH_SUPPORT7_FEE;
	public static int CH_SUPPORT8_FEE;
	public static long CH_MPREG_FEE_RATIO;
	public static int CH_MPREG1_FEE;
	public static int CH_MPREG2_FEE;
	public static int CH_MPREG3_FEE;
	public static int CH_MPREG4_FEE;
	public static int CH_MPREG5_FEE;
	public static long CH_HPREG_FEE_RATIO;
	public static int CH_HPREG1_FEE;
	public static int CH_HPREG2_FEE;
	public static int CH_HPREG3_FEE;
	public static int CH_HPREG4_FEE;
	public static int CH_HPREG5_FEE;
	public static int CH_HPREG6_FEE;
	public static int CH_HPREG7_FEE;
	public static int CH_HPREG8_FEE;
	public static int CH_HPREG9_FEE;
	public static int CH_HPREG10_FEE;
	public static int CH_HPREG11_FEE;
	public static int CH_HPREG12_FEE;
	public static int CH_HPREG13_FEE;
	public static long CH_EXPREG_FEE_RATIO;
	public static int CH_EXPREG1_FEE;
	public static int CH_EXPREG2_FEE;
	public static int CH_EXPREG3_FEE;
	public static int CH_EXPREG4_FEE;
	public static int CH_EXPREG5_FEE;
	public static int CH_EXPREG6_FEE;
	public static int CH_EXPREG7_FEE;
	public static long CH_ITEM_FEE_RATIO;
	public static int CH_ITEM1_FEE;
	public static int CH_ITEM2_FEE;
	public static int CH_ITEM3_FEE;
	public static long CH_CURTAIN_FEE_RATIO;
	public static int CH_CURTAIN1_FEE;
	public static int CH_CURTAIN2_FEE;
	public static long CH_FRONT_FEE_RATIO;
	public static int CH_FRONT1_FEE;
	public static int CH_FRONT2_FEE;

	// --------------------------------------------------
	// Castle Settings
	// --------------------------------------------------
	public static long CS_TELE_FEE_RATIO;
	public static int CS_TELE1_FEE;
	public static int CS_TELE2_FEE;
	public static long CS_MPREG_FEE_RATIO;
	public static int CS_MPREG1_FEE;
	public static int CS_MPREG2_FEE;
	public static int CS_MPREG3_FEE;
	public static int CS_MPREG4_FEE;
	public static long CS_HPREG_FEE_RATIO;
	public static int CS_HPREG1_FEE;
	public static int CS_HPREG2_FEE;
	public static int CS_HPREG3_FEE;
	public static int CS_HPREG4_FEE;
	public static int CS_HPREG5_FEE;
	public static long CS_EXPREG_FEE_RATIO;
	public static int CS_EXPREG1_FEE;
	public static int CS_EXPREG2_FEE;
	public static int CS_EXPREG3_FEE;
	public static int CS_EXPREG4_FEE;
	public static long CS_SUPPORT_FEE_RATIO;
	public static int CS_SUPPORT1_FEE;
	public static int CS_SUPPORT2_FEE;
	public static int CS_SUPPORT3_FEE;
	public static int CS_SUPPORT4_FEE;

	// --------------------------------------------------
	// Events settings
	// --------------------------------------------------

	/** Olympiad */
	public static int OLY_START_TIME;
	public static int OLY_MIN;
	public static long OLY_CPERIOD;
	public static long OLY_BATTLE;
	public static long OLY_WPERIOD;
	public static long OLY_VPERIOD;
	public static int OLY_WAIT_TIME;
	public static int OLY_WAIT_BATTLE;
	public static int OLY_WAIT_END;
	public static int OLY_START_POINTS;
	public static int OLY_WEEKLY_POINTS;
	public static int OLY_MIN_MATCHES;
	public static int OLY_CLASSED;
	public static int OLY_NONCLASSED;
	public static IntIntHolder[] OLY_CLASSED_REWARD;
	public static IntIntHolder[] OLY_NONCLASSED_REWARD;
	public static int OLY_GP_PER_POINT;
	public static int OLY_HERO_POINTS;
	public static int OLY_RANK1_POINTS;
	public static int OLY_RANK2_POINTS;
	public static int OLY_RANK3_POINTS;
	public static int OLY_RANK4_POINTS;
	public static int OLY_RANK5_POINTS;
	public static int OLY_MAX_POINTS;
	public static int OLY_DIVIDER_CLASSED;
	public static int OLY_DIVIDER_NON_CLASSED;
	public static boolean OLY_ANNOUNCE_GAMES;
	public static int OLY_ENCHANT_LIMIT;

	/** SevenSigns Festival */
	public static boolean SEVEN_SIGNS_BYPASS_PREREQUISITES;
	public static int FESTIVAL_MIN_PLAYER;
	public static int MAXIMUM_PLAYER_CONTRIB;
	public static long FESTIVAL_MANAGER_START;
	public static long FESTIVAL_LENGTH;
	public static long FESTIVAL_CYCLE_LENGTH;
	public static long FESTIVAL_FIRST_SPAWN;
	public static long FESTIVAL_FIRST_SWARM;
	public static long FESTIVAL_SECOND_SPAWN;
	public static long FESTIVAL_SECOND_SWARM;
	public static long FESTIVAL_CHEST_SPAWN;

	/** Four Sepulchers */
	public static int FS_TIME_ENTRY;
	public static int FS_TIME_END;
	public static int FS_PARTY_MEMBER_COUNT;

	/** dimensional rift */
	public static int RIFT_MIN_PARTY_SIZE;
	public static int RIFT_SPAWN_DELAY;
	public static int RIFT_MAX_JUMPS;
	public static int RIFT_AUTO_JUMPS_TIME_MIN;
	public static int RIFT_AUTO_JUMPS_TIME_MAX;
	public static int RIFT_ENTER_COST_RECRUIT;
	public static int RIFT_ENTER_COST_SOLDIER;
	public static int RIFT_ENTER_COST_OFFICER;
	public static int RIFT_ENTER_COST_CAPTAIN;
	public static int RIFT_ENTER_COST_COMMANDER;
	public static int RIFT_ENTER_COST_HERO;
	public static double RIFT_BOSS_ROOM_TIME_MULTIPLY;

	/** Lottery */
	public static int LOTTERY_PRIZE;
	public static int LOTTERY_TICKET_PRICE;
	public static double LOTTERY_5_NUMBER_RATE;
	public static double LOTTERY_4_NUMBER_RATE;
	public static double LOTTERY_3_NUMBER_RATE;
	public static int LOTTERY_2_AND_1_NUMBER_PRIZE;

	/** Fishing tournament */
	public static boolean ALLOW_FISH_CHAMPIONSHIP;
	public static int FISH_CHAMPIONSHIP_REWARD_ITEM;
	public static int FISH_CHAMPIONSHIP_REWARD_1;
	public static int FISH_CHAMPIONSHIP_REWARD_2;
	public static int FISH_CHAMPIONSHIP_REWARD_3;
	public static int FISH_CHAMPIONSHIP_REWARD_4;
	public static int FISH_CHAMPIONSHIP_REWARD_5;

	public static int EVENT_MEDAL_COUNT;
	public static int EVENT_MEDAL_CHANCE;

	public static int GLITTERING_MEDAL_COUNT;
	public static int GLITTERING_MEDAL_CHANCE;

	public static int STAR_CHANCE;
	public static int BEAD_CHANCE;
	public static int FIR_CHANCE;
	public static int FLOWER_CHANCE;

	public static int STAR_COUNT;
	public static int BEAD_COUNT;
	public static int FIR_COUNT;
	public static int FLOWER_COUNT;

	public static boolean EVENT_COMMANDS;

	public static boolean CTF_EVENT_ENABLED;
	public static String[] CTF_EVENT_INTERVAL;
	public static int CTF_EVENT_PARTICIPATION_TIME;
	public static int CTF_EVENT_RUNNING_TIME;
	public static String CTF_NPC_LOC_NAME;
	public static int CTF_EVENT_PARTICIPATION_NPC_ID;
	public static int CTF_EVENT_TEAM_1_HEADQUARTERS_ID;
	public static int CTF_EVENT_TEAM_2_HEADQUARTERS_ID;
	public static int CTF_EVENT_TEAM_1_FLAG;
	public static int CTF_EVENT_TEAM_2_FLAG;
	public static int CTF_EVENT_CAPTURE_SKILL;
	public static int[] CTF_EVENT_PARTICIPATION_NPC_COORDINATES = new int[4];
	public static int[] CTF_EVENT_PARTICIPATION_FEE = new int[2];
	public static int CTF_EVENT_MIN_PLAYERS_IN_TEAMS;
	public static int CTF_EVENT_MAX_PLAYERS_IN_TEAMS;
	public static int CTF_EVENT_RESPAWN_TELEPORT_DELAY;
	public static int CTF_EVENT_START_LEAVE_TELEPORT_DELAY;
	public static String CTF_EVENT_TEAM_1_NAME;
	public static int[] CTF_EVENT_TEAM_1_COORDINATES = new int[3];
	public static String CTF_EVENT_TEAM_2_NAME;
	public static int[] CTF_EVENT_TEAM_2_COORDINATES = new int[3];
	public static int[] CTF_EVENT_TEAM_1_FLAG_COORDINATES = new int[4];
	public static int[] CTF_EVENT_TEAM_2_FLAG_COORDINATES = new int[4];
	public static List<int[]> CTF_EVENT_REWARDS;
	public static boolean CTF_EVENT_TARGET_TEAM_MEMBERS_ALLOWED;
	public static boolean CTF_EVENT_SCROLL_ALLOWED;
	public static boolean CTF_EVENT_POTIONS_ALLOWED;
	public static boolean CTF_EVENT_SUMMON_BY_ITEM_ALLOWED;
	public static List<Integer> CTF_DOORS_IDS_TO_OPEN;
	public static List<Integer> CTF_DOORS_IDS_TO_CLOSE;
	public static boolean CTF_REWARD_TEAM_TIE;
	public static byte CTF_EVENT_MIN_LVL;
	public static byte CTF_EVENT_MAX_LVL;
	public static int CTF_EVENT_EFFECTS_REMOVAL;
	public static Map<Integer, Integer> CTF_EVENT_FIGHTER_BUFFS;
	public static Map<Integer, Integer> CTF_EVENT_MAGE_BUFFS;

	public static boolean DM_EVENT_ENABLED;
	public static String[] DM_EVENT_INTERVAL;
	public static Long DM_EVENT_PARTICIPATION_TIME;
	public static int DM_EVENT_RUNNING_TIME;
	public static String DM_NPC_LOC_NAME;
	public static int DM_EVENT_PARTICIPATION_NPC_ID;
	public static int[] DM_EVENT_PARTICIPATION_NPC_COORDINATES = new int[4];
	public static int[] DM_EVENT_PARTICIPATION_FEE = new int[2];
	public static int DM_EVENT_MIN_PLAYERS;
	public static int DM_EVENT_MAX_PLAYERS;
	public static int DM_EVENT_RESPAWN_TELEPORT_DELAY;
	public static int DM_EVENT_START_LEAVE_TELEPORT_DELAY;
	public static List<int[]> DM_EVENT_PLAYER_COORDINATES;
	public static Map<Integer, List<int[]>> DM_EVENT_REWARDS;
	public static int DM_REWARD_FIRST_PLAYERS;
	public static boolean DM_SHOW_TOP_RANK;
	public static int DM_TOP_RANK;
	public static boolean DM_EVENT_SCROLL_ALLOWED;
	public static boolean DM_EVENT_POTIONS_ALLOWED;
	public static boolean DM_EVENT_SUMMON_BY_ITEM_ALLOWED;
	public static List<Integer> DM_DOORS_IDS_TO_OPEN;
	public static List<Integer> DM_DOORS_IDS_TO_CLOSE;
	public static boolean DM_REWARD_PLAYERS_TIE;
	public static byte DM_EVENT_MIN_LVL;
	public static byte DM_EVENT_MAX_LVL;
	public static int DM_EVENT_EFFECTS_REMOVAL;
	public static Map<Integer, Integer> DM_EVENT_FIGHTER_BUFFS;
	public static Map<Integer, Integer> DM_EVENT_MAGE_BUFFS;
	public static String DISABLE_ID_CLASSES_STRING;
	public static List<Integer> DISABLE_ID_CLASSES;

	public static boolean LM_EVENT_ENABLED;
	public static String[] LM_EVENT_INTERVAL;
	public static Long LM_EVENT_PARTICIPATION_TIME;
	public static int LM_EVENT_RUNNING_TIME;
	public static String LM_NPC_LOC_NAME;
	public static int LM_EVENT_PARTICIPATION_NPC_ID;
	public static short LM_EVENT_PLAYER_CREDITS;
	public static int[] LM_EVENT_PARTICIPATION_NPC_COORDINATES = new int[4];
	public static int[] LM_EVENT_PARTICIPATION_FEE = new int[2];
	public static int LM_EVENT_MIN_PLAYERS;
	public static int LM_EVENT_MAX_PLAYERS;
	public static int LM_EVENT_RESPAWN_TELEPORT_DELAY;
	public static int LM_EVENT_START_LEAVE_TELEPORT_DELAY;
	public static List<int[]> LM_EVENT_PLAYER_COORDINATES;
	public static List<int[]> LM_EVENT_REWARDS;
	public static boolean LM_EVENT_SCROLL_ALLOWED;
	public static boolean LM_EVENT_POTIONS_ALLOWED;
	public static boolean LM_EVENT_SUMMON_BY_ITEM_ALLOWED;
	public static List<Integer> LM_DOORS_IDS_TO_OPEN;
	public static List<Integer> LM_DOORS_IDS_TO_CLOSE;
	public static boolean LM_REWARD_PLAYERS_TIE;
	public static byte LM_EVENT_MIN_LVL;
	public static byte LM_EVENT_MAX_LVL;
	public static int LM_EVENT_EFFECTS_REMOVAL;
	public static Map<Integer, Integer> LM_EVENT_FIGHTER_BUFFS;
	public static Map<Integer, Integer> LM_EVENT_MAGE_BUFFS;

	public static boolean TVT_EVENT_ENABLED;
	public static String[] TVT_EVENT_INTERVAL;
	public static int TVT_EVENT_PARTICIPATION_TIME;
	public static int TVT_EVENT_RUNNING_TIME;
	public static String TVT_NPC_LOC_NAME;
	public static int TVT_EVENT_PARTICIPATION_NPC_ID;
	public static int[] TVT_EVENT_PARTICIPATION_NPC_COORDINATES = new int[4];
	public static int[] TVT_EVENT_PARTICIPATION_FEE = new int[2];
	public static int TVT_EVENT_MIN_PLAYERS_IN_TEAMS;
	public static int TVT_EVENT_MAX_PLAYERS_IN_TEAMS;
	public static int TVT_EVENT_RESPAWN_TELEPORT_DELAY;
	public static int TVT_EVENT_START_LEAVE_TELEPORT_DELAY;
	public static String TVT_EVENT_TEAM_1_NAME;
	public static int[] TVT_EVENT_TEAM_1_COORDINATES = new int[3];
	public static String TVT_EVENT_TEAM_2_NAME;
	public static int[] TVT_EVENT_TEAM_2_COORDINATES = new int[3];
	public static List<int[]> TVT_EVENT_REWARDS;
	public static boolean TVT_EVENT_TARGET_TEAM_MEMBERS_ALLOWED;
	public static boolean TVT_EVENT_SCROLL_ALLOWED;
	public static boolean TVT_EVENT_POTIONS_ALLOWED;
	public static boolean TVT_EVENT_SUMMON_BY_ITEM_ALLOWED;
	public static List<Integer> TVT_DOORS_IDS_TO_OPEN;
	public static List<Integer> TVT_DOORS_IDS_TO_CLOSE;
	public static boolean TVT_REWARD_TEAM_TIE;
	public static byte TVT_EVENT_MIN_LVL;
	public static byte TVT_EVENT_MAX_LVL;
	public static int TVT_EVENT_EFFECTS_REMOVAL;
	public static Map<Integer, Integer> TVT_EVENT_FIGHTER_BUFFS;
	public static Map<Integer, Integer> TVT_EVENT_MAGE_BUFFS;
	public static boolean TVT_REWARD_PLAYER;
	public static String TVT_EVENT_ON_KILL;
	public static String DISABLE_ID_CLASSES_STRING_TVT;
	public static List<Integer> DISABLE_ID_CLASSES_TVT;
	public static boolean ALLOW_TVT_DLG;
	public static int TVT_EVENT_MAX_PARTICIPANTS_PER_IP;

	// --------------------------------------------------
	// GeoEngine
	// --------------------------------------------------

	/** Geodata */
	public static String GEODATA_PATH;
	public static GeoType GEODATA_TYPE;

	/** Path checking */
	public static int PART_OF_CHARACTER_HEIGHT;
	public static int MAX_OBSTACLE_HEIGHT;

	/** Path finding */
	public static String PATHFIND_BUFFERS;
	public static int MOVE_WEIGHT;
	public static int MOVE_WEIGHT_DIAG;
	public static int OBSTACLE_WEIGHT;
	public static int OBSTACLE_WEIGHT_DIAG;
	public static int HEURISTIC_WEIGHT;
	public static int HEURISTIC_WEIGHT_DIAG;
	public static int MAX_ITERATIONS;

	// --------------------------------------------------
	// HexID
	// --------------------------------------------------

	public static int SERVER_ID;
	public static byte[] HEX_ID;

	// --------------------------------------------------
	// Loginserver
	// --------------------------------------------------

	public static String LOGINSERVER_HOSTNAME;
	public static int LOGINSERVER_PORT;

	public static int LOGIN_TRY_BEFORE_BAN;
	public static int LOGIN_BLOCK_AFTER_BAN;
	public static boolean ACCEPT_NEW_GAMESERVER;

	public static boolean SHOW_LICENCE;

	public static boolean AUTO_CREATE_ACCOUNTS;

	public static boolean FLOOD_PROTECTION;
	public static int FAST_CONNECTION_LIMIT;
	public static int NORMAL_CONNECTION_TIME;
	public static int FAST_CONNECTION_TIME;
	public static int MAX_CONNECTION_PER_IP;

	// --------------------------------------------------
	// NPCs / Monsters
	// --------------------------------------------------

	/** Spawn */
	public static double SPAWN_MULTIPLIER;
	public static String[] SPAWN_EVENTS;

	/** Champion Mod */
	public static int CHAMPION_FREQUENCY;
	public static boolean CHAMPION_DEEPBLUE_DROP_RULES;
	public static int CHAMP_MIN_LVL;
	public static int CHAMP_MAX_LVL;
	public static int CHAMPION_HP;
	public static double CHAMPION_HP_REGEN;
	public static double CHAMPION_RATE_XP;
	public static double CHAMPION_RATE_SP;
	public static double PREMIUM_CHAMPION_RATE_XP;
	public static double PREMIUM_CHAMPION_RATE_SP;
	public static int CHAMPION_REWARDS;
	public static int PREMIUM_CHAMPION_REWARDS;
	public static int CHAMPION_ADENAS_REWARDS;
	public static int PREMIUM_CHAMPION_ADENAS_REWARDS;
	public static double CHAMPION_ATK;
	public static double CHAMPION_SPD_ATK;
	public static int CHAMPION_REWARD;
	public static int CHAMPION_REWARD_ID;
	public static int CHAMPION_REWARD_QTY;
	public static int CHAMPION_AURA;

	/** Class Master */
	public static boolean ALLOW_ENTIRE_TREE;
	public static ClassMasterSettings CLASS_MASTER_SETTINGS;

	/** Wedding Manager */
	public static int WEDDING_PRICE;
	public static boolean WEDDING_SAMESEX;
	public static boolean WEDDING_FORMALWEAR;

	/** Scheme Buffer */
	public static int BUFFER_MAX_SCHEMES;
	public static int BUFFER_STATIC_BUFF_COST;

	/** Wyvern Manager */
	public static int WYVERN_REQUIRED_LEVEL;
	public static int WYVERN_REQUIRED_CRYSTALS;

	/** Misc */
	public static boolean FREE_TELEPORT;
	public static int LVL_FREE_TELEPORT;
	public static boolean ANNOUNCE_MAMMON_SPAWN;
	public static boolean MOB_AGGRO_IN_PEACEZONE;
	public static boolean SHOW_NPC_LVL;
	public static boolean SHOW_NPC_CREST;
	public static boolean SHOW_SUMMON_CREST;

	/** Raid Boss */
	public static double RAID_HP_REGEN_MULTIPLIER;
	public static double RAID_MP_REGEN_MULTIPLIER;
	public static double RAID_DEFENCE_MULTIPLIER;
	public static int RAID_MINION_RESPAWN_TIMER;

	public static boolean RAID_DISABLE_CURSE;

	/** Grand Boss */
	public static int SPAWN_INTERVAL_ANTHARAS;
	public static int RANDOM_SPAWN_TIME_ANTHARAS;
	public static int WAIT_TIME_ANTHARAS;

	public static int SPAWN_INTERVAL_BAIUM;
	public static int RANDOM_SPAWN_TIME_BAIUM;

	public static int SPAWN_INTERVAL_FRINTEZZA;
	public static int RANDOM_SPAWN_TIME_FRINTEZZA;
	public static int WAIT_TIME_FRINTEZZA;

	public static int SPAWN_INTERVAL_SAILREN;
	public static int RANDOM_SPAWN_TIME_SAILREN;
	public static int WAIT_TIME_SAILREN;

	public static int SPAWN_INTERVAL_VALAKAS;
	public static int RANDOM_SPAWN_TIME_VALAKAS;
	public static int WAIT_TIME_VALAKAS;

	public static int SPAWN_INTERVAL_DR_CHAOS;
	public static int RANDOM_SPAWN_TIME_DR_CHAOS;

	// High Priestess van Halter
	public static int SPAWN_INTERVAL_HALTER;
	public static int RANDOM_SPAWN_TIME_HALTER;

	/** AI */
	public static boolean GUARD_ATTACK_AGGRO_MOB;
	public static int RANDOM_WALK_RATE;
	public static int MAX_DRIFT_RANGE;
	public static int MIN_NPC_ANIMATION;
	public static int MAX_NPC_ANIMATION;
	public static int MIN_MONSTER_ANIMATION;
	public static int MAX_MONSTER_ANIMATION;

	// --------------------------------------------------
	// Offline
	// --------------------------------------------------

	public static boolean OFFLINE_TRADE_ENABLE;
	public static boolean OFFLINE_CRAFT_ENABLE;
	public static boolean OFFLINE_MODE_IN_PEACE_ZONE;
	public static boolean OFFLINE_MODE_NO_DAMAGE;
	public static boolean RESTORE_OFFLINERS;
	public static int OFFLINE_MAX_DAYS;
	public static boolean OFFLINE_DISCONNECT_FINISHED;
	public static boolean OFFLINE_SLEEP_EFFECT;
	public static boolean RESTORE_STORE_ITEMS;

	// --------------------------------------------------
	// Players
	// --------------------------------------------------

	/** Misc */
	public static boolean EFFECT_CANCELING;
	public static double HP_REGEN_MULTIPLIER;
	public static double MP_REGEN_MULTIPLIER;
	public static double CP_REGEN_MULTIPLIER;
	public static int PLAYER_SPAWN_PROTECTION;
	public static int PLAYER_FAKEDEATH_UP_PROTECTION;
	public static double RESPAWN_RESTORE_HP;
	public static int MAX_PVTSTORE_SLOTS_DWARF;
	public static int MAX_PVTSTORE_SLOTS_OTHER;
	public static boolean DEEPBLUE_DROP_RULES;
	public static boolean ALLOW_DELEVEL;
	public static int DEATH_PENALTY_CHANCE;

	/** Inventory & WH */
	public static int INVENTORY_MAXIMUM_NO_DWARF;
	public static int INVENTORY_MAXIMUM_DWARF;
	public static int INVENTORY_MAXIMUM_PET;
	public static int MAX_ITEM_IN_PACKET;
	public static double WEIGHT_LIMIT;
	public static int WAREHOUSE_SLOTS_NO_DWARF;
	public static int WAREHOUSE_SLOTS_DWARF;
	public static int WAREHOUSE_SLOTS_CLAN;
	public static int FREIGHT_SLOTS;
	public static boolean REGION_BASED_FREIGHT;
	public static int FREIGHT_PRICE;

	/** Enchant */
	public static Map<Integer, Double> ENCHANT_CHANCE_WEAPON;
	public static Map<Integer, Double> ENCHANT_CHANCE_ARMOR;
	public static Map<Integer, Double> BLESSED_ENCHANT_CHANCE_WEAPON;
	public static Map<Integer, Double> BLESSED_ENCHANT_CHANCE_ARMOR;
	public static Map<Integer, Double> CRYSTAL_ENCHANT_CHANCE_WEAPON;
	public static Map<Integer, Double> CRYSTAL_ENCHANT_CHANCE_ARMOR;
	public static int ENCHANT_MAX_WEAPON;
	public static int ENCHANT_MAX_ARMOR;
	public static int ENCHANT_SAFE_MAX;
	public static int ENCHANT_SAFE_MAX_FULL;
	public static int ENCHANT_FAILED_VALUE;

	/** Augmentations */
	public static int AUGMENTATION_NG_SKILL_CHANCE;
	public static int AUGMENTATION_NG_GLOW_CHANCE;
	public static int AUGMENTATION_MID_SKILL_CHANCE;
	public static int AUGMENTATION_MID_GLOW_CHANCE;
	public static int AUGMENTATION_HIGH_SKILL_CHANCE;
	public static int AUGMENTATION_HIGH_GLOW_CHANCE;
	public static int AUGMENTATION_TOP_SKILL_CHANCE;
	public static int AUGMENTATION_TOP_GLOW_CHANCE;
	public static int AUGMENTATION_BASESTAT_CHANCE;

	/** Karma & PvP */
	public static boolean KARMA_PLAYER_CAN_SHOP;
	public static boolean KARMA_PLAYER_CAN_USE_GK;
	public static boolean KARMA_PLAYER_CAN_TELEPORT;
	public static boolean KARMA_PLAYER_CAN_TRADE;
	public static boolean KARMA_PLAYER_CAN_USE_WH;

	public static boolean KARMA_DROP_GM;
	public static boolean KARMA_AWARD_PK_KILL;
	public static int KARMA_PK_LIMIT;

	public static int[] KARMA_NONDROPPABLE_PET_ITEMS;
	public static int[] KARMA_NONDROPPABLE_ITEMS;

	public static int PVP_NORMAL_TIME;
	public static int PVP_PVP_TIME;

	/** Party */
	public static String PARTY_XP_CUTOFF_METHOD;
	public static int PARTY_XP_CUTOFF_LEVEL;
	public static double PARTY_XP_CUTOFF_PERCENT;
	public static int PARTY_RANGE;

	/** GMs & Admin Stuff */
	public static int DEFAULT_ACCESS_LEVEL;
	public static boolean GM_HERO_AURA;
	public static boolean GM_STARTUP_INVULNERABLE;
	public static boolean GM_STARTUP_INVISIBLE;
	public static boolean GM_STARTUP_BLOCK_ALL;
	public static boolean GM_STARTUP_AUTO_LIST;

	/** petitions */
	public static boolean PETITIONING_ALLOWED;
	public static int MAX_PETITIONS_PER_PLAYER;
	public static int MAX_PETITIONS_PENDING;

	/** Crafting **/
	public static boolean IS_CRAFTING_ENABLED;
	public static int DWARF_RECIPE_LIMIT;
	public static int COMMON_RECIPE_LIMIT;
	public static boolean BLACKSMITH_USE_RECIPES;

	/** Skills & Classes **/
	public static boolean AUTO_LEARN_SKILLS;
	public static int LVL_AUTO_LEARN_SKILLS;
	public static boolean MAGIC_FAILURES;
	public static int PERFECT_SHIELD_BLOCK_RATE;
	public static boolean LIFE_CRYSTAL_NEEDED;
	public static boolean SP_BOOK_NEEDED;
	public static boolean ES_SP_BOOK_NEEDED;
	public static boolean DIVINE_SP_BOOK_NEEDED;
	public static boolean SUBCLASS_WITHOUT_QUESTS;

	/** Buffs */
	public static boolean STORE_SKILL_COOLTIME;
	public static int MAX_BUFFS_AMOUNT;

	// --------------------------------------------------
	// Sieges
	// --------------------------------------------------

	public static int SIEGE_LENGTH;
	public static int MINIMUM_CLAN_LEVEL;
	public static int MAX_ATTACKERS_NUMBER;
	public static int MAX_DEFENDERS_NUMBER;
	public static int ATTACKERS_RESPAWN_DELAY;

	public static int CH_MINIMUM_CLAN_LEVEL;
	public static int CH_MAX_ATTACKERS_NUMBER;

	// --------------------------------------------------
	// Server
	// --------------------------------------------------

	public static String HOSTNAME;
	public static String GAMESERVER_HOSTNAME;
	public static int GAMESERVER_PORT;
	public static String GAMESERVER_LOGIN_HOSTNAME;
	public static int GAMESERVER_LOGIN_PORT;
	public static int REQUEST_ID;
	public static boolean ACCEPT_ALTERNATE_ID;
	public static boolean USE_BLOWFISH_CIPHER;

	/** Access to database */
	public static String DATABASE_URL;
	public static String DATABASE_LOGIN;
	public static String DATABASE_PASSWORD;
	public static int DATABASE_MAX_CONNECTIONS;

	public static String CNAME_TEMPLATE;
	public static String TITLE_TEMPLATE;
	public static String PET_NAME_TEMPLATE;

	/** serverList & Test */
	public static boolean SERVER_LIST_BRACKET;
	public static boolean SERVER_LIST_CLOCK;
	public static int SERVER_LIST_AGE;
	public static boolean SERVER_LIST_TESTSERVER;
	public static boolean SERVER_LIST_PVPSERVER;
	public static boolean SERVER_GMONLY;

	/** clients related */
	public static int DELETE_DAYS;
	public static int MAXIMUM_ONLINE_USERS;

	/** Auto-loot */
	public static boolean AUTO_LOOT;
	public static boolean AUTO_LOOT_HERBS;
	public static boolean AUTO_LOOT_RAID;

	/** Items Management */
	public static boolean ALLOW_DISCARDITEM;
	public static boolean MULTIPLE_ITEM_DROP;
	public static int HERB_AUTO_DESTROY_TIME;
	public static int ITEM_AUTO_DESTROY_TIME;
	public static int EQUIPABLE_ITEM_AUTO_DESTROY_TIME;
	public static Map<Integer, Integer> SPECIAL_ITEM_DESTROY_TIME;
	public static int PLAYER_DROPPED_ITEM_MULTIPLIER;

	/** Rate control */
	public static double RATE_XP;
	public static double RATE_SP;
	public static double RATE_PARTY_XP;
	public static double RATE_PARTY_SP;
	public static double RATE_DROP_CURRENCY;
	public static double RATE_DROP_ITEMS;
	public static double RATE_DROP_ITEMS_BY_RAID;
	public static double RATE_DROP_ITEMS_BY_GRAND;
	public static double RATE_DROP_SPOIL;

	public static double PREMIUM_RATE_XP;
	public static double PREMIUM_RATE_SP;
	public static double PREMIUM_RATE_DROP_CURRENCY;
	public static double PREMIUM_RATE_DROP_SPOIL;
	public static double PREMIUM_RATE_DROP_ITEMS;
	public static double PREMIUM_RATE_DROP_ITEMS_BY_RAID;
	public static double PREMIUM_RATE_DROP_ITEMS_BY_GRAND;

	public static double PREMIUM_RATE_QUEST_DROP;
	public static double PREMIUM_RATE_QUEST_REWARD;
	public static double PREMIUM_RATE_QUEST_REWARD_XP;
	public static double PREMIUM_RATE_QUEST_REWARD_SP;
	public static double PREMIUM_RATE_QUEST_REWARD_ADENA;

	public static double RATE_DROP_HERBS;
	public static int RATE_DROP_MANOR;

	public static double RATE_QUEST_DROP;
	public static double RATE_QUEST_REWARD;
	public static double RATE_QUEST_REWARD_XP;
	public static double RATE_QUEST_REWARD_SP;
	public static double RATE_QUEST_REWARD_ADENA;

	public static double RATE_KARMA_EXP_LOST;
	public static double RATE_SIEGE_GUARDS_PRICE;

	public static int PLAYER_DROP_LIMIT;
	public static int PLAYER_RATE_DROP;
	public static int PLAYER_RATE_DROP_ITEM;
	public static int PLAYER_RATE_DROP_EQUIP;
	public static int PLAYER_RATE_DROP_EQUIP_WEAPON;

	public static int KARMA_DROP_LIMIT;
	public static int KARMA_RATE_DROP;
	public static int KARMA_RATE_DROP_ITEM;
	public static int KARMA_RATE_DROP_EQUIP;
	public static int KARMA_RATE_DROP_EQUIP_WEAPON;

	public static double PET_XP_RATE;
	public static int PET_FOOD_RATE;
	public static double SINEATER_XP_RATE;

	/** Allow types */
	public static boolean ALLOW_FREIGHT;
	public static boolean ALLOW_WAREHOUSE;
	public static boolean ALLOW_WEAR;
	public static int WEAR_DELAY;
	public static int WEAR_PRICE;
	public static boolean ALLOW_LOTTERY;
	public static boolean ALLOW_WATER;
	public static boolean ALLOW_BOAT;
	public static boolean ALLOW_CURSED_WEAPONS;
	public static boolean ALLOW_MANOR;
	public static boolean ENABLE_FALLING_DAMAGE;

	/** Debug & Dev */
	public static boolean NO_SPAWNS;
	public static boolean DEVELOPER;
	public static boolean PACKET_HANDLER_DEBUG;

	/** Deadlock Detector */
	public static boolean DEADLOCK_DETECTOR;
	public static int DEADLOCK_CHECK_INTERVAL;
	public static boolean RESTART_ON_DEADLOCK;

	/** Logs */
	public static boolean LOG_CHAT;
	public static boolean LOG_ITEMS;
	public static boolean GMAUDIT;

	/** Community Board */
	public static boolean ENABLE_COMMUNITY_BOARD;
	public static String BBS_DEFAULT;

	/** Flood Protectors */

	public static int ROLL_DICE_TIME;
	public static int HERO_VOICE_TIME;
	public static int SUBCLASS_TIME;
	public static int DROP_ITEM_TIME;
	public static int SERVER_BYPASS_TIME;
	public static int MULTISELL_TIME;
	public static int MANUFACTURE_TIME;
	public static int MANOR_TIME;
	public static int SENDMAIL_TIME;
	public static int CHARACTER_SELECT_TIME;
	public static int GLOBAL_CHAT_TIME;
	public static int TRADE_CHAT_TIME;
	public static int SOCIAL_TIME;
	public static int ITEM_TIME;
	public static int ACTION_TIME;

	/** ThreadPool */
	public static int SCHEDULED_THREAD_POOL_COUNT;
	public static int THREADS_PER_SCHEDULED_THREAD_POOL;
	public static int INSTANT_THREAD_POOL_COUNT;
	public static int THREADS_PER_INSTANT_THREAD_POOL;

	/** Misc */
	public static boolean L2WALKER_PROTECTION;
	public static boolean SERVER_NEWS;
	public static int ZONE_TOWN;

	// --------------------------------------------------
	// Those "hidden" settings haven't configs to avoid admins to fuck their server
	// You still can experiment changing values here. But don't say I didn't warn
	// you.
	// --------------------------------------------------

	/** Reserve Host on LoginServerThread */
	public static boolean RESERVE_HOST_ON_LOGIN = false; // default false

	/** MMO settings */
	public static int SELECTOR_SLEEP_TIME = 20; // default 20
	public static int MAX_SEND_PER_PASS = 80; // default 80
	public static int MAX_READ_PER_PASS = 80; // default 80
	public static int HELPER_BUFFER_COUNT = 20; // default 20

	/** Infinity SS and Arrows */
	public static boolean INFINITY_SS;
	public static boolean INFINITY_ARROWS;

	public static boolean ENABLE_MENU_IN_BOOK;

	/** Olympiad Period */
	public static boolean OLY_USE_CUSTOM_PERIOD_SETTINGS;
	public static OlympiadPeriod OLY_PERIOD;
	public static int OLY_PERIOD_MULTIPLIER;

	public static boolean ENABLE_MODIFY_SKILL_DURATION;
	public static HashMap<Integer, Integer> SKILL_DURATION_LIST;

	public static String GLOBAL_CHAT;
	public static String TRADE_CHAT;
	public static int CHAT_ALL_LEVEL;
	public static int CHAT_TELL_LEVEL;
	public static int CHAT_SHOUT_LEVEL;
	public static int CHAT_TRADE_LEVEL;

	public static boolean ENABLE_MENU;
	public static boolean ENABLE_ONLINE_COMMAND;

	public static boolean ENABLE_RAIDBOSS_COMMAND;
	public static int RAID_BOSS_DROP_PAGE_LIMIT;
	public static String RAID_BOSS_DATE_FORMAT;
	public static String RAID_BOSS_IDS;
	public static List<Integer> LIST_RAID_BOSS_IDS;

	public static boolean BOTS_PREVENTION;
	public static int KILLS_COUNTER;
	public static int KILLS_COUNTER_RANDOMIZATION;
	public static int VALIDATION_TIME;
	public static int PUNISHMENT;
	public static int PUNISHMENT_TIME;

	public static boolean USE_PREMIUM_SERVICE;
	public static boolean ALTERNATE_DROP_LIST;
	public static boolean ENABLE_SKIPPING;

	public static boolean ATTACK_PTS;
	public static boolean SUBCLASS_SKILLS;
	public static boolean GAME_SUBCLASS_EVERYWHERE;

	public static boolean SHOW_NPC_INFO;
	public static boolean ALLOW_GRAND_BOSSES_TELEPORT;

	// chatfilter
	public static List<String> FILTER_LIST;

	public static boolean USE_SAY_FILTER;
	public static String CHAT_FILTER_CHARS;

	public static boolean CABAL_BUFFER;
	public static boolean SUPER_HASTE;

	public static String RESTRICTED_CHAR_NAMES;
	public static List<String> LIST_RESTRICTED_CHAR_NAMES = new ArrayList<>();

	public static int FAKE_ONLINE_AMOUNT;

	public static String BUFFS_CATEGORY;
	public static ArrayList<String> PREMIUM_BUFFS_CATEGORY = new ArrayList<>();

	public static boolean ANTIFEED_ENABLE;
	public static boolean ANTIFEED_DUALBOX;
	public static boolean ANTIFEED_DISCONNECTED_AS_DUALBOX;
	public static int ANTIFEED_INTERVAL;

	public static int DUALBOX_CHECK_MAX_PLAYERS_PER_IP;
	public static int DUALBOX_CHECK_MAX_OLYMPIAD_PARTICIPANTS_PER_IP;
	public static Map<Integer, Integer> DUALBOX_CHECK_WHITELIST;

	public static List<Integer> AUTO_LOOT_ITEM_IDS;

	/**
	 * Initialize {@link ExProperties} from specified configuration file.
	 *
	 * @param filename : File name to be loaded.
	 * @return ExProperties : Initialized {@link ExProperties}.
	 */
	public static final ExProperties initProperties(String filename) {
		final ExProperties result = new ExProperties();

		try {
			result.load(new File(filename));
		} catch (Exception e) {
			LOGGER.error("An error occured loading '{}' config.", e, filename);
		}

		return result;
	}

	/**
	 * Loads offline shop settings
	 */
	private static final void loadOfflineShop() {
		final ExProperties offline = initProperties(OFFLINE_FILE);
		OFFLINE_TRADE_ENABLE = offline.getProperty("OfflineTradeEnable", false);
		OFFLINE_CRAFT_ENABLE = offline.getProperty("OfflineCraftEnable", false);
		OFFLINE_MODE_IN_PEACE_ZONE = offline.getProperty("OfflineModeInPeaceZone", false);
		OFFLINE_MODE_NO_DAMAGE = offline.getProperty("OfflineModeNoDamage", false);
		RESTORE_OFFLINERS = offline.getProperty("RestoreOffliners", false);
		OFFLINE_MAX_DAYS = offline.getProperty("OfflineMaxDays", 10);
		OFFLINE_DISCONNECT_FINISHED = offline.getProperty("OfflineDisconnectFinished", true);
		OFFLINE_SLEEP_EFFECT = offline.getProperty("OfflineSleepEffect", true);
		RESTORE_STORE_ITEMS = offline.getProperty("RestoreStoreItems", false);
	}

	/**
	 * Loads clan and clan hall settings.
	 */
	private static final void loadClans() {
		final ExProperties clans = initProperties(CLANS_FILE);

		CLAN_JOIN_DAYS = clans.getProperty("DaysBeforeJoinAClan", 5);
		CLAN_CREATE_DAYS = clans.getProperty("DaysBeforeCreateAClan", 10);
		MAX_NUM_OF_CLANS_IN_ALLY = clans.getProperty("MaxNumOfClansInAlly", 3);
		CLAN_MEMBERS_FOR_WAR = clans.getProperty("ClanMembersForWar", 15);
		CLAN_WAR_PENALTY_WHEN_ENDED = clans.getProperty("ClanWarPenaltyWhenEnded", 5);
		CLAN_DISSOLVE_DAYS = clans.getProperty("DaysToPassToDissolveAClan", 7);
		ALLY_JOIN_DAYS_WHEN_LEAVED = clans.getProperty("DaysBeforeJoinAllyWhenLeaved", 1);
		ALLY_JOIN_DAYS_WHEN_DISMISSED = clans.getProperty("DaysBeforeJoinAllyWhenDismissed", 1);
		ACCEPT_CLAN_DAYS_WHEN_DISMISSED = clans.getProperty("DaysBeforeAcceptNewClanWhenDismissed", 1);
		CREATE_ALLY_DAYS_WHEN_DISSOLVED = clans.getProperty("DaysBeforeCreateNewAllyWhenDissolved", 10);
		MEMBERS_CAN_WITHDRAW_FROM_CLANWH = clans.getProperty("MembersCanWithdrawFromClanWH", false);

		MANOR_REFRESH_TIME = clans.getProperty("ManorRefreshTime", 20);
		MANOR_REFRESH_MIN = clans.getProperty("ManorRefreshMin", 0);
		MANOR_APPROVE_TIME = clans.getProperty("ManorApproveTime", 6);
		MANOR_APPROVE_MIN = clans.getProperty("ManorApproveMin", 0);
		MANOR_MAINTENANCE_MIN = clans.getProperty("ManorMaintenanceMin", 6);
		MANOR_SAVE_PERIOD_RATE = clans.getProperty("ManorSavePeriodRate", 2) * 3600000;

		CH_TELE_FEE_RATIO = clans.getProperty("ClanHallTeleportFunctionFeeRatio", 86400000L);
		CH_TELE1_FEE = clans.getProperty("ClanHallTeleportFunctionFeeLvl1", 7000);
		CH_TELE2_FEE = clans.getProperty("ClanHallTeleportFunctionFeeLvl2", 14000);
		CH_SUPPORT_FEE_RATIO = clans.getProperty("ClanHallSupportFunctionFeeRatio", 86400000L);
		CH_SUPPORT1_FEE = clans.getProperty("ClanHallSupportFeeLvl1", 17500);
		CH_SUPPORT2_FEE = clans.getProperty("ClanHallSupportFeeLvl2", 35000);
		CH_SUPPORT3_FEE = clans.getProperty("ClanHallSupportFeeLvl3", 49000);
		CH_SUPPORT4_FEE = clans.getProperty("ClanHallSupportFeeLvl4", 77000);
		CH_SUPPORT5_FEE = clans.getProperty("ClanHallSupportFeeLvl5", 147000);
		CH_SUPPORT6_FEE = clans.getProperty("ClanHallSupportFeeLvl6", 252000);
		CH_SUPPORT7_FEE = clans.getProperty("ClanHallSupportFeeLvl7", 259000);
		CH_SUPPORT8_FEE = clans.getProperty("ClanHallSupportFeeLvl8", 364000);
		CH_MPREG_FEE_RATIO = clans.getProperty("ClanHallMpRegenerationFunctionFeeRatio", 86400000L);
		CH_MPREG1_FEE = clans.getProperty("ClanHallMpRegenerationFeeLvl1", 14000);
		CH_MPREG2_FEE = clans.getProperty("ClanHallMpRegenerationFeeLvl2", 26250);
		CH_MPREG3_FEE = clans.getProperty("ClanHallMpRegenerationFeeLvl3", 45500);
		CH_MPREG4_FEE = clans.getProperty("ClanHallMpRegenerationFeeLvl4", 96250);
		CH_MPREG5_FEE = clans.getProperty("ClanHallMpRegenerationFeeLvl5", 140000);
		CH_HPREG_FEE_RATIO = clans.getProperty("ClanHallHpRegenerationFunctionFeeRatio", 86400000L);
		CH_HPREG1_FEE = clans.getProperty("ClanHallHpRegenerationFeeLvl1", 4900);
		CH_HPREG2_FEE = clans.getProperty("ClanHallHpRegenerationFeeLvl2", 5600);
		CH_HPREG3_FEE = clans.getProperty("ClanHallHpRegenerationFeeLvl3", 7000);
		CH_HPREG4_FEE = clans.getProperty("ClanHallHpRegenerationFeeLvl4", 8166);
		CH_HPREG5_FEE = clans.getProperty("ClanHallHpRegenerationFeeLvl5", 10500);
		CH_HPREG6_FEE = clans.getProperty("ClanHallHpRegenerationFeeLvl6", 12250);
		CH_HPREG7_FEE = clans.getProperty("ClanHallHpRegenerationFeeLvl7", 14000);
		CH_HPREG8_FEE = clans.getProperty("ClanHallHpRegenerationFeeLvl8", 15750);
		CH_HPREG9_FEE = clans.getProperty("ClanHallHpRegenerationFeeLvl9", 17500);
		CH_HPREG10_FEE = clans.getProperty("ClanHallHpRegenerationFeeLvl10", 22750);
		CH_HPREG11_FEE = clans.getProperty("ClanHallHpRegenerationFeeLvl11", 26250);
		CH_HPREG12_FEE = clans.getProperty("ClanHallHpRegenerationFeeLvl12", 29750);
		CH_HPREG13_FEE = clans.getProperty("ClanHallHpRegenerationFeeLvl13", 36166);
		CH_EXPREG_FEE_RATIO = clans.getProperty("ClanHallExpRegenerationFunctionFeeRatio", 86400000L);
		CH_EXPREG1_FEE = clans.getProperty("ClanHallExpRegenerationFeeLvl1", 21000);
		CH_EXPREG2_FEE = clans.getProperty("ClanHallExpRegenerationFeeLvl2", 42000);
		CH_EXPREG3_FEE = clans.getProperty("ClanHallExpRegenerationFeeLvl3", 63000);
		CH_EXPREG4_FEE = clans.getProperty("ClanHallExpRegenerationFeeLvl4", 105000);
		CH_EXPREG5_FEE = clans.getProperty("ClanHallExpRegenerationFeeLvl5", 147000);
		CH_EXPREG6_FEE = clans.getProperty("ClanHallExpRegenerationFeeLvl6", 163331);
		CH_EXPREG7_FEE = clans.getProperty("ClanHallExpRegenerationFeeLvl7", 210000);
		CH_ITEM_FEE_RATIO = clans.getProperty("ClanHallItemCreationFunctionFeeRatio", 86400000L);
		CH_ITEM1_FEE = clans.getProperty("ClanHallItemCreationFunctionFeeLvl1", 210000);
		CH_ITEM2_FEE = clans.getProperty("ClanHallItemCreationFunctionFeeLvl2", 490000);
		CH_ITEM3_FEE = clans.getProperty("ClanHallItemCreationFunctionFeeLvl3", 980000);
		CH_CURTAIN_FEE_RATIO = clans.getProperty("ClanHallCurtainFunctionFeeRatio", 86400000L);
		CH_CURTAIN1_FEE = clans.getProperty("ClanHallCurtainFunctionFeeLvl1", 2002);
		CH_CURTAIN2_FEE = clans.getProperty("ClanHallCurtainFunctionFeeLvl2", 2625);
		CH_FRONT_FEE_RATIO = clans.getProperty("ClanHallFrontPlatformFunctionFeeRatio", 86400000L);
		CH_FRONT1_FEE = clans.getProperty("ClanHallFrontPlatformFunctionFeeLvl1", 3031);
		CH_FRONT2_FEE = clans.getProperty("ClanHallFrontPlatformFunctionFeeLvl2", 9331);

		CS_TELE_FEE_RATIO = clans.getProperty("CastleTeleportFunctionFeeRatio", 604800000L);
		CS_TELE1_FEE = clans.getProperty("CastleTeleportFunctionFeeLvl1", 7000);
		CS_TELE2_FEE = clans.getProperty("CastleTeleportFunctionFeeLvl2", 14000);
		CS_SUPPORT_FEE_RATIO = clans.getProperty("CastleSupportFunctionFeeRatio", 86400000L);
		CS_SUPPORT1_FEE = clans.getProperty("CastleSupportFeeLvl1", 7000);
		CS_SUPPORT2_FEE = clans.getProperty("CastleSupportFeeLvl2", 21000);
		CS_SUPPORT3_FEE = clans.getProperty("CastleSupportFeeLvl3", 37000);
		CS_SUPPORT4_FEE = clans.getProperty("CastleSupportFeeLvl4", 52000);
		CS_MPREG_FEE_RATIO = clans.getProperty("CastleMpRegenerationFunctionFeeRatio", 86400000L);
		CS_MPREG1_FEE = clans.getProperty("CastleMpRegenerationFeeLvl1", 2000);
		CS_MPREG2_FEE = clans.getProperty("CastleMpRegenerationFeeLvl2", 6500);
		CS_MPREG3_FEE = clans.getProperty("CastleMpRegenerationFeeLvl3", 13750);
		CS_MPREG4_FEE = clans.getProperty("CastleMpRegenerationFeeLvl4", 20000);
		CS_HPREG_FEE_RATIO = clans.getProperty("CastleHpRegenerationFunctionFeeRatio", 86400000L);
		CS_HPREG1_FEE = clans.getProperty("CastleHpRegenerationFeeLvl1", 1000);
		CS_HPREG2_FEE = clans.getProperty("CastleHpRegenerationFeeLvl2", 1500);
		CS_HPREG3_FEE = clans.getProperty("CastleHpRegenerationFeeLvl3", 2250);
		CS_HPREG4_FEE = clans.getProperty("CastleHpRegenerationFeeLvl4", 3270);
		CS_HPREG5_FEE = clans.getProperty("CastleHpRegenerationFeeLvl5", 5166);
		CS_EXPREG_FEE_RATIO = clans.getProperty("CastleExpRegenerationFunctionFeeRatio", 86400000L);
		CS_EXPREG1_FEE = clans.getProperty("CastleExpRegenerationFeeLvl1", 9000);
		CS_EXPREG2_FEE = clans.getProperty("CastleExpRegenerationFeeLvl2", 15000);
		CS_EXPREG3_FEE = clans.getProperty("CastleExpRegenerationFeeLvl3", 21000);
		CS_EXPREG4_FEE = clans.getProperty("CastleExpRegenerationFeeLvl4", 30000);
	}

	private static final void loadDonate() {
		final ExProperties donate = initProperties(Config.DONATE_FILE);
		ENABLE_DONATE_INSTANCE = donate.getProperty("EnableInitGameserver", false);
		DONATE_BYPASS_TIME = donate.getProperty("FloodProtectionTime", 4200);
		ENABLE_DONATE_VOICED_COMMAND = donate.getProperty("EnableVoiceCommand", false);

		DONATE_ITEMID = donate.getProperty("DonateItemId", 4037);
		DONATE_ITEM_RECOVER_COUNT = donate.getProperty("RecoverItemCount", 4037);

	}

	private static final void loadBalance() {
		final ExProperties balance = initProperties(Config.BALANCE_FILE);
		ENABLE_BALANCE_ATTACK_TYPE = balance.getProperty("EnableBalancePhysical", false);
		ENABLE_BALANCE_LOGGER_PHYSICAL = balance.getProperty("EnableLoggerPhysical", false);

		ENABLE_BALANCE_MAGICAL_TYPE = balance.getProperty("EnableBalanceMagical", false);
		ENABLE_BALANCE_LOGGER_MAGICAL = balance.getProperty("EnableLoggerMagical", false);

	}

	/**
	 * Loads event settings.<br>
	 * Such as olympiad, seven signs festival, four sepulchures, dimensional rift,
	 * weddings, lottery, fishing championship.
	 */
	private static final void loadEvents() {
		final ExProperties events = initProperties(EVENTS_FILE);

		OLY_START_TIME = events.getProperty("OlyStartTime", 18);
		OLY_MIN = events.getProperty("OlyMin", 0);
		OLY_CPERIOD = events.getProperty("OlyCPeriod", 21600000L);
		OLY_BATTLE = events.getProperty("OlyBattle", 180000L);
		OLY_WPERIOD = events.getProperty("OlyWPeriod", 604800000L);
		OLY_VPERIOD = events.getProperty("OlyVPeriod", 86400000L);
		OLY_WAIT_TIME = events.getProperty("OlyWaitTime", 30);
		OLY_WAIT_BATTLE = events.getProperty("OlyWaitBattle", 60);
		OLY_WAIT_END = events.getProperty("OlyWaitEnd", 40);
		OLY_START_POINTS = events.getProperty("OlyStartPoints", 18);
		OLY_WEEKLY_POINTS = events.getProperty("OlyWeeklyPoints", 3);
		OLY_MIN_MATCHES = events.getProperty("OlyMinMatchesToBeClassed", 5);
		OLY_CLASSED = events.getProperty("OlyClassedParticipants", 5);
		OLY_NONCLASSED = events.getProperty("OlyNonClassedParticipants", 9);
		OLY_CLASSED_REWARD = events.parseIntIntList("OlyClassedReward", "6651-50");
		OLY_NONCLASSED_REWARD = events.parseIntIntList("OlyNonClassedReward", "6651-30");
		OLY_GP_PER_POINT = events.getProperty("OlyGPPerPoint", 1000);
		OLY_HERO_POINTS = events.getProperty("OlyHeroPoints", 300);
		OLY_RANK1_POINTS = events.getProperty("OlyRank1Points", 100);
		OLY_RANK2_POINTS = events.getProperty("OlyRank2Points", 75);
		OLY_RANK3_POINTS = events.getProperty("OlyRank3Points", 55);
		OLY_RANK4_POINTS = events.getProperty("OlyRank4Points", 40);
		OLY_RANK5_POINTS = events.getProperty("OlyRank5Points", 30);
		OLY_MAX_POINTS = events.getProperty("OlyMaxPoints", 10);
		OLY_DIVIDER_CLASSED = events.getProperty("OlyDividerClassed", 3);
		OLY_DIVIDER_NON_CLASSED = events.getProperty("OlyDividerNonClassed", 5);
		OLY_ANNOUNCE_GAMES = events.getProperty("OlyAnnounceGames", true);
		OLY_ENCHANT_LIMIT = events.getProperty("OlyMaxEnchant", -1);

		SEVEN_SIGNS_BYPASS_PREREQUISITES = events.getProperty("SevenSignsBypassPrerequisites", false);
		FESTIVAL_MIN_PLAYER = MathUtil.limit(events.getProperty("FestivalMinPlayer", 5), 2, 9);
		MAXIMUM_PLAYER_CONTRIB = events.getProperty("MaxPlayerContrib", 1000000);
		FESTIVAL_MANAGER_START = events.getProperty("FestivalManagerStart", 120000L);
		FESTIVAL_LENGTH = events.getProperty("FestivalLength", 1080000L);
		FESTIVAL_CYCLE_LENGTH = events.getProperty("FestivalCycleLength", 2280000L);
		FESTIVAL_FIRST_SPAWN = events.getProperty("FestivalFirstSpawn", 120000L);
		FESTIVAL_FIRST_SWARM = events.getProperty("FestivalFirstSwarm", 300000L);
		FESTIVAL_SECOND_SPAWN = events.getProperty("FestivalSecondSpawn", 540000L);
		FESTIVAL_SECOND_SWARM = events.getProperty("FestivalSecondSwarm", 720000L);
		FESTIVAL_CHEST_SPAWN = events.getProperty("FestivalChestSpawn", 900000L);

		FS_TIME_ENTRY = events.getProperty("EntryTime", 55);
		FS_TIME_END = events.getProperty("EndTime", 50);
		FS_PARTY_MEMBER_COUNT = MathUtil.limit(events.getProperty("NeededPartyMembers", 4), 2, 9);

		RIFT_MIN_PARTY_SIZE = events.getProperty("RiftMinPartySize", 2);
		RIFT_MAX_JUMPS = events.getProperty("MaxRiftJumps", 4);
		RIFT_SPAWN_DELAY = events.getProperty("RiftSpawnDelay", 10000);
		RIFT_AUTO_JUMPS_TIME_MIN = events.getProperty("AutoJumpsDelayMin", 480);
		RIFT_AUTO_JUMPS_TIME_MAX = events.getProperty("AutoJumpsDelayMax", 600);
		RIFT_ENTER_COST_RECRUIT = events.getProperty("RecruitCost", 18);
		RIFT_ENTER_COST_SOLDIER = events.getProperty("SoldierCost", 21);
		RIFT_ENTER_COST_OFFICER = events.getProperty("OfficerCost", 24);
		RIFT_ENTER_COST_CAPTAIN = events.getProperty("CaptainCost", 27);
		RIFT_ENTER_COST_COMMANDER = events.getProperty("CommanderCost", 30);
		RIFT_ENTER_COST_HERO = events.getProperty("HeroCost", 33);
		RIFT_BOSS_ROOM_TIME_MULTIPLY = events.getProperty("BossRoomTimeMultiply", 1.);

		LOTTERY_PRIZE = events.getProperty("LotteryPrize", 50000);
		LOTTERY_TICKET_PRICE = events.getProperty("LotteryTicketPrice", 2000);
		LOTTERY_5_NUMBER_RATE = events.getProperty("Lottery5NumberRate", 0.6);
		LOTTERY_4_NUMBER_RATE = events.getProperty("Lottery4NumberRate", 0.2);
		LOTTERY_3_NUMBER_RATE = events.getProperty("Lottery3NumberRate", 0.2);
		LOTTERY_2_AND_1_NUMBER_PRIZE = events.getProperty("Lottery2and1NumberPrize", 200);

		ALLOW_FISH_CHAMPIONSHIP = events.getProperty("AllowFishChampionship", true);
		FISH_CHAMPIONSHIP_REWARD_ITEM = events.getProperty("FishChampionshipRewardItemId", 57);
		FISH_CHAMPIONSHIP_REWARD_1 = events.getProperty("FishChampionshipReward1", 800000);
		FISH_CHAMPIONSHIP_REWARD_2 = events.getProperty("FishChampionshipReward2", 500000);
		FISH_CHAMPIONSHIP_REWARD_3 = events.getProperty("FishChampionshipReward3", 300000);
		FISH_CHAMPIONSHIP_REWARD_4 = events.getProperty("FishChampionshipReward4", 200000);
		FISH_CHAMPIONSHIP_REWARD_5 = events.getProperty("FishChampionshipReward5", 100000);

		EVENT_MEDAL_COUNT = events.getProperty("EventMedalCount", 1);
		EVENT_MEDAL_CHANCE = events.getProperty("EventMedalChance", 40);

		GLITTERING_MEDAL_COUNT = events.getProperty("GlitteringMedalCount", 1);
		GLITTERING_MEDAL_CHANCE = events.getProperty("GlitteringMedalChance", 2);

		STAR_CHANCE = events.getProperty("StarChance", 20);
		BEAD_CHANCE = events.getProperty("BeadChance", 20);
		FIR_CHANCE = events.getProperty("FirChance", 50);
		FLOWER_CHANCE = events.getProperty("FlowerChance", 5);

		STAR_COUNT = events.getProperty("StarCount", 1);
		BEAD_COUNT = events.getProperty("BeadCount", 1);
		FIR_COUNT = events.getProperty("FirCount", 1);
		FLOWER_COUNT = events.getProperty("FlowerCount", 1);

		EVENT_COMMANDS = events.getProperty("AllowEventCommands", false);

		CTF_EVENT_ENABLED = events.getProperty("CTFEventEnabled", false);
		CTF_EVENT_INTERVAL = events.getProperty("CTFEventInterval", "20:00").split(",");
		CTF_EVENT_PARTICIPATION_TIME = events.getProperty("CTFEventParticipationTime", 3600);
		CTF_EVENT_RUNNING_TIME = events.getProperty("CTFEventRunningTime", 1800);
		CTF_NPC_LOC_NAME = events.getProperty("CTFNpcLocName", "Giran Town");
		CTF_EVENT_PARTICIPATION_NPC_ID = events.getProperty("CTFEventParticipationNpcId", 0);
		CTF_EVENT_TEAM_1_HEADQUARTERS_ID = events.getProperty("CTFEventFirstTeamHeadquartersId", 0);
		CTF_EVENT_TEAM_2_HEADQUARTERS_ID = events.getProperty("CTFEventSecondTeamHeadquartersId", 0);
		CTF_EVENT_TEAM_1_FLAG = events.getProperty("CTFEventFirstTeamFlag", 0);
		CTF_EVENT_TEAM_2_FLAG = events.getProperty("CTFEventSecondTeamFlag", 0);
		CTF_EVENT_CAPTURE_SKILL = events.getProperty("CTFEventCaptureSkillId", 0);

		if (CTF_EVENT_PARTICIPATION_NPC_ID == 0) {
			CTF_EVENT_ENABLED = false;
			LOGGER.warn("CTFEventEngine[Config.load()]: invalid config property -> CTFEventParticipationNpcId");
		} else {
			String[] ctfNpcCoords = events.getProperty("CTFEventParticipationNpcCoordinates", "0,0,0").split(",");
			if (ctfNpcCoords.length < 3) {
				CTF_EVENT_ENABLED = false;
				LOGGER.warn(
						"CTFEventEngine[Config.load()]: invalid config property -> CTFEventParticipationNpcCoordinates");
			} else {
				CTF_EVENT_PARTICIPATION_NPC_COORDINATES = new int[4];
				CTF_EVENT_PARTICIPATION_NPC_COORDINATES[0] = Integer.parseInt(ctfNpcCoords[0]);
				CTF_EVENT_PARTICIPATION_NPC_COORDINATES[1] = Integer.parseInt(ctfNpcCoords[1]);
				CTF_EVENT_PARTICIPATION_NPC_COORDINATES[2] = Integer.parseInt(ctfNpcCoords[2]);
				if (ctfNpcCoords.length == 4)
					CTF_EVENT_PARTICIPATION_NPC_COORDINATES[3] = Integer.parseInt(ctfNpcCoords[3]);

				CTF_EVENT_REWARDS = new ArrayList<>();
				CTF_DOORS_IDS_TO_OPEN = new ArrayList<>();
				CTF_DOORS_IDS_TO_CLOSE = new ArrayList<>();
				CTF_EVENT_TEAM_1_COORDINATES = new int[3];
				CTF_EVENT_TEAM_2_COORDINATES = new int[3];

				CTF_EVENT_MIN_PLAYERS_IN_TEAMS = events.getProperty("CTFEventMinPlayersInTeams", 1);
				CTF_EVENT_MAX_PLAYERS_IN_TEAMS = events.getProperty("CTFEventMaxPlayersInTeams", 20);
				CTF_EVENT_MIN_LVL = Byte.parseByte(events.getProperty("CTFEventMinPlayerLevel", "1"));
				CTF_EVENT_MAX_LVL = Byte.parseByte(events.getProperty("CTFEventMaxPlayerLevel", "80"));
				CTF_EVENT_RESPAWN_TELEPORT_DELAY = events.getProperty("CTFEventRespawnTeleportDelay", 20);
				CTF_EVENT_START_LEAVE_TELEPORT_DELAY = events.getProperty("CTFEventStartLeaveTeleportDelay", 20);
				CTF_EVENT_EFFECTS_REMOVAL = events.getProperty("CTFEventEffectsRemoval", 0);
				CTF_EVENT_TEAM_1_NAME = events.getProperty("CTFEventTeam1Name", "Team1");
				CTF_EVENT_TEAM_2_NAME = events.getProperty("CTFEventTeam2Name", "Team2");
				ctfNpcCoords = events.getProperty("CTFEventTeam1Coordinates", "0,0,0").split(",");
				if (ctfNpcCoords.length < 3) {
					CTF_EVENT_ENABLED = false;
					LOGGER.warn("CTFEventEngine[Config.load()]: invalid config property -> CTFEventTeam1Coordinates");
				} else {
					CTF_EVENT_TEAM_1_COORDINATES[0] = Integer.parseInt(ctfNpcCoords[0]);
					CTF_EVENT_TEAM_1_COORDINATES[1] = Integer.parseInt(ctfNpcCoords[1]);
					CTF_EVENT_TEAM_1_COORDINATES[2] = Integer.parseInt(ctfNpcCoords[2]);
					ctfNpcCoords = events.getProperty("CTFEventTeam2Coordinates", "0,0,0").split(",");
					if (ctfNpcCoords.length < 3) {
						CTF_EVENT_ENABLED = false;
						LOGGER.warn(
								"CTFEventEngine[Config.load()]: invalid config property -> CTFEventTeam2Coordinates");
					} else {
						CTF_EVENT_TEAM_2_COORDINATES[0] = Integer.parseInt(ctfNpcCoords[0]);
						CTF_EVENT_TEAM_2_COORDINATES[1] = Integer.parseInt(ctfNpcCoords[1]);
						CTF_EVENT_TEAM_2_COORDINATES[2] = Integer.parseInt(ctfNpcCoords[2]);

						if (CTF_EVENT_TEAM_1_HEADQUARTERS_ID == 0) {
							CTF_EVENT_ENABLED = false;
							LOGGER.warn(
									"CTFEventEngine[Config.load()]: invalid config property -> CTFEventFirstTeamHeadquartersId");
						} else {
							ctfNpcCoords = events.getProperty("CTFEventTeam1FlagCoordinates", "0,0,0").split(",");
							if (ctfNpcCoords.length < 3) {
								CTF_EVENT_ENABLED = false;
								LOGGER.warn(
										"CTFEventEngine[Config.load()]: invalid config property -> CTFEventTeam1FlagCoordinates");
							} else {
								CTF_EVENT_TEAM_1_FLAG_COORDINATES = new int[4];
								CTF_EVENT_TEAM_1_FLAG_COORDINATES[0] = Integer.parseInt(ctfNpcCoords[0]);
								CTF_EVENT_TEAM_1_FLAG_COORDINATES[1] = Integer.parseInt(ctfNpcCoords[1]);
								CTF_EVENT_TEAM_1_FLAG_COORDINATES[2] = Integer.parseInt(ctfNpcCoords[2]);
								if (ctfNpcCoords.length == 4)
									CTF_EVENT_TEAM_1_FLAG_COORDINATES[3] = Integer.parseInt(ctfNpcCoords[3]);
							}

							if (CTF_EVENT_TEAM_2_HEADQUARTERS_ID == 0) {
								CTF_EVENT_ENABLED = false;
								LOGGER.warn(
										"CTFEventEngine[Config.load()]: invalid config property -> CTFEventSecondTeamHeadquartersId");
							} else {
								ctfNpcCoords = events.getProperty("CTFEventTeam2FlagCoordinates", "0,0,0").split(",");
								if (ctfNpcCoords.length < 3) {
									CTF_EVENT_ENABLED = false;
									LOGGER.warn(
											"CTFEventEngine[Config.load()]: invalid config property -> CTFEventTeam2FlagCoordinates");
								} else {
									CTF_EVENT_TEAM_2_FLAG_COORDINATES = new int[4];
									CTF_EVENT_TEAM_2_FLAG_COORDINATES[0] = Integer.parseInt(ctfNpcCoords[0]);
									CTF_EVENT_TEAM_2_FLAG_COORDINATES[1] = Integer.parseInt(ctfNpcCoords[1]);
									CTF_EVENT_TEAM_2_FLAG_COORDINATES[2] = Integer.parseInt(ctfNpcCoords[2]);
									if (ctfNpcCoords.length == 4)
										CTF_EVENT_TEAM_2_FLAG_COORDINATES[3] = Integer.parseInt(ctfNpcCoords[3]);
								}

								ctfNpcCoords = events.getProperty("CTFEventParticipationFee", "0,0").split(",");
								try {
									CTF_EVENT_PARTICIPATION_FEE[0] = Integer.parseInt(ctfNpcCoords[0]);
									CTF_EVENT_PARTICIPATION_FEE[1] = Integer.parseInt(ctfNpcCoords[1]);
								} catch (NumberFormatException nfe) {
									if (ctfNpcCoords.length > 0)
										LOGGER.warn(
												"CTFEventEngine[Config.load()]: invalid config property -> CTFEventParticipationFee");
								}
								ctfNpcCoords = events.getProperty("CTFEventReward", "57,100000").split(";");
								for (String reward : ctfNpcCoords) {
									String[] rewardSplit = reward.split(",");
									if (rewardSplit.length != 2)
										LOGGER.warn(
												"CTFEventEngine[Config.load()]: invalid config property -> CTFEventReward");
									else {
										try {
											CTF_EVENT_REWARDS.add(new int[] { Integer.parseInt(rewardSplit[0]),
													Integer.parseInt(rewardSplit[1]) });
										} catch (NumberFormatException nfe) {
											if (!reward.isEmpty()) {
												LOGGER.warn(
														"CTFEventEngine[Config.load()]: invalid config property -> CTFEventReward");
											}
										}
									}
								}
							}
						}

						CTF_EVENT_TARGET_TEAM_MEMBERS_ALLOWED = events.getProperty("CTFEventTargetTeamMembersAllowed",
								true);
						CTF_EVENT_SCROLL_ALLOWED = events.getProperty("CTFEventScrollsAllowed", false);
						CTF_EVENT_POTIONS_ALLOWED = events.getProperty("CTFEventPotionsAllowed", false);
						CTF_EVENT_SUMMON_BY_ITEM_ALLOWED = events.getProperty("CTFEventSummonByItemAllowed", false);
						CTF_REWARD_TEAM_TIE = events.getProperty("CTFRewardTeamTie", false);
						ctfNpcCoords = events.getProperty("CTFDoorsToOpen", "").split(";");
						for (String door : ctfNpcCoords) {
							try {
								CTF_DOORS_IDS_TO_OPEN.add(Integer.parseInt(door));
							} catch (NumberFormatException nfe) {
								if (!door.isEmpty())
									LOGGER.warn(
											"CTFEventEngine[Config.load()]: invalid config property -> CTFDoorsToOpen");
							}
						}

						ctfNpcCoords = events.getProperty("CTFDoorsToClose", "").split(";");
						for (String door : ctfNpcCoords) {
							try {
								CTF_DOORS_IDS_TO_CLOSE.add(Integer.parseInt(door));
							} catch (NumberFormatException nfe) {
								if (!door.isEmpty())
									LOGGER.warn(
											"CTFEventEngine[Config.load()]: invalid config property -> CTFDoorsToClose");
							}
						}

						ctfNpcCoords = events.getProperty("CTFEventFighterBuffs", "").split(";");
						if (!ctfNpcCoords[0].isEmpty()) {
							CTF_EVENT_FIGHTER_BUFFS = new HashMap<>(ctfNpcCoords.length);
							for (String skill : ctfNpcCoords) {
								String[] skillSplit = skill.split(",");
								if (skillSplit.length != 2)
									LOGGER.warn(
											"CTFEventEngine[Config.load()]: invalid config property -> CTFEventFighterBuffs");
								else {
									try {
										CTF_EVENT_FIGHTER_BUFFS.put(Integer.parseInt(skillSplit[0]),
												Integer.parseInt(skillSplit[1]));
									} catch (NumberFormatException nfe) {
										if (!skill.isEmpty())
											LOGGER.warn(
													"CTFEventEngine[Config.load()]: invalid config property -> CTFEventFighterBuffs");
									}
								}
							}
						}

						ctfNpcCoords = events.getProperty("CTFEventMageBuffs", "").split(";");
						if (!ctfNpcCoords[0].isEmpty()) {
							CTF_EVENT_MAGE_BUFFS = new HashMap<>(ctfNpcCoords.length);
							for (String skill : ctfNpcCoords) {
								String[] skillSplit = skill.split(",");
								if (skillSplit.length != 2) {
									LOGGER.warn(
											"CTFEventEngine[Config.load()]: invalid config property -> CTFEventMageBuffs");
								} else {
									try {
										CTF_EVENT_MAGE_BUFFS.put(Integer.parseInt(skillSplit[0]),
												Integer.parseInt(skillSplit[1]));
									} catch (NumberFormatException nfe) {
										if (!skill.isEmpty())
											LOGGER.warn(
													"CTFEventEngine[Config.load()]: invalid config property -> CTFEventMageBuffs");
									}
								}
							}
						}
					}
				}
			}
		}

		DM_EVENT_ENABLED = events.getProperty("DMEventEnabled", false);
		DM_EVENT_INTERVAL = events.getProperty("DMEventInterval", "8:00,14:00,20:00,2:00").split(",");
		String[] timeParticipation = events.getProperty("DMEventParticipationTime", "01:00:00").split(":");
		Long timeDM = 0L;
		timeDM += Long.parseLong(timeParticipation[0]) * 3600L;
		timeDM += Long.parseLong(timeParticipation[1]) * 60L;
		timeDM += Long.parseLong(timeParticipation[2]);
		DM_EVENT_PARTICIPATION_TIME = timeDM * 1000L;
		DM_EVENT_RUNNING_TIME = events.getProperty("DMEventRunningTime", 1800);
		DM_NPC_LOC_NAME = events.getProperty("DMNpcLocName", "Giran Town");
		DM_EVENT_PARTICIPATION_NPC_ID = events.getProperty("DMEventParticipationNpcId", 0);
		DM_SHOW_TOP_RANK = events.getProperty("DMShowTopRank", false);
		DM_TOP_RANK = events.getProperty("DMTopRank", 10);
		if (DM_EVENT_PARTICIPATION_NPC_ID == 0) {
			DM_EVENT_ENABLED = false;
			LOGGER.warn("DMEventEngine[Config.load()]: invalid config property -> DMEventParticipationNpcId");
		} else {
			String[] propertySplit = events.getProperty("DMEventParticipationNpcCoordinates", "0,0,0").split(",");
			if (propertySplit.length < 3) {
				DM_EVENT_ENABLED = false;
				LOGGER.warn(
						"DMEventEngine[Config.load()]: invalid config property -> DMEventParticipationNpcCoordinates");
			} else {
				if (DM_EVENT_ENABLED) {
					DM_EVENT_REWARDS = new HashMap<>();
					DM_DOORS_IDS_TO_OPEN = new ArrayList<>();
					DM_DOORS_IDS_TO_CLOSE = new ArrayList<>();
					DM_EVENT_PLAYER_COORDINATES = new ArrayList<>();

					DM_EVENT_PARTICIPATION_NPC_COORDINATES = new int[4];
					DM_EVENT_PARTICIPATION_NPC_COORDINATES[0] = Integer.parseInt(propertySplit[0]);
					DM_EVENT_PARTICIPATION_NPC_COORDINATES[1] = Integer.parseInt(propertySplit[1]);
					DM_EVENT_PARTICIPATION_NPC_COORDINATES[2] = Integer.parseInt(propertySplit[2]);

					if (propertySplit.length == 4)
						DM_EVENT_PARTICIPATION_NPC_COORDINATES[3] = Integer.parseInt(propertySplit[3]);
					DM_EVENT_MIN_PLAYERS = events.getProperty("DMEventMinPlayers", 1);
					DM_EVENT_MAX_PLAYERS = events.getProperty("DMEventMaxPlayers", 20);
					DM_EVENT_MIN_LVL = (byte) events.getProperty("DMEventMinPlayerLevel", 1);
					DM_EVENT_MAX_LVL = (byte) events.getProperty("DMEventMaxPlayerLevel", 80);
					DM_EVENT_RESPAWN_TELEPORT_DELAY = events.getProperty("DMEventRespawnTeleportDelay", 20);
					DM_EVENT_START_LEAVE_TELEPORT_DELAY = events.getProperty("DMEventStartLeaveTeleportDelay", 20);
					DM_EVENT_EFFECTS_REMOVAL = events.getProperty("DMEventEffectsRemoval", 0);

					propertySplit = events.getProperty("DMEventParticipationFee", "0,0").split(",");
					try {
						DM_EVENT_PARTICIPATION_FEE[0] = Integer.parseInt(propertySplit[0]);
						DM_EVENT_PARTICIPATION_FEE[1] = Integer.parseInt(propertySplit[1]);
					} catch (NumberFormatException nfe) {
						if (propertySplit.length > 0)
							LOGGER.warn(
									"DMEventEngine[Config.load()]: invalid config property -> DMEventParticipationFee");
					}

					DM_REWARD_FIRST_PLAYERS = events.getProperty("DMRewardFirstPlayers", 3);

					propertySplit = events.getProperty("DMEventReward", "57,100000;5575,5000|57,50000|57,25000")
							.split("\\|");
					int i = 1;
					if (DM_REWARD_FIRST_PLAYERS < propertySplit.length)
						LOGGER.warn(
								"DMEventEngine[Config.load()]: invalid config property -> DMRewardFirstPlayers < DMEventReward");
					else {
						for (String pos : propertySplit) {
							List<int[]> value = new ArrayList<>();
							String[] rewardSplit = pos.split("\\;");
							for (String rewards : rewardSplit) {
								String[] reward = rewards.split("\\,");
								if (reward.length != 2)
									LOGGER.warn(
											"DMEventEngine[Config.load()]: invalid config property -> DMEventReward \""
													+ pos + "\"");
								else {
									try {
										value.add(
												new int[] { Integer.parseInt(reward[0]), Integer.parseInt(reward[1]) });
									} catch (NumberFormatException nfe) {
										LOGGER.warn(
												"DMEventEngine[Config.load()]: invalid config property -> DMEventReward \""
														+ pos + "\"");
									}
								}

								try {
									if (value.isEmpty())
										DM_EVENT_REWARDS.put(i, DM_EVENT_REWARDS.get(i - 1));
									else
										DM_EVENT_REWARDS.put(i, value);
								} catch (Exception e) {
									LOGGER.warn(
											"DMEventEngine[Config.load()]: invalid config property -> DMEventReward array index out of bounds (1)");
									e.printStackTrace();
								}
								i++;
							}
						}

						int countPosRewards = DM_EVENT_REWARDS.size();
						if (countPosRewards < DM_REWARD_FIRST_PLAYERS) {
							for (i = countPosRewards + 1; i <= DM_REWARD_FIRST_PLAYERS; i++) {
								try {
									DM_EVENT_REWARDS.put(i, DM_EVENT_REWARDS.get(i - 1));
								} catch (Exception e) {
									LOGGER.warn(
											"DMEventEngine[Config.load()]: invalid config property -> DMEventReward array index out of bounds (2)");
									e.printStackTrace();
								}
							}
						}
					}

					propertySplit = events.getProperty("DMEventPlayerCoordinates", "0,0,0").split(";");
					for (String coordPlayer : propertySplit) {
						String[] coordSplit = coordPlayer.split(",");
						if (coordSplit.length != 3)
							LOGGER.warn(
									"DMEventEngine[Config.load()]: invalid config property -> DMEventPlayerCoordinates \""
											+ coordPlayer + "\"");
						else {
							try {
								DM_EVENT_PLAYER_COORDINATES.add(new int[] { Integer.parseInt(coordSplit[0]),
										Integer.parseInt(coordSplit[1]), Integer.parseInt(coordSplit[2]) });
							} catch (NumberFormatException nfe) {
								if (!coordPlayer.isEmpty())
									LOGGER.warn(
											"DMEventEngine[Config.load()]: invalid config property -> DMEventPlayerCoordinates \""
													+ coordPlayer + "\"");
							}
						}
					}

					DM_EVENT_SCROLL_ALLOWED = events.getProperty("DMEventScrollsAllowed", false);
					DM_EVENT_POTIONS_ALLOWED = events.getProperty("DMEventPotionsAllowed", false);
					DM_EVENT_SUMMON_BY_ITEM_ALLOWED = events.getProperty("DMEventSummonByItemAllowed", false);
					DM_REWARD_PLAYERS_TIE = events.getProperty("DMRewardPlayersTie", false);

					DISABLE_ID_CLASSES_STRING = events.getProperty("DMDisabledForClasses");
					DISABLE_ID_CLASSES = new ArrayList<>();
					for (String class_id : DISABLE_ID_CLASSES_STRING.split(","))
						DISABLE_ID_CLASSES.add(Integer.parseInt(class_id));

					propertySplit = events.getProperty("DMDoorsToOpen", "").split(";");
					for (String door : propertySplit) {
						try {
							DM_DOORS_IDS_TO_OPEN.add(Integer.parseInt(door));
						} catch (NumberFormatException nfe) {
							if (!door.isEmpty())
								LOGGER.warn("DMEventEngine[Config.load()]: invalid config property -> DMDoorsToOpen \""
										+ door + "\"");
						}
					}

					propertySplit = events.getProperty("DMDoorsToClose", "").split(";");
					for (String door : propertySplit) {
						try {
							DM_DOORS_IDS_TO_CLOSE.add(Integer.parseInt(door));
						} catch (NumberFormatException nfe) {
							if (!door.isEmpty())
								LOGGER.warn("DMEventEngine[Config.load()]: invalid config property -> DMDoorsToClose \""
										+ door + "\"");
						}
					}

					propertySplit = events.getProperty("DMEventFighterBuffs", "").split(";");
					if (!propertySplit[0].isEmpty()) {
						DM_EVENT_FIGHTER_BUFFS = new HashMap<>(propertySplit.length);
						for (String skill : propertySplit) {
							String[] skillSplit = skill.split(",");
							if (skillSplit.length != 2)
								LOGGER.warn(
										"DMEventEngine[Config.load()]: invalid config property -> DMEventFighterBuffs \""
												+ skill + "\"");
							else {
								try {
									DM_EVENT_FIGHTER_BUFFS.put(Integer.parseInt(skillSplit[0]),
											Integer.parseInt(skillSplit[1]));
								} catch (NumberFormatException nfe) {
									if (!skill.isEmpty())
										LOGGER.warn(
												"DMEventEngine[Config.load()]: invalid config property -> DMEventFighterBuffs \""
														+ skill + "\"");
								}
							}
						}
					}

					propertySplit = events.getProperty("DMEventMageBuffs", "").split(";");
					if (!propertySplit[0].isEmpty()) {
						DM_EVENT_MAGE_BUFFS = new HashMap<>(propertySplit.length);
						for (String skill : propertySplit) {
							String[] skillSplit = skill.split(",");
							if (skillSplit.length != 2)
								LOGGER.warn(
										"DMEventEngine[Config.load()]: invalid config property -> DMEventMageBuffs \""
												+ skill + "\"");
							else {
								try {
									DM_EVENT_MAGE_BUFFS.put(Integer.parseInt(skillSplit[0]),
											Integer.parseInt(skillSplit[1]));
								} catch (NumberFormatException nfe) {
									if (!skill.isEmpty())
										LOGGER.warn(
												"DMEventEngine[Config.load()]: invalid config property -> DMEventMageBuffs \""
														+ skill + "\"");
								}
							}
						}
					}
				}
			}
			LM_EVENT_ENABLED = events.getProperty("LMEventEnabled", false);
			LM_EVENT_INTERVAL = events.getProperty("LMEventInterval", "8:00,14:00,20:00,2:00").split(",");
			String[] timeParticipation2 = events.getProperty("LMEventParticipationTime", "01:00:00").split(":");
			Long timeLM = 0L;
			timeLM += Long.parseLong(timeParticipation2[0]) * 3600L;
			timeLM += Long.parseLong(timeParticipation2[1]) * 60L;
			timeLM += Long.parseLong(timeParticipation2[2]);
			LM_EVENT_PARTICIPATION_TIME = timeLM * 1000L;
			LM_EVENT_RUNNING_TIME = events.getProperty("LMEventRunningTime", 1800);
			LM_NPC_LOC_NAME = events.getProperty("LMNpcLocName", "Giran Town");
			LM_EVENT_PARTICIPATION_NPC_ID = events.getProperty("LMEventParticipationNpcId", 0);
			short credits = Short.parseShort(events.getProperty("LMEventPlayerCredits", "1"));
			LM_EVENT_PLAYER_CREDITS = (credits > 0 ? credits : 1);
			if (LM_EVENT_PARTICIPATION_NPC_ID == 0) {
				LM_EVENT_ENABLED = false;
				LOGGER.warn("LMEventEngine[Config.load()]: invalid config property -> LMEventParticipationNpcId");
			} else {
				String[] propertySplitLM = events.getProperty("LMEventParticipationNpcCoordinates", "0,0,0").split(",");
				if (propertySplitLM.length < 3) {
					LM_EVENT_ENABLED = false;
					LOGGER.warn(
							"LMEventEngine[Config.load()]: invalid config property -> LMEventParticipationNpcCoordinates");
				} else {
					if (LM_EVENT_ENABLED) {
						LM_EVENT_REWARDS = new ArrayList<>();
						LM_DOORS_IDS_TO_OPEN = new ArrayList<>();
						LM_DOORS_IDS_TO_CLOSE = new ArrayList<>();
						LM_EVENT_PLAYER_COORDINATES = new ArrayList<>();

						LM_EVENT_PARTICIPATION_NPC_COORDINATES = new int[4];
						LM_EVENT_PARTICIPATION_NPC_COORDINATES[0] = Integer.parseInt(propertySplitLM[0]);
						LM_EVENT_PARTICIPATION_NPC_COORDINATES[1] = Integer.parseInt(propertySplitLM[1]);
						LM_EVENT_PARTICIPATION_NPC_COORDINATES[2] = Integer.parseInt(propertySplitLM[2]);

						if (propertySplitLM.length == 4)
							LM_EVENT_PARTICIPATION_NPC_COORDINATES[3] = Integer.parseInt(propertySplitLM[3]);
						LM_EVENT_MIN_PLAYERS = events.getProperty("LMEventMinPlayers", 1);
						LM_EVENT_MAX_PLAYERS = events.getProperty("LMEventMaxPlayers", 20);
						LM_EVENT_MIN_LVL = (byte) events.getProperty("LMEventMinPlayerLevel", 1);
						LM_EVENT_MAX_LVL = (byte) events.getProperty("LMEventMaxPlayerLevel", 80);
						LM_EVENT_RESPAWN_TELEPORT_DELAY = events.getProperty("LMEventRespawnTeleportDelay", 20);
						LM_EVENT_START_LEAVE_TELEPORT_DELAY = events.getProperty("LMEventStartLeaveTeleportDelay", 20);
						LM_EVENT_EFFECTS_REMOVAL = events.getProperty("LMEventEffectsRemoval", 0);

						propertySplitLM = events.getProperty("LMEventParticipationFee", "0,0").split(",");
						try {
							LM_EVENT_PARTICIPATION_FEE[0] = Integer.parseInt(propertySplitLM[0]);
							LM_EVENT_PARTICIPATION_FEE[1] = Integer.parseInt(propertySplitLM[1]);
						} catch (NumberFormatException nfe) {
							if (propertySplitLM.length > 0)
								LOGGER.warn(
										"LMEventEngine[Config.load()]: invalid config property -> LMEventParticipationFee");
						}

						propertySplitLM = events.getProperty("LMEventReward", "57,100000;5575,5000").split("\\;");
						for (String reward : propertySplitLM) {
							String[] rewardSplit2 = reward.split("\\,");
							try {
								LM_EVENT_REWARDS.add(new int[] { Integer.parseInt(rewardSplit2[0]),
										Integer.parseInt(rewardSplit2[1]) });
							} catch (NumberFormatException nfe) {
								LOGGER.warn(
										"LMEventEngine[Config.load()]: invalid config property -> LM_EVENT_REWARDS");
							}
						}

						propertySplitLM = events.getProperty("LMEventPlayerCoordinates", "0,0,0").split(";");
						for (String coordPlayer : propertySplitLM) {
							String[] coordSplit = coordPlayer.split(",");
							if (coordSplit.length != 3)
								LOGGER.warn(
										"LMEventEngine[Config.load()]: invalid config property -> LMEventPlayerCoordinates \""
												+ coordPlayer + "\"");
							else {
								try {
									LM_EVENT_PLAYER_COORDINATES.add(new int[] { Integer.parseInt(coordSplit[0]),
											Integer.parseInt(coordSplit[1]), Integer.parseInt(coordSplit[2]) });
								} catch (NumberFormatException nfe) {
									if (!coordPlayer.isEmpty())
										LOGGER.warn(
												"LMEventEngine[Config.load()]: invalid config property -> LMEventPlayerCoordinates \""
														+ coordPlayer + "\"");
								}
							}
						}

						LM_EVENT_SCROLL_ALLOWED = events.getProperty("LMEventScrollsAllowed", false);
						LM_EVENT_POTIONS_ALLOWED = events.getProperty("LMEventPotionsAllowed", false);
						LM_EVENT_SUMMON_BY_ITEM_ALLOWED = events.getProperty("LMEventSummonByItemAllowed", false);
						LM_REWARD_PLAYERS_TIE = events.getProperty("LMRewardPlayersTie", false);

						DISABLE_ID_CLASSES_STRING = events.getProperty("LMDisabledForClasses");
						DISABLE_ID_CLASSES = new ArrayList<>();
						for (String class_id : DISABLE_ID_CLASSES_STRING.split(","))
							DISABLE_ID_CLASSES.add(Integer.parseInt(class_id));

						propertySplitLM = events.getProperty("LMDoorsToOpen", "").split(";");
						for (String door : propertySplitLM) {
							try {
								LM_DOORS_IDS_TO_OPEN.add(Integer.parseInt(door));
							} catch (NumberFormatException nfe) {
								if (!door.isEmpty())
									LOGGER.warn(
											"LMEventEngine[Config.load()]: invalid config property -> LMDoorsToOpen \""
													+ door + "\"");
							}
						}

						propertySplitLM = events.getProperty("LMDoorsToClose", "").split(";");
						for (String door : propertySplitLM) {
							try {
								LM_DOORS_IDS_TO_CLOSE.add(Integer.parseInt(door));
							} catch (NumberFormatException nfe) {
								if (!door.isEmpty())
									LOGGER.warn(
											"LMEventEngine[Config.load()]: invalid config property -> LMDoorsToClose \""
													+ door + "\"");
							}
						}

						propertySplitLM = events.getProperty("LMEventFighterBuffs", "").split(";");
						if (!propertySplitLM[0].isEmpty()) {
							LM_EVENT_FIGHTER_BUFFS = new HashMap<>(propertySplitLM.length);
							for (String skill : propertySplitLM) {
								String[] skillSplit = skill.split(",");
								if (skillSplit.length != 2)
									LOGGER.warn(
											"LMEventEngine[Config.load()]: invalid config property -> LMEventFighterBuffs \""
													+ skill + "\"");
								else {
									try {
										LM_EVENT_FIGHTER_BUFFS.put(Integer.parseInt(skillSplit[0]),
												Integer.parseInt(skillSplit[1]));
									} catch (NumberFormatException nfe) {
										if (!skill.isEmpty())
											LOGGER.warn(
													"LMEventEngine[Config.load()]: invalid config property -> LMEventFighterBuffs \""
															+ skill + "\"");
									}
								}
							}
						}

						propertySplitLM = events.getProperty("LMEventMageBuffs", "").split(";");
						if (!propertySplitLM[0].isEmpty()) {
							LM_EVENT_MAGE_BUFFS = new HashMap<>(propertySplitLM.length);
							for (String skill : propertySplitLM) {
								String[] skillSplit = skill.split(",");
								if (skillSplit.length != 2)
									LOGGER.warn(
											"LMEventEngine[Config.load()]: invalid config property -> LMEventMageBuffs \""
													+ skill + "\"");
								else {
									try {
										LM_EVENT_MAGE_BUFFS.put(Integer.parseInt(skillSplit[0]),
												Integer.parseInt(skillSplit[1]));
									} catch (NumberFormatException nfe) {
										if (!skill.isEmpty())
											LOGGER.warn(
													"LMEventEngine[Config.load()]: invalid config property -> LMEventMageBuffs \""
															+ skill + "\"");
									}
								}
							}
						}
					}
				}

				TVT_EVENT_ENABLED = events.getProperty("TvTEventEnabled", false);
				TVT_EVENT_INTERVAL = events.getProperty("TvTEventInterval", "20:00").split(",");
				TVT_EVENT_PARTICIPATION_TIME = events.getProperty("TvTEventParticipationTime", 3600);
				TVT_EVENT_RUNNING_TIME = events.getProperty("TvTEventRunningTime", 1800);
				TVT_NPC_LOC_NAME = events.getProperty("TvTNpcLocName", "Giran Town");
				TVT_EVENT_PARTICIPATION_NPC_ID = events.getProperty("TvTEventParticipationNpcId", 0);

				if (TVT_EVENT_PARTICIPATION_NPC_ID == 0) {
					TVT_EVENT_ENABLED = false;
					LOGGER.warn("TvTEventEngine: invalid config property -> TvTEventParticipationNpcId");
				} else {
					String[] propertySplitTvT = events.getProperty("TvTEventParticipationNpcCoordinates", "0,0,0")
							.split(",");
					if (propertySplitTvT.length < 3) {
						TVT_EVENT_ENABLED = false;
						LOGGER.warn("TvTEventEngine: invalid config property -> TvTEventParticipationNpcCoordinates");
					} else {
						TVT_EVENT_REWARDS = new ArrayList<>();
						TVT_DOORS_IDS_TO_OPEN = new ArrayList<>();
						TVT_DOORS_IDS_TO_CLOSE = new ArrayList<>();
						TVT_EVENT_PARTICIPATION_NPC_COORDINATES = new int[4];
						TVT_EVENT_TEAM_1_COORDINATES = new int[3];
						TVT_EVENT_TEAM_2_COORDINATES = new int[3];
						TVT_EVENT_PARTICIPATION_NPC_COORDINATES[0] = Integer.parseInt(propertySplitTvT[0]);
						TVT_EVENT_PARTICIPATION_NPC_COORDINATES[1] = Integer.parseInt(propertySplitTvT[1]);
						TVT_EVENT_PARTICIPATION_NPC_COORDINATES[2] = Integer.parseInt(propertySplitTvT[2]);
						if (propertySplitTvT.length == 4) {
							TVT_EVENT_PARTICIPATION_NPC_COORDINATES[3] = Integer.parseInt(propertySplitTvT[3]);
						}
						TVT_EVENT_MIN_PLAYERS_IN_TEAMS = events.getProperty("TvTEventMinPlayersInTeams", 1);
						TVT_EVENT_MAX_PLAYERS_IN_TEAMS = events.getProperty("TvTEventMaxPlayersInTeams", 20);
						TVT_EVENT_MIN_LVL = Byte.parseByte(events.getProperty("TvTEventMinPlayerLevel", "1"));
						TVT_EVENT_MAX_LVL = Byte.parseByte(events.getProperty("TvTEventMaxPlayerLevel", "80"));
						TVT_EVENT_RESPAWN_TELEPORT_DELAY = events.getProperty("TvTEventRespawnTeleportDelay", 20);
						TVT_EVENT_START_LEAVE_TELEPORT_DELAY = events.getProperty("TvTEventStartLeaveTeleportDelay",
								20);
						TVT_EVENT_EFFECTS_REMOVAL = events.getProperty("TvTEventEffectsRemoval", 0);
						TVT_EVENT_TEAM_1_NAME = events.getProperty("TvTEventTeam1Name", "Team1");
						propertySplitTvT = events.getProperty("TvTEventTeam1Coordinates", "0,0,0").split(",");
						if (propertySplitTvT.length < 3) {
							TVT_EVENT_ENABLED = false;
							LOGGER.warn("TvTEventEngine: invalid config property -> TvTEventTeam1Coordinates");
						} else {
							TVT_EVENT_TEAM_1_COORDINATES[0] = Integer.parseInt(propertySplitTvT[0]);
							TVT_EVENT_TEAM_1_COORDINATES[1] = Integer.parseInt(propertySplitTvT[1]);
							TVT_EVENT_TEAM_1_COORDINATES[2] = Integer.parseInt(propertySplitTvT[2]);
							TVT_EVENT_TEAM_2_NAME = events.getProperty("TvTEventTeam2Name", "Team2");
							propertySplitTvT = events.getProperty("TvTEventTeam2Coordinates", "0,0,0").split(",");
							if (propertySplitTvT.length < 3) {
								TVT_EVENT_ENABLED = false;
								LOGGER.warn("TvTEventEngine: invalid config property -> TvTEventTeam2Coordinates");
							} else {
								TVT_EVENT_TEAM_2_COORDINATES[0] = Integer.parseInt(propertySplitTvT[0]);
								TVT_EVENT_TEAM_2_COORDINATES[1] = Integer.parseInt(propertySplitTvT[1]);
								TVT_EVENT_TEAM_2_COORDINATES[2] = Integer.parseInt(propertySplitTvT[2]);
								propertySplitTvT = events.getProperty("TvTEventParticipationFee", "0,0").split(",");

								try {
									TVT_EVENT_PARTICIPATION_FEE[0] = Integer.parseInt(propertySplitTvT[0]);
									TVT_EVENT_PARTICIPATION_FEE[1] = Integer.parseInt(propertySplitTvT[1]);
								} catch (NumberFormatException nfe) {
									if (propertySplitTvT.length > 0) {
										LOGGER.warn(
												"TvTEventEngine: invalid config property -> TvTEventParticipationFee");
									}
								}

								propertySplitTvT = events.getProperty("TvTEventReward", "57,100000").split(";");
								for (String reward : propertySplitTvT) {
									String[] rewardSplit = reward.split(",");
									if (rewardSplit.length != 2)
										LOGGER.warn("TvTEventEngine: invalid config property -> TvTEventReward \""
												+ reward + "\"");
									else {
										try {
											TVT_EVENT_REWARDS.add(new int[] { Integer.parseInt(rewardSplit[0]),
													Integer.parseInt(rewardSplit[1]) });
										} catch (NumberFormatException nfe) {
											if (!reward.isEmpty())
												LOGGER.warn(
														"TvTEventEngine: invalid config property -> TvTEventReward \""
																+ reward + "\"");
										}
									}
								}

								TVT_EVENT_TARGET_TEAM_MEMBERS_ALLOWED = events
										.getProperty("TvTEventTargetTeamMembersAllowed", true);
								TVT_EVENT_SCROLL_ALLOWED = events.getProperty("TvTEventScrollsAllowed", false);
								TVT_EVENT_POTIONS_ALLOWED = events.getProperty("TvTEventPotionsAllowed", false);
								TVT_EVENT_SUMMON_BY_ITEM_ALLOWED = events.getProperty("TvTEventSummonByItemAllowed",
										false);
								TVT_REWARD_TEAM_TIE = events.getProperty("TvTRewardTeamTie", false);
								propertySplitTvT = events.getProperty("TvTDoorsToOpen", "").split(";");
								for (String door : propertySplitTvT) {
									try {
										TVT_DOORS_IDS_TO_OPEN.add(Integer.parseInt(door));
									} catch (NumberFormatException nfe) {
										if (!door.isEmpty())
											LOGGER.warn("TvTEventEngine: invalid config property -> TvTDoorsToOpen \""
													+ door + "\"");
									}
								}

								propertySplitTvT = events.getProperty("TvTDoorsToClose", "").split(";");
								for (String door : propertySplitTvT) {
									try {
										TVT_DOORS_IDS_TO_CLOSE.add(Integer.parseInt(door));
									} catch (NumberFormatException nfe) {
										if (!door.isEmpty())
											LOGGER.warn("TvTEventEngine: invalid config property -> TvTDoorsToClose \""
													+ door + "\"");
									}
								}

								propertySplitTvT = events.getProperty("TvTEventFighterBuffs", "").split(";");
								if (!propertySplitTvT[0].isEmpty()) {
									TVT_EVENT_FIGHTER_BUFFS = new HashMap<>(propertySplitTvT.length);
									for (String skill : propertySplitTvT) {
										String[] skillSplit = skill.split(",");
										if (skillSplit.length != 2)
											LOGGER.warn(
													"TvTEventEngine: invalid config property -> TvTEventFighterBuffs \""
															+ skill + "\"");
										else {
											try {
												TVT_EVENT_FIGHTER_BUFFS.put(Integer.parseInt(skillSplit[0]),
														Integer.parseInt(skillSplit[1]));
											} catch (NumberFormatException nfe) {
												if (!skill.isEmpty())
													LOGGER.warn(
															"TvTEventEngine: invalid config property -> TvTEventFighterBuffs \""
																	+ skill + "\"");
											}
										}
									}
								}

								propertySplitTvT = events.getProperty("TvTEventMageBuffs", "").split(";");
								if (!propertySplitTvT[0].isEmpty()) {
									TVT_EVENT_MAGE_BUFFS = new HashMap<>(propertySplitTvT.length);
									for (String skill : propertySplitTvT) {
										String[] skillSplit = skill.split(",");
										if (skillSplit.length != 2)
											LOGGER.warn(
													"TvTEventEngine: invalid config property -> TvTEventMageBuffs \""
															+ skill + "\"");
										else {
											try {
												TVT_EVENT_MAGE_BUFFS.put(Integer.parseInt(skillSplit[0]),
														Integer.parseInt(skillSplit[1]));
											} catch (NumberFormatException nfe) {
												if (!skill.isEmpty())
													LOGGER.warn(
															"TvTEventEngine: invalid config property -> TvTEventMageBuffs \""
																	+ skill + "\"");
											}
										}
									}
								}

								TVT_REWARD_PLAYER = events.getProperty("TvTRewardOnlyKillers", false);

								TVT_EVENT_ON_KILL = events.getProperty("TvTEventOnKill", "pmteam");
								DISABLE_ID_CLASSES_STRING_TVT = events.getProperty("TvTDisabledForClasses");
								DISABLE_ID_CLASSES_TVT = new ArrayList<>();
								for (String class_id : DISABLE_ID_CLASSES_STRING_TVT.split(","))
									DISABLE_ID_CLASSES_TVT.add(Integer.parseInt(class_id));

								ALLOW_TVT_DLG = events.getProperty("AllowDlgTvTInvite", false);
								TVT_EVENT_MAX_PARTICIPANTS_PER_IP = events.getProperty("TvTEventMaxParticipantsPerIP",
										0);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Loads geoengine settings.
	 */
	private static final void loadGeoengine() {
		final ExProperties geoengine = initProperties(GEOENGINE_FILE);

		GEODATA_PATH = geoengine.getProperty("GeoDataPath", "./data/geodata/");
		GEODATA_TYPE = Enum.valueOf(GeoType.class, geoengine.getProperty("GeoDataType", "L2OFF"));

		PART_OF_CHARACTER_HEIGHT = geoengine.getProperty("PartOfCharacterHeight", 75);
		MAX_OBSTACLE_HEIGHT = geoengine.getProperty("MaxObstacleHeight", 32);

		PATHFIND_BUFFERS = geoengine.getProperty("PathFindBuffers", "500x10;1000x10;3000x5;5000x3;10000x3");
		MOVE_WEIGHT = geoengine.getProperty("MoveWeight", 10);
		MOVE_WEIGHT_DIAG = geoengine.getProperty("MoveWeightDiag", 14);
		OBSTACLE_WEIGHT = geoengine.getProperty("ObstacleWeight", 30);
		OBSTACLE_WEIGHT_DIAG = (int) (OBSTACLE_WEIGHT * Math.sqrt(2));
		HEURISTIC_WEIGHT = geoengine.getProperty("HeuristicWeight", 12);
		HEURISTIC_WEIGHT_DIAG = geoengine.getProperty("HeuristicWeightDiag", 18);
		MAX_ITERATIONS = geoengine.getProperty("MaxIterations", 3500);
	}

	/**
	 * Loads hex ID settings.
	 */
	private static final void loadHexID() {
		final ExProperties hexid = initProperties(HEXID_FILE);

		SERVER_ID = Integer.parseInt(hexid.getProperty("ServerID"));
		HEX_ID = new BigInteger(hexid.getProperty("HexID"), 16).toByteArray();
	}

	/**
	 * Saves hex ID file.
	 *
	 * @param serverId : The ID of server.
	 * @param hexId    : The hex ID of server.
	 */
	public static final void saveHexid(int serverId, String hexId) {
		saveHexid(serverId, hexId, HEXID_FILE);
	}

	/**
	 * Saves hexID file.
	 *
	 * @param serverId : The ID of server.
	 * @param hexId    : The hexID of server.
	 * @param filename : The file name.
	 */
	public static final void saveHexid(int serverId, String hexId, String filename) {
		try {
			final File file = new File(filename);
			file.createNewFile();

			final Properties hexSetting = new Properties();
			hexSetting.setProperty("ServerID", String.valueOf(serverId));
			hexSetting.setProperty("HexID", hexId);

			try (OutputStream out = new FileOutputStream(file)) {
				hexSetting.store(out, "the hexID to auth into login");
			}
		} catch (Exception e) {
			LOGGER.error("Failed to save hex ID to '{}' file.", e, filename);
		}
	}

	/**
	 * Loads NPC settings.<br>
	 * Such as champion monsters, NPC buffer, class master, wyvern, raid bosses and
	 * grand bosses, AI.
	 */
	private static final void loadNpcs() {
		final ExProperties npcs = initProperties(NPCS_FILE);

		SPAWN_MULTIPLIER = npcs.getProperty("SpawnMultiplier", 1.);
		SPAWN_EVENTS = npcs.getProperty("SpawnEvents", new String[] { "extra_mob", "18age", "start_weapon", });

		CHAMPION_FREQUENCY = npcs.getProperty("ChampionFrequency", 0);
		CHAMPION_DEEPBLUE_DROP_RULES = npcs.getProperty("UseChampionDeepBlueDropRules", false);
		CHAMP_MIN_LVL = npcs.getProperty("ChampionMinLevel", 20);
		CHAMP_MAX_LVL = npcs.getProperty("ChampionMaxLevel", 70);
		CHAMPION_HP = npcs.getProperty("ChampionHp", 8);
		CHAMPION_HP_REGEN = npcs.getProperty("ChampionHpRegen", 1.);
		CHAMPION_RATE_XP = npcs.getProperty("ChampionRateXp", 1.);
		CHAMPION_RATE_SP = npcs.getProperty("ChampionRateSp", 1.);
		PREMIUM_CHAMPION_RATE_XP = npcs.getProperty("PremiumChampionRateXp", 1.);
		PREMIUM_CHAMPION_RATE_SP = npcs.getProperty("PremiumChampionRateSp", 1.);
		CHAMPION_REWARDS = npcs.getProperty("ChampionRewards", 1);
		PREMIUM_CHAMPION_REWARDS = npcs.getProperty("PremiumChampionRewards", 1);
		CHAMPION_ADENAS_REWARDS = npcs.getProperty("ChampionAdenasRewards", 1);
		PREMIUM_CHAMPION_ADENAS_REWARDS = npcs.getProperty("PremiumChampionAdenasRewards", 1);
		CHAMPION_ATK = npcs.getProperty("ChampionAtk", 1.);
		CHAMPION_SPD_ATK = npcs.getProperty("ChampionSpdAtk", 1.);
		CHAMPION_REWARD = npcs.getProperty("ChampionRewardItem", 0);
		CHAMPION_REWARD_ID = npcs.getProperty("ChampionRewardItemID", 6393);
		CHAMPION_REWARD_QTY = npcs.getProperty("ChampionRewardItemQty", 1);
		CHAMPION_AURA = npcs.getProperty("ChampionAura", 0);

		ALLOW_ENTIRE_TREE = npcs.getProperty("AllowEntireTree", false);
		CLASS_MASTER_SETTINGS = new ClassMasterSettings(npcs.getProperty("ConfigClassMaster"));

		WEDDING_PRICE = npcs.getProperty("WeddingPrice", 1000000);
		WEDDING_SAMESEX = npcs.getProperty("WeddingAllowSameSex", false);
		WEDDING_FORMALWEAR = npcs.getProperty("WeddingFormalWear", true);

		BUFFER_MAX_SCHEMES = npcs.getProperty("BufferMaxSchemesPerChar", 4);
		BUFFER_STATIC_BUFF_COST = npcs.getProperty("BufferStaticCostPerBuff", -1);

		WYVERN_REQUIRED_LEVEL = npcs.getProperty("RequiredStriderLevel", 55);
		WYVERN_REQUIRED_CRYSTALS = npcs.getProperty("RequiredCrystalsNumber", 10);

		FREE_TELEPORT = npcs.getProperty("FreeTeleport", false);
		LVL_FREE_TELEPORT = npcs.getProperty("LvlFreeTeleport", 40);
		ANNOUNCE_MAMMON_SPAWN = npcs.getProperty("AnnounceMammonSpawn", true);
		MOB_AGGRO_IN_PEACEZONE = npcs.getProperty("MobAggroInPeaceZone", true);
		SHOW_NPC_LVL = npcs.getProperty("ShowNpcLevel", false);
		SHOW_NPC_CREST = npcs.getProperty("ShowNpcCrest", false);
		SHOW_SUMMON_CREST = npcs.getProperty("ShowSummonCrest", false);

		RAID_HP_REGEN_MULTIPLIER = npcs.getProperty("RaidHpRegenMultiplier", 1.);
		RAID_MP_REGEN_MULTIPLIER = npcs.getProperty("RaidMpRegenMultiplier", 1.);
		RAID_DEFENCE_MULTIPLIER = npcs.getProperty("RaidDefenceMultiplier", 1.);
		RAID_MINION_RESPAWN_TIMER = npcs.getProperty("RaidMinionRespawnTime", 300000);

		RAID_DISABLE_CURSE = npcs.getProperty("DisableRaidCurse", false);

		SPAWN_INTERVAL_ANTHARAS = npcs.getProperty("AntharasSpawnInterval", 264);
		RANDOM_SPAWN_TIME_ANTHARAS = npcs.getProperty("AntharasRandomSpawn", 72);
		WAIT_TIME_ANTHARAS = npcs.getProperty("AntharasWaitTime", 30) * 60000;

		SPAWN_INTERVAL_BAIUM = npcs.getProperty("BaiumSpawnInterval", 168);
		RANDOM_SPAWN_TIME_BAIUM = npcs.getProperty("BaiumRandomSpawn", 48);

		SPAWN_INTERVAL_FRINTEZZA = npcs.getProperty("FrintezzaSpawnInterval", 48);
		RANDOM_SPAWN_TIME_FRINTEZZA = npcs.getProperty("FrintezzaRandomSpawn", 8);
		WAIT_TIME_FRINTEZZA = npcs.getProperty("FrintezzaWaitTime", 1) * 60000;

		SPAWN_INTERVAL_SAILREN = npcs.getProperty("SailrenSpawnInterval", 36);
		RANDOM_SPAWN_TIME_SAILREN = npcs.getProperty("SailrenRandomSpawn", 24);
		WAIT_TIME_SAILREN = npcs.getProperty("SailrenWaitTime", 5) * 60000;

		SPAWN_INTERVAL_VALAKAS = npcs.getProperty("ValakasSpawnInterval", 264);
		RANDOM_SPAWN_TIME_VALAKAS = npcs.getProperty("ValakasRandomSpawn", 72);
		WAIT_TIME_VALAKAS = npcs.getProperty("ValakasWaitTime", 30) * 60000;

		SPAWN_INTERVAL_DR_CHAOS = npcs.getProperty("DrChaosSpawnInterval", 36);
		RANDOM_SPAWN_TIME_DR_CHAOS = npcs.getProperty("DrChaosRandomSpawn", 24);

		SPAWN_INTERVAL_HALTER = npcs.getProperty("HalterSpawnInterval", 36);
		RANDOM_SPAWN_TIME_HALTER = npcs.getProperty("HalterRandomSpawn", 24);

		GUARD_ATTACK_AGGRO_MOB = npcs.getProperty("GuardAttackAggroMob", false);
		RANDOM_WALK_RATE = npcs.getProperty("RandomWalkRate", 30);
		MAX_DRIFT_RANGE = npcs.getProperty("MaxDriftRange", 200);
		MIN_NPC_ANIMATION = npcs.getProperty("MinNPCAnimation", 20);
		MAX_NPC_ANIMATION = npcs.getProperty("MaxNPCAnimation", 40);
		MIN_MONSTER_ANIMATION = npcs.getProperty("MinMonsterAnimation", 10);
		MAX_MONSTER_ANIMATION = npcs.getProperty("MaxMonsterAnimation", 40);

		// -------------------- Buffs Master NPC -------------------

		BUFFS_MASTER_MAX_SKILLS_PER_SCHEME = npcs.getProperty("MaxSkillsPerScheme", 24);
		BUFFS_MASTER_STATIC_COST_PER_BUFF = npcs.getProperty("BufferStaticCostPerBuff", 0);
		BUFFS_MASTER_PAYMENT_ITEM = npcs.getProperty("BufferPaymentItem", 57);
		BUFFS_MASTER_PAYMENT_ITEM_NAME = npcs.getProperty("PaymentItemName", "");

		BUFFS_MASTER_CP_RESTORE_PRICE = npcs.getProperty("CPRestorePrice", 0);
		BUFFS_MASTER_HP_RESTORE_PRICE = npcs.getProperty("HPRestorePrice", 0);
		BUFFS_MASTER_MP_RESTORE_PRICE = npcs.getProperty("MPRestorePrice", 0);

		BUFFS_MASTER_CAN_USE_KARMA = npcs.getProperty("CanUseWithKarma", false);
		BUFFS_MASTER_CAN_USE_OUTSIDE_TOWN = npcs.getProperty("CanUseOutsideTown", false);
		BUFFS_MASTER_CAN_USE_IN_COMBAT = npcs.getProperty("CanUseInCombat", false);

		String warScheme = npcs.getProperty("WarriorScheme");
		String[] array = warScheme.split(";");
		BUFFS_MASTER_WARRIOR_SCHEME = new int[array.length];
		for (int i = 0; i < array.length; i++)
			BUFFS_MASTER_WARRIOR_SCHEME[i] = Integer.parseInt(array[i]);

		String mystScheme = npcs.getProperty("MysticScheme");
		String[] array2 = mystScheme.split(";");
		BUFFS_MASTER_MYSTIC_SCHEME = new int[array2.length];
		for (int i = 0; i < array2.length; i++)
			BUFFS_MASTER_MYSTIC_SCHEME[i] = Integer.parseInt(array2[i]);

		String healerScheme = npcs.getProperty("HealerScheme");
		String[] array3 = healerScheme.split(";");
		BUFFS_MASTER_HEALER_SCHEME = new int[array3.length];
		for (int i = 0; i < array3.length; i++)
			BUFFS_MASTER_HEALER_SCHEME[i] = Integer.parseInt(array3[i]);

		String tankerScheme = npcs.getProperty("TankerScheme");
		String[] array4 = tankerScheme.split(";");
		BUFFS_MASTER_TANKER_SCHEME = new int[array4.length];
		for (int i = 0; i < array4.length; i++)
			BUFFS_MASTER_TANKER_SCHEME[i] = Integer.parseInt(array4[i]);

		// ----------------------------------------------------------
		BLOCK_SELL_ITEMS_ADENA = Boolean.parseBoolean(npcs.getProperty("DisablePriceAdenaItems", "False"));
	}

	/**
	 * Loads player settings.<br>
	 * Such as stats, inventory/warehouse, enchant, augmentation, karma, party,
	 * admin, petition, skill learn.
	 */
	private static final void loadPlayers() {
		final ExProperties players = initProperties(PLAYERS_FILE);

		EFFECT_CANCELING = players.getProperty("CancelLesserEffect", true);
		HP_REGEN_MULTIPLIER = players.getProperty("HpRegenMultiplier", 1.);
		MP_REGEN_MULTIPLIER = players.getProperty("MpRegenMultiplier", 1.);
		CP_REGEN_MULTIPLIER = players.getProperty("CpRegenMultiplier", 1.);
		PLAYER_SPAWN_PROTECTION = players.getProperty("PlayerSpawnProtection", 0);
		PLAYER_FAKEDEATH_UP_PROTECTION = players.getProperty("PlayerFakeDeathUpProtection", 5);
		RESPAWN_RESTORE_HP = players.getProperty("RespawnRestoreHP", 0.7);
		MAX_PVTSTORE_SLOTS_DWARF = players.getProperty("MaxPvtStoreSlotsDwarf", 5);
		MAX_PVTSTORE_SLOTS_OTHER = players.getProperty("MaxPvtStoreSlotsOther", 4);
		DEEPBLUE_DROP_RULES = players.getProperty("UseDeepBlueDropRules", true);
		ALLOW_DELEVEL = players.getProperty("AllowDelevel", true);
		DEATH_PENALTY_CHANCE = players.getProperty("DeathPenaltyChance", 20);

		INVENTORY_MAXIMUM_NO_DWARF = players.getProperty("MaximumSlotsForNoDwarf", 80);
		INVENTORY_MAXIMUM_DWARF = players.getProperty("MaximumSlotsForDwarf", 100);
		INVENTORY_MAXIMUM_PET = players.getProperty("MaximumSlotsForPet", 12);
		MAX_ITEM_IN_PACKET = Math.max(INVENTORY_MAXIMUM_NO_DWARF, INVENTORY_MAXIMUM_DWARF);
		WEIGHT_LIMIT = players.getProperty("WeightLimit", 1.);
		WAREHOUSE_SLOTS_NO_DWARF = players.getProperty("MaximumWarehouseSlotsForNoDwarf", 100);
		WAREHOUSE_SLOTS_DWARF = players.getProperty("MaximumWarehouseSlotsForDwarf", 120);
		WAREHOUSE_SLOTS_CLAN = players.getProperty("MaximumWarehouseSlotsForClan", 150);
		FREIGHT_SLOTS = players.getProperty("MaximumFreightSlots", 20);
		REGION_BASED_FREIGHT = players.getProperty("RegionBasedFreight", true);
		FREIGHT_PRICE = players.getProperty("FreightPrice", 1000);

		ENCHANT_CHANCE_WEAPON = new HashMap<>();
		String[] property = players.getProperty("EnchantChanceWeapon", (String[]) null, ",");
		for (String data : property) {
			String[] enchant = data.split("-");
			ENCHANT_CHANCE_WEAPON.put(Integer.parseInt(enchant[0]), Double.parseDouble(enchant[1]));
		}
		ENCHANT_CHANCE_ARMOR = new HashMap<>();
		property = players.getProperty("EnchantChanceArmor", (String[]) null, ",");
		for (String data : property) {
			String[] enchant = data.split("-");
			ENCHANT_CHANCE_ARMOR.put(Integer.valueOf(Integer.parseInt(enchant[0])),
					Double.valueOf(Double.parseDouble(enchant[1])));
		}
		BLESSED_ENCHANT_CHANCE_WEAPON = new HashMap<>();
		property = players.getProperty("BlessedEnchantChanceWeapon", (String[]) null, ",");
		for (String data : property) {
			String[] enchant = data.split("-");
			BLESSED_ENCHANT_CHANCE_WEAPON.put(Integer.valueOf(Integer.parseInt(enchant[0])),
					Double.valueOf(Double.parseDouble(enchant[1])));
		}
		BLESSED_ENCHANT_CHANCE_ARMOR = new HashMap<>();
		property = players.getProperty("BlessedEnchantChanceArmor", (String[]) null, ",");
		for (String data : property) {
			String[] enchant = data.split("-");
			BLESSED_ENCHANT_CHANCE_ARMOR.put(Integer.valueOf(Integer.parseInt(enchant[0])),
					Double.valueOf(Double.parseDouble(enchant[1])));
		}
		CRYSTAL_ENCHANT_CHANCE_WEAPON = new HashMap<>();
		property = players.getProperty("CrystalEnchantChanceWeapon", (String[]) null, ",");
		for (String data : property) {
			String[] enchant = data.split("-");
			CRYSTAL_ENCHANT_CHANCE_WEAPON.put(Integer.valueOf(Integer.parseInt(enchant[0])),
					Double.valueOf(Double.parseDouble(enchant[1])));
		}
		CRYSTAL_ENCHANT_CHANCE_ARMOR = new HashMap<>();
		property = players.getProperty("CrystalEnchantChanceArmor", (String[]) null, ",");
		for (String data : property) {
			String[] enchant = data.split("-");
			CRYSTAL_ENCHANT_CHANCE_ARMOR.put(Integer.valueOf(Integer.parseInt(enchant[0])),
					Double.valueOf(Double.parseDouble(enchant[1])));
		}

		ENCHANT_MAX_WEAPON = players.getProperty("EnchantMaxWeapon", 0);
		ENCHANT_MAX_ARMOR = players.getProperty("EnchantMaxArmor", 0);
		ENCHANT_SAFE_MAX = players.getProperty("EnchantSafeMax", 3);
		ENCHANT_SAFE_MAX_FULL = players.getProperty("EnchantSafeMaxFull", 4);
		ENCHANT_FAILED_VALUE = players.getProperty("EnchantFailedValue", 0);

		AUGMENTATION_NG_SKILL_CHANCE = players.getProperty("AugmentationNGSkillChance", 15);
		AUGMENTATION_NG_GLOW_CHANCE = players.getProperty("AugmentationNGGlowChance", 0);
		AUGMENTATION_MID_SKILL_CHANCE = players.getProperty("AugmentationMidSkillChance", 30);
		AUGMENTATION_MID_GLOW_CHANCE = players.getProperty("AugmentationMidGlowChance", 40);
		AUGMENTATION_HIGH_SKILL_CHANCE = players.getProperty("AugmentationHighSkillChance", 45);
		AUGMENTATION_HIGH_GLOW_CHANCE = players.getProperty("AugmentationHighGlowChance", 70);
		AUGMENTATION_TOP_SKILL_CHANCE = players.getProperty("AugmentationTopSkillChance", 60);
		AUGMENTATION_TOP_GLOW_CHANCE = players.getProperty("AugmentationTopGlowChance", 100);
		AUGMENTATION_BASESTAT_CHANCE = players.getProperty("AugmentationBaseStatChance", 1);

		KARMA_PLAYER_CAN_SHOP = players.getProperty("KarmaPlayerCanShop", false);
		KARMA_PLAYER_CAN_USE_GK = players.getProperty("KarmaPlayerCanUseGK", false);
		KARMA_PLAYER_CAN_TELEPORT = players.getProperty("KarmaPlayerCanTeleport", true);
		KARMA_PLAYER_CAN_TRADE = players.getProperty("KarmaPlayerCanTrade", true);
		KARMA_PLAYER_CAN_USE_WH = players.getProperty("KarmaPlayerCanUseWareHouse", true);
		KARMA_DROP_GM = players.getProperty("CanGMDropEquipment", false);
		KARMA_AWARD_PK_KILL = players.getProperty("AwardPKKillPVPPoint", true);
		KARMA_PK_LIMIT = players.getProperty("MinimumPKRequiredToDrop", 5);
		KARMA_NONDROPPABLE_PET_ITEMS = players.getProperty("ListOfPetItems",
				new int[] { 2375, 3500, 3501, 3502, 4422, 4423, 4424, 4425, 6648, 6649, 6650 });
		KARMA_NONDROPPABLE_ITEMS = players.getProperty("ListOfNonDroppableItemsForPK",
				new int[] { 1147, 425, 1146, 461, 10, 2368, 7, 6, 2370, 2369 });

		PVP_NORMAL_TIME = players.getProperty("PvPVsNormalTime", 40000);
		PVP_PVP_TIME = players.getProperty("PvPVsPvPTime", 20000);

		PARTY_XP_CUTOFF_METHOD = players.getProperty("PartyXpCutoffMethod", "level");
		PARTY_XP_CUTOFF_PERCENT = players.getProperty("PartyXpCutoffPercent", 3.);
		PARTY_XP_CUTOFF_LEVEL = players.getProperty("PartyXpCutoffLevel", 20);
		PARTY_RANGE = players.getProperty("PartyRange", 1500);

		DEFAULT_ACCESS_LEVEL = players.getProperty("DefaultAccessLevel", 0);
		GM_HERO_AURA = players.getProperty("GMHeroAura", false);
		GM_STARTUP_INVULNERABLE = players.getProperty("GMStartupInvulnerable", false);
		GM_STARTUP_INVISIBLE = players.getProperty("GMStartupInvisible", false);
		GM_STARTUP_BLOCK_ALL = players.getProperty("GMStartupBlockAll", false);
		GM_STARTUP_AUTO_LIST = players.getProperty("GMStartupAutoList", true);

		PETITIONING_ALLOWED = players.getProperty("PetitioningAllowed", true);
		MAX_PETITIONS_PER_PLAYER = players.getProperty("MaxPetitionsPerPlayer", 5);
		MAX_PETITIONS_PENDING = players.getProperty("MaxPetitionsPending", 25);

		IS_CRAFTING_ENABLED = players.getProperty("CraftingEnabled", true);
		DWARF_RECIPE_LIMIT = players.getProperty("DwarfRecipeLimit", 50);
		COMMON_RECIPE_LIMIT = players.getProperty("CommonRecipeLimit", 50);
		BLACKSMITH_USE_RECIPES = players.getProperty("BlacksmithUseRecipes", true);

		AUTO_LEARN_SKILLS = players.getProperty("AutoLearnSkills", false);
		LVL_AUTO_LEARN_SKILLS = players.getProperty("LvlAutoLearnSkills", 40);
		MAGIC_FAILURES = players.getProperty("MagicFailures", true);
		PERFECT_SHIELD_BLOCK_RATE = players.getProperty("PerfectShieldBlockRate", 5);
		LIFE_CRYSTAL_NEEDED = players.getProperty("LifeCrystalNeeded", true);
		SP_BOOK_NEEDED = players.getProperty("SpBookNeeded", true);
		ES_SP_BOOK_NEEDED = players.getProperty("EnchantSkillSpBookNeeded", true);
		DIVINE_SP_BOOK_NEEDED = players.getProperty("DivineInspirationSpBookNeeded", true);
		SUBCLASS_WITHOUT_QUESTS = players.getProperty("SubClassWithoutQuests", false);

		MAX_BUFFS_AMOUNT = players.getProperty("MaxBuffsAmount", 20);
		STORE_SKILL_COOLTIME = players.getProperty("StoreSkillCooltime", true);

		RANDOM_CRAFT_ITEM_ID_CONSUME = players.getProperty("RandomCraftItemId", 57);

		RANDOM_CRAFT_ITEM_CONSUME_REFRESH = players.getProperty("RandomCraftConsumeRefresh", 50000);

		RANDOM_CRAFT_ITEM_CONSUME_CREATE = players.getProperty("RandromCraftConsumeCreate", 300000);

		ANNOUNCE_AIO = players.getProperty("AnnounceAioLogin", true);
		ENABLE_AIO_COIN = players.getProperty("EnableAioCoin", false);
		AIO_COIN_ID = players.getProperty("AioCoinId", 10);
		AIO_COIN_DAYS = players.getProperty("SetAioDays", 10);
		AIO_ITEM_ID = players.getProperty("RewardAioItemId", 10);
		AIO_SKILLS = new HashMap<>();
		for (String skillInfo : players.getProperty("AioSkills").split(";")) {
			String[] info = skillInfo.split(",");
			AIO_SKILLS.put(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
		}
	}

	private static final void loadNewbies() {
		final ExProperties newbie = initProperties(NEWBIE_FILE);
		NEWBIE_DIST = Integer.parseInt(newbie.getProperty("Dist", "80"));
		NEWBIE_LADO = Integer.parseInt(newbie.getProperty("Yaw", "80"));
		NEWBIE_ALTURA = Integer.parseInt(newbie.getProperty("Pitch", "80"));

		ENABLE_STARTUP = newbie.getProperty("StartupEnabled", true);

		String[] TelepropertySplit = newbie.getProperty("TeleToLocation", "0,0,0").split(",");
		if (TelepropertySplit.length < 3)
			System.out.println("NewbiesSystemEngine[Config.load()]: invalid config property -> TeleToLocation");
		else {
			TELE_TO_LOCATION[0] = Integer.parseInt(TelepropertySplit[0]);
			TELE_TO_LOCATION[1] = Integer.parseInt(TelepropertySplit[1]);
			TELE_TO_LOCATION[2] = Integer.parseInt(TelepropertySplit[2]);
		}
		NEWBIE_ITEMS_ENCHANT = Integer.parseInt(newbie.getProperty("EnchantItens", "4"));
	}

	private static final void loadBossEvent()
	{
		final ExProperties bossEvent = initProperties(BOSS_EVENT_INSTANCED);
		BOSS_EVENT_BY_TIME_OF_DAY = bossEvent.getProperty("EventTime", "20:00").split(",");
		for (String bossList : bossEvent.getProperty("BossList", "29046;29029").split(";"))
		{
			BOSS_EVENT_ID.add(Integer.parseInt(bossList));
		}
		bossEvent.getProperty("EventLocation", "174229,-88032,-5116").split(",");
		for (String locationsList : bossEvent.getProperty("LocationsList", "10468,-24569,-3645;174229,-88032,-5116").split(";"))
		{
			String[] coords = locationsList.split(",");
			int x = Integer.parseInt(coords[0]);
			int y = Integer.parseInt(coords[1]);
			int z = Integer.parseInt(coords[2]);
			BOSS_EVENT_LOCATION.add(new Location(x, y, z));
		}

		BOSS_EVENT_MIN_PLAYERS = Integer.parseInt(bossEvent.getProperty("MinPlayers", "1"));
		BOSS_EVENT_MIN_DAMAGE_TO_OBTAIN_REWARD = Integer.parseInt(bossEvent.getProperty("MinDamage", "2000"));
		BOSS_EVENT_REGISTRATION_TIME = Integer.parseInt(bossEvent.getProperty("RegistrationTime", "120"));
		BOSS_EVENT_REWARD_ID = Integer.parseInt(bossEvent.getProperty("RewardId", "3470"));
		BOSS_EVENT_REWARD_COUNT = Integer.parseInt(bossEvent.getProperty("RewardCount", "10"));
		BOSS_EVENT_TIME_TO_WAIT = Integer.parseInt(bossEvent.getProperty("WaitTime", "30"));
		BOSS_EVENT_TIME_TO_TELEPORT_PLAYERS = Integer.parseInt(bossEvent.getProperty("TeleportTime", "15"));
		BOSS_EVENT_REWARD_LAST_ATTACKER = Boolean.parseBoolean(bossEvent.getProperty("RewardLastAttacker", "true"));
		BOSS_EVENT_REWARD_MAIN_DAMAGE_DEALER = Boolean.parseBoolean(bossEvent.getProperty("RewardMainDamageDealer", "true"));
		for (String rewards : bossEvent.getProperty("GeneralRewards", "57,100000;3470,10").split(";"))
		{
			String[] reward = rewards.split(",");
			BOSS_EVENT_GENERAL_REWARDS.put(Integer.parseInt(reward[0]), Integer.parseInt(reward[1]));
		}
		for (String rewards : bossEvent.getProperty("MainDamageDealerRewards", "57,100000;3470,10").split(";"))
		{
			String[] reward = rewards.split(",");
			BOSS_EVENT_MAIN_DAMAGE_DEALER_REWARDS.put(Integer.parseInt(reward[0]), Integer.parseInt(reward[1]));
		}
		for (String rewards : bossEvent.getProperty("LastAttackerRewards", "57,100000;3470,10").split(";"))
		{
			String[] reward = rewards.split(",");
			BOSS_EVENT_LAST_ATTACKER_REWARDS.put(Integer.parseInt(reward[0]), Integer.parseInt(reward[1]));
		}
		BOSS_EVENT_REGISTRATION_NPC_ID = Integer.parseInt(bossEvent.getProperty("RegisterNpcID", "10003"));
		BOSS_EVENT_TIME_TO_DESPAWN_BOSS = Integer.parseInt(bossEvent.getProperty("TimeToDespawnBoss", "300"));
		String[] regLoc = bossEvent.getProperty("RegisterNpcLocation", "82727,148605,-3471").split(",");
		BOSS_EVENT_NPC_REGISTER_LOC = new Location(Integer.parseInt(regLoc[0]), Integer.parseInt(regLoc[1]), Integer.parseInt(regLoc[2]));
		BOSS_EVENT_TIME_ON_SCREEN = Boolean.parseBoolean(bossEvent.getProperty("EventTimeOnScreen", "true"));
		BOSS_EVENT_MAX_PLAYERS_PER_IP = Integer.parseInt(bossEvent.getProperty("MaxPlayersPerIP", "10"));
	}


	/**
	 * Loads siege settings.
	 */
	private static final void loadSieges() {
		final ExProperties sieges = initProperties(Config.SIEGE_FILE);

		SIEGE_LENGTH = sieges.getProperty("SiegeLength", 120);
		MINIMUM_CLAN_LEVEL = sieges.getProperty("SiegeClanMinLevel", 4);
		MAX_ATTACKERS_NUMBER = sieges.getProperty("AttackerMaxClans", 10);
		MAX_DEFENDERS_NUMBER = sieges.getProperty("DefenderMaxClans", 10);
		ATTACKERS_RESPAWN_DELAY = sieges.getProperty("AttackerRespawn", 10000);

		CH_MINIMUM_CLAN_LEVEL = sieges.getProperty("ChSiegeClanMinLevel", 4);
		CH_MAX_ATTACKERS_NUMBER = sieges.getProperty("ChAttackerMaxClans", 10);
	}

	/**
	 * Loads gameserver settings.<br>
	 * IP addresses, database, rates, feature enabled/disabled, misc.
	 */
	private static final void loadServer() {
		final ExProperties server = initProperties(SERVER_FILE);

		HOSTNAME = server.getProperty("Hostname", "*");
		GAMESERVER_HOSTNAME = server.getProperty("GameserverHostname");
		GAMESERVER_PORT = server.getProperty("GameserverPort", 7777);
		GAMESERVER_LOGIN_HOSTNAME = server.getProperty("LoginHost", "127.0.0.1");
		GAMESERVER_LOGIN_PORT = server.getProperty("LoginPort", 9014);
		REQUEST_ID = server.getProperty("RequestServerID", 0);
		ACCEPT_ALTERNATE_ID = server.getProperty("AcceptAlternateID", true);
		USE_BLOWFISH_CIPHER = server.getProperty("UseBlowfishCipher", true);

		DATABASE_URL = server.getProperty("URL", "jdbc:mariadb://localhost/acis");
		DATABASE_LOGIN = server.getProperty("Login", "root");
		DATABASE_PASSWORD = server.getProperty("Password", "");
		DATABASE_MAX_CONNECTIONS = server.getProperty("MaximumDbConnections", 10);

		CNAME_TEMPLATE = server.getProperty("CnameTemplate", ".*");
		TITLE_TEMPLATE = server.getProperty("TitleTemplate", ".*");
		PET_NAME_TEMPLATE = server.getProperty("PetNameTemplate", ".*");

		SERVER_LIST_BRACKET = server.getProperty("ServerListBrackets", false);
		SERVER_LIST_CLOCK = server.getProperty("ServerListClock", false);
		SERVER_GMONLY = server.getProperty("ServerGMOnly", false);
		SERVER_LIST_AGE = server.getProperty("ServerListAgeLimit", 0);
		SERVER_LIST_TESTSERVER = server.getProperty("TestServer", false);
		SERVER_LIST_PVPSERVER = server.getProperty("PvpServer", true);

		DELETE_DAYS = server.getProperty("DeleteCharAfterDays", 7);
		MAXIMUM_ONLINE_USERS = server.getProperty("MaximumOnlineUsers", 100);

		AUTO_LOOT = server.getProperty("AutoLoot", false);
		AUTO_LOOT_HERBS = server.getProperty("AutoLootHerbs", false);
		AUTO_LOOT_RAID = server.getProperty("AutoLootRaid", false);

		ALLOW_DISCARDITEM = server.getProperty("AllowDiscardItem", true);
		MULTIPLE_ITEM_DROP = server.getProperty("MultipleItemDrop", true);
		HERB_AUTO_DESTROY_TIME = server.getProperty("AutoDestroyHerbTime", 15) * 1000;
		ITEM_AUTO_DESTROY_TIME = server.getProperty("AutoDestroyItemTime", 600) * 1000;
		EQUIPABLE_ITEM_AUTO_DESTROY_TIME = server.getProperty("AutoDestroyEquipableItemTime", 0) * 1000;
		SPECIAL_ITEM_DESTROY_TIME = new HashMap<>();
		String[] data = server.getProperty("AutoDestroySpecialItemTime", (String[]) null, ",");
		if (data != null) {
			for (String itemData : data) {
				String[] item = itemData.split("-");
				SPECIAL_ITEM_DESTROY_TIME.put(Integer.parseInt(item[0]), Integer.parseInt(item[1]) * 1000);
			}
		}
		PLAYER_DROPPED_ITEM_MULTIPLIER = server.getProperty("PlayerDroppedItemMultiplier", 1);

		ALLOW_FREIGHT = server.getProperty("AllowFreight", true);
		ALLOW_WAREHOUSE = server.getProperty("AllowWarehouse", true);
		ALLOW_WEAR = server.getProperty("AllowWear", true);
		WEAR_DELAY = server.getProperty("WearDelay", 5);
		WEAR_PRICE = server.getProperty("WearPrice", 10);
		ALLOW_LOTTERY = server.getProperty("AllowLottery", true);
		ALLOW_WATER = server.getProperty("AllowWater", true);
		ALLOW_MANOR = server.getProperty("AllowManor", true);
		ALLOW_BOAT = server.getProperty("AllowBoat", true);
		ALLOW_CURSED_WEAPONS = server.getProperty("AllowCursedWeapons", true);

		ENABLE_FALLING_DAMAGE = server.getProperty("EnableFallingDamage", true);

		NO_SPAWNS = server.getProperty("NoSpawns", false);
		DEVELOPER = server.getProperty("Developer", false);
		PACKET_HANDLER_DEBUG = server.getProperty("PacketHandlerDebug", false);

		DEADLOCK_DETECTOR = server.getProperty("DeadLockDetector", false);
		DEADLOCK_CHECK_INTERVAL = server.getProperty("DeadLockCheckInterval", 20);
		RESTART_ON_DEADLOCK = server.getProperty("RestartOnDeadlock", false);

		LOG_CHAT = server.getProperty("LogChat", false);
		LOG_ITEMS = server.getProperty("LogItems", false);
		GMAUDIT = server.getProperty("GMAudit", false);

		ENABLE_COMMUNITY_BOARD = server.getProperty("EnableCommunityBoard", false);
		BBS_DEFAULT = server.getProperty("BBSDefault", "_bbshome");

		ROLL_DICE_TIME = server.getProperty("RollDiceTime", 4200);
		HERO_VOICE_TIME = server.getProperty("HeroVoiceTime", 10000);
		SUBCLASS_TIME = server.getProperty("SubclassTime", 2000);
		DROP_ITEM_TIME = server.getProperty("DropItemTime", 1000);
		SERVER_BYPASS_TIME = server.getProperty("ServerBypassTime", 100);
		MULTISELL_TIME = server.getProperty("MultisellTime", 100);
		MANUFACTURE_TIME = server.getProperty("ManufactureTime", 300);
		MANOR_TIME = server.getProperty("ManorTime", 3000);
		SENDMAIL_TIME = server.getProperty("SendMailTime", 10000);
		CHARACTER_SELECT_TIME = server.getProperty("CharacterSelectTime", 3000);
		GLOBAL_CHAT_TIME = server.getProperty("GlobalChatTime", 0);
		TRADE_CHAT_TIME = server.getProperty("TradeChatTime", 0);
		SOCIAL_TIME = server.getProperty("SocialTime", 2000);
		ITEM_TIME = server.getProperty("ItemTime", 100);
		ACTION_TIME = server.getProperty("ActionTime", 2000);

		SCHEDULED_THREAD_POOL_COUNT = server.getProperty("ScheduledThreadPoolCount", -1);
		THREADS_PER_SCHEDULED_THREAD_POOL = server.getProperty("ThreadsPerScheduledThreadPool", 4);
		INSTANT_THREAD_POOL_COUNT = server.getProperty("InstantThreadPoolCount", -1);
		THREADS_PER_INSTANT_THREAD_POOL = server.getProperty("ThreadsPerInstantThreadPool", 2);

		L2WALKER_PROTECTION = server.getProperty("L2WalkerProtection", false);
		ZONE_TOWN = server.getProperty("ZoneTown", 0);
		SERVER_NEWS = server.getProperty("ShowServerNews", false);
	}

	private static final void loadRates() {
		final ExProperties rates = initProperties(RATES_FILE);
		RATE_XP = rates.getProperty("RateXp", 1.);
		RATE_SP = rates.getProperty("RateSp", 1.);
		RATE_PARTY_XP = rates.getProperty("RatePartyXp", 1.);
		RATE_PARTY_SP = rates.getProperty("RatePartySp", 1.);
		RATE_DROP_CURRENCY = rates.getProperty("RateDropCurency", 1.);
		RATE_DROP_ITEMS = rates.getProperty("RateDropItems", 1.);
		RATE_DROP_ITEMS_BY_RAID = rates.getProperty("RateRaidDropItems", 1.);
		RATE_DROP_ITEMS_BY_GRAND = rates.getProperty("RateGrandDropItems", 1.);
		RATE_DROP_SPOIL = rates.getProperty("RateDropSpoil", 1.);

		PREMIUM_RATE_XP = rates.getProperty("PremiumRateXp", 2.);
		PREMIUM_RATE_SP = rates.getProperty("PremiumRateSp", 2.);
		PREMIUM_RATE_DROP_CURRENCY = rates.getProperty("PremiumRateDropCurency", 2.);
		PREMIUM_RATE_DROP_SPOIL = rates.getProperty("PremiumRateDropSpoil", 2.);
		PREMIUM_RATE_DROP_ITEMS = rates.getProperty("PremiumRateDropItems", 2.);
		PREMIUM_RATE_DROP_ITEMS_BY_RAID = rates.getProperty("PremiumRateRaidDropItems", 2.);
		PREMIUM_RATE_DROP_ITEMS_BY_GRAND = rates.getProperty("PremiumRateGrandDropItems", 2.);

		PREMIUM_RATE_QUEST_DROP = rates.getProperty("PremiumRateQuestDrop", 2.);
		PREMIUM_RATE_QUEST_REWARD = rates.getProperty("PremiumRateQuestReward", 2.);
		PREMIUM_RATE_QUEST_REWARD_XP = rates.getProperty("PremiumRateQuestRewardXP", 2.);
		PREMIUM_RATE_QUEST_REWARD_SP = rates.getProperty("PremiumRateQuestRewardSP", 2.);
		PREMIUM_RATE_QUEST_REWARD_ADENA = rates.getProperty("PremiumRateQuestRewardAdena", 2.);

		RATE_DROP_HERBS = rates.getProperty("RateDropHerbs", 1.);
		RATE_DROP_MANOR = rates.getProperty("RateDropManor", 1);
		RATE_QUEST_DROP = rates.getProperty("RateQuestDrop", 1.);
		RATE_QUEST_REWARD = rates.getProperty("RateQuestReward", 1.);
		RATE_QUEST_REWARD_XP = rates.getProperty("RateQuestRewardXP", 1.);
		RATE_QUEST_REWARD_SP = rates.getProperty("RateQuestRewardSP", 1.);
		RATE_QUEST_REWARD_ADENA = rates.getProperty("RateQuestRewardAdena", 1.);
		RATE_KARMA_EXP_LOST = rates.getProperty("RateKarmaExpLost", 1.);
		RATE_SIEGE_GUARDS_PRICE = rates.getProperty("RateSiegeGuardsPrice", 1.);
		PLAYER_DROP_LIMIT = rates.getProperty("PlayerDropLimit", 3);
		PLAYER_RATE_DROP = rates.getProperty("PlayerRateDrop", 5);
		PLAYER_RATE_DROP_ITEM = rates.getProperty("PlayerRateDropItem", 70);
		PLAYER_RATE_DROP_EQUIP = rates.getProperty("PlayerRateDropEquip", 25);
		PLAYER_RATE_DROP_EQUIP_WEAPON = rates.getProperty("PlayerRateDropEquipWeapon", 5);
		PET_XP_RATE = rates.getProperty("PetXpRate", 1.);
		PET_FOOD_RATE = rates.getProperty("PetFoodRate", 1);
		SINEATER_XP_RATE = rates.getProperty("SinEaterXpRate", 1.);
		KARMA_DROP_LIMIT = rates.getProperty("KarmaDropLimit", 10);
		KARMA_RATE_DROP = rates.getProperty("KarmaRateDrop", 70);
		KARMA_RATE_DROP_ITEM = rates.getProperty("KarmaRateDropItem", 50);
		KARMA_RATE_DROP_EQUIP = rates.getProperty("KarmaRateDropEquip", 40);
		KARMA_RATE_DROP_EQUIP_WEAPON = rates.getProperty("KarmaRateDropEquipWeapon", 10);
	}

	public static boolean DRESS_ME_ACTIVE_SKILLS;
	public static boolean DRESS_ME_ACTIVE_RESURRECT;
	public static int DRESS_ME_RESS_SKILL_TIME;
	public static int DRESS_ME_BUFFER_SKILL_ID;
	public static int DRESS_ME_BUFFER_SKILL_TIME;

	private static final void loadCustomQuestConfig() {
		final ExProperties quest = initProperties(CUSTOMQUESTS);
		IMPERIAL1 = quest.getProperty("Imperialreward1", 961);
		IMPERIAL2 = quest.getProperty("Imperialreward2", 961);
		IMPERIAL3 = quest.getProperty("Imperialreward3", 961);
		IMPERIAL4 = quest.getProperty("Imperialreward4", 961);
		IMPERIAL5 = quest.getProperty("Imperialreward5", 961);
		IMPERIAL6 = quest.getProperty("Imperialreward6", 961);
		IMPERIAL7 = quest.getProperty("Imperialreward7", 961);
		IMPERIAL8 = quest.getProperty("Imperialreward8", 961);
		IMPERIAL9 = quest.getProperty("Imperialreward9", 961);
		IMPERIAL10 = quest.getProperty("Imperialreward10", 961);
		IMPERIALSTABLE1 = quest.getProperty("ImperialStablereward1", 961);
		IMPERIALSTABLE2 = quest.getProperty("ImperialStablereward2", 961);
		RANDOM_AMMOUNT = quest.getProperty("RandomRewardAmount", 1);
		STABLE_AMOUNT1 = quest.getProperty("StableRewardAmount1", 1);
		STABLE_AMOUNT2 = quest.getProperty("StableRewardAmount2", 1);

		// Q617 GatheTheFlames

		FOG1 = quest.getProperty("FogReward1", 961);
		FOG2 = quest.getProperty("FogReward2", 961);
		FOG3 = quest.getProperty("FogReward3", 961);
		FOG4 = quest.getProperty("FogReward4", 961);
		FOG5 = quest.getProperty("FogReward5", 961);
		FOG6 = quest.getProperty("FogReward6", 961);
		FOG7 = quest.getProperty("FogReward7", 961);
		FOG8 = quest.getProperty("FogReward8", 961);
		FOG9 = quest.getProperty("FogReward9", 961);
		FOG10 = quest.getProperty("FogReward10", 961);
		FOGSTABLE1 = quest.getProperty("FogStablereward1", 961);
		FOGSTABLE2 = quest.getProperty("FogStablereward2", 961);
		FOG_RANDOM_AMMOUNT = quest.getProperty("FogRandomRewardAmount", 1);
		FOG_STABLE_AMOUNT1 = quest.getProperty("FogStableRewardAmount1", 1);
		FOG_STABLE_AMOUNT2 = quest.getProperty("FogStableRewardAmount2", 1);

	}

	private static final void load() {
		final ExProperties rusacis = initProperties(FILE_L2JHOST);

		ENABLE_MENU_IN_BOOK = rusacis.getProperty("EnableMenuBook", false);

		DRESS_ME_ACTIVE_SKILLS = rusacis.getProperty("AgathionSkillEnable", false);
		DRESS_ME_ACTIVE_RESURRECT = rusacis.getProperty("AgathionResurrectSkillEnable", false);
		DRESS_ME_RESS_SKILL_TIME = rusacis.getProperty("AgathionSkillTimeResurrect", 20);
		DRESS_ME_BUFFER_SKILL_TIME = rusacis.getProperty("AgathionBufferSkillTime", 20);
		DRESS_ME_BUFFER_SKILL_ID = rusacis.getProperty("AgathionBufferSkillId", 20);

		INFINITY_SS = rusacis.getProperty("InfinitySS", false);
		INFINITY_ARROWS = rusacis.getProperty("InfinityArrows", false);

		DISABLE_ATTACK_NPC_TYPE = rusacis.getProperty("DisableAttackToNpcs", false);
		ALLOWED_NPC_TYPES = rusacis.getProperty("AllowedNPCTypes");
		LIST_ALLOWED_NPC_TYPES = new ArrayList<>();
		for (String npc_type : ALLOWED_NPC_TYPES.split(","))
			LIST_ALLOWED_NPC_TYPES.add(npc_type);

		RAID_BOSS_IDS = rusacis.getProperty("RaidBossIds", "0,0");
		LIST_RAID_BOSS_IDS = new ArrayList<>();
		for (String val : RAID_BOSS_IDS.split(",")) {
			int npcId = Integer.parseInt(val);
			LIST_RAID_BOSS_IDS.add(npcId);
		}

		RAID_BOSS_DROP_PAGE_LIMIT = rusacis.getProperty("RaidBossDropPageLimit", 15);
		RAID_BOSS_DATE_FORMAT = rusacis.getProperty("RaidBossDateFormat", "MMM dd, HH:mm");

		ANNOUNCE_CASTLE_LORDS = rusacis.getProperty("AnnounceCastleLords", false);
		ANNOUNCE_LORDS_ENTER_BY_CLAN_MEMBER_MSG = rusacis.getProperty("AnnounceLordsLoginByClanMemberMsg",
				"The Lord %player% leader of %castle% of the clan %clan% is now online.");

		ANNOUNCE_KILL = rusacis.getProperty("AnnounceKill", false);
		ANNOUNCE_PVP_MSG = rusacis.getProperty("AnnouncePvpMsg", "$killer has defeated $target");
		ANNOUNCE_PK_MSG = rusacis.getProperty("AnnouncePkMsg", "$killer has slaughtered $target");

		ANNOUNCE_VIP_ENTER = rusacis.getProperty("AnnounceVipLogin", false);
		ANNOUNCE_VIP_ENTER_BY_CLAN_MEMBER_MSG = rusacis.getProperty("AnnounceVipLoginByClanMemberMsg",
				"The Vip %player% of the clan %clan% is now online.");
		ANNOUNCE_VIP_ENTER_BY_PLAYER_MSG = rusacis.getProperty("AnnounceVipLoginByPlayerMsg",
				"The Vip %player% is now online.");

		ANNOUNCE_HERO_ONLY_BASECLASS = rusacis.getProperty("AnnounceHero", false);
		ANNOUNCE_HERO_ENTER_BY_CLAN_MEMBER_MSG = rusacis.getProperty("AnnounceHeroLoginByClanMemberMsg",
				"The Hero %player% from %classe% and of the clan %clan% is now online.");
		ANNOUNCE_HERO_ENTER_BY_PLAYER_MSG = rusacis.getProperty("AnnounceHeroLoginByPlayerMsg",
				"The Hero %player% from %classe% is now online.");

		ANNOUNCE_TOP = rusacis.getProperty("AnnounceTopKiller", false);
		ANNOUNCE_TOP_PVP_ENTER_BY_CLAN_MEMBER_MSG = rusacis.getProperty("AnnounceTopPvPLoginByClanMemberMsg",
				"The Hero %player% from %classe% and of the clan %clan% is now online.");
		ANNOUNCE_TOP_PVP_ENTER_BY_PLAYER_MSG = rusacis.getProperty("AnnounceTopPvPLoginByPlayerMsg",
				"The Hero %player% from %classe% is now online.");
		ANNOUNCE_TOP_PK_ENTER_BY_CLAN_MEMBER_MSG = rusacis.getProperty("AnnounceTopPkLoginByClanMemberMsg",
				"The Hero %player% from %classe% and of the clan %clan% is now online.");
		ANNOUNCE_TOP_PK_ENTER_BY_PLAYER_MSG = rusacis.getProperty("AnnounceTopPkLoginByPlayerMsg",
				"The Hero %player% from %classe% is now online.");

		ENABLE_BOSS_DEFEATED_MSG = rusacis.getProperty("EnableBossDefeatedMsg", false);
		RAID_BOSS_DEFEATED_BY_CLAN_MEMBER_MSG = rusacis.getProperty("RaidBossDefeatedByClanMemberMsg",
				"Raid Boss %raidboss% has been defeated by %player% of clan %clan%.");
		RAID_BOSS_DEFEATED_BY_PLAYER_MSG = rusacis.getProperty("RaidBossDefeatedByPlayerMsg",
				"Raid Boss %raidboss% has been defeated by %player%.");
		GRAND_BOSS_DEFEATED_BY_CLAN_MEMBER_MSG = rusacis.getProperty("GrandBossDefeatedByClanMemberMsg",
				"Raid Boss %grandboss% has been defeated by %player% of clan %clan%.");
		GRAND_BOSS_DEFEATED_BY_PLAYER_MSG = rusacis.getProperty("GrandBossDefeatedByPlayerMsg",
				"Raid Boss %grandboss% has been defeated by %player%.");

		ALLOW_ANNOUNCE_ONLINE_PLAYERS = Boolean.parseBoolean(rusacis.getProperty("AllowAnnounceOnlinePlayers", "True"));
		ANNOUNCE_ONLINE_PLAYERS_DELAY = Integer.parseInt(rusacis.getProperty("AnnounceOnlinePlayersDelay", "300"));

		OLY_USE_CUSTOM_PERIOD_SETTINGS = rusacis.getProperty("OlyUseCustomPeriodSettings", false);
		OLY_PERIOD = OlympiadPeriod.valueOf(rusacis.getProperty("OlyPeriod", "MONTH"));
		OLY_PERIOD_MULTIPLIER = rusacis.getProperty("OlyPeriodMultiplier", 1);

		ENABLE_MODIFY_SKILL_DURATION = rusacis.getProperty("EnableModifySkillDuration", false);
		if (ENABLE_MODIFY_SKILL_DURATION) {
			SKILL_DURATION_LIST = new HashMap<>();
			String[] propertySplit = rusacis.getProperty("SkillDurationList", "").split(";");

			for (String skill : propertySplit) {
				String[] skillSplit = skill.split(",");
				if (skillSplit.length != 2)
					LOGGER.warn("[SkillDurationList]: invalid config property -> SkillDurationList \"" + skill + "\"");
				else {
					try {
						SKILL_DURATION_LIST.put(Integer.parseInt(skillSplit[0]), Integer.parseInt(skillSplit[1]));
					} catch (NumberFormatException nfe) {
						nfe.printStackTrace();

						if (!skill.equals(""))
							LOGGER.warn("[SkillDurationList]: invalid config property -> SkillList \"" + skillSplit[0]
									+ "\"" + skillSplit[1]);
					}
				}
			}
		}

		GLOBAL_CHAT = rusacis.getProperty("GlobalChat", "ON");
		TRADE_CHAT = rusacis.getProperty("TradeChat", "ON");
		CHAT_ALL_LEVEL = rusacis.getProperty("AllChatLevel", 1);
		CHAT_TELL_LEVEL = rusacis.getProperty("TellChatLevel", 1);
		CHAT_SHOUT_LEVEL = rusacis.getProperty("ShoutChatLevel", 1);
		CHAT_TRADE_LEVEL = rusacis.getProperty("TradeChatLevel", 1);

		ENABLE_MENU = rusacis.getProperty("EnableMenu", false);
		ENABLE_ONLINE_COMMAND = rusacis.getProperty("EnabledOnlineCommand", false);
		ENABLE_RAIDBOSS_COMMAND = rusacis.getProperty("EnabledRaidinfoCommand", false);

		BOTS_PREVENTION = rusacis.getProperty("EnableBotsPrevention", false);
		KILLS_COUNTER = rusacis.getProperty("KillsCounter", 60);
		KILLS_COUNTER_RANDOMIZATION = rusacis.getProperty("KillsCounterRandomization", 50);
		VALIDATION_TIME = rusacis.getProperty("ValidationTime", 60);
		PUNISHMENT = rusacis.getProperty("Punishment", 0);
		PUNISHMENT_TIME = rusacis.getProperty("PunishmentTime", 60);

		USE_PREMIUM_SERVICE = rusacis.getProperty("UsePremiumServices", false);
		ALTERNATE_DROP_LIST = rusacis.getProperty("AlternateDropList", false);
		ENABLE_SKIPPING = rusacis.getProperty("EnableSkippingItems", false);

		ATTACK_PTS = rusacis.getProperty("AttackPTS", true);
		SUBCLASS_SKILLS = rusacis.getProperty("SubClassSkills", false);
		GAME_SUBCLASS_EVERYWHERE = rusacis.getProperty("SubclassEverywhere", false);

		SHOW_NPC_INFO = rusacis.getProperty("ShowNpcInfo", false);
		ALLOW_GRAND_BOSSES_TELEPORT = rusacis.getProperty("AllowGrandBossesTeleport", false);

		USE_SAY_FILTER = rusacis.getProperty("UseChatFilter", false);
		CHAT_FILTER_CHARS = rusacis.getProperty("ChatFilterChars", "^_^");

		try {
			FILTER_LIST = Files.lines(Paths.get(CHAT_FILTER_FILE), StandardCharsets.UTF_8).map(String::trim)
					.filter(line -> (!line.isEmpty() && (line.charAt(0) != '#'))).collect(Collectors.toList());
			LOGGER.info("Loaded " + FILTER_LIST.size() + " Filter Words.");
		} catch (IOException e) {
			LOGGER.warn("Error while loading chat filter words!", e);
		}

		CABAL_BUFFER = rusacis.getProperty("CabalBuffer", false);
		SUPER_HASTE = rusacis.getProperty("SuperHaste", false);

		RESTRICTED_CHAR_NAMES = rusacis.getProperty("ListOfRestrictedCharNames", "");
		LIST_RESTRICTED_CHAR_NAMES = new ArrayList<>();
		for (String name : RESTRICTED_CHAR_NAMES.split(","))
			LIST_RESTRICTED_CHAR_NAMES.add(name.toLowerCase());

		FAKE_ONLINE_AMOUNT = rusacis.getProperty("FakeOnlineAmount", 1);

		BUFFS_CATEGORY = rusacis.getProperty("PremiumBuffsCategory", "");
		PREMIUM_BUFFS_CATEGORY = new ArrayList<>();
		for (String buffs : BUFFS_CATEGORY.split(","))
			PREMIUM_BUFFS_CATEGORY.add(buffs);

		ANTIFEED_ENABLE = rusacis.getProperty("AntiFeedEnable", false);
		ANTIFEED_DUALBOX = rusacis.getProperty("AntiFeedDualbox", true);
		ANTIFEED_DISCONNECTED_AS_DUALBOX = rusacis.getProperty("AntiFeedDisconnectedAsDualbox", true);
		ANTIFEED_INTERVAL = rusacis.getProperty("AntiFeedInterval", 120) * 1000;

		DUALBOX_CHECK_MAX_PLAYERS_PER_IP = rusacis.getProperty("DualboxCheckMaxPlayersPerIP", 0);
		DUALBOX_CHECK_MAX_OLYMPIAD_PARTICIPANTS_PER_IP = rusacis.getProperty("DualboxCheckMaxOlympiadParticipantsPerIP",
				0);
		String[] propertySplit = rusacis.getProperty("DualboxCheckWhitelist", "127.0.0.1,0").split(";");
		DUALBOX_CHECK_WHITELIST = new HashMap<>(propertySplit.length);
		for (String entry : propertySplit) {
			String[] entrySplit = entry.split(",");
			if (entrySplit.length != 2)
				LOGGER.warn("DualboxCheck[Config.load()]: invalid config property -> DualboxCheckWhitelist \"", entry,
						"\"");
			else {
				try {
					int num = Integer.parseInt(entrySplit[1]);
					num = num == 0 ? -1 : num;
					DUALBOX_CHECK_WHITELIST.put(InetAddress.getByName(entrySplit[0]).hashCode(), num);
				} catch (UnknownHostException e) {
					LOGGER.warn("DualboxCheck[Config.load()]: invalid address -> DualboxCheckWhitelist \"",
							entrySplit[0], "\"");
				} catch (NumberFormatException e) {
					LOGGER.warn("DualboxCheck[Config.load()]: invalid number -> DualboxCheckWhitelist \"",
							entrySplit[1], "\"");
				}
			}
		}

		String[] autoLootItemIds = rusacis.getProperty("AutoLootItemIds", "0").split(",");
		AUTO_LOOT_ITEM_IDS = new ArrayList<>(autoLootItemIds.length);
		for (String item : autoLootItemIds) {
			Integer itm = 0;
			try {
				itm = Integer.parseInt(item);
			} catch (NumberFormatException nfe) {
				LOGGER.warn("Auto loot item ids: Wrong ItemId passed: " + item);
			}

			if (itm != 0)
				AUTO_LOOT_ITEM_IDS.add(itm);
		}
		BANKING_SYSTEM_ENABLED = Boolean.parseBoolean(rusacis.getProperty("BankingEnabled", "false"));
		BANKING_SYSTEM_GOLDBAR_ID = Integer.parseInt(rusacis.getProperty("BankingGoldbar_Id", "1"));
		BANKING_SYSTEM_GOLDBARS = Integer.parseInt(rusacis.getProperty("BankingGoldbarCount", "1"));
		BANKING_SYSTEM_ADENA = Integer.parseInt(rusacis.getProperty("BankingAdenaCount", "500000000"));

		ITEM_ZONE = Integer.parseInt(rusacis.getProperty("ZonaItem", "9599"));
		BUFFER_ZONE = Integer.parseInt(rusacis.getProperty("bufferzoneID", "9599"));

		ALLOW_DRESS_ME_SYSTEM = rusacis.getProperty("AllowDressMeSystem", false);
		String temp = rusacis.getProperty("DressMeChests", "");
		String[] temp2 = temp.split(";");
		for (String s : temp2) {
			String[] t = s.split(",");
			DRESS_ME_CHESTS.put(t[0], Integer.parseInt(t[1]));
		}
		temp = rusacis.getProperty("DressMeHairAll", "");
		temp2 = temp.split(";");
		for (String s : temp2) {
			String[] t = s.split(",");
			DRESS_ME_HAIRALL.put(t[0], Integer.parseInt(t[1]));
		}
		temp = rusacis.getProperty("DressMeLegs", "");
		temp2 = temp.split(";");
		for (String s : temp2) {
			String[] t = s.split(",");
			DRESS_ME_LEGS.put(t[0], Integer.parseInt(t[1]));
		}
		temp = rusacis.getProperty("DressMeBoots", "");
		temp2 = temp.split(";");
		for (String s : temp2) {
			String[] t = s.split(",");
			DRESS_ME_BOOTS.put(t[0], Integer.parseInt(t[1]));
		}
		temp = rusacis.getProperty("DressMeGloves", "");
		temp2 = temp.split(";");
		for (String s : temp2) {
			String[] t = s.split(",");
			DRESS_ME_GLOVES.put(t[0], Integer.parseInt(t[1]));
		}
		ID_VISUAL_EFFECT_KNIGHTS = rusacis.getProperty("MagicEffectUseKnights", 0);
		ID_VISUAL_EFFECT_WARRIOR = rusacis.getProperty("MagicEffectUseWarrior", 0);
		ID_VISUAL_EFFECT_WIZARDS = rusacis.getProperty("MagicEffectUseWizards", 0);

		MONSTERS_DROPS = rusacis.getProperty("MonsterDrops", false);

		MAX_PARTY_HEALER_BISHOP = rusacis.getProperty("LimitPartyBishop", 1);
		MAX_PARTY_ORC_OVER = rusacis.getProperty("LimitPartyOverLord", 1);
		MAX_PARTY_DUELISTA = rusacis.getProperty("LimitPartyDuelista", 1);
		MAX_PARTY_WARLORD = rusacis.getProperty("LimitPartyWarlord", 1);

		String skillsConfig = rusacis.getProperty("Skillclass1");
		for (String skillInfo : skillsConfig.split(";")) {
			String[] info = skillInfo.split(",");
			SKILLCLASS1.put(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
		}
		String skillsClass2 = rusacis.getProperty("Skillclass2");
		for (String skillInfo : skillsClass2.split(";")) {
			String[] info = skillInfo.split(",");
			SKILLCLASS2.put(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
		}
		String skillsClass3 = rusacis.getProperty("Skillclass3");
		for (String skillInfo : skillsClass3.split(";")) {
			String[] info = skillInfo.split(",");
			SKILLCLASS3.put(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
		}
		String skillsClass4 = rusacis.getProperty("Skillclass4");
		for (String skillInfo : skillsClass4.split(";")) {
			String[] info = skillInfo.split(",");
			SKILLCLASS4.put(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
		}
		String skillsClass5 = rusacis.getProperty("Skillclass5");
		for (String skillInfo : skillsClass5.split(";")) {
			String[] info = skillInfo.split(",");
			SKILLCLASS5.put(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
		}
		String skillsClass6 = rusacis.getProperty("Skillclass6");
		for (String skillInfo : skillsClass6.split(";")) {
			String[] info = skillInfo.split(",");
			SKILLCLASS6.put(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
		}
		String skillsClass7 = rusacis.getProperty("Skillclass7");
		for (String skillInfo : skillsClass7.split(";")) {
			String[] info = skillInfo.split(",");
			SKILLCLASS7.put(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
		}
		String skillsClass8 = rusacis.getProperty("Skillclass8");
		for (String skillInfo : skillsClass8.split(";")) {
			String[] info = skillInfo.split(",");
			SKILLCLASS8.put(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
		}
		String skillsClass9 = rusacis.getProperty("Skillclass9");
		for (String skillInfo : skillsClass9.split(";")) {
			String[] info = skillInfo.split(",");
			SKILLCLASS9.put(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
		}
		String skillsClass10 = rusacis.getProperty("Skillclass10");
		for (String skillInfo : skillsClass10.split(";")) {
			String[] info = skillInfo.split(",");
			SKILLCLASS10.put(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
		}
		String skillsClass11 = rusacis.getProperty("Skillclass11");
		for (String skillInfo : skillsClass11.split(";")) {
			String[] info = skillInfo.split(",");
			SKILLCLASS11.put(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
		}
		String skillsClass12 = rusacis.getProperty("Skillclass12");
		for (String skillInfo : skillsClass12.split(";")) {
			String[] info = skillInfo.split(",");
			SKILLCLASS12.put(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
		}
		String skillsClass13 = rusacis.getProperty("Skillclass13");
		for (String skillInfo : skillsClass13.split(";")) {
			String[] info = skillInfo.split(",");
			SKILLCLASS13.put(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
		}
		String skillsClass14 = rusacis.getProperty("Skillclass14");
		for (String skillInfo : skillsClass14.split(";")) {
			String[] info = skillInfo.split(",");
			SKILLCLASS14.put(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
		}
		String skillsClass15 = rusacis.getProperty("Skillclass15");
		for (String skillInfo : skillsClass15.split(";")) {
			String[] info = skillInfo.split(",");
			SKILLCLASS15.put(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
		}
		String skillsClass16 = rusacis.getProperty("Skillclass16");
		for (String skillInfo : skillsClass16.split(";")) {
			String[] info = skillInfo.split(",");
			SKILLCLASS16.put(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
		}
		String skillsClass17 = rusacis.getProperty("Skillclass17");
		for (String skillInfo : skillsClass17.split(";")) {
			String[] info = skillInfo.split(",");
			SKILLCLASS17.put(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
		}
		String skillsClass18 = rusacis.getProperty("Skillclass18");
		for (String skillInfo : skillsClass18.split(";")) {
			String[] info = skillInfo.split(",");
			SKILLCLASS18.put(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
		}
		String skillsClass19 = rusacis.getProperty("Skillclass19");
		for (String skillInfo : skillsClass19.split(";")) {
			String[] info = skillInfo.split(",");
			SKILLCLASS19.put(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
		}
		String skillsClass20 = rusacis.getProperty("Skillclass20");
		for (String skillInfo : skillsClass20.split(";")) {
			String[] info = skillInfo.split(",");
			SKILLCLASS20.put(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
		}
		String skillsClass21 = rusacis.getProperty("Skillclass21");
		for (String skillInfo : skillsClass21.split(";")) {
			String[] info = skillInfo.split(",");
			SKILLCLASS21.put(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
		}
		String skillsClass22 = rusacis.getProperty("Skillclass22");
		for (String skillInfo : skillsClass22.split(";")) {
			String[] info = skillInfo.split(",");
			SKILLCLASS22.put(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
		}
		String Skillslist = rusacis.getProperty("randomskill");
		for (String skillInfo : Skillslist.split(";")) {
			String[] info = skillInfo.split(",");
			RANDOM_SKILLS.put(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
		}
		//npcSeller
		ID_NPC_SKILL_SELLER = rusacis.getProperty("idNpcSeller", 15021992);
		ITEM_ID_SKILL_SELLER = rusacis.getProperty("ItemIDVendaSkill", 57);
		QUANT_OF_GOLD_ITEM = rusacis.getProperty("QuantidadeItem", 1);

		String SkillSeller = rusacis.getProperty("SkillIds");
		for (String skillInfo : SkillSeller.split(";")) {
			String[] info = skillInfo.split(",");
			SKILLIDSSELLER.put(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
		}
	}

	/**
	 * Loads loginserver settings.<br>
	 * IP addresses, database, account, misc.
	 */
	private static final void loadLogin() {
		final ExProperties server = initProperties(LOGINSERVER_FILE);

		HOSTNAME = server.getProperty("Hostname", "localhost");
		LOGINSERVER_HOSTNAME = server.getProperty("LoginserverHostname", "*");
		LOGINSERVER_PORT = server.getProperty("LoginserverPort", 2106);
		GAMESERVER_LOGIN_HOSTNAME = server.getProperty("LoginHostname", "*");
		GAMESERVER_LOGIN_PORT = server.getProperty("LoginPort", 9014);
		LOGIN_TRY_BEFORE_BAN = server.getProperty("LoginTryBeforeBan", 3);
		LOGIN_BLOCK_AFTER_BAN = server.getProperty("LoginBlockAfterBan", 600);
		ACCEPT_NEW_GAMESERVER = server.getProperty("AcceptNewGameServer", false);
		SHOW_LICENCE = server.getProperty("ShowLicence", true);

		DATABASE_URL = server.getProperty("URL", "jdbc:mariadb://localhost/acis");
		DATABASE_LOGIN = server.getProperty("Login", "root");
		DATABASE_PASSWORD = server.getProperty("Password", "");
		DATABASE_MAX_CONNECTIONS = server.getProperty("MaximumDbConnections", 5);

		AUTO_CREATE_ACCOUNTS = server.getProperty("AutoCreateAccounts", true);

		FLOOD_PROTECTION = server.getProperty("EnableFloodProtection", true);
		FAST_CONNECTION_LIMIT = server.getProperty("FastConnectionLimit", 15);
		NORMAL_CONNECTION_TIME = server.getProperty("NormalConnectionTime", 700);
		FAST_CONNECTION_TIME = server.getProperty("FastConnectionTime", 350);
		MAX_CONNECTION_PER_IP = server.getProperty("MaxConnectionPerIP", 50);
	}

	public static final void loadGameServer() {
		LOGGER.info("Loading gameserver configuration files.");

		// offline settings
		loadOfflineShop();

		// clans settings
		loadClans();

		// events settings
		loadEvents();

		// geoengine settings
		loadGeoengine();

		// hexID
		loadHexID();

		// NPCs/monsters settings
		loadNpcs();

		// players settings
		loadPlayers();

		// siege settings
		loadSieges();

		// Newbie settings
		loadNewbies();

		// server settings
		loadServer();

		// rates settings
		loadRates();

		// settings
		load();

		// balance
		loadBalance();

        //event_boss
		loadBossEvent();

		// donate
		loadDonate();

		// custom quests
		loadCustomQuestConfig();


	}

	public static final void loadLoginServer() {
		LOGGER.info("Loading loginserver configuration files.");

		// login settings
		loadLogin();
	}

	public static  void loadAccountManager() {
		LOGGER.info("Loading account manager configuration files.");

		// login settings
		loadLogin();
	}

	public static void loadGameServerRegistration() {
		LOGGER.info("Loading gameserver registration configuration files.");

		// login settings
		loadLogin();
	}

	public static final class ClassMasterSettings {
		private final Map<Integer, Boolean> _allowedClassChange;
		private final Map<Integer, List<IntIntHolder>> _claimItems;
		private final Map<Integer, List<IntIntHolder>> _rewardItems;

		public ClassMasterSettings(String configLine) {
			_allowedClassChange = new HashMap<>(3);
			_claimItems = new HashMap<>(3);
			_rewardItems = new HashMap<>(3);

			if (configLine != null)
				parseConfigLine(configLine.trim());
		}

		private void parseConfigLine(String configLine) {
			StringTokenizer st = new StringTokenizer(configLine, ";");
			while (st.hasMoreTokens()) {
				// Get allowed class change.
				int job = Integer.parseInt(st.nextToken());

				_allowedClassChange.put(job, true);

				List<IntIntHolder> items = new ArrayList<>();

				// Parse items needed for class change.
				if (st.hasMoreTokens()) {
					StringTokenizer st2 = new StringTokenizer(st.nextToken(), "[],");
					while (st2.hasMoreTokens()) {
						StringTokenizer st3 = new StringTokenizer(st2.nextToken(), "()");
						items.add(
								new IntIntHolder(Integer.parseInt(st3.nextToken()), Integer.parseInt(st3.nextToken())));
					}
				}

				// Feed the map, and clean the list.
				_claimItems.put(job, items);
				items = new ArrayList<>();

				// Parse gifts after class change.
				if (st.hasMoreTokens()) {
					StringTokenizer st2 = new StringTokenizer(st.nextToken(), "[],");
					while (st2.hasMoreTokens()) {
						StringTokenizer st3 = new StringTokenizer(st2.nextToken(), "()");
						items.add(
								new IntIntHolder(Integer.parseInt(st3.nextToken()), Integer.parseInt(st3.nextToken())));
					}
				}

				_rewardItems.put(job, items);
			}
		}

		public boolean isAllowed(int job) {
			if (_allowedClassChange == null)
				return false;

			if (_allowedClassChange.containsKey(job))
				return _allowedClassChange.get(job);

			return false;
		}

		public List<IntIntHolder> getRewardItems(int job) {
			return _rewardItems.get(job);
		}

		public List<IntIntHolder> getRequiredItems(int job) {
			return _claimItems.get(job);
		}
	}
}