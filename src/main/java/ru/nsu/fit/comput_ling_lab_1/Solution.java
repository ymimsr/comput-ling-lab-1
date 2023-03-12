package ru.nsu.fit.comput_ling_lab_1;

import ru.nsu.fit.comput_ling_lab_1.domain.Dictionary;
import ru.nsu.fit.comput_ling_lab_1.domain.Grammeme;
import ru.nsu.fit.comput_ling_lab_1.domain.Lemma;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;

public class Solution {

    public static void main(String[] args) {
        IDictionaryParser dictionaryParser = new StAXDictionaryParser();
        try {
            Dictionary dictionary = dictionaryParser.parse(
                    new File(Solution.class
                            .getResource("dictopcorpora.xml").toURI())
            );
            for (Map.Entry<String, Grammeme> pair : dictionary.getNameGrammemeMap().entrySet()) {
                Grammeme grammeme = pair.getValue();
                System.out.println(grammeme.getName() + " " + grammeme.getAlias() + " " + grammeme.getDescription() +
                        " " + Optional.ofNullable(grammeme.getParent()).orElse(new Grammeme()).getName());
            }

            for (Lemma lemma : dictionary.getLemmas()) {
                System.out.println(lemma.getContent());
            }
        } catch (ParserConfigurationException | URISyntaxException | FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }
}
