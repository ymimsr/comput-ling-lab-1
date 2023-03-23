package ru.nsu.fit.comput_ling_lab_1.domain;

import java.util.*;

public class GraphemeNode {

    private final Map<String, GraphemeNode> children = new HashMap<>();
    private final GraphemeNode parent;
    private final String grapheme;

    // might be two or more words (or empty if node is not end node)
    private final Set<Word> words = new HashSet<>();

    public GraphemeNode(GraphemeNode parent, String grapheme) {
        this.parent = parent;
        this.grapheme = grapheme;
    }

    public boolean isEndNode() {
        return !words.isEmpty();
    }

    public Map<String, GraphemeNode> getChildren() {
        return children;
    }

    public void addChild(GraphemeNode child) {
        children.put(child.getGrapheme(), child);
    }

    public GraphemeNode getParent() {
        return parent;
    }

    public String getGrapheme() {
        return grapheme;
    }

    public Set<Word> getWords() {
        return this.words;
    }

    public void addWord(Word word) {
        words.add(word);
    }
}
