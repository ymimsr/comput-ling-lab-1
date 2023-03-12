package ru.nsu.fit.comput_ling_lab_1.domain;

import java.util.Set;

public class Lemma {

    private String content;
    // grammemes related to lexem, such as part of speech, etc
    private Set<Grammeme> grammemes;
    // different forms of word
    private Set<Word> words;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<Grammeme> getGrammemes() {
        return grammemes;
    }

    public void setGrammemes(Set<Grammeme> grammemes) {
        this.grammemes = grammemes;
    }

    public Set<Word> getWords() {
        return words;
    }

    public void setWords(Set<Word> words) {
        this.words = words;
    }
}
