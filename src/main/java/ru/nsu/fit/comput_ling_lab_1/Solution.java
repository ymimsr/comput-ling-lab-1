package ru.nsu.fit.comput_ling_lab_1;

import ru.nsu.fit.comput_ling_lab_1.domain.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URISyntaxException;
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
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while ((line = consoleReader.readLine()) != null && !line.equals("") && !line.equals("стоп")) {
                morphAnalyzer.analyze(line);
            }
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
