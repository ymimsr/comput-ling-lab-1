package ru.nsu.fit.comput_ling_lab_1;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

import ru.nsu.fit.comput_ling_lab_1.domain.TreeDictionary;

public class ConcordFinder {
    /**
     * Finds all concordances of a given phrase in a given texts.
     * @param rootFolder path to a folder containing source texts
     * @param treeDictionary dictionary used for words recognition
     * @param phrase a goal phrase
     * @param leftAppendix a number of words showed to the left of the goal phrase
     * @param rightAppendix a number of words showed to the right of the goal phrase
     * @return a map of concordances sorted by frequency
     */
    private static HashMap<String, Integer> concordances = new HashMap<>();
    public static HashMap<String, Integer> findConcordances(File rootFolder, TreeDictionary treeDictionary, String phrase, Integer leftAppendix, Integer rightAppendix) {
        MorphAnalyzer m = new MorphAnalyzer(treeDictionary);
        String [] goal = phrase.split("[ \n\r]");
        for (int i = 0; i < goal.length; i++) {
            if (treeDictionary.getWord(goal[i]) != null)
                goal[i] = treeDictionary.getWord(goal[i]).getParent().getContent();
        }
        try {
            for (final File file : Objects.requireNonNull(rootFolder.listFiles())) {
                String text = new String(Files.readAllBytes(file.toPath()), Charset.forName("windows-1251"));
                String [] tokens = text.split("[ \n\r]");
                String [] normals = new String[tokens.length];
                for (int i = 0; i < tokens.length; i++) {
                    List<String> list = m.getTextTokens(tokens[i]);
                    if (list.isEmpty())  {
                        normals[i] = "";
                        continue;
                    }
                    normals[i] = list.get(0);
                    if (treeDictionary.getWord(normals[i]) != null)
                        normals[i] = treeDictionary.getWord(normals[i]).getParent().getContent();
                }
                for (int i = 1; i <= leftAppendix; i++)
                    NConcordances(normals, goal, tokens, i, 0);
                for (int i = 0; i <= leftAppendix; i++)
                    for (int j = 1; j <= rightAppendix; j++)
                        NConcordances(normals, goal, tokens, i, j);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        ArrayList<Integer> list = new ArrayList<>();
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : concordances.entrySet()) {
            list.add(entry.getValue());
        }
        Collections.sort(list);
        Collections.reverse(list);
        for (int num : list) {
            for (Map.Entry<String, Integer> entry : concordances.entrySet()) {
                if (entry.getValue().equals(num)) {
                    sortedMap.put(entry.getKey(), num);
                }
            }
        }

        return sortedMap;
    }
    private static void NConcordances(String[] normals, String[] goal, String[] tokens, Integer leftAppendix, Integer rightAppendix){
        for (int i = leftAppendix; i < normals.length - goal.length - rightAppendix + 1; i++) {
            for (int k = 0; k < goal.length; k++) {
                if (!normals[i+k].equals(goal[k]))
                    break;
                if (k + 1 == goal.length) {
                    StringBuilder concordanceBuild = new StringBuilder(tokens[i]);
                    for (int j = i + 1; j < goal.length + i; j++) {
                        concordanceBuild.append(" ").append(tokens[j]);
                    }
                    for (int j = i - 1; j >= i - leftAppendix; j--) {
                        concordanceBuild.insert(0, tokens[j] + " ");
                    }
                    for (int j = i + 1; j <= i + rightAppendix; j++) {
                        concordanceBuild.append(" ").append(tokens[j + goal.length - 1]);
                    }
                    String concordance = concordanceBuild.toString();
                    if (concordances.containsKey(concordance))
                        concordances.replace(concordance, concordances.get(concordance) + 1);
                    else
                        concordances.put(concordance, 1);
                }
            }
        }
    }
}
