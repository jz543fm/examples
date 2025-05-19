import java.util.*;

public class WordCounter {

    private final Map<String, Integer> wordCounts = new HashMap<>();

    public void processLine(String line) {
        String[] words = line.toLowerCase().split("\\W+");
        for (String word : words) {
            if (!word.isEmpty()) {
                wordCounts.merge(word, 1, Integer::sum);
            }
        }
    }

    public void merge(WordCounter other) {
        for (Map.Entry<String, Integer> entry : other.wordCounts.entrySet()) {
            wordCounts.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }
    }

    public List<Map.Entry<String, Integer>> getTopWords(int n) {
        return wordCounts.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .limit(n)
                .toList();
    }
}
