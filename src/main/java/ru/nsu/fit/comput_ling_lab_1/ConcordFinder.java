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
     * @return a list of concordances sorted alphabetically
     */
    public static ArrayList<String> findConcordances(File rootFolder, TreeDictionary treeDictionary, String phrase, Integer leftAppendix, Integer rightAppendix) {
        MorphAnalyzer m = new MorphAnalyzer(treeDictionary);
        String [] goal = phrase.split("[ \n\r]");
        for (int i = 0; i < goal.length; i++) {
            if (treeDictionary.getWord(goal[i]) != null)
                goal[i] = treeDictionary.getWord(goal[i]).getParent().getContent();
        }
        ArrayList<String> concordances = new ArrayList<>();
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
                for (int i = 0; i < normals.length - goal.length + 1; i++) {
                    for (int k = 0; k < goal.length; k++) {
                        if (!normals[i+k].equals(goal[k]))
                            break;
                        if (k + 1 == goal.length) {
                            StringBuilder concordance = new StringBuilder(tokens[i]);
                            for (int j = i + 1; j < goal.length + i; j++) {
                                concordance.append(" ").append(tokens[j]);
                            }
                            for (int j = 1; j <= leftAppendix; j++) {
                                int jk = i - j;
                                if (jk >= 0 && jk < tokens.length)
                                    concordance.insert(0, tokens[jk] + " ");
                            }
                            for (int j = 1; j <= rightAppendix; j++) {
                                int jk = i + j;
                                if (jk >= 0 && jk < tokens.length)
                                    concordance.append(" ").append(tokens[jk + goal.length - 1]);
                            }
                            concordances.add(concordance.toString());
                        }
                    }
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        Collections.sort(concordances);
        return concordances;
    }
}
