/*
 * Copyright 2007 David Kellum
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

/**
 * Checked exception for various BeanAccessor errors. 
 * @author David Kellum
 */
public class BeanException extends Exception
{
    public BeanException( String message, Throwable cause )
    {
        super( message, cause );
    }

    public BeanException( String message )
    {
        super( message );
    }

    public BeanException( Throwable cause )
    {
        super( cause );
    }

    private static final long serialVersionUID = 1L;
}