#--
# Copyright (c) 2012 David Kellum
#
# Licensed under the Apache License, Version 2.0 (the "License"); you
# may not use this file except in compliance with the License.  You
# may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
# implied.  See the License for the specific language governing
# permissions and limitations under the License.
#++

require 'gravitext-xmlprod'

module Gravitext::XMLProd

  class Element
    java_import 'com.gravitext.xml.tree.TreeUtils'

    # Shorthand for attribute( name )
    def []( name )
      attribute( name )
    end

    # Return Array of child elements matching tag and/or further
    # constrained where yielding to block returns true.
    def select( tag = nil )
      tag = Tag.new( tag, Tag.WILDCARD_NS ) unless tag.nil? || tag.is_a?( Tag )
      if block_given?
        children.select do |c|
          c.element? && ( tag.nil? || c.tag == tag ) && yield( c )
        end
      else
        children.select do |c|
          c.element? && ( c.tag == tag )
        end
      end
    end

    # Return first child element matching tag and/or where yielding to
    # block returns true. Without a block is equivalent to
    # first_element.
    def find( tag = nil )
      tag = Tag.new( tag, Tag.WILDCARD_NS ) unless tag.nil? || tag.is_a?( Tag )
      if block_given?
        children.find do |c|
          c.element? && ( tag.nil? || c.tag == tag ) && yield( c )
        end
      else
        first_element( tag )
      end
    end

    def to_xml( indentor = Indentor::COMPRESSED )
      # FIXME: Can this be optimized more directly to ruby 8-bit
      # String?
      TreeUtils.produce_string( self, indentor )
    end

  end

end
