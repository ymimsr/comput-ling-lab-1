package ru.nsu.fit.comput_ling_lab_1.domain;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class TreeDictionary {

    private final Map<String, GraphemeNode> roots = new HashMap<>();

    public TreeDictionary() {
        // alphabet
        roots.put("а", new GraphemeNode(null, "а"));
        roots.put("б", new GraphemeNode(null, "б"));
        roots.put("в", new GraphemeNode(null, "в"));
        roots.put("г", new GraphemeNode(null, "г"));
        roots.put("д", new GraphemeNode(null, "д"));
        roots.put("е", new GraphemeNode(null, "е"));
        roots.put("ё", new GraphemeNode(null, "ё"));
        roots.put("ж", new GraphemeNode(null, "ж"));
        roots.put("з", new GraphemeNode(null, "з"));
        roots.put("и", new GraphemeNode(null, "и"));
        roots.put("й", new GraphemeNode(null, "й"));
        roots.put("к", new GraphemeNode(null, "к"));
        roots.put("л", new GraphemeNode(null, "л"));
        roots.put("м", new GraphemeNode(null, "м"));
        roots.put("н", new GraphemeNode(null, "н"));
        roots.put("о", new GraphemeNode(null, "о"));
        roots.put("п", new GraphemeNode(null, "п"));
        roots.put("р", new GraphemeNode(null, "р"));
        roots.put("с", new GraphemeNode(null, "с"));
        roots.put("т", new GraphemeNode(null, "т"));
        roots.put("у", new GraphemeNode(null, "у"));
        roots.put("ф", new GraphemeNode(null, "ф"));
        roots.put("х", new GraphemeNode(null, "х"));
        roots.put("ц", new GraphemeNode(null, "ц"));
        roots.put("ч", new GraphemeNode(null, "ч"));
        roots.put("ш", new GraphemeNode(null, "ш"));
        roots.put("щ", new GraphemeNode(null, "щ"));
        roots.put("ъ", new GraphemeNode(null, "ъ"));
        roots.put("ы", new GraphemeNode(null, "ы"));
        roots.put("ь", new GraphemeNode(null, "ь"));
        roots.put("э", new GraphemeNode(null, "э"));
        roots.put("ю", new GraphemeNode(null, "ю"));
        roots.put("я", new GraphemeNode(null, "я"));

        // also numbers
        roots.put("0", new GraphemeNode(null, "0"));
        roots.put("1", new GraphemeNode(null, "1"));
        roots.put("2", new GraphemeNode(null, "2"));
        roots.put("3", new GraphemeNode(null, "3"));
        roots.put("4", new GraphemeNode(null, "4"));
        roots.put("5", new GraphemeNode(null, "5"));
        roots.put("6", new GraphemeNode(null, "6"));
        roots.put("7", new GraphemeNode(null, "7"));
        roots.put("8", new GraphemeNode(null, "8"));
        roots.put("9", new GraphemeNode(null, "9"));
    }

    public Map<String, GraphemeNode> getRoots() {
        return roots;
    }

    // used after initializing dictionary
    public Word getWord(String sWord) {
        GraphemeNode curNode = roots.get(String.valueOf(sWord.charAt(0)).toLowerCase(Locale.ROOT));

        // probably english word or smth
        if (curNode == null)
            return null;

        for (int i = 1; i < sWord.length(); i++) {
            String grapheme = String.valueOf(sWord.charAt(i));
            curNode = curNode.getChildren().get(grapheme);

            if (curNode == null)
                return null;
        }

        if (curNode.getWords().isEmpty())
            return null;

        // currently, returns a first word from set
        return curNode.getWords().iterator().next();
    }
}
