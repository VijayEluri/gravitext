/*
 * Copyright (c) 2007-2011 David Kellum
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

package com.gravitext.util;

import java.text.NumberFormat;

/**
 * Utilities to format numeric values with metric (SI) unit or percent
 * difference suffixes, and limited fractional digits.
 *
 * @author David Kellum
 */
public final class Metric
{
    /**
     * Write formatted value to out, using six total characters and
     * the most appropriate metric unit suffix.  Metric suffixes in
     * the range peta (P, 1e15) to femto (f, 1e-15) are supported.
     * Values outside of the supported metric range may force
     * formatting of more than six characters.  In some cases the
     * value may be left padded with a space: (ex: " 1.000").
     */
    public static void format( double value, StringBuilder out )
    {
        format( value, out, 3 );
    }

    /**
     * Write formatted value to out, using six total characters and
     * the most appropriate metric unit suffix.  Metric suffixes in
     * the range peta (P, 1e15) to femto (f, 1e-15) are supported.
     * Values outside of the supported metric range may force
     * formatting of more than six characters.  In some cases the
     * value may be left padded with a space: (ex: " 1.000").
     * @param maxFDigits maximum fractional digits
     *        (i.e. zero for original integers, 3 for input fractional values)
     */
    public static void format( double value, StringBuilder out, int maxFDigits )
    {
        char p = 0;
        double v = value;

        for( Range r : RANGES ) {
            if( v >= r.pos || v <= r.neg ) {
                p = r.postfix;
                v *= r.fac;
                break;
            }
        }

        int fdigits = 3;
        if( v >= 0.0d ) {
            if( p == 0 ) { // 99,999 9,999 999.99, 99.999, 9.999
                if      ( v >= 999.995 ) fdigits = 0;
                else if ( v >= 99.9995 ) fdigits = 2;
            }
            else { //9,999x, 999.9x, 99.99x, 9.999x
                if      ( v >= 999.95 ) fdigits = 0;
                else if ( v >= 99.995 ) fdigits = 1;
                else if ( v >= 9.995 )  fdigits = 2;
            }
        }
        else {
            if( p == 0 ) { // -9,999 -999.9, -99.99 -9.999
                if      ( v <= -999.95 ) fdigits = 0;
                else if ( v <= -99.995 ) fdigits = 1;
                else if ( v <= -9.9995 ) fdigits = 2;
            }
            else { //-999x, -99.9x, -9.99x
                if      ( v <= -99.95 ) fdigits = 0;
                else if ( v <= -9.995 ) fdigits = 1;
                else fdigits = 2;
            }
        }
        if( p == 0 ) fdigits = Math.min( fdigits, maxFDigits );

        NumberFormat f = NumberFormat.getNumberInstance();
        f.setMinimumFractionDigits( fdigits );
        f.setMaximumFractionDigits( fdigits );
        f.setGroupingUsed( true );

        String vf = f.format( v );

        int plen = ( (p == 0) ? 6 : 5 ) - vf.length();
        while( plen-- > 0 ) out.append( ' ' );
        out.append( vf );
        if( p != 0 ) out.append( p );
    }

    /**
     * Format value as a difference to out.  Values greater than 1.0
     * will be formatted as a multiple (ex: 1.1 --&gt; "2.100x").
     * Note that 1.0 is added to the difference in this case. Consider
     * an initial value 1.0 and final value 2.1. The difference is
     * computed as (final - initial) / initial = 1.1 and thus
     * formatted as "2.100x".  All other values (&lt;= 1.0) will be
     * formatted as a positive or negative percentage: (ex: 0.25
     * --&gt; "+25.0%").  Values will be left padded as necessary for
     * output to six character width. Values outside the range
     * (-9.995, 9998.5) will be formatted using more than six
     * characters.
     */
    public static void formatDifference( double value, StringBuilder out )
    {
        if( Double.isNaN( value ) ) {
            out.append( "   N/A" );
            return;
        }

        double r = value;
        char symbol;
        int fdigits = 3;
        if( r > 1.0d ) {
            r += 1.0d;
            if      ( r >= 999.95 ) fdigits = 0;
            else if ( r >= 99.995 ) fdigits = 1;
            else if ( r >=  9.995 ) fdigits = 2;
            symbol = 'x';
        }
        else {
            r *= 100d;
            if      ( r >= 99.995 ) fdigits = 0;
            else if ( r >=  9.995 ) fdigits = 1;
            else if ( r <= -99.95 ) fdigits = 0;
            else if ( r <= -9.995 ) fdigits = 1;
            else fdigits = 2;
            symbol = '%';
        }

        NumberFormat f = NumberFormat.getNumberInstance();
        f.setMinimumFractionDigits( fdigits );
        f.setMaximumFractionDigits( fdigits );
        f.setGroupingUsed( true );

        if( r > 0d && symbol == '%' ) out.append('+');
        String vf = f.format( r );
        int flen = vf.length();
        while( out.length() < ( 5 - flen ) ) out.append( ' ' );
        out.append( vf );
        out.append( symbol );
    }

    /**
     * Return formatted value as String using six characters and the
     * most appropriate metric suffix.
     * @see #format(double, StringBuilder)
     */
    public static String format( double value )
    {
        StringBuilder b = new StringBuilder( 16 );
        format( value, b );
        return b.toString();
    }

    /**
     * Return formatted value as String using six characters, the
     * most appropriate metric suffix, and no fractional digits.
     */
    public static String format( long value )
    {
        return formatInteger( (double) value );
    }

    /**
     * Return formatted value as String using six characters, the
     * most appropriate metric suffix, and no fractional digits.
     */
    public static String formatInteger( double value )
    {
        StringBuilder b = new StringBuilder( 16 );
        format( value, b, 0 );
        return b.toString();
    }

    /**
     * Return formatted difference value as String using six
     * characters.
     * @see #formatDifference(double, StringBuilder)
     */
    public static String formatDifference( double value )
    {
        StringBuilder b = new StringBuilder( 16 );
        formatDifference( value, b );
        return b.toString();
    }

    private static final class Range
    {
        Range( double pos, double neg, double fac, char postfix )
        {
            this.pos = pos;
            this.neg = neg;
            this.fac = fac;
            this.postfix = postfix;
        }

        double pos;
        double neg;
        double fac;
        char postfix;
    }

    private static final Range[] RANGES = new Range[] {
        new Range(   999.95e12d,  -999.5e12d,  1e-15d, 'P' ),
        new Range(   999.95e9d,   -999.5e9d,   1e-12d, 'T' ),
        new Range(   999.95e6d,   -999.5e6d,   1e-9d,  'G' ),
        new Range(   999.95e3d,   -999.5e3d,   1e-6d,  'M' ),
        new Range( 99999.5e0d,   -9999.5e0d,   1e-3d,  'k' ),
        new Range(   999.95e-3d,  -999.5e-3d,  1d,     '\0'),
        new Range(   999.95e-6d,  -999.5e-6d,  1e+3d,  'm' ),
        new Range(   999.95e-9d,  -999.5e-9d,  1e+6d,  'µ' ),
        new Range(   999.95e-12d, -999.5e-12d, 1e+9d,  'n' ),
        new Range(   999.95e-15d, -999.5e-15d, 1e+12d, 'p' ),
        new Range(   999.95e-18d, -999.5e-18d, 1e+15d, 'f' )
    };
}
