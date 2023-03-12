package ru.nsu.fit.comput_ling_lab_1.domain;

import java.util.*;

public class Dictionary {

    // name => grammeme
    private final Map<String, Grammeme> nameGrammemeMap;
    private final Set<Lemma> lemmata;
    private final Set<Word> words;

    public Dictionary(Map<String, Grammeme> nameGrammemeMap, Set<Lemma> lemmata, Set<Word> words) {
        this.nameGrammemeMap = nameGrammemeMap;
        this.lemmata = lemmata;
        this.words = words;
    }

    public Dictionary() {
        this(new HashMap<>(), new HashSet<>(), new HashSet<>());
    }

    public Map<String, Grammeme> getNameGrammemeMap() {
        return nameGrammemeMap;
    }

    public Set<Word> getWords() {
        return words;
    }

    public Set<Lemma> getLemmas() {
        return lemmata;
    }
}
