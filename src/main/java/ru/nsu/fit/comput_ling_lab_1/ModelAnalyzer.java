package ru.nsu.fit.comput_ling_lab_1;

import ru.nsu.fit.comput_ling_lab_1.domain.Grammeme;
import ru.nsu.fit.comput_ling_lab_1.domain.TreeDictionary;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class ModelAnalyzer {
    private static List<String> fragments = new ArrayList<>();
    public static List<String> analyzeByModel(File rootFolder, TreeDictionary treeDictionary, String model) {
        MorphAnalyzer m = new MorphAnalyzer(treeDictionary);
        String [] goal = model.split(" ");
        try {
            for (final File file : Objects.requireNonNull(rootFolder.listFiles())) {
                String text = new String(Files.readAllBytes(file.toPath()), Charset.forName("windows-1251"));
                String [] tokens = text.replaceAll("\\p{P}", "").split("[ \n\r]");
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
                singleText(normals, goal, tokens, treeDictionary);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return fragments;
    }
    private static void singleText(String[] normals, String[] goal, String[] tokens, TreeDictionary treeDictionary){
        for (int i = 0; i < tokens.length; i++) {
            final Set<Grammeme> grammemes = new HashSet<>();
            int gap = 0;
            StringBuilder fragmentBuild = new StringBuilder();
            StringBuilder contextBuild = new StringBuilder();
            if (!(i==0)) contextBuild.append(tokens[i - 1]);
            int k_start = 0;
            for (int j = 0; i + j < tokens.length; j++) {
                grammemes.clear();
                getGrammemes(tokens[i + j], treeDictionary, grammemes);
                for (int k = k_start; k < goal.length; k++) {
                    int finalK = k;
                    if ((goal[finalK].charAt(0) == '\"' || (grammemes.stream().noneMatch(p -> p.getAlias().equals(goal[finalK])) &&
                            !(grammemes.stream().anyMatch(p -> p.getAlias().equals("вн")) &&
                                    goal[k].equals("им"))))
                            && (goal[finalK].charAt(0) != '\"' || !Objects.equals(tokens[i + j], goal[finalK].replaceAll("\"", "")))) {
//                    if (grammemes.stream().noneMatch(p -> p.getAlias().equals(goal[finalK])) &&
//                            !(grammemes.stream().anyMatch(p -> p.getAlias().equals("вн")) &&
//                                    goal[k].equals("им"))) {
                        if (j > 0 && gap < 3) {
                            contextBuild.append(" ").append(tokens[i + j]);
                            gap++;
                            break;
                        }
                        j = tokens.length;
                        break;
                    }
                    if (k + 1 >= goal.length) {
                        fragmentBuild.append(" ").append(tokens[i + j]);
                        contextBuild.append(" ").append(tokens[i + j]);
                        if (i + j + 1 < tokens.length)
                            contextBuild.append(" ").append(tokens[i + j + 1]);
                        String fragment = fragmentBuild.toString().trim();
                        String context = contextBuild.toString();
                        fragments.add(fragment);
                        fragments.add(context);
                        j = tokens.length;
                        break;
                    }
                    else if (goal[k + 1].equals("+")) {
                        gap = 0;
                        k++;
                        k_start = k + 1;
                        fragmentBuild.append(" ").append(tokens[i + j]);
                        contextBuild.append(" ").append(tokens[i + j]);
                        j++;
                        if (i + j >= tokens.length) break;
                        grammemes.clear();
                        getGrammemes(tokens[i + j], treeDictionary, grammemes);
                    }
                }
            }
        }
    }

    private static void getGrammemes(String word, TreeDictionary treeDictionary, Set<Grammeme> grammemes) {
        if (treeDictionary.getWord(word) != null) {
            grammemes.addAll(treeDictionary.getWord(word).getGrammemes());
            grammemes.addAll(
                    treeDictionary
                            .getWord(word)
                            .getParent()
                            .getGrammemes()
                            .stream()
                            .filter(
                                    lemmaGrammeme -> {
                                        if (lemmaGrammeme.getParent() != null) {
                                            return grammemes.stream().noneMatch(
                                                    grammeme -> {
                                                        if (grammeme.getParent() != null) {
                                                            return lemmaGrammeme.getParent().getName()
                                                                    .equals(grammeme.getParent().getName());
                                                        } else {
                                                            return false;
                                                        }
                                                    }
                                            );
                                        } else {
                                            return true;
                                        }
                                    }
                            )
                            .collect(Collectors.toList())
            );
        }
    }
}
