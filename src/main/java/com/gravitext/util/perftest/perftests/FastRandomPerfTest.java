package com.gravitext.util.perftest.perftests;

import com.gravitext.util.perftest.FastRandom;
import com.gravitext.util.perftest.ConcurrentTest;

public class FastRandomPerfTest implements ConcurrentTest
{
    public int runTest( int run, int seed ) throws Exception
    {
        FastRandom g = new FastRandom( seed + run );
        for( int i = 0; i < 10000; ++i ) g.nextInt(100);
        return ( ( g.nextInt() & 0x7fffffff ) % 3 );
    }
}
