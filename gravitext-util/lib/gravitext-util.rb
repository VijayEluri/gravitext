#--
# Copyright (c) 2007-2010 David Kellum
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

require 'gravitext-util/version'
require 'java'

module Gravitext
  module Util
    require File.join( LIB_DIR, "gravitext-util-#{ VERSION }.jar" )

    import 'com.gravitext.util.FastRandom'
  end

  require 'gravitext-util/concurrent'
  require 'gravitext-util/unimap'
end
