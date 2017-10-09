/**
 * @author Kimberley Louw, Matthew Boyes, Mika Smith
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for generating combinations of a list
 */
public class Combinations<T> {
    private List<T> source = new ArrayList<>();

    /**
     * Creates a new Combinations source
     * @param source List
     */
    public Combinations(List<T> source) {
        this.source.addAll(source);
    }

    /**
     * Returns a List of Combinations of the source list Choose k
     * @param k k as in n Choose k (Mathematics)
     * @return List of Lists
     */
    public List<List<T>> choose(int k) {
        List<List<T>> result = new ArrayList<>();

        combinations(result, new ArrayList<>(), source, k);

        return result;
    }

    private void combinations(List<List<T>> result, List<T> partial, List<T> choices, int remaining) {
        for (int i = 0; i < choices.size(); i++) {
            T item = choices.get(i);
            List<T> tmp = new ArrayList<>();
            tmp.addAll(partial);
            tmp.add(item);

            if (remaining > 1) {
                combinations(result, tmp, choices.subList(i + 1, choices.size()), remaining - 1);
            } else {
                result.add(tmp);
            }
        }
    }
}
