package l2jhost.data.custom.SkillBox;

import l2jhost.data.XMLDocument;
import l2jhost.data.custom.CapsuleBox.CapsuleBoxItem;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SkillBoxData extends XMLDocument {
    private Map<Integer, SkillBoxItem> skillBoxItems;

    public SkillBoxData() {
        skillBoxItems = new HashMap<>();
        load();
    }

    public void reload()
    {
        skillBoxItems.clear();
        load();
    }

    public static SkillBoxData getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        protected static final SkillBoxData INSTANCE = new SkillBoxData();
    }

    @Override
    protected void load() {
        loadDocument("./data/xml/SkillBox.xml");
        LOG.info("SkillBoxData: Loaded " + skillBoxItems.size() + " skills.");
    }

    @Override
    protected void parseDocument(Document doc, File file) {
        try {
            final Node root = doc.getFirstChild();

            for (Node node = root.getFirstChild(); node != null; node = node.getNextSibling()) {
                if (!"skillbox_items".equalsIgnoreCase(node.getNodeName())) {
                    continue;
                }

                NamedNodeMap attrs = node.getAttributes();
                int id = Integer.parseInt(attrs.getNamedItem("ID").getNodeValue());
                int playerLevel = Integer.parseInt(attrs.getNamedItem("PlayerLevel").getNodeValue());

                SkillBoxItem skillBoxItem = new SkillBoxItem(id, playerLevel);

                for (Node itemNode = node.getFirstChild(); itemNode != null; itemNode = itemNode.getNextSibling()) {
                    if (!"skill".equalsIgnoreCase(itemNode.getNodeName())) {
                        continue;
                    }

                    attrs = itemNode.getAttributes();
                    int skillId = Integer.parseInt(attrs.getNamedItem("SkillId").getNodeValue());
                    int skillLevel = Integer.parseInt(attrs.getNamedItem("SkillLevel").getNodeValue());
                    int skillEnchantLevel = Integer.parseInt(attrs.getNamedItem("SkillEnchantLevel").getNodeValue());
                    int skillChance = Integer.parseInt(attrs.getNamedItem("SkillChance").getNodeValue());

                    SkillBoxItem.Skill skill = new SkillBoxItem.Skill(skillId, skillLevel, skillEnchantLevel, skillChance);
                    skillBoxItem.addSkill(skill);
                }

                skillBoxItems.put(id, skillBoxItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, SkillBoxItem> getCapsuleBoxItems() {
        return skillBoxItems;
    }

    public SkillBoxItem getSkillBoxItemById(int id) {
        return skillBoxItems.get(id);
    }

    private static int getRandomAmount(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }
}
