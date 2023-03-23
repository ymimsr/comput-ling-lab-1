package ru.nsu.fit.comput_ling_lab_1.domain;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class TreeDictionaryMapper {

    public TreeDictionary mapToTreeDictionary(Dictionary dictionary) {
        TreeDictionary treeDictionary = new TreeDictionary();
        Map<String, GraphemeNode> roots = treeDictionary.getRoots();
        Set<Word> words = dictionary.getWords();

        for (Word word : words) {
            String sWord = word.getContent();
            GraphemeNode curNode = roots.get(String.valueOf(sWord.charAt(0)).toLowerCase(Locale.ROOT));
            if (curNode == null) {
                System.out.println(word.getContent());
                System.exit(-1);
            }
            for (int i = 1; i < sWord.length(); i++) {
                String grapheme = String.valueOf(sWord.charAt(i));
                if (curNode.getChildren().containsKey(grapheme)) {
                    curNode = curNode.getChildren().get(grapheme);
                } else {
                    GraphemeNode newNode = new GraphemeNode(curNode, grapheme);
                    curNode.addChild(newNode);
                    curNode = newNode;
                }
            }

            curNode.addWord(word);
        }

        return treeDictionary;
    }

}
