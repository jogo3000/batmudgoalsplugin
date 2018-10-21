package batmudgoalsplugin.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class SkillCostLibraryMapAdapter extends
        XmlAdapter<AdaptedSkillCostList, Map<String, Map<Integer, Integer>>> {

    @Override
    public Map<String, Map<Integer, Integer>> unmarshal(AdaptedSkillCostList v)
            throws Exception {
        Map<String, Map<Integer, Integer>> map = new HashMap<>();
        for (AdaptedSkillCostEntry entry : v.list) {
            if (!map.containsKey(entry.skill))
                map.put(entry.skill, new HashMap<Integer, Integer>());
            map.get(entry.skill).put(entry.percent, entry.cost);
        }
        return map;
    }

    @Override
    public AdaptedSkillCostList marshal(Map<String, Map<Integer, Integer>> v)
            throws Exception {
        AdaptedSkillCostList list = new AdaptedSkillCostList();
        list.list = new ArrayList<>();
        for (Entry<String, Map<Integer, Integer>> skillEntry : v.entrySet()) {
            for (Entry<Integer, Integer> valueEntry : skillEntry.getValue()
                    .entrySet()) {
                list.list.add(new AdaptedSkillCostEntry(skillEntry.getKey(),
                        valueEntry.getKey(), valueEntry.getValue()));
            }
        }
        return list;
    }
}
