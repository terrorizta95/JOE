/* Long.java -- object wrapper for long
 Copyright (C) 1998, 1999, 2001, 2002 Free Software Foundation, Inc.

 This file is part of GNU Classpath.

 GNU Classpath is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2, or (at your option)
 any later version.

 GNU Classpath is distributed in the hope that it will be useful, but
 WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with GNU Classpath; see the file COPYING.  If not, write to the
 Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 02111-1307 USA.

 Linking this library statically or dynamically with other modules is
 making a combined work based on this library.  Thus, the terms and
 conditions of the GNU General Public License cover the whole
 combination.

 As a special exception, the copyright holders of this library give you
 permission to link this library with independent modules to produce an
 executable, regardless of the license terms of these independent
 modules, and to copy and distribute the resulting executable under
 terms of your choice, provided that you also meet, for each linked
 independent module, the terms and conditions of the license of that
 module.  An independent module is a module which is not derived from
 or based on this library.  If you modify this library, you may extend
 this exception to your version of the library, but you are not
 obligated to do so.  If you do not wish to do so, delete this
 exception statement from your version. */

package java.lang;

import baremetal.vm.VMClassLoader;

/**
 * Instances of class <code>Long</code> represent primitive <code>long</code>
 * values.
 * 
 * Additionally, this class provides various helper functions and variables
 * related to longs.
 * 
 * @author Paul Fisher
 * @author John Keiser
 * @author Warren Levy
 * @author Eric Blake <ebb9@email.byu.edu>
 * @since 1.0
 * @status updated to 1.4
 */
public final class Long extends Number implements Comparable {
    /**
     * Compatible with JDK 1.0.2+.
     */
    private static final long serialVersionUID = 4290774380558885855L;

    /**
     * The minimum value a <code>long</code> can represent is
     * -9223372036854775808L (or -2 <sup>63 </sup>).
     */
    public static final long MIN_VALUE = 0x8000000000000000L;

    /**
     * The maximum value a <code>long</code> can represent is
     * 9223372036854775807 (or 2 <sup>63 </sup>- 1).
     */
    public static final long MAX_VALUE = 0x7fffffffffffffffL;

    /**
     * The primitive type <code>long</code> is represented by this
     * <code>Class</code> object.
     * 
     * @since 1.1
     */
    public static final Class TYPE = VMClassLoader.getPrimitiveClass('J');

    /**
     * The immutable value of this Long.
     * 
     * @serial the wrapped long
     */
    private final long value;

    /**
     * Create a <code>Long</code> object representing the value of the
     * <code>long</code> argument.
     * 
     * @param value
     *            the value to use
     */
    public Long(long value) {
        this.value = value;
    }

    /**
     * Create a <code>Long</code> object representing the value of the
     * argument after conversion to a <code>long</code>.
     * 
     * @param s
     *            the string to convert
     * @throws NumberFormatException
     *             if the String does not contain a long
     * @see #valueOf(String)
     */
    public Long(String s) {
        value = parseLong(s, 10, false);
    }

    /**
     * Converts the <code>long</code> to a <code>String</code> using the
     * specified radix (base). If the radix exceeds
     * <code>Character.MIN_RADIX</code> or <code>Character.MAX_RADIX</code>,
     * 10 is used instead. If the result is negative, the leading character is
     * '-' ('\\u002D'). The remaining characters come from
     * <code>Character.forDigit(digit, radix)</code> ('0'-'9','a'-'z').
     * 
     * @param num
     *            the <code>long</code> to convert to <code>String</code>
     * @param radix
     *            the radix (base) to use in the conversion
     * @return the <code>String</code> representation of the argument
     */
    public static String toString(long num, int radix) {
        // Use the Integer toString for efficiency if possible.
        if ((int) num == num)
            return Integer.toString((int) num, radix);

        if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
            radix = 10;

        // For negative numbers, print out the absolute value w/ a leading '-'.
        // Use an array large enough for a binary number.
        char[] buffer = new char[65];
        int i = 65;
        boolean isNeg = false;
        if (num < 0) {
            isNeg = true;
            num = -num;

            // When the value is MIN_VALUE, it overflows when made positive
            if (num < 0) {
                buffer[--i] = digits[(int) (-(num + radix) % radix)];
                num = -(num / radix);
            }
        }

        do {
            buffer[--i] = digits[(int) (num % radix)];
            num /= radix;
        } while (num > 0);

        if (isNeg)
            buffer[--i] = '-';

        // Package constructor avoids an array copy.
        return new String(buffer, i, 65 - i, true);
    }

    /**
     * Converts the <code>long</code> to a <code>String</code> assuming it
     * is unsigned in base 16.
     * 
     * @param l
     *            the <code>long</code> to convert to <code>String</code>
     * @return the <code>String</code> representation of the argument
     */
    public static String toHexString(long l) {
        return toUnsignedString(l, 4);
    }

    /**
     * Converts the <code>long</code> to a <code>String</code> assuming it
     * is unsigned in base 8.
     * 
     * @param l
     *            the <code>long</code> to convert to <code>String</code>
     * @return the <code>String</code> representation of the argument
     */
    public static String toOctalString(long l) {
        return toUnsignedString(l, 3);
    }

    /**
     * Converts the <code>long</code> to a <code>String</code> assuming it
     * is unsigned in base 2.
     * 
     * @param l
     *            the <code>long</code> to convert to <code>String</code>
     * @return the <code>String</code> representation of the argument
     */
    public static String toBinaryString(long l) {
        return toUnsignedString(l, 1);
    }

    /**
     * Converts the <code>long</code> to a <code>String</code> and assumes a
     * radix of 10.
     * 
     * @param num
     *            the <code>long</code> to convert to <code>String</code>
     * @return the <code>String</code> representation of the argument
     * @see #toString(long, int)
     */
    public static String toString(long num) {
        return toString(num, 10);
    }

    /**
     * Converts the specified <code>String</code> into an <code>int</code>
     * using the specified radix (base). The string must not be
     * <code>null</code> or empty. It may begin with an optional '-', which
     * will negate the answer, provided that there are also valid digits. Each
     * digit is parsed as if by <code>Character.digit(d, radix)</code>, and
     * must be in the range <code>0</code> to <code>radix - 1</code>.
     * Finally, the result must be within <code>MIN_VALUE</code> to
     * <code>MAX_VALUE</code>, inclusive. Unlike Double.parseDouble, you may
     * not have a leading '+'; and 'l' or 'L' as the last character is only
     * valid in radices 22 or greater, where it is a digit and not a type
     * indicator.
     * 
     * @param s
     *            the <code>String</code> to convert
     * @param radix
     *            the radix (base) to use in the conversion
     * @return the <code>String</code> argument converted to</code> long
     *         </code>
     * @throws NumberFormatException
     *             if <code>s</code> cannot be parsed as a <code>long</code>
     */
    public static long parseLong(String str, int radix) {
        return parseLong(str, radix, false);
    }

    /**
     * Converts the specified <code>String</code> into a <code>long</code>.
     * This function assumes a radix of 10.
     * 
     * @param s
     *            the <code>String</code> to convert
     * @return the <code>int</code> value of <code>s</code>
     * @throws NumberFormatException
     *             if <code>s</code> cannot be parsed as a <code>long</code>
     * @see #parseLong(String, int)
     */
    public static long parseLong(String s) {
        return parseLong(s, 10, false);
    }

    /**
     * Creates a new <code>Long</code> object using the <code>String</code>
     * and specified radix (base).
     * 
     * @param s
     *            the <code>String</code> to convert
     * @param radix
     *            the radix (base) to convert with
     * @return the new <code>Long</code>
     * @throws NumberFormatException
     *             if <code>s</code> cannot be parsed as a <code>long</code>
     * @see #parseLong(String, int)
     */
    public static Long valueOf(String s, int radix) {
        return new Long(parseLong(s, radix, false));
    }

    /**
     * Creates a new <code>Long</code> object using the <code>String</code>,
     * assuming a radix of 10.
     * 
     * @param s
     *            the <code>String</code> to convert
     * @return the new <code>Long</code>
     * @throws NumberFormatException
     *             if <code>s</code> cannot be parsed as a <code>long</code>
     * @see #Long(String)
     * @see #parseLong(String)
     */
    public static Long valueOf(String s) {
        return new Long(parseLong(s, 10, false));
    }

    /**
     * Convert the specified <code>String</code> into a <code>Long</code>.
     * The <code>String</code> may represent decimal, hexadecimal, or octal
     * numbers.
     * 
     * <p>
     * The extended BNF grammar is as follows: <br>
     * 
     * <pre>
     * <em>
     * DecodableString
     * </em>
     * :
     *       ( [ 
     * <code>
     * -
     * </code>
     *  ] 
     * <em>
     * DecimalNumber
     * </em>
     *  )
     *     | ( [ 
     * <code>
     * -
     * </code>
     *  ] ( 
     * <code>
     * 
     * </code>
     *  | 
     * <code>
     * 
     * </code>
     * 
     *               | 
     * <code>
     * #
     * </code>
     *  ) 
     * <em>
     * HexDigit
     * </em>
     *  { 
     * <em>
     * HexDigit
     * </em>
     *  } )
     *     | ( [ 
     * <code>
     * -
     * </code>
     *  ] 
     * <code>
     * 0
     * </code>
     *  { 
     * <em>
     * OctalDigit
     * </em>
     *  } )
     *  
     * <em>
     * DecimalNumber
     * </em>
     * :
     *         
     * <em>
     * DecimalDigit except '0'
     * </em>
     *  { 
     * <em>
     * DecimalDigit
     * </em>
     *  }
     *  
     * <em>
     * DecimalDigit
     * </em>
     * :
     *         
     * <em>
     * Character.digit(d, 10) has value 0 to 9
     * </em>
     * <em>
     * OctalDigit
     * </em>
     * :
     *         
     * <em>
     * Character.digit(d, 8) has value 0 to 7
     * </em>
     * <em>
     * DecimalDigit
     * </em>
     * :
     *         
     * <em>
     * Character.digit(d, 16) has value 0 to 15
     * </em>
     * </pre>
     * 
     * Finally, the value must be in the range <code>MIN_VALUE</code> to
     * <code>MAX_VALUE</code>, or an exception is thrown. Note that you
     * cannot use a trailing 'l' or 'L', unlike in Java source code.
     * 
     * @param s
     *            the <code>String</code> to interpret
     * @return the value of the String as a <code>Long</code>
     * @throws NumberFormatException
     *             if <code>s</code> cannot be parsed as a <code>long</code>
     * @throws NullPointerException
     *             if <code>s</code> is null
     * @since 1.2
     */
    public static Long decode(String str) {
        return new Long(parseLong(str, 10, true));
    }

    /**
     * Return the value of this <code>Long</code> as a <code>byte</code>.
     * 
     * @return the byte value
     */
    public byte byteValue() {
        return (byte) value;
    }

    /**
     * Return the value of this <code>Long</code> as a <code>short</code>.
     * 
     * @return the short value
     */
    public short shortValue() {
        return (short) value;
    }

    /**
     * Return the value of this <code>Long</code> as an <code>int</code>.
     * 
     * @return the int value
     */
    public int intValue() {
        return (int) value;
    }

    /**
     * Return the value of this <code>Long</code>.
     * 
     * @return the long value
     */
    public long longValue() {
        return value;
    }

    /**
     * Return the value of this <code>Long</code> as a <code>float</code>.
     * 
     * @return the float value
     */
    public float floatValue() {
        return value;
    }

    /**
     * Return the value of this <code>Long</code> as a <code>double</code>.
     * 
     * @return the double value
     */
    public double doubleValue() {
        return value;
    }

    /**
     * Converts the <code>Long</code> value to a <code>String</code> and
     * assumes a radix of 10.
     * 
     * @return the <code>String</code> representation
     */
    public String toString() {
        return toString(value, 10);
    }

    /**
     * Return a hashcode representing this Object. <code>Long</code>'s hash
     * code is calculated by <code>(int) (value ^ (value &gt;&gt; 32))</code>.
     * 
     * @return this Object's hash code
     */
    public int hashCode() {
        return (int) (value ^ (value >>> 32));
    }

    /**
     * Returns <code>true</code> if <code>obj</code> is an instance of
     * <code>Long</code> and represents the same long value.
     * 
     * @param obj
     *            the object to compare
     * @return whether these Objects are semantically equal
     */
    public boolean equals(Object obj) {
        return obj instanceof Long && value == ((Long) obj).value;
    }

    /**
     * Get the specified system property as a <code>Long</code>. The
     * <code>decode()</code> method will be used to interpret the value of the
     * property.
     * 
     * @param nm
     *            the name of the system property
     * @return the system property as a <code>Long</code>, or null if the
     *         property is not found or cannot be decoded
     * @throws SecurityException
     *             if accessing the system property is forbidden
     * @see System#getProperty(String)
     * @see #decode(String)
     */
    public static Long getLong(String nm) {
        return getLong(nm, null);
    }

    /**
     * Get the specified system property as a <code>Long</code>, or use a
     * default <code>long</code> value if the property is not found or is not
     * decodable. The <code>decode()</code> method will be used to interpret
     * the value of the property.
     * 
     * @param nm
     *            the name of the system property
     * @param val
     *            the default value
     * @return the value of the system property, or the default
     * @throws SecurityException
     *             if accessing the system property is forbidden
     * @see System#getProperty(String)
     * @see #decode(String)
     */
    public static Long getLong(String nm, long val) {
        Long result = getLong(nm, null);
        return result == null ? new Long(val) : result;
    }

    /**
     * Get the specified system property as a <code>Long</code>, or use a
     * default <code>Long</code> value if the property is not found or is not
     * decodable. The <code>decode()</code> method will be used to interpret
     * the value of the property.
     * 
     * @param nm
     *            the name of the system property
     * @param val
     *            the default value
     * @return the value of the system property, or the default
     * @throws SecurityException
     *             if accessing the system property is forbidden
     * @see System#getProperty(String)
     * @see #decode(String)
     */
    public static Long getLong(String nm, Long def) {
        if (nm == null || "".equals(nm))
            return def;
        nm = System.getProperty(nm);
        if (nm == null)
            return def;
        try {
            return decode(nm);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * Compare two Longs numerically by comparing their <code>long</code>
     * values. The result is positive if the first is greater, negative if the
     * second is greater, and 0 if the two are equal.
     * 
     * @param l
     *            the Long to compare
     * @return the comparison
     * @since 1.2
     */
    public int compareTo(Long l) {
        if (value == l.value)
            return 0;
        // Returns just -1 or 1 on inequality; doing math might overflow the
        // long.
        return value > l.value ? 1 : -1;
    }

    /**
     * Behaves like <code>compareTo(Long)</code> unless the Object is not a
     * <code>Long</code>.
     * 
     * @param o
     *            the object to compare
     * @return the comparison
     * @throws ClassCastException
     *             if the argument is not a <code>Long</code>
     * @see #compareTo(Long)
     * @see Comparable
     * @since 1.2
     */
    public int compareTo(Object o) {
        return compareTo((Long) o);
    }

    /**
     * Helper for converting unsigned numbers to String.
     * 
     * @param num
     *            the number
     * @param exp
     *            log2(digit) (ie. 1, 3, or 4 for binary, oct, hex)
     */
    private static String toUnsignedString(long num, int exp) {
        // Use the Integer toUnsignedString for efficiency if possible.
        // If NUM<0 then this particular optimization doesn't work
        // properly.
        if (num >= 0 && (int) num == num)
            return Integer.toUnsignedString((int) num, exp);

        // Use an array large enough for a binary number.
        int mask = (1 << exp) - 1;
        char[] buffer = new char[64];
        int i = 64;
        do {
            buffer[--i] = digits[(int) num & mask];
            num >>>= exp;
        } while (num != 0);

        // Package constructor avoids an array copy.
        return new String(buffer, i, 64 - i, true);
    }

    /**
     * Helper for parsing longs.
     * 
     * @param str
     *            the string to parse
     * @param radix
     *            the radix to use, must be 10 if decode is true
     * @param decode
     *            if called from decode
     * @return the parsed long value
     * @throws NumberFormatException
     *             if there is an error
     * @throws NullPointerException
     *             if decode is true and str is null
     * @see #parseLong(String, int)
     * @see #decode(String)
     */
    private static long parseLong(String str, int radix, boolean decode) {
        if (!decode && str == null)
            throw new NumberFormatException();
        int index = 0;
        int len = str.length();
        boolean isNeg = false;
        if (len == 0)
            throw new NumberFormatException();
        int ch = str.charAt(index);
        if (ch == '-') {
            if (len == 1)
                throw new NumberFormatException();
            isNeg = true;
            ch = str.charAt(++index);
        }
        if (decode) {
            if (ch == '0') {
                if (++index == len)
                    return 0;
                if ((str.charAt(index) & ~('x' ^ 'X')) == 'X') {
                    radix = 16;
                    index++;
                } else
                    radix = 8;
            } else if (ch == '#') {
                radix = 16;
                index++;
            }
        }
        if (index == len)
            throw new NumberFormatException();

        long max = MAX_VALUE / radix;
        // We can't directly write `max = (MAX_VALUE + 1) / radix'.
        // So instead we fake it.
        if (isNeg && MAX_VALUE % radix == radix - 1)
            ++max;

        long val = 0;
        while (index < len) {
            if (val < 0 || val > max)
                throw new NumberFormatException();

            ch = Character.digit(str.charAt(index++), radix);
            val = val * radix + ch;
            if (ch < 0 || (val < 0 && (!isNeg || val != MIN_VALUE)))
                throw new NumberFormatException();
        }
        return isNeg ? -val : val;
    }
}