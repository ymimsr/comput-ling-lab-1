package ru.nsu.fit.comput_ling_lab_1;

import ru.nsu.fit.comput_ling_lab_1.domain.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Solution {

    public static void main(String[] args) {
        System.out.println("Parsing XML dictionary...");
        Dictionary dictionary = parseXMLDictionary();
        System.out.println("Finished parsing XML dictionary");
        TreeDictionaryMapper treeDictionaryMapper = new TreeDictionaryMapper();
        System.out.println("Initializing internal representation...");
        TreeDictionary treeDictionary = treeDictionaryMapper.mapToTreeDictionary(dictionary);
        System.out.println("Finished building IR");
        System.out.println("Ready to analyze");
        //MorphAnalyzer morphAnalyzer = new MorphAnalyzer(treeDictionary);

        File rootFolder = new File("E:/projects/comput-ling-lab-1/src/main/resources/ru/nsu/fit/comput_ling_lab_1/text_corpus");
        Map<String, Integer> concordances =
                ConcordFinder.findConcordances(rootFolder, treeDictionary, "убийство", 5, 5);
//        for (Map.Entry<> x : concordances) {
//            System.out.println(x);
//        }
        //            for (final File file : Objects.requireNonNull(rootFolder.listFiles())) {
//                BufferedReader reader = new BufferedReader(new FileReader(file));
//                StringBuilder text = new StringBuilder();
//                String newLine;
//                while ((newLine = reader.readLine()) != null) {
//                    text.append(newLine).append("\n");
//                }
//
//                reader.close();
//            }
//
//            morphAnalyzer.printResults();
//            morphAnalyzer.printStats();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
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
