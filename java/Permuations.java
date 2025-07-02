import java.util.ArrayList;
import java.util.List;

public class Permuations {
    public static List<List<Integer>> generatePermutations(List<Integer> input) {
        List<List<Integer>> results = new ArrayList<>();
        backtrack(input, 0, results);
        return results;
    }

    private static void backtrack(List<Integer> list, int start, List<List<Integer>> results) {
        if (start == list.size() - 1) {
            if(!results.contains(new ArrayList<>(list)))
                results.add(new ArrayList<>(list)); // Ajouter une copie de la liste actuelle
            return;
        }

        for (int i = start; i < list.size(); i++) {
            swap(list, start, i);               // Échanger les éléments
            backtrack(list, start + 1, results); // Continuer la permutation
            swap(list, start, i);               // Restaurer l'état précédent (backtracking)
        }
    }

    private static void swap(List<Integer> list, int i, int j) {
        Integer temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}
