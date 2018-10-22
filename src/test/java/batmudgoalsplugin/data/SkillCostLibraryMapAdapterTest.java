package batmudgoalsplugin.data;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SkillCostLibraryMapAdapterTest {

    SkillCostLibraryMapAdapter adapter = new SkillCostLibraryMapAdapter();

    @Test
    @DisplayName("Given skillcost map has 'attack' skill costs between 1-10, they are adapted")
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
    @DisplayName("Given adapted skillcosts contain 'attack' between 1-9 and 'looting and burning' at 10, they are back-adapted to a map")
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
                new AdaptedSkillCostEntry("looting and burning", 10, 10)));

        Map<String, Map<Integer, Integer>> actual = adapter.unmarshal(toBeAdapted);

        assertAll("Keys 1-9 go to 'attack' and key 10 goes to 'looting and burning'",
                () -> assertEquals(Stream.of("attack", "looting and burning").collect(Collectors.toSet()),
                        actual.keySet()),
                () -> assertEquals(mapOfIntegers(1, 9), actual.get("attack")),
                () -> assertEquals(mapOfIntegers(10, 10), actual.get("looting and burning")));
    }

    private Map<Integer, Integer> mapOfIntegers(int start, int end) {
        return IntStream.rangeClosed(start, end)
                .boxed()
                .collect(Collectors.toMap(Function.identity(), Function.identity()));
    }

}
