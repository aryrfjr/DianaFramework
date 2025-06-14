package org.dianaframework.xml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 
 * A basic XML DOM parser.
 * 
 * @version $Id: XMLDOMParser.java 1594 2007-02-07 11:12:15Z bruno $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public class XMLDOMParser {
private Tag root = null;

    public void parse(String file) throws IOException, XMLDOMParserException {
        parse(new FileInputStream(file));
    }

    public void parse(InputStream file) throws IOException, XMLDOMParserException {
        DOMParser parser = new DOMParser();
        try {
            parser.parse(new InputSource((InputStream)file));
            Document doc = parser.getDocument();
            root = createTag(doc.getDocumentElement());
            loadTag(doc.getDocumentElement().getChildNodes(), root);
        } catch (IOException ioe) {
            throw ioe;
        } catch (Exception e) {
            throw new XMLDOMParserException("Exception when parse.\n"+e.toString());
        }
    }

    public Tag getRoot() {
        return root;
    }

    private void loadTag(NodeList children, Tag father) throws IOException, XMLDOMParserException {
        Tag tag = null;
        for (int i=0; i< children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                tag = createTag(children.item(i));
                father.addChild(tag);
                loadTag(children.item(i).getChildNodes(), tag);
            } else if (children.item(i).getNodeType() == Node.TEXT_NODE && !children.item(i).getNodeValue().trim().equals("") && children.item(i).getParentNode() != null && children.item(i).getParentNode().getNodeName().equalsIgnoreCase("fragmento-sql")) {
                father.setContent(children.item(i).getNodeValue().trim());
            }
        }
    }

    private Tag createTag(Node tag) {
        Tag ret = new Tag();
        ret.setName(tag.getNodeName());
        ret.setContent(tag.getNodeValue());
        loadTagAttributes(ret, tag);
        return ret;
    }

    private void loadTagAttributes(Tag tag, Node node) {
        NamedNodeMap attributes = node.getAttributes();
        for (int inc = 0; inc < attributes.getLength(); inc++)
            tag.addAttribute(new TagAttribute(attributes.item(inc).getNodeName(), attributes.item(inc).getNodeValue()));
    }

}
