/*
 * Copyright (c) 2008-2013 David Kellum
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License.  You may
 * obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.gravitext.xml.producer;

import java.util.Date;

import junit.framework.TestCase;

public class CharSequenceOverloadTest extends TestCase
{

    boolean foo( CharSequence s )
    {
        return true;
    }
    boolean foo( Object o )
    {
        return false;
    }

    interface IAlt
    {
    }

    class Bar implements IAlt, CharSequence
    {

        public char charAt( int index )
        {
            return 0;
        }

        public int length()
        {
            return 0;
        }

        public CharSequence subSequence( int start, int end )
        {
            return null;
        }
    };

    public void test()
    {
        assertFalse( foo( new Date() ) );
        assertTrue( foo( "somestring" ) );

        assertTrue( foo( new Bar() ) );

        // But CharSequence interface can be hidden.
        IAlt ialt = new Bar();
        assertFalse( foo( ialt ) );
    }

}
