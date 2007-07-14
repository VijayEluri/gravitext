package com.gravitext.htmap;

import java.util.Map;

/**
 * <b>H</b>eterogeneous <b>T</b>ype-safe extension to the <b>Map</b>
 * interface.  All map keys are of the generic type 
 * {@link Key Key&lt;T&gt;}, where T is a key-specific value
 * type. HTMap extends the Map interface, so a HTMap implementation
 * also exposes the raw-typed {@code Map<Key,Object>} methods. Use of
 * the raw-typed mutator methods of the Map interface are type-checked
 * at runtime to guarantee type safety.  The HTMap {@link #get get()}
 * and {@link #remove remove()} methods are {@code Key} type-safe
 * overloads of the equivalent {@code Map} methods.  A new
 * {@link #set set()} method is introduced which offers a compile-time
 * type-safe alternative to {@code Map.put()}.  In this case an
 * overload is not possible because the erased signatures would be the
 * same. The following code sample shows the common usage pattern:
 * 
 * <code><pre>
 * KeySpace KS = new KeySpace();
 * Key&lt;Double> DBLKEY = KS.create( "DBLKEY", Double.class );
 * Key&lt;String> STRKEY = KS.create( "STRKEY", String.class );
 * HTMap htmap = createMap( KS, ... ); 
 * 
 * htmap.set( STRKEY, "string value" );
 * htmap.set( DBLKEY, 3.4 );  // Autoboxed to Double.class
 * 
 * String svalue = htmap.get( STRKEY ); // No cast required 
 * double dvalue = htmap.get( DBLKEY ); // Auto-unboxed
 * 
 * htmap.set( STRKEY, 666 ); // Compile time error
 * htmap.put( STRKEY, 666 ); // Runtime ClassCastException
 * </pre></code>
 * 
 * <p>The null key is not supported by this interface.</p>
 *
 * @see Key
 * @see KeySpace
 * @author David Kellum
 */
public interface HTMap 
    extends Map<Key, Object>
{
    /**
     * Associate the specified value with the specified key.
     * @see Map#put(Object, Object)
     * @param <T> the value type provided by the key
     * @return the previous value associated with key, or null if no
     * such value was present.
     * @throws ClassCastException if value's runtime type is not
     * assignable to {@code key.valueType()}.
     * @throws NullPointerException if key is null, or if null values
     * are not supported and value is null.
     */
    <T, V extends T> T set( Key<T> key, V value );
    
    /**
     * Returns the value previously set with the specified key.
     * 
     * @see Map#get(Object)
     * @param <T> the value type provided by the key
     * @return the value associated with key, or null if no such value
     * is present.
     * @throws NullPointerException if key is null.
     */
    <T> T get( Key<T> key );
    
    /**
     * Removes any value previously set with for the specified key.
     * @see Map#remove(Object)
     * @param <T> the value type provided by the key
     * @return the previous value associated with key, or null if no
     * such value was present.
     * @throws NullPointerException if key is null.
     */
    <T> T remove( Key<T> key );
}
