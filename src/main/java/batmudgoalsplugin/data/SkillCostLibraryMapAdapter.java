package batmudgoalsplugin.data;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class SkillCostLibraryMapAdapter extends
        XmlAdapter<AdaptedSkillCostList, Map<String, Map<Integer, Integer>>> {

    @Override
    public Map<String, Map<Integer, Integer>> unmarshal(AdaptedSkillCostList v)
            throws Exception {
        return v.list
                .stream()
                .collect(
                        Collectors.groupingBy(e -> e.skill,
                                Collectors.mapping(Function.identity(),
                                        Collectors.toMap(e -> e.percent, e -> e.cost))));

    }

    @Override
    public AdaptedSkillCostList marshal(Map<String, Map<Integer, Integer>> v)
            throws Exception {
        return new AdaptedSkillCostList(
                v.entrySet()
                        .stream()
                        .flatMap(skillCosts -> skillCosts.getValue().entrySet().stream()
                                .map(costPerLevel -> new AdaptedSkillCostEntry(skillCosts.getKey(),
                                        costPerLevel.getKey(),
                                        costPerLevel.getValue())))
                        .collect(Collectors.toList()));
    }
}
