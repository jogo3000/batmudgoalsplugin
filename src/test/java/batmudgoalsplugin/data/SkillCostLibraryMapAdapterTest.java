package batmudgoalsplugin.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

public class SkillCostLibraryMapAdapterTest {

    SkillCostLibraryMapAdapter adapter = new SkillCostLibraryMapAdapter();

    @Test
    public void testAdaptSkillCosts() throws Exception {
        Map<Integer, Integer> skillCostsPerLevel = IntStream.rangeClosed(1, 10)
                .boxed()
                .collect(Collectors.toMap(Function.identity(), Function.identity()));

        Map<String, Map<Integer, Integer>> map = new HashMap<>();
        map.put("attack", skillCostsPerLevel);
        AdaptedSkillCostList adapted = adapter.marshal(map);

        List<AdaptedSkillCostEntry> expected = Arrays.asList(
                new AdaptedSkillCostEntry("attack", 1, 1),
                new AdaptedSkillCostEntry("attack", 2, 2),
                new AdaptedSkillCostEntry("attack", 3, 3),
                new AdaptedSkillCostEntry("attack", 4, 4),
                new AdaptedSkillCostEntry("attack", 5, 5),
                new AdaptedSkillCostEntry("attack", 6, 6),
                new AdaptedSkillCostEntry("attack", 7, 7),
                new AdaptedSkillCostEntry("attack", 8, 8),
                new AdaptedSkillCostEntry("attack", 9, 9),
                new AdaptedSkillCostEntry("attack", 10, 10));
        assertIterableEquals(expected, adapted.list);

    }

    @Test
    void testBackwardAdaptSkillCosts() throws Exception {
        AdaptedSkillCostList toBeAdapted = new AdaptedSkillCostList(Arrays.asList(
                new AdaptedSkillCostEntry("attack", 1, 1),
                new AdaptedSkillCostEntry("attack", 2, 2),
                new AdaptedSkillCostEntry("attack", 3, 3),
                new AdaptedSkillCostEntry("attack", 4, 4),
                new AdaptedSkillCostEntry("attack", 5, 5),
                new AdaptedSkillCostEntry("attack", 6, 6),
                new AdaptedSkillCostEntry("attack", 7, 7),
                new AdaptedSkillCostEntry("attack", 8, 8),
                new AdaptedSkillCostEntry("attack", 9, 9),
                new AdaptedSkillCostEntry("attack", 10, 10)));

        Map<String, Map<Integer, Integer>> actual = adapter.unmarshal(toBeAdapted);
        assertIterableEquals(Arrays.asList("attack"), actual.keySet());
        assertEquals(IntStream.rangeClosed(1, 10)
                .boxed()
                .map(i -> new AbstractMap.SimpleEntry<>(i, i))
                .collect(Collectors.toSet()),
                actual.values().stream().map(Map::entrySet).flatMap(Set::stream)
                        .collect(Collectors.toSet()));
    }

}
