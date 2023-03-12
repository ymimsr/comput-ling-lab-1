package ru.nsu.fit.comput_ling_lab_1;

import ru.nsu.fit.comput_ling_lab_1.domain.Dictionary;
import ru.nsu.fit.comput_ling_lab_1.domain.Grammeme;
import ru.nsu.fit.comput_ling_lab_1.domain.Lemma;
import ru.nsu.fit.comput_ling_lab_1.domain.Word;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StAXDictionaryParser implements IDictionaryParser{

    // <grammemes> tag related constants
    private static final String GRAMMEMES_TAG = "grammemes";
    private static final String GRAMMEME_TAG = "grammeme";
    private static final String NAME_TAG = "name";
    private static final String ALIAS_TAG = "alias";
    private static final String DESCRIPTION_TAG = "description";
    private static final String PARENT_ATTRIBUTE = "parent";

    // <lemmas> tag related constants
    private static final String LEMMAS_TAG = "lemmas";
    private static final String LEMMA_TAG = "lemma";
    private static final String LEMMA_CONTENT_TAG = "l";
    private static final String LEMMA_GRAMMEME_TAG = "g";
    private static final String LEMMA_CONTENT_ATTRIBUTE = "t";
    private static final String LEMMA_GRAMMEME_ATTRIBUTE = "v";

    //<f> tag related constants
    private static final String WORD_TAG = "f";
    private static final String WORD_GRAMMEME_TAG = "g";
    private static final String WORD_CONTENT_ATTRIBUTE = "t";
    private static final String WORD_GRAMMEME_ATTRIBUTE = "v";

    @Override
    public Dictionary parse(File file) throws FileNotFoundException {
        Dictionary dictionary = new Dictionary();

        XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
        XMLEventReader xmlEventReader;
        try {
            xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(file));

            parseGrammemes(dictionary, xmlEventReader);
            parseLemmas(dictionary, xmlEventReader);

            return dictionary;
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

        return null;
    }

    // example
    // <grammeme parent="POST"><name>NOUN</name><alias>СУЩ</alias><description>имя существительное</description></grammeme>
    private void parseGrammemes(Dictionary dictionary, XMLEventReader xmlEventReader) throws XMLStreamException{
        Map<String, Grammeme> grammemes = dictionary.getNameGrammemeMap();

        // All should be initialized, parentName can be blank though
        String parentName = "";
        String name = "";
        String alias = "";
        String description = "";

        while (xmlEventReader.hasNext()) {
            XMLEvent nextEvent = xmlEventReader.nextEvent();

            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                switch (startElement.getName().getLocalPart()) {
                    case GRAMMEME_TAG -> {
                        Attribute aParentName = startElement.getAttributeByName(new QName(PARENT_ATTRIBUTE));
                        parentName = aParentName.getValue();
                    }
                    case NAME_TAG -> {
                        nextEvent = xmlEventReader.nextEvent();
                        name = nextEvent.asCharacters().getData();
                    }
                    case ALIAS_TAG -> {
                        nextEvent = xmlEventReader.nextEvent();
                        alias = nextEvent.asCharacters().getData();
                    }
                    case DESCRIPTION_TAG -> {
                        nextEvent = xmlEventReader.nextEvent();
                        description = nextEvent.asCharacters().getData();
                    }
                }
            }

            if (nextEvent.isEndElement()) {
                EndElement endElement = nextEvent.asEndElement();
                if (endElement.getName().getLocalPart().equals(GRAMMEME_TAG)) {
                    Grammeme grammeme;

                    if (grammemes.containsKey(name)) {
                        grammeme = grammemes.get(name);
                    } else {
                        grammeme = new Grammeme();
                        grammeme.setName(name);
                        grammemes.put(name, grammeme);
                    }

                    grammeme.setAlias(alias);
                    grammeme.setDescription(description);

                    if (!parentName.isEmpty()) {
                        Grammeme parentGrammeme;
                        if (grammemes.containsKey(parentName)) {
                            parentGrammeme = grammemes.get(parentName);
                        } else {
                            parentGrammeme = new Grammeme();
                            parentGrammeme.setName(parentName);
                            grammemes.put(parentName, parentGrammeme);
                        }

                        grammeme.setParent(parentGrammeme);
                    }

                    parentName = "";
                    name = "";
                    alias = "";
                    description = "";
                }
                else if (endElement.getName().getLocalPart().equals(GRAMMEMES_TAG)) {
                    return;
                }
            }
        }
    }

    // example
    // <lemma id="3" rev="3"><l t="ёжик"><g v="NOUN"/><g v="anim"/><g v="masc"/></l><f t="ёжик"><g v="sing"/>
    // <g v="nomn"/></f><f t="ёжика"><g v="sing"/><g v="gent"/></f><f t="ёжику"><g v="sing"/><g v="datv"/></f>
    // <f t="ёжика"><g v="sing"/><g v="accs"/></f><f t="ёжиком"><g v="sing"/><g v="ablt"/></f><f t="ёжике">
    // <g v="sing"/><g v="loct"/></f><f t="ёжики"><g v="plur"/><g v="nomn"/></f><f t="ёжиков"><g v="plur"/>
    // <g v="gent"/></f><f t="ёжикам"><g v="plur"/><g v="datv"/></f><f t="ёжиков"><g v="plur"/><g v="accs"/>
    // </f><f t="ёжиками"><g v="plur"/><g v="ablt"/></f><f t="ёжиках"><g v="plur"/><g v="loct"/></f></lemma>
    private void parseLemmas(Dictionary dictionary, XMLEventReader xmlEventReader) throws XMLStreamException {
        Set<Lemma> lemmata = dictionary.getLemmas();

        Lemma lemma = new Lemma();
        Set<Grammeme> lemmaGrammemes = new HashSet<>();

        while (xmlEventReader.hasNext()) {
            XMLEvent nextEvent = xmlEventReader.nextEvent();

            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                switch (startElement.getName().getLocalPart()) {
                    case LEMMA_CONTENT_TAG -> {
                        Attribute aLemmaContent = startElement.getAttributeByName(new QName(LEMMA_CONTENT_ATTRIBUTE));
                        lemma.setContent(aLemmaContent.getValue());
                    }
                    case LEMMA_GRAMMEME_TAG -> {
                        Attribute aLemmaGrammeme = startElement.getAttributeByName(new QName(LEMMA_GRAMMEME_ATTRIBUTE));
                        lemmaGrammemes.add(dictionary.getNameGrammemeMap().get(aLemmaGrammeme.getValue()));
                    }
                }
            }

            if (nextEvent.isEndElement()) {
                EndElement endElement = nextEvent.asEndElement();
                switch (endElement.getName().getLocalPart()) {
                    case LEMMAS_TAG -> {
                        return;
                    }
                    case LEMMA_TAG -> {
                        lemmata.add(lemma);

                        lemma = new Lemma();
                        lemmaGrammemes = new HashSet<>();
                    }
                    case LEMMA_CONTENT_TAG -> {
                        lemma.setGrammemes(lemmaGrammemes);
                        parseWords(dictionary, lemma, xmlEventReader);
                    }
                }
            }
        }
    }

    // example
    // <f t="ёжик"><g v="sing"/><g v="nomn"/></f>
    private void parseWords(Dictionary dictionary, Lemma lemma, XMLEventReader xmlEventReader) throws XMLStreamException {
        Set<Word> words = new HashSet<>();

        Word word = new Word();
        Set<Grammeme> wordGrammemes = new HashSet<>();

        while (xmlEventReader.hasNext()) {
            XMLEvent peekEvent = xmlEventReader.peek();
            if (peekEvent.isEndElement()) {
                EndElement endElement = peekEvent.asEndElement();
                if (endElement.getName().getLocalPart().equals(LEMMA_TAG)) {
                    lemma.setWords(words);
                    dictionary.getWords().addAll(words);
                    return;
                }
            }

            XMLEvent nextEvent = xmlEventReader.nextEvent();

            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                switch (startElement.getName().getLocalPart()) {
                    case WORD_TAG -> {
                        Attribute aWordContent = startElement.getAttributeByName(new QName(WORD_CONTENT_ATTRIBUTE));
                        word.setContent(aWordContent.getValue());
                    }
                    case WORD_GRAMMEME_TAG -> {
                        Attribute aWordGrammeme = startElement.getAttributeByName(new QName(WORD_GRAMMEME_ATTRIBUTE));
                        wordGrammemes.add(dictionary.getNameGrammemeMap().get(aWordGrammeme.getValue()));
                    }
                }
            }

            if (nextEvent.isEndElement()) {
                EndElement endElement = nextEvent.asEndElement();
                if (endElement.getName().getLocalPart().equals(WORD_TAG)) {
                    word.setParent(lemma);
                    word.setGrammemes(wordGrammemes);
                    words.add(word);

                    word = new Word();
                    wordGrammemes = new HashSet<>();
                }
            }
        }
    }

}
