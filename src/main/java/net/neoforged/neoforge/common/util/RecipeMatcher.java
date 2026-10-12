package net.neoforged.neoforge.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Port of NeoForge's {@code RecipeMatcher}: finds a bipartite matching between
 * inputs and predicates (ingredients). Fabric port note: NeoForge uses a
 * bitmask-based finder; this implementation uses straightforward backtracking
 * which is equivalent for the small inputs seen in crafting (<= 9 stacks).
 */
public class RecipeMatcher
{
    /**
     * Attempts to match each element of {@code tests} to a distinct element of
     * {@code inputs}.
     *
     * @return a list of {@code inputs} indices, one per entry of {@code tests},
     *         or {@code null} if no complete matching exists.
     */
    public static <T> List<Integer> findMatches(List<T> inputs, List<? extends Predicate<T>> tests)
    {
        if (tests.size() > inputs.size()) return null;

        int[] assignment = new int[tests.size()];
        Arrays.fill(assignment, -1);
        boolean[] used = new boolean[inputs.size()];

        if (!match(0, inputs, tests, used, assignment)) return null;

        List<Integer> result = new ArrayList<>(tests.size());
        for (int i : assignment) result.add(i);
        return result;
    }

    private static <T> boolean match(int ti, List<T> inputs, List<? extends Predicate<T>> tests, boolean[] used, int[] assignment)
    {
        if (ti == tests.size()) return true;

        for (int i = 0; i < inputs.size(); i++)
        {
            if (!used[i] && tests.get(ti).test(inputs.get(i)))
            {
                used[i] = true;
                assignment[ti] = i;
                if (match(ti + 1, inputs, tests, used, assignment)) return true;
                used[i] = false;
                assignment[ti] = -1;
            }
        }
        return false;
    }
}
