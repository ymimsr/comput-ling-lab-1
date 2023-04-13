package ru.nsu.fit.comput_ling_lab_1;

import ru.nsu.fit.comput_ling_lab_1.domain.Grammeme;
import ru.nsu.fit.comput_ling_lab_1.domain.Lemma;
import ru.nsu.fit.comput_ling_lab_1.domain.TreeDictionary;
import ru.nsu.fit.comput_ling_lab_1.domain.Word;

import java.util.*;
import java.util.stream.Collectors;

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
        List<String> tokens = getTextTokens(text);
        Set<Lemma> lemmasInThisText = new HashSet<>();

        for (String token : tokens) {
            System.out.println("Analyzing word " + token);
            Word word = treeDictionary.getWord(token);

            if (word == null) {
                unidentifiedWords.add(token);
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

        sort();
    }

    private List<Word> sort() {
        return resultWords.stream()
                .sorted(Comparator.comparing(o -> lemmaFrequencyMap.get(o.getParent())))
                .collect(Collectors.toList());
    }

    public void printResults() {
        List<Word> sortedWords = sort();

        for (Word word : sortedWords) {
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

    public void printStats() {
        System.out.println("Соотношение нераспознанных слов к распознанным: "
                + (100.0 * unidentifiedWords.size() / (resultWords.size() + unidentifiedWords.size())) + "%.");
        System.out.println("Количество распознанных слов: " + resultWords.size());
        System.out.println("Количество нераспознанных слов: " + unidentifiedWords.size());
    }

    public List<String> getTextTokens(String text) {
        List<String> tokens = new ArrayList<>();

        Scanner scanner = new Scanner(text);
        while (scanner.hasNext()) {
            StringBuilder curWord = new StringBuilder();
            String wordToParse = scanner.next();
            for (int i = 0; i < wordToParse.length(); i++) {
                char symbol = wordToParse.charAt(i);

                if (!((symbol >= 'а' && symbol <= 'я')
                        || (symbol >= 'А' && symbol <= 'Я')
                        || (symbol >= '0' && symbol <= '9')
                        || (symbol == '-'))) {
                    if (i == 0) {
                        int newI = -1;
                        for (int k = 0; k < wordToParse.length(); k++) {
                            symbol = wordToParse.charAt(i);
                            if ((symbol >= 'а' && symbol <= 'я')
                                    || (symbol >= 'А' && symbol <= 'Я')
                                    || (symbol >= '0' && symbol <= '9')
                                    || (symbol == '-')) {
                                newI = k;
                            }
                        }

                        if (newI == -1)
                            break;
                    }

                    boolean isInvalidWord = false;
                    for (int k = i + 1; k < wordToParse.length(); k++) {
                        symbol = wordToParse.charAt(i);
                        if ((symbol >= 'а' && symbol <= 'я')
                                || (symbol >= 'А' && symbol <= 'Я')
                                || (symbol >= '0' && symbol <= '9')
                                || (symbol == '-')) {
                            isInvalidWord = true;
                            break;
                        }
                    }

                    if (!isInvalidWord && !curWord.isEmpty())
                        tokens.add(curWord.toString());

                    break;
                } else {
                    curWord.append(symbol);
                }

                if (i == wordToParse.length() - 1 && !curWord.isEmpty())
                    tokens.add(curWord.toString());
            }
        }


        return tokens;
    }

}
