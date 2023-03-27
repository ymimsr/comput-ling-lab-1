package ru.nsu.fit.comput_ling_lab_1;

import ru.nsu.fit.comput_ling_lab_1.domain.Grammeme;
import ru.nsu.fit.comput_ling_lab_1.domain.Lemma;
import ru.nsu.fit.comput_ling_lab_1.domain.TreeDictionary;
import ru.nsu.fit.comput_ling_lab_1.domain.Word;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MorphAnalyzer {

    private final TreeDictionary treeDictionary;
    private final Map<Lemma, Integer> lemmaFrequencyMap = new HashMap<>();
    private final Map<Lemma, Integer> lemmaTextFrequencyMap = new HashMap<>();
    private final Set<Word> resultWords = new HashSet<>();
    private final Set<String> unidentifiedWords = new HashSet<>();

    public MorphAnalyzer(TreeDictionary treeDictionary) {
        this.treeDictionary = treeDictionary;
    }

    public void analyze(String text) {
        // removes spaces, punctuation marks etc
        String[] sWords = text.split("[\\p{Punct}\\s]+");
        Set<Lemma> lemmasInThisText = new HashSet<>();

        for (String sWord : sWords) {
            System.out.println("Analyzing word " + sWord);
            Word word = treeDictionary.getWord(sWord);

            if (word == null) {
                unidentifiedWords.add(sWord);
                continue;
            }

            if (lemmasInThisText.add(word.getParent())) {
                if (lemmaTextFrequencyMap.containsKey(word.getParent())) {
                    lemmaTextFrequencyMap.replace(word.getParent(),
                            lemmaTextFrequencyMap.get(word.getParent()) + 1);
                } else {
                    lemmaTextFrequencyMap.put(word.getParent(), 1);
                }
            }

            if (lemmaFrequencyMap.containsKey(word.getParent())) {
                lemmaFrequencyMap.replace(word.getParent(), lemmaFrequencyMap.get(word.getParent()) + 1);
            } else {
                lemmaFrequencyMap.put(word.getParent(), 1);
            }

            resultWords.add(word);
        }
    }

    public void printResults() {
        for (Word word : resultWords) {
            System.out.println("==========");

            System.out.println("Слово: " + word.getContent());

            System.out.println("Лексема: " + word.getParent().getContent());

            System.out.print("Граммемы: ");

            int index = 0;
            for (Grammeme grammeme : word.getParent().getGrammemes()) {
                System.out.print(grammeme.getDescription());
                if (index != word.getParent().getGrammemes().size() - 1) {
                    System.out.print(", ");
                }

                index++;
            }

            index = 0;
            for (Grammeme grammeme : word.getGrammemes()) {
                if (index == 0) {
                    System.out.print(", ");
                }
                System.out.print(grammeme.getDescription());
                if (index != word.getGrammemes().size() - 1) {
                    System.out.print(", ");
                }

                index++;
            }
            System.out.println();

            System.out.println("Частота: " + lemmaFrequencyMap.get(word.getParent()) + " раз.");
            System.out.println("Текстовая частота: " + lemmaTextFrequencyMap.get(word.getParent()) + " раз.");
        }

        System.out.println("==========");

        System.out.println("Список неопознанных слов: ");

        int index = 0;
        for (String sWord : unidentifiedWords) {
            System.out.print(sWord);
            if (index != unidentifiedWords.size() - 1) {
                System.out.print(", ");
            } else {
                System.out.println();
            }

            index++;
        }

        System.out.println("==========");
    }

}
