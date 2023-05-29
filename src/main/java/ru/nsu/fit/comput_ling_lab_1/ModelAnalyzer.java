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
            if (treeDictionary.getWord(tokens[i]) != null) {
                grammemes.addAll(treeDictionary.getWord(tokens[i]).getGrammemes());
                grammemes.addAll(
                        treeDictionary
                                .getWord(tokens[i])
                                .getParent()
                                .getGrammemes()
                                .stream()
                                .filter(
                                        lemmaGrammeme -> grammemes.stream().noneMatch(
                                                grammeme -> lemmaGrammeme.getParent().getName()
                                                        .equals(grammeme.getParent().getName())
                                        )
                                )
                                .collect(Collectors.toList())
                );
            }
            outerLoop:
            for (int j = 0; j < goal.length; j++) {
                for (; j < goal.length && !goal[j].equals("+"); j++) {
                    int finalJ = j;
                    if (grammemes.stream().noneMatch(p -> p.getAlias().equals(goal[finalJ])))
                        break outerLoop;
                    if (j + 1 >= goal.length) {
                        StringBuilder fragmentBuild = new StringBuilder(tokens[i]);
                        for (int k = i; k < goal.length + i - 1; k++) {
                            if (!(k < tokens.length))
                                break outerLoop;
                            fragmentBuild.append(" ").append(tokens[k]);
                        }
                        String fragment = fragmentBuild.toString();
                        fragments.add(fragment);
                    }
                    else if (goal[j + 1].equals("+")) {
                        i++;
                        j++;
                        if (i >= tokens.length) return;
                        if (treeDictionary.getWord(tokens[i]) != null) {
                            grammemes.addAll(treeDictionary.getWord(tokens[i]).getGrammemes());
                            grammemes.addAll(
                                    treeDictionary
                                            .getWord(tokens[i])
                                            .getParent()
                                            .getGrammemes()
                                            .stream()
                                            .filter(
                                                    lemmaGrammeme -> grammemes.stream().noneMatch(
                                                            grammeme -> lemmaGrammeme.getParent().getName()
                                                                    .equals(grammeme.getParent().getName())
                                                    )
                                            )
                                            .collect(Collectors.toList())
                            );
                        }
                    }

                }
            }
        }
    }
}
