package com.neo.whylearnenglish.bean;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * XML转Letter对象
 *
 * Created by Neo on 2016/12/15.
 */

public class Xml2Letter {

    private static Letter letter;
    private static StringBuilder builder;
    private static final String KEY = "key";
    private static final String PS = "ps";
    private static final String PRON = "pron";
    private static final String POS = "pos";
    private static final String ACCEPTATION = "acceptation";
    private static final String SENT = "sent";
    private static final String ORIG = "orig";
    private static final String TRANS = "trans";
    private static Letter.POS pos;
    private static List<Letter.POS> posList;
    private static ArrayList<Object> sentenceList;
    private static Letter.Pronunciation pronunciation;
    private static List<Letter.Pronunciation> pronList;
    private static Letter.Sentence sentence;

    /**
     * 解析请求广告图xml字符串为Map
     *
     * @param str
     * @return
     */
    public static Letter parseImageXml(String str) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes());
            parser.parse(is, new MapHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return letter;
    }

    private static class MapHandler extends DefaultHandler {

        private Letter.Sentence sentence;

        public MapHandler() {
            letter = new Letter();
            letter.pronunciationList = new ArrayList<>();
            letter.posList = new ArrayList<>();
            letter.sentenceList = new ArrayList<>();
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            builder = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if (localName.equals(PS)) {
                pronunciation = new Letter.Pronunciation();
            } else if (localName.equals(PRON)) {
                if (pronunciation == null) {
                    pronunciation = new Letter.Pronunciation();
                }
            } else {
                pronunciation = null;
            }

            if(localName.equals(SENT)){
                sentence = new Letter.Sentence();
            }
            builder.setLength(0);// 将字符长度设为0，重新开始读取元素内的字符节点
        }

        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            super.characters(ch, start, length);
            builder.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            super.endElement(uri, localName, qName);
            if (localName.equals(KEY)) {
                letter.key = builder.toString();
            } else if (localName.equals(PS)) {
                pronunciation = new Letter.Pronunciation();
                pronunciation.ps = builder.toString();
            } else if (localName.equals(PRON)) {
                if(null != pronunciation){
                    pronunciation.pron = builder.toString();
                    letter.pronunciationList.add(pronunciation);
                    pronunciation = null;
                }
            } else if (localName.equals(POS)) {
                pos = new Letter.POS();
                pos.posName = builder.toString();
            } else if (localName.equals(ACCEPTATION)) {
                if (null != pos) {
                    pos.acceptation = builder.toString();
                    letter.posList.add(pos);
                    pos = null;
                }
            } else if (localName.equals(ORIG)) {
                sentence.orig = builder.toString();
            } else if (localName.equals(TRANS)){
                sentence.trans = builder.toString();
            } else if (localName.equals(SENT)){
                letter.sentenceList.add(sentence);
            }
        }

    }
}