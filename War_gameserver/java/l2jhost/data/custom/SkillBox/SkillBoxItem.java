package l2jhost.data.custom.SkillBox;

import net.sf.l2j.gameserver.network.serverpackets.SkillList;

import java.util.ArrayList;
import java.util.List;

public class SkillBoxItem {
    private int id;
    private int playerLevel;
    private List<Skill> skill;

    public SkillBoxItem(int id, int playerLevel) {
        this.id = id;
        this.playerLevel = playerLevel;
        skill = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public int getPlayerLevel() {
        return playerLevel;
    }

    public List<Skill> getSkills() {
        return skill;
    }

    public void addSkill(Skill skill) {
        if (skill != null) {
            getSkills().add(skill);
        }
    }


    public static class Skill extends SkillList {
        private int skillId;
        private int skillLevel;
        private int skillEnchantLevel;
        private int skillChance;



        public  Skill(int skillid, int skillLevel, int skillEnchantLevel, int skillChance) {
            this.skillId = skillid;
            this.skillLevel = skillLevel;
            this.skillEnchantLevel = skillEnchantLevel;
            this.skillChance = skillChance;
        }

        public int getSkillId() {
            return skillId;
        }

        public int getAmount() {
            return skillLevel;
        }

        public int getEnchantLevel() {
            return skillEnchantLevel;
        }

        public int getChance() {
            return skillChance;
        }


    }
}

