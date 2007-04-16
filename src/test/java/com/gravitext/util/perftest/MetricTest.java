package com.gravitext.util.perftest;

import java.util.Random;

import junit.framework.TestCase;

public class MetricTest extends TestCase
{
    public void testPositives()
    {
        assertEquals( "999.9f",  Metric.format( 999.94e-15d ) );
        assertEquals( "1.000p",  Metric.format( 999.95e-15d ) );
        assertEquals( "999.9p",  Metric.format( 999.94e-12d ) );
        assertEquals( "1.000n",  Metric.format( 999.95e-12d ) );
        assertEquals( "999.9n",  Metric.format( 999.94e-9d ) );
        assertEquals( "1.000µ",  Metric.format( 999.95e-9d ) );
        assertEquals( "999.9µ",  Metric.format( 999.94e-6d ) );
        assertEquals( "1.000m",  Metric.format( 999.95e-6d ) );
        assertEquals( "10.00m",  Metric.format( 9999.5e-6d ) );
        assertEquals( "999.9m",  Metric.format( 999.94e-3d ) );
        assertEquals( " 1.000",  Metric.format( 999.95e-3d ) );
        assertEquals( " 9.999",  Metric.format( 9.9994d ) );
        assertEquals( "10.000",  Metric.format( 9.9995d ) );
        assertEquals( "99.999",  Metric.format( 99.9994d ) );
        assertEquals( "100.00",  Metric.format( 99.9995d ) );
        assertEquals( "999.99",  Metric.format( 999.994d ) );
        assertEquals( " 1,000",  Metric.format( 999.995d ) );
        assertEquals( "10,000",  Metric.format( 9999.94d ) );
        assertEquals( "10,000",  Metric.format( 9999.95d ) );
        assertEquals( "99,999",  Metric.format( 99999.4d ) );
        assertEquals( "100.0k",  Metric.format( 99999.5d ) );
        assertEquals( "999.9k",  Metric.format( 999.94e3d ) );
        assertEquals( "1.000M",  Metric.format( 999.95e3d ) );        
        assertEquals( "999.9M",  Metric.format( 999.94e6d ) );
        assertEquals( "1.000G",  Metric.format( 999.95e6d ) );        
    }
    
    public void testNegatives()
    {
        assertEquals( " -999f",  Metric.format( -999.4e-15d ) );
        assertEquals( "-1.00p",  Metric.format( -999.5e-15d ) );
        assertEquals( " -999p",  Metric.format( -999.4e-12d ) );
        assertEquals( "-1.00n",  Metric.format( -999.5e-12d ) );
        assertEquals( " -999n",  Metric.format( -999.4e-9d ) );
        assertEquals( "-1.00µ",  Metric.format( -999.5e-9d ) );
        assertEquals( " -999µ",  Metric.format( -999.4e-6d ) );
        assertEquals( "-1.00m",  Metric.format( -999.5e-6d ) );
        assertEquals( "-10.0m",  Metric.format( -9999.5e-6d ) );
        assertEquals( " -999m",  Metric.format( -999.4e-3d ) );
        assertEquals( "-1.000",  Metric.format( -999.5e-3d ) );
        assertEquals( "-9.999",  Metric.format( -9.9994d ) );
        assertEquals( "-10.00",  Metric.format( -9.9995d ) );
        assertEquals( "-99.99",  Metric.format( -99.994d ) );
        assertEquals( "-100.0",  Metric.format( -99.995d ) );
        assertEquals( "-999.9",  Metric.format( -999.94d ) );
        assertEquals( "-1,000",  Metric.format( -999.95d ) );
        assertEquals( "-9,999",  Metric.format( -9999.4d ) );
        assertEquals( "-10.0k",  Metric.format( -9999.5d ) );
        assertEquals( "-99.9k",  Metric.format( -99940d ) );
        assertEquals( " -100k",  Metric.format( -99950d ) );
        assertEquals( " -999k",  Metric.format( -999.4e3d ) );
        assertEquals( "-1.00M",  Metric.format( -999.5e3d ) );        
        assertEquals( " -999M",  Metric.format( -999.4e6d ) );
        assertEquals( "-1.00G",  Metric.format( -999.5e6d ) );        
    }
    
    public void test0()
    {
        assertEquals( " 0.000", Metric.format( 0.0d ) );
        assertEquals( " 0.000", Metric.format( 1e-18d ) );
    }
    
    public void testRandomSize()
    {
        Random r = new Random();
        
        StringBuilder b = new StringBuilder(6);
        for( int i = 1; i < 1000; ++i ) {
            b.setLength( 0 );
            double v = r.nextDouble() * Math.pow( 10d, r.nextInt( 31 ) - 15 );
            if( r.nextBoolean() ) v = -v;
            Metric.format( v, b );
            if( b.length() != 6 ) {
                fail( "Value: " + v + " ==> " + b );
            }
        }
    }
    
}
