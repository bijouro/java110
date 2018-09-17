package com.saerom.edds.comm;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * xml 내용을 String값으로 받아와 xml의 유효성을 검사하는 유틸클래스
 * 
 * @author Lee sung-hee
 * @since 2009-07-28
 * 
 */
public class UtilOfXMLVaildate extends DefaultHandler {

    private static final UtilOfXMLVaildate instance = new UtilOfXMLVaildate();

    private static UtilOfXMLVaildate getInstance() {
        return instance;
    }

    /**
     * 입력받은 파일명의 xml의 유효성을 검사한다.
     * 
     * @param fileName
     *            검사할 xml 파일 명
     * @return validation이 된 xml 인지 확인
     */
    public static boolean isVaildated(String fileName) {
        boolean result = true;
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(true);
        SAXParser parser;

        try {
            factory.setFeature(
                    "http://apache.org/xml/features/validation/schema", true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            parser = factory.newSAXParser();
            parser.parse(fileName, UtilOfXMLVaildate.getInstance());

        } catch (SAXException e) {
            result = false;
        } catch (IOException e) {
            System.out.println("존재하지 않는 문서");
            result = false;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return result;

    }

    /**
     * xml 내용을 String 으로 받와 검사한다
     * 
     * @param source
     *            검사할 xml 문자열
     * @return
     */
    public static boolean isValidatedForString(String source) {
        boolean result = true;

        InputStream stream = new ByteArrayInputStream(source.getBytes());

        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(true);
        SAXParser parser;

        try {
            factory.setFeature(
                    "http://apache.org/xml/features/validation/schema", true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            parser = factory.newSAXParser();
            parser.parse(stream, UtilOfXMLVaildate.getInstance());

        } catch (SAXException e) {
            result = false;
        } catch (IOException e) {
            System.out.println("존재하지 않는 문서");
            result = false;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return result;
    }

    // warning 이벤트가 발생했을 때 실행
    public void warning(SAXParseException e) throws SAXException {
        // throw new SAXException("warning 이벤트 처리");
        System.out.println("warning 발생");
        System.out.println(e.getLineNumber() + "행 :: " + e.getColumnNumber()+"열");
        throw new SAXException(e);
    }

    // Validation 에러 이벤트가 발생했을 때 실행
    public void error(SAXParseException e) throws SAXException {
        System.out.println("(DTD 또는 XML Schema 문서 구조 위배)");
        System.out.println(e.getLineNumber() + "행 :: " + e.getColumnNumber()+"열");
        throw new SAXException(e);
    }

    // Well-formed 에러 이벤트가 발생했을 때 실행
    public void fatalError(SAXParseException e) throws SAXException {
        System.out.print("(XML 권고안의 내용 위배)");
        System.out.println();
        System.out.println(e.getLineNumber() + "행 :: " + e.getColumnNumber()+"열");
        throw new SAXException(e);
    }

}