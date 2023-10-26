package net.sf.l2j.gameserver.handler;

import l2jhost.data.custom.AioItem;
import net.sf.l2j.gameserver.handler.itemhandlers.*;
import net.sf.l2j.gameserver.handler.itemhandlers.custom.*;
import net.sf.l2j.gameserver.handler.itemhandlers.custom.skillseller.SkillBox;
import net.sf.l2j.gameserver.model.item.kind.EtcItem;

import java.util.HashMap;
import java.util.Map;

public class ItemHandler {
	private final Map<Integer, IItemHandler> _entries = new HashMap<>();

	protected ItemHandler() {
		registerHandler(new BeastSoulShots());
		registerHandler(new BeastSpices());
		registerHandler(new BeastSpiritShots());
		registerHandler(new BlessedSpiritShots());
		registerHandler(new Books());
		registerHandler(new BreakingArrow());
		registerHandler(new DewdropOfDestruction());
		registerHandler(new Calculators());
		registerHandler(new Elixirs());
		registerHandler(new EnchantScrolls());
		registerHandler(new FishShots());
		registerHandler(new Harvesters());
		registerHandler(new ItemSkills());
		registerHandler(new Keys());
		registerHandler(new Maps());
		registerHandler(new MercenaryTickets());
		registerHandler(new PaganKeys());
		registerHandler(new PetFoods());
		registerHandler(new Recipes());
		registerHandler(new RollingDices());
		registerHandler(new ScrollsOfResurrection());
		registerHandler(new Seeds());
		registerHandler(new SevenSignsRecords());
		registerHandler(new SoulShots());
		registerHandler(new SpecialXMas());
		registerHandler(new SoulCrystals());
		registerHandler(new SpiritShots());
		registerHandler(new SummonItems());

		registerHandler(new Skins());
		registerHandler(new AioItem());
		registerHandler(new CapsuleBox_System());
		registerHandler(new LevelCoin());
		registerHandler(new ClanItem());
		registerHandler(new RandomSkill());
		registerHandler(new BuySkillClass1());
		registerHandler(new BuySkillClass2());
		registerHandler(new BuySkillClass3());
		registerHandler(new BuySkillClass4());
		registerHandler(new BuySkillClass5());
		registerHandler(new BuySkillClass6());
		registerHandler(new BuySkillClass7());
		registerHandler(new BuySkillClass8());
		registerHandler(new BuySkillClass9());
		registerHandler(new BuySkillClass10());
		registerHandler(new BuySkillClass11());
		registerHandler(new BuySkillClass12());
		registerHandler(new BuySkillClass13());
		registerHandler(new BuySkillClass14());
		registerHandler(new BuySkillClass15());
		registerHandler(new BuySkillClass16());
		registerHandler(new BuySkillClass17());
		registerHandler(new BuySkillClass18());
		registerHandler(new BuySkillClass19());
		registerHandler(new BuySkillClass20());
		registerHandler(new CoinPremium());
		registerHandler(new SkillBox());

	}

	private void registerHandler(IItemHandler handler) {
		_entries.put(handler.getClass().getSimpleName().intern().hashCode(), handler);
	}

	public IItemHandler getHandler(EtcItem item) {
		if (item == null || item.getHandlerName() == null)
			return null;

		return _entries.get(item.getHandlerName().hashCode());
	}

	public int size() {
		return _entries.size();
	}

	public static ItemHandler getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {
		protected static final ItemHandler INSTANCE = new ItemHandler();
	}

}