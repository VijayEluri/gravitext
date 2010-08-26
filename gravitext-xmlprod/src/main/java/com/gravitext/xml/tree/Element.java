/*
 * Copyright (c) 2010 David Kellum
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

package com.gravitext.xml.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gravitext.xml.producer.Attribute;
import com.gravitext.xml.producer.Namespace;
import com.gravitext.xml.producer.Tag;

public final class Element extends Node
{
    public Element( Tag tag )
    {
        if( tag == null ) {
            throw new NullPointerException( getClass().getName() );
        }

        _tag = tag;
    }

    public Element( String name, Namespace ns )
    {
        this( new Tag( name, ns ) );
    }

    public Element( String name )
    {
        this( name, null );
    }

    @Override
    public boolean isElement()
    {
        return true;
    }

    @Override
    public Element asElement()
    {
        return this;
    }

    public Tag tag()
    {
        return _tag;
    }

    public String name()
    {
        return _tag.name();
    }

    public Namespace namespace()
    {
        return _tag.namespace();
    }

    public void addAttribute( AttributeValue avalue )
    {
        if( _attributes == EMPTY_ATTS ) {
            _attributes = new ArrayList<AttributeValue>(3);
        }

        _attributes.add( avalue );
    }

    public void addAttribute( Attribute attribute, CharSequence value )
    {
        addAttribute( new AttributeValue( attribute, value ) );
    }

    public void addAttribute( String name, CharSequence value )
    {
        addAttribute( new AttributeValue( new Attribute( name ), value ) );
    }

    public void addChild( Node node )
    {
        if( _children == EMPTY_CHILDREN ) {
            _children = new ArrayList<Node>(3);
        }

        node.detach();
        _children.add( node );
        node.setParent( this );
    }

    /**
     * Additional namespace declarations rooted at this element. Should not
     * include this elements namespace.
     */
    public void addNamespace( Namespace ns )
    {
        if( _spaces == EMPTY_NAMESPACES ) {
            _spaces = new ArrayList<Namespace>(3);
        }

        _spaces.add( ns );
    }

    public List<AttributeValue> attributes()
    {
        return _attributes;
    }

    public List<Node> children()
    {
        return _children;
    }

    public void insertChild( int index, Node node )
    {
        if( _children == EMPTY_CHILDREN ) {
            _children = new ArrayList<Node>(3);
        }

        node.detach();
        _children.add( index, node );
        node.setParent( this );
    }

    public List<Namespace> namespaceDeclarations()
    {
        return _spaces;
    }

    public void setAttributes( List<AttributeValue> attributes )
    {
        _attributes = attributes;
    }

    void removeChild( Node node )
    {
        _children.remove( node );
    }

    private static final List<AttributeValue> EMPTY_ATTS =
        Collections.emptyList();
    private static final List<Node> EMPTY_CHILDREN = Collections.emptyList();
    private static final List<Namespace> EMPTY_NAMESPACES =
        Collections.emptyList();

    private Tag _tag;
    private List<AttributeValue> _attributes = EMPTY_ATTS;
    private List<Namespace> _spaces = EMPTY_NAMESPACES;
    private List<Node> _children = EMPTY_CHILDREN;
}
