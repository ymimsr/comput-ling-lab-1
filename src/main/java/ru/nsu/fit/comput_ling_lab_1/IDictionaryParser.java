package ru.nsu.fit.comput_ling_lab_1;

import ru.nsu.fit.comput_ling_lab_1.domain.Dictionary;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;

public interface IDictionaryParser {

    Dictionary parse(File f) throws ParserConfigurationException, FileNotFoundException;

}
