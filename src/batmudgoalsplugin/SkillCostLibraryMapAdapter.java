package batmudgoalsplugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class SkillCostLibraryMapAdapter extends
		XmlAdapter<AdaptedSkillCostList, Map<String, Map<String, String>>> {

	@Override
	public Map<String, Map<String, String>> unmarshal(AdaptedSkillCostList v)
			throws Exception {
		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		for (AdaptedSkillCostEntry entry : v.list) {
			if (!map.containsKey(entry.skill))
				map.put(entry.skill, new HashMap<String, String>());
			map.get(entry.skill).put(entry.percent, entry.cost);
		}
		return map;
	}

	@Override
	public AdaptedSkillCostList marshal(Map<String, Map<String, String>> v)
			throws Exception {
		AdaptedSkillCostList list = new AdaptedSkillCostList();
		list.list = new ArrayList<AdaptedSkillCostEntry>();
		for (Entry<String, Map<String, String>> skillEntry : v.entrySet()) {
			for (Entry<String, String> valueEntry : skillEntry.getValue()
					.entrySet()) {
				list.list.add(new AdaptedSkillCostEntry(skillEntry.getKey(),
						valueEntry.getKey(), valueEntry.getValue()));
			}
		}
		return list;
	}
}
