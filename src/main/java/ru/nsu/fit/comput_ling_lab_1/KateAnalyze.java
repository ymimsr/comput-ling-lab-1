package ru.nsu.fit.comput_ling_lab_1;

import ru.nsu.fit.comput_ling_lab_1.domain.Dictionary;
import ru.nsu.fit.comput_ling_lab_1.domain.TreeDictionary;
import ru.nsu.fit.comput_ling_lab_1.domain.TreeDictionaryMapper;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Objects;

public class KateAnalyze {
    public static void main(String[] args) {
        System.out.println("Parsing XML dictionary...");
        Dictionary dictionary = parseXMLDictionary();
        System.out.println("Finished parsing XML dictionary");
        TreeDictionaryMapper treeDictionaryMapper = new TreeDictionaryMapper();
        System.out.println("Initializing internal representation...");
        TreeDictionary treeDictionary = treeDictionaryMapper.mapToTreeDictionary(dictionary);
        System.out.println("Finished building IR");
        System.out.println("Ready to analyze");
        MorphAnalyzer m = new MorphAnalyzer(treeDictionary);

        try {
            File rootFolder = new File(Solution.class
                    .getResource("barbies").toURI());

            for (final File file : Objects.requireNonNull(rootFolder.listFiles())) {
                String text = new String(Files.readAllBytes(file.toPath()), Charset.forName("windows-1251"));
                m.analyze(text);
            }

            m.printResults();
            m.printStats();
        } catch (IOException | URISyntaxException exception) {
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
