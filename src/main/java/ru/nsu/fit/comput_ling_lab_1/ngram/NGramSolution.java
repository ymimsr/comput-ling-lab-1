package ru.nsu.fit.comput_ling_lab_1.ngram;

import ru.nsu.fit.comput_ling_lab_1.IDictionaryParser;
import ru.nsu.fit.comput_ling_lab_1.MorphAnalyzer;
import ru.nsu.fit.comput_ling_lab_1.Solution;
import ru.nsu.fit.comput_ling_lab_1.StAXDictionaryParser;
import ru.nsu.fit.comput_ling_lab_1.domain.Dictionary;
import ru.nsu.fit.comput_ling_lab_1.domain.TreeDictionary;
import ru.nsu.fit.comput_ling_lab_1.domain.TreeDictionaryMapper;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NGramSolution {

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
        NGramAnalyzer nGramAnalyzer = new NGramAnalyzer(treeDictionary, morphAnalyzer);

        File rootFolder = new File("E:/projects/comput-ling-lab-1/src/main/resources/ru/nsu/fit/comput_ling_lab_1/text_corpus");
        nGramAnalyzer.analyze(rootFolder, 3, 10);

        for (NGram nGram : nGramAnalyzer.findByNestedNGram(List.of("убийство", "человека"))) {
            System.out.println(nGram);
        }

        try {
            System.setOut(new PrintStream(
                    new BufferedOutputStream(
                            new FileOutputStream(
                                    "E:\\projects\\comput-ling-lab-1\\src\\main\\resources\\ru\\nsu\\fit\\comput_ling_lab_1\\ngrams.txt"
                            ))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        nGramAnalyzer.printStats();

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
