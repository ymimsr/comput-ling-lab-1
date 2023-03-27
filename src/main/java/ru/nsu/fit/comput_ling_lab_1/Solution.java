package ru.nsu.fit.comput_ling_lab_1;

import ru.nsu.fit.comput_ling_lab_1.domain.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Map;
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
        MorphAnalyzer morphAnalyzer = new MorphAnalyzer(treeDictionary);

        try {
            File rootFolder = new File("E:/projects/comput-ling-lab-1/src/main/resources/ru/nsu/fit/comput_ling_lab_1/text_corpus");

            for (final File file : rootFolder.listFiles()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String text = reader.readLine();
                morphAnalyzer.analyze(text);
                reader.close();
            }

            morphAnalyzer.printResults();
        } catch (IOException exception) {
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
