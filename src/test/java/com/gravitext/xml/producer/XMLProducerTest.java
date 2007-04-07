package com.gravitext.xml.producer;


import java.io.IOException;

import junit.framework.TestCase;

public class XMLProducerTest extends TestCase
{ 
    static final Namespace DEF = 
        new Namespace( Namespace.DEFAULT, "urn:foo.def" );
    static final Namespace NS1 = new Namespace( "ns1", "urn:foo.ns1" );
    static final Namespace NSA = new Namespace( "nsa", "urn:foo.nsa" );
     
    static final Tag DOC = new Tag( "doc"      );
    static final Tag SUB = new Tag( "sub"      );
    static final Tag SB2 = new Tag( "sb2"      );
    static final Tag NSB = new Tag( "sub", NS1 );
    static final Tag ODF = new Tag( "odf", DEF );
    
    static final Attribute AT  = new Attribute( "at" );
    static final Attribute AT1 = new Attribute( "at1", NS1 );
    static final Attribute AT2 = new Attribute( "at2", NSA );
    
    public void testTags() throws IOException
    {
        StringBuilder out = new StringBuilder();
        XMLProducer p = new XMLProducer( out );
        p.setIndent( Indentor.PRETTY );
        p.putXMLDeclaration( "ISO-8859-1" );
        p.putComment   ( "dtd" );
        p.putComment   ( "next" );
        p.putSystemDTD ( DOC.name(), "./bogus.dtd" );
        p.putComment   ( "start" );
        p.startTag     ( DOC ).addAttr( "att", "attval" );
        p.putComment   (  "inside" );
        p.startTag     (  SUB );
        p.putChars     (   "<!" ).putChars( "&" );
        p.putComment   (   "more" ).putChars( "chars" ).endTag( SUB );
        p.putComment   (  "almost" );
        p.endTag       ( DOC );
        p.putComment   ( "after" );
        p.putComment   ( "end" );

        assertEquals( "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                      "<!--dtd-->\n" +
                      "<!--next-->\n" +
                      "<!DOCTYPE doc SYSTEM \"./bogus.dtd\">\n" +
                      "<!--start-->\n" +
                      "<doc att=\"attval\">\n" +
                      " <!--inside-->\n" +
                      " <sub>&lt;!&amp;\n" +
                      "  <!--more-->chars</sub>\n" +
                      " <!--almost-->\n" +
                      "</doc>\n" +
                      "<!--after-->\n" +
                      "<!--end-->\n",
                      out.toString() );
    }

    private static enum Colors {
        BLUE
    }
    
    public void testTypes() throws IOException
    {
        StringBuilder out = new StringBuilder();
        XMLProducer p = new XMLProducer( out );
        p.setIndent( Indentor.PRETTY );
        
        p.startTag  ( DOC );
        p.startTag  (  SUB ).putChars( (short) -12 ).endTag();
        p.startTag  (  SUB ).putChars( 1234567     ).endTag();
        p.startTag  (  SUB ).putChars( -123456789L ).endTag();
        p.startTag  (  SUB ).putChars( Colors.BLUE ).endTag();

        p.startTag  (  SUB ).addAttr( AT,   (short) 12  ).endTag();
        p.startTag  (  SUB ).addAttr( AT,   -1234567    ).endTag();
        p.startTag  (  SUB ).addAttr( AT,   1234567890L ).endTag();
        p.startTag  (  SUB ).addAttr( AT,   Colors.BLUE ).endTag();
        
        p.startTag  (  SUB ).addAttr( "at", (short) 12  ).endTag();
        p.startTag  (  SUB ).addAttr( "at", -1234567    ).endTag();
        p.startTag  (  SUB ).addAttr( "at", 1234567890L ).endTag();
        p.startTag  (  SUB ).addAttr( "at", Colors.BLUE ).endTag();
        
        p.endTag    ( DOC );
        
        assertEquals( "<doc>\n" +
                      " <sub>-12</sub>\n" +
                      " <sub>1234567</sub>\n" +
                      " <sub>-123456789</sub>\n" +
                      " <sub>BLUE</sub>\n" +
                      " <sub at=\"12\"/>\n" +
                      " <sub at=\"-1234567\"/>\n" +
                      " <sub at=\"1234567890\"/>\n" +
                      " <sub at=\"BLUE\"/>\n" +
                      " <sub at=\"12\"/>\n" +
                      " <sub at=\"-1234567\"/>\n" +
                      " <sub at=\"1234567890\"/>\n" +
                      " <sub at=\"BLUE\"/>\n" +
                      "</doc>\n",
                      out.toString() );
    }
    
    public void testCompressed() throws IOException
    {
        StringBuilder out = new StringBuilder();
        XMLProducer p = new XMLProducer( out );
        p.setIndent( Indentor.COMPRESSED );
        
        p.startTag( DOC );
        p.startTag(  SUB ).putChars( "val" ).endTag( SUB );
        p.endTag  ( DOC );
        
        assertEquals( "<doc><sub>val</sub></doc>", out.toString() );
    }
       
    public void testCustomIndent() throws IOException
    {
        StringBuilder out = new StringBuilder();
        XMLProducer p = new XMLProducer( out );
        p.setIndent( new Indentor("\t ") );
        
        p.startTag( DOC );
        p.startTag(  SUB );
        p.startTag(   SB2 ).putChars( "val" ).endTag();
        p.endTag  (  SUB );
        p.endTag  ( DOC );
        
        assertEquals( "<doc>\n" +
                      "\t <sub>\n" +
                      "\t \t <sb2>val</sb2>\n" +
                      "\t </sub>\n" +
                      "</doc>\n",
                      out.toString() );
    }
    
    public void test11Encoder() throws IOException
    {
        StringBuilder out = new StringBuilder();
        XMLProducer p = new XMLProducer
            ( new CharacterEncoder( out, Version.V1_1 ) );
        p.setIndent( Indentor.LINE_BREAK );
        p.putXMLDeclaration( "UTF-8" );
        p.startTag( DOC ).putChars( "\b" ).endTag();
        
        assertEquals( "<?xml version=\"1.1\" encoding=\"UTF-8\"?>\n" + 
                      "<doc>&#x8;</doc>\n",
                      out.toString() ); 
    }
  
    public void testNamespaces() throws IOException
    {
        StringBuilder out = new StringBuilder();
        XMLProducer p = new XMLProducer( out );
        p.setIndent( Indentor.PRETTY );
                
        p.startTag( DOC ).addNamespace( DEF );
        p.startTag(  NSB );
        p.startTag(   NSB ).putChars( "val" ).endTag();
        p.startTag(   ODF ).addAttr( AT1, "av1" ).endTag();
        p.endTag  (  NSB );
        p.startTag(  NSB );
        p.startTag(   ODF ).addAttr( AT2, "av2" ).endTag();
        p.endTag  (  NSB );
        p.endTag  ( DOC );
        
        assertEquals( 
            "<doc xmlns=\"urn:foo.def\">\n" +
            " <ns1:sub xmlns:ns1=\"urn:foo.ns1\">\n" +
            "  <ns1:sub>val</ns1:sub>\n" +
            "  <odf ns1:at1=\"av1\"/>\n" +
            " </ns1:sub>\n" +
            " <ns1:sub xmlns:ns1=\"urn:foo.ns1\">\n" +
            "  <odf xmlns:nsa=\"urn:foo.nsa\" nsa:at2=\"av2\"/>\n" +
            " </ns1:sub>\n" +
            "</doc>\n",
            out.toString() );
    }
    
    public void testStateError1() throws IOException
    {
        StringBuilder out = new StringBuilder();
        
        XMLProducer p = new XMLProducer( out );
        try {
            p.putComment( "comment" );
            p.putXMLDeclaration( "UTF-8" );
            fail();
        }
        catch( IllegalStateException e ) {
            System.out.println( "Expected Error: " + e );
        }
    }
    
    public void testStateError2() throws IOException
    {
        StringBuilder out = new StringBuilder();
        
        XMLProducer p = new XMLProducer( out );
        try {
            p.startTag( new Tag( "doc" ) ).endTag();
            p.putComment( "recover?" );
            p.startTag( new Tag( "another" ) );
            fail();
        }
        catch( IllegalStateException e ) {
            System.out.println( "Expected Error: " + e );
        }
    }
}
