package com.gravitext.xml.producer;

import java.io.IOException;

import junit.framework.TestCase;

public class IndentorTest extends TestCase
{
    public void testInvarients()
    {
        assertTrue ( Indentor.COMPRESSED.clone().isCompressed() );
        assertTrue (          new Indentor(null).isCompressed() );
        assertFalse( Indentor.LINE_BREAK.clone().isCompressed() );
        assertFalse(           new Indentor("X").isCompressed() );

        assertTrue ( Indentor.LINE_BREAK.clone().isLineBreak() );
        assertTrue (            new Indentor("").isLineBreak() );
        assertFalse( Indentor.COMPRESSED.clone().isLineBreak() );
        assertFalse(           new Indentor("X").isLineBreak() );
    }
    
    public void testLargeIndent() throws IOException
    {
        Indentor i = new Indentor("12");
        StringBuilder b = new StringBuilder(128);
        i.indent( b, 3 );
        assertEquals( "\n121212", b.toString() );
        b.setLength( 0 );
        i.indent( b, 17 );
        assertEquals( 2 * 17 + 1, b.length() );
        b.setLength(  0 );
        i.indent( b, 67 );
        assertEquals( 2 * 67 + 1, b.length() );
    }
    
    public void testCompressed() throws IOException
    {
        Indentor i = Indentor.COMPRESSED.clone();
        StringBuilder b = new StringBuilder(128);
        i.indent( b, 3 );
        assertEquals( "", b.toString() );
    }
    
    public void testLineBreaks() throws IOException
    {
        Indentor i = Indentor.LINE_BREAK.clone();
        StringBuilder b = new StringBuilder(128);
        i.indent( b, 3 );
        assertEquals( "\n", b.toString() );
    }
     
}
