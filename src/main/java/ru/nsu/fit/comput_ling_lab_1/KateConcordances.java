package ru.nsu.fit.comput_ling_lab_1;

import ru.nsu.fit.comput_ling_lab_1.domain.Dictionary;
import ru.nsu.fit.comput_ling_lab_1.domain.Grammeme;
import ru.nsu.fit.comput_ling_lab_1.domain.TreeDictionary;
import ru.nsu.fit.comput_ling_lab_1.domain.TreeDictionaryMapper;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public class KateConcordances {
    public static void main(String[] args) throws URISyntaxException {
        System.out.println("Parsing XML dictionary...");
        Dictionary dictionary = parseXMLDictionary();
        System.out.println("Finished parsing XML dictionary");
        TreeDictionaryMapper treeDictionaryMapper = new TreeDictionaryMapper();
        System.out.println("Initializing internal representation...");
        TreeDictionary treeDictionary = treeDictionaryMapper.mapToTreeDictionary(dictionary);
        System.out.println("Finished building IR");
        System.out.println("Ready to analyze");

        String testModel = "ПРИЛ ср им + СУЩ ср им";
        List<String> s = ModelAnalyzer.analyzeByModel(
                new File(Solution.class.getResource("barbies").toURI()), treeDictionary, testModel);
        for (String str : s) {
            System.out.println(str);
        }


//        HashMap<String, Integer> result = ConcordFinder.findConcordances(
//                new File(Solution.class.getResource("barbies").toURI()),
//                treeDictionary, "красивая кукла", 4, 4);
//        System.out.println(result.size());
//        for (Map.Entry<String, Integer> entry : result.entrySet()) System.out.println(entry.getValue() + " " + entry.getKey());
    }
    private static Dictionary parseXMLDictionary() {
        try {
            IDictionaryParser dictionaryParser = new StAXDictionaryParser();

            return dictionaryParser.parse(
                    new File(Solution.class
                            .getResource("dict.opcorpora.xml").toURI())
            );
        } catch (ParserConfigurationException | URISyntaxException | FileNotFoundException exception) {
            exception.printStackTrace();

            return null;
        }
    }
}
