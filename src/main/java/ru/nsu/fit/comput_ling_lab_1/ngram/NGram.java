package ru.nsu.fit.comput_ling_lab_1.ngram;

import ru.nsu.fit.comput_ling_lab_1.domain.Lemma;

import java.util.*;

public class NGram {

    private List<Lemma> lemmas = new ArrayList<>();
    private List<NGram> parents = new ArrayList<>();
    private Map<Integer, List<Integer>> textEntries = new HashMap<>();
    private int n;
    private int textFrequency;
    private int frequency;

    public Map<Integer, List<Integer>> getTextEntries() {
        return textEntries;
    }

    public void setTextEntries(Map<Integer, List<Integer>> textEntries) {
        this.textEntries = textEntries;
    }

    public List<Lemma> getLemmas() {
        return lemmas;
    }

    public void setLemmas(List<Lemma> lemmas) {
        this.lemmas = lemmas;
    }

    public List<NGram> getParents() {
        return parents;
    }

    public void setParents(List<NGram> parents) {
        this.parents = parents;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getTextFrequency() {
        return textFrequency;
    }

    public void setTextFrequency(int textFrequency) {
        this.textFrequency = textFrequency;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("<");
        for (Lemma lemma : lemmas) {
            stringBuilder.append(lemma.getContent()).append(",");
        }
        stringBuilder.append(frequency).append(",");
        stringBuilder.append(textFrequency).append(">");
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NGram nGram = (NGram) o;
        return Objects.equals(lemmas, nGram.lemmas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lemmas);
    }
}
