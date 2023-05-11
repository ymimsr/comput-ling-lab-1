package ru.nsu.fit.comput_ling_lab_1.ngram;

import ru.nsu.fit.comput_ling_lab_1.MorphAnalyzer;
import ru.nsu.fit.comput_ling_lab_1.domain.Lemma;
import ru.nsu.fit.comput_ling_lab_1.domain.TreeDictionary;
import ru.nsu.fit.comput_ling_lab_1.domain.Word;
import java.io.*;
import java.util.*;


public class NGramAnalyzer {

    private final TreeDictionary treeDictionary;
    private final MorphAnalyzer morphAnalyzer;
    private final List<List<Lemma>> textsLemmas = new ArrayList<>();
    private final Map<Lemma, List<NGram>> lemmaNGram = new HashMap<>();
    private final Map<Integer, List<NGram>> nGramsByN = new HashMap<>();

    public NGramAnalyzer(TreeDictionary treeDictionary, MorphAnalyzer morphAnalyzer) {
        this.treeDictionary = treeDictionary;
        this.morphAnalyzer = morphAnalyzer;
    }

    public void analyze(
            File rootFolder,
            int threshold,
            int n
    ) {
        for (final File file : Objects.requireNonNull(rootFolder.listFiles())) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuilder text = new StringBuilder();
                String newLine;
                while ((newLine = reader.readLine()) != null) {
                    text.append(newLine).append("\n");
                }

                List<String> textTokens = morphAnalyzer.getTextTokens(text.toString());
                textsLemmas.add(tokensToLemmas(textTokens));

                reader.close();
            } catch (IOException exception) {
                exception.printStackTrace();
                return;
            }
        }

        build2Grams(threshold);
        for (int i = 3; i <= n; i++) {
            buildNPlusOneGrams(i, threshold);
        }
    }

    public List<NGram> findByWord(String sWord) {
        Word word;
        if ((word = treeDictionary.getWord(sWord)) == null)
            return new ArrayList<>();

        Lemma lemma = word.getParent();
        return lemmaNGram.get(lemma);
    }

    private List<Lemma> tokensToLemmas(List<String> tokens) {
        List<Lemma> lemmas = new ArrayList<>();
        for (String token : tokens) {
            Word word;
            if ((word = treeDictionary.getWord(token)) != null) {
                lemmas.add(word.getParent());
            }
        }

        return lemmas;
    }

    private void buildNPlusOneGrams(int n, int threshold) {
        nGramsByN.put(n, new ArrayList<>());

        for (NGram nGram : nGramsByN.get(n - 1)) {
            for (Map.Entry<Integer, List<Integer>> pair : nGram.getTextEntries().entrySet()) {
                int textIndex = pair.getKey();
                for (int index : pair.getValue()) {
                    if (index + n >= textsLemmas.get(textIndex).size())
                        continue;

                    Lemma lemma = textsLemmas.get(textIndex).get(index + n);
                    List<Lemma> extendedLemmas = new ArrayList<>(nGram.getLemmas());
                    extendedLemmas.add(lemma);

                    // otherwise, such nGram hasn't been parsed yet
                    if (nGramsByN.get(n).stream().anyMatch(it -> it.getLemmas().equals(extendedLemmas))) {
                        NGram presNGram = nGramsByN.get(n)
                                .stream()
                                .filter(it -> it.getLemmas().equals(extendedLemmas))
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException("shouldn't be thrown"));

                        presNGram.setFrequency(presNGram.getFrequency() + 1);
                        if (!presNGram.getTextEntries().containsKey(textIndex)) {
                            List<Integer> indexes = new ArrayList<>();
                            indexes.add(index);

                            presNGram.getTextEntries().put(textIndex, indexes);
                            presNGram.setTextFrequency(presNGram.getTextFrequency() + 1);
                        } else {
                            presNGram.getTextEntries().get(textIndex).add(index);
                        }
                    } else {
                        addNewNGram(extendedLemmas, n, textIndex, index);
                    }
                }
            }
        }

        List<NGram> toRemove = new ArrayList<>();
        for (NGram nGram : nGramsByN.get(n)) {
            if (nGram.getFrequency() < threshold) {
                toRemove.add(nGram);
            }
        }

        for (NGram nGram : toRemove) {
            removeNGram(nGram);
        }
    }

    private void build2Grams(int threshold) {
        for (int k = 0; k < textsLemmas.size(); k++) {
            List<Lemma> textLemmas = textsLemmas.get(k);
            for (int i = 0; i < textLemmas.size() - 1; i++) {
                List<Lemma> lemmas = new ArrayList<>();

                Lemma lemma = textLemmas.get(i);
                Lemma nextLemma = textLemmas.get(i + 1);

                lemmas.add(lemma);
                lemmas.add(nextLemma);

                // otherwise, such nGram hasn't been parsed yet
                if (lemmaNGram.containsKey(lemma) && lemmaNGram.containsKey(nextLemma)) {
                    NGram nGram = lemmaNGram.get(lemma)
                            .stream()
                            .filter(it -> it.getLemmas().equals(lemmas))
                            .findFirst()
                            .orElse(null);

                    if (nGram != null) {
                        nGram.setFrequency(nGram.getFrequency() + 1);
                        if (!nGram.getTextEntries().containsKey(k)) {
                            List<Integer> indexes = new ArrayList<>();
                            indexes.add(i);

                            nGram.getTextEntries().put(k, indexes);
                            nGram.setTextFrequency(nGram.getTextFrequency() + 1);
                        } else {
                            nGram.getTextEntries().get(k).add(i);
                        }
                    } else {
                        addNewNGram(lemmas, 2, k, i);
                    }
                } else {
                    addNewNGram(lemmas, 2, k, i);
                }
            }
        }

        List<NGram> toRemove = new ArrayList<>();
        for (NGram nGram : nGramsByN.get(2)) {
            if (nGram.getFrequency() < threshold) {
                toRemove.add(nGram);
            }
        }

        for (NGram nGram : toRemove) {
            removeNGram(nGram);
        }
    }

    private void addNewNGram(List<Lemma> lemmas, int n, int textIndex, int index) {
        NGram newNGram = new NGram();

        List<Integer> indexes = new ArrayList<>();
        indexes.add(index);

        newNGram.setLemmas(lemmas);
        newNGram.setN(n);
        newNGram.setTextFrequency(1);
        newNGram.setFrequency(1);
        newNGram.getTextEntries().put(textIndex, indexes);

        for (Lemma lemma : lemmas) {
            if (lemmaNGram.containsKey(lemma)) {
                lemmaNGram.get(lemma).add(newNGram);
            } else {
                List<NGram> nGrams = new ArrayList<>();
                nGrams.add(newNGram);
                lemmaNGram.put(lemma, nGrams);
            }
        }

        if (nGramsByN.containsKey(n)) {
            nGramsByN.get(n).add(newNGram);
        } else {
            List<NGram> nGrams = new ArrayList<>();
            nGrams.add(newNGram);
            nGramsByN.put(n, nGrams);
        }
    }

    private void removeNGram(NGram nGram) {
        nGramsByN.get(nGram.getN()).remove(nGram);
        for (Lemma lemma : nGram.getLemmas()) {
            lemmaNGram.get(lemma).remove(nGram);
        }
    }

}
