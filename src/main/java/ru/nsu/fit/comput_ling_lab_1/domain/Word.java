package ru.nsu.fit.comput_ling_lab_1.domain;

import java.util.Set;

public class Word {

    private Lemma parent;
    private String content;
    // grammemes related to certain word form, such as plural/singular, etc
    private Set<Grammeme> grammemes;

    public Lemma getParent() {
        return parent;
    }

    public String getContent() {
        return content;
    }

    public Set<Grammeme> getGrammemes() {
        return grammemes;
    }

    public void setParent(Lemma parent) {
        this.parent = parent;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setGrammemes(Set<Grammeme> grammemes) {
        this.grammemes = grammemes;
    }
}
