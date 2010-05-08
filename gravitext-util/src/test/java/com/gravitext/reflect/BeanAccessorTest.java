/*
 * Copyright (c) 2007-2010 David Kellum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gravitext.reflect;

import com.gravitext.reflect.BeanAccessor;
import com.gravitext.reflect.BeanException;

import static org.junit.Assert.*;
import org.junit.Test;

public class BeanAccessorTest
{
    @Test
    public void testPrimativeTypeAssignable()
    {
        // Testing JDK behavior
        assertTrue( Boolean.TYPE.isAssignableFrom( Boolean.TYPE ) );
        assertFalse( Boolean.class.isAssignableFrom( Boolean.TYPE ) );
    }

    @Test
    public void testSetPropertyString()
        throws BeanException
    {
        BeanB bean = new BeanB();
        BeanAccessor accessor = new BeanAccessor( bean );

        accessor.setProperty( "integer", "-45" );
        assertEquals( -45, bean.getInteger() );

        accessor.setProperty( "enum", "VALUE2" );
        assertEquals( SomeEnum.VALUE2, bean.getEnum() );

        accessor.setProperty( "bool", "True"  );
        assertTrue( bean.isBool() );

        accessor.setProperty( "bool", "False"  );
        assertFalse( bean.isBool() );

        accessor.setProperty( "char", "A" );
        assertEquals( 'A', bean.getChar() );

    }

    @Test
    public void testSetPropertyNumber()
        throws BeanException
    {
        BeanB bean = new BeanB();
        BeanAccessor accessor = new BeanAccessor( bean );

        accessor.setProperty( "integer", 1 );
        assertEquals( 1, bean.getInteger() );

        accessor.setProperty( "integer", 2.0f );
        assertEquals( 2, bean.getInteger() );
    }

    @Test
    public void testSetPropertyAssignable()
        throws BeanException
    {
        BeanB bean = new BeanB();
        BeanAccessor accessor = new BeanAccessor( bean );

        accessor.setProperty( "string", "value" );
        assertEquals( "value", bean.getString() );

        StringBuilder sb = new StringBuilder();
        sb.append( "cvalue" );
        accessor.setProperty( "chars", sb );
        assertEquals( "cvalue", bean.getChars().toString() );

        accessor.setProperty( "string", sb );
        assertEquals( "cvalue", bean.getString() );

        accessor.setProperty( "string", null );
        assertNull( bean.getString() );

        Foo foo = new Foo();
        accessor.setProperty( "foo", foo );
        assertEquals( foo, bean.getFoo() );

        accessor.setProperty( "foo", null );
        assertNull( bean.getFoo() );
    }

    @Test
    public void testNullPrimativeError()
    {
        BeanB bean = new BeanB();
        BeanAccessor accessor = new BeanAccessor( bean );

        try {
            accessor.setProperty( "integer", null );
            fail();
        }
        catch( BeanException x ) {
            //System.out.println( "Excepted: " + x );
        }
    }

    private enum SomeEnum {
        VALUE1,
        VALUE2
    }

    private class Foo {
    }

    @SuppressWarnings("unused")
    private class BeanA
    {
        public SomeEnum getEnum()
        {
            return _enum;
        }

        public void setEnum( SomeEnum val )
        {
            _enum = val;
        }

        public int getInteger()
        {
            return _integer;
        }
        public void setInteger( int integer )
        {
            _integer = integer;
        }

        public boolean isBool()
        {
            return _bool;
        }

        public void setBool( boolean bool )
        {
            _bool = bool;
        }

        public char getChar()
        {
            return _char;
        }

        public void setChar( char c )
        {
            _char = c;
        }

        private SomeEnum _enum = SomeEnum.VALUE1;
        private int _integer = -1;
        private boolean _bool = false;
        private char _char = '!';

    }

    @SuppressWarnings("unused")
    private class BeanB extends BeanA
    {

        public CharSequence getChars()
        {
            return _chars;
        }
        public void setChars( CharSequence chars )
        {
            _chars = chars;
        }

        public String getString()
        {
            return _string;
        }
        public void setString( String string )
        {
            _string = string;
        }

        public Foo getFoo()
        {
            return _foo;
        }
        public void setFoo( Foo foo )
        {
            _foo = foo;
        }

        private CharSequence _chars = null;
        private String _string = null;
        private Foo _foo = null;
    }
}
