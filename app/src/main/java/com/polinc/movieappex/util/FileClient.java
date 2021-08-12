package com.polinc.movieappex.util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;




public class FileClient {
 
	 /**
     * The extension separator character.
     */
    private static final char EXTENSION_SEPARATOR = '.';

    /**
     * The Unix separator character.
     */
    private static final char UNIX_SEPARATOR = '/';

    /**
     * The Windows separator character.
     */
    private static final char WINDOWS_SEPARATOR = '\\';

    /**
     * The system separator character.
     */
    private static final char SYSTEM_SEPARATOR = File.separatorChar;

    /**
     * The separator character that is the opposite of the system separator.
     */
    private static final char OTHER_SEPARATOR;
    static {
        if (SYSTEM_SEPARATOR == WINDOWS_SEPARATOR) {
            OTHER_SEPARATOR = UNIX_SEPARATOR;
        } else {
            OTHER_SEPARATOR = WINDOWS_SEPARATOR;
        }
    }

    /**
     * Instances should NOT be constructed in standard programming.
     */
    

    //-----------------------------------------------------------------------
    /**
     * Checks if the character is a separator.
     * 
     * @param ch  the character to check
     * @return true if it is a separator character
     */
    private static boolean isSeparator(char ch) {
        return (ch == UNIX_SEPARATOR) || (ch == WINDOWS_SEPARATOR);
    }

    //-----------------------------------------------------------------------
    /**
     * Normalizes a path, removing double and single dot path steps.
     * <p>
     * This method normalizes a path to a standard format.
     * The input may contain separators in either Unix or Windows format.
     * The output will contain separators in the format of the system.
     * <p>
     * A trailing slash will be retained.
     * A double slash will be merged to a single slash (but UNC names are handled).
     * A single dot path segment will be removed.
     * A double dot will cause that path segment and the one before to be removed.
     * If the double dot has no parent path segment to work with, <code>null</code>
     * is returned.
     * <p>
     * The output will be the same on both Unix and Windows except
     * for the separator character.
     * <pre>
     * /foo//               -->   /foo/
     * /foo/./              -->   /foo/
     * /foo/../bar          -->   /bar
     * /foo/../bar/         -->   /bar/
     * /foo/../bar/../baz   -->   /baz
     * //foo//./bar         -->   /foo/bar
     * /../                 -->   null
     * ../foo               -->   null
     * foo/bar/..           -->   foo/
     * foo/../../bar        -->   null
     * foo/../bar           -->   bar
     * //server/foo/../bar  -->   //server/bar
     * //server/../bar      -->   null
     * C:\foo\..\bar        -->   C:\bar
     * C:\..\bar            -->   null
     * ~/foo/../bar/        -->   ~/bar/
     * ~/../bar             -->   null
     * </pre>
     * (Note the file separator returned will be correct for Windows/Unix)
     *
     * @param filename  the filename to normalize, null returns null
     * @return the normalized filename, or null if invalid
     */
    public static String normalize(String filename) {
        return doNormalize(filename, true);
    }

    //-----------------------------------------------------------------------
    /**
     * Normalizes a path, removing double and single dot path steps,
     * and removing any final directory separator.
     * <p>
     * This method normalizes a path to a standard format.
     * The input may contain separators in either Unix or Windows format.
     * The output will contain separators in the format of the system.
     * <p>
     * A trailing slash will be removed.
     * A double slash will be merged to a single slash (but UNC names are handled).
     * A single dot path segment will be removed.
     * A double dot will cause that path segment and the one before to be removed.
     * If the double dot has no parent path segment to work with, <code>null</code>
     * is returned.
     * <p>
     * The output will be the same on both Unix and Windows except
     * for the separator character.
     * <pre>
     * /foo//               -->   /foo
     * /foo/./              -->   /foo
     * /foo/../bar          -->   /bar
     * /foo/../bar/         -->   /bar
     * /foo/../bar/../baz   -->   /baz
     * //foo//./bar         -->   /foo/bar
     * /../                 -->   null
     * ../foo               -->   null
     * foo/bar/..           -->   foo
     * foo/../../bar        -->   null
     * foo/../bar           -->   bar
     * //server/foo/../bar  -->   //server/bar
     * //server/../bar      -->   null
     * C:\foo\..\bar        -->   C:\bar
     * C:\..\bar            -->   null
     * ~/foo/../bar/        -->   ~/bar
     * ~/../bar             -->   null
     * </pre>
     * (Note the file separator returned will be correct for Windows/Unix)
     *
     * @param filename  the filename to normalize, null returns null
     * @return the normalized filename, or null if invalid
     */
    public static String normalizeNoEndSeparator(String filename) {
        return doNormalize(filename, false);
    }

    /**
     * Internal method to perform the normalization.
     *
     * @param filename  the filename
     * @param keepSeparator  true to keep the final separator
     * @return the normalized filename
     */
    private static String doNormalize(String filename, boolean keepSeparator) {
        if (filename == null) {
            return null;
        }
        int size = filename.length();
        if (size == 0) {
            return filename;
        }
        int prefix = getPrefixLength(filename);
        if (prefix < 0) {
            return null;
        }
        
        char[] array = new char[size + 2];  // +1 for possible extra slash, +2 for arraycopy
        filename.getChars(0, filename.length(), array, 0);
        
        // fix separators throughout
        for (int i = 0; i < array.length; i++) {
            if (array[i] == OTHER_SEPARATOR) {
                array[i] = SYSTEM_SEPARATOR;
            }
        }
        
        // add extra separator on the end to simplify code below
        boolean lastIsDirectory = true;
        if (array[size - 1] != SYSTEM_SEPARATOR) {
            array[size++] = SYSTEM_SEPARATOR;
            lastIsDirectory = false;
        }
        
        // adjoining slashes
        for (int i = prefix + 1; i < size; i++) {
            if (array[i] == SYSTEM_SEPARATOR && array[i - 1] == SYSTEM_SEPARATOR) {
                System.arraycopy(array, i, array, i - 1, size - i);
                size--;
                i--;
            }
        }
        
        // dot slash
        for (int i = prefix + 1; i < size; i++) {
            if (array[i] == SYSTEM_SEPARATOR && array[i - 1] == '.' &&
                    (i == prefix + 1 || array[i - 2] == SYSTEM_SEPARATOR)) {
                if (i == size - 1) {
                    lastIsDirectory = true;
                }
                System.arraycopy(array, i + 1, array, i - 1, size - i);
                size -=2;
                i--;
            }
        }
        
        // double dot slash
        outer:
        for (int i = prefix + 2; i < size; i++) {
            if (array[i] == SYSTEM_SEPARATOR && array[i - 1] == '.' && array[i - 2] == '.' &&
                    (i == prefix + 2 || array[i - 3] == SYSTEM_SEPARATOR)) {
                if (i == prefix + 2) {
                    return null;
                }
                if (i == size - 1) {
                    lastIsDirectory = true;
                }
                int j;
                for (j = i - 4 ; j >= prefix; j--) {
                    if (array[j] == SYSTEM_SEPARATOR) {
                        // remove b/../ from a/b/../c
                        System.arraycopy(array, i + 1, array, j + 1, size - i);
                        size -= (i - j);
                        i = j + 1;
                        continue outer;
                    }
                }
                // remove a/../ from a/../c
                System.arraycopy(array, i + 1, array, prefix, size - i);
                size -= (i + 1 - prefix);
                i = prefix + 1;
            }
        }
        
        if (size <= 0) {  // should never be less than 0
            return "";
        }
        if (size <= prefix) {  // should never be less than prefix
            return new String(array, 0, size);
        }
        if (lastIsDirectory && keepSeparator) {
            return new String(array, 0, size);  // keep trailing separator
        }
        return new String(array, 0, size - 1);  // lose trailing separator
    }

    //-----------------------------------------------------------------------
    /**
     * Concatenates a filename to a base path using normal command line style rules.
     * <p>
     * The effect is equivalent to resultant directory after changing
     * directory to the first argument, followed by changing directory to
     * the second argument.
     * <p>
     * The first argument is the base path, the second is the path to concatenate.
     * The returned path is always normalized via {@link #normalize(String)},
     * thus <code>..</code> is handled.
     * <p>
     * If <code>pathToAdd</code> is absolute (has an absolute prefix), then
     * it will be normalized and returned.
     * Otherwise, the paths will be joined, normalized and returned.
     * <p>
     * The output will be the same on both Unix and Windows except
     * for the separator character.
     * <pre>
     * /foo/ + bar          -->   /foo/bar
     * /foo + bar           -->   /foo/bar
     * /foo + /bar          -->   /bar
     * /foo + C:/bar        -->   C:/bar
     * /foo + C:bar         -->   C:bar (*)
     * /foo/a/ + ../bar     -->   foo/bar
     * /foo/ + ../../bar    -->   null
     * /foo/ + /bar         -->   /bar
     * /foo/.. + /bar       -->   /bar
     * /foo + bar/c.txt     -->   /foo/bar/c.txt
     * /foo/c.txt + bar     -->   /foo/c.txt/bar (!)
     * </pre>
     * (*) Note that the Windows relative drive prefix is unreliable when
     * used with this method.
     * (!) Note that the first parameter must be a path. If it ends with a name, then
     * the name will be built into the concatenated path. If this might be a problem,
     * use {@link #getFullPath(String)} on the base path argument.
     *
     * @param basePath  the base path to attach to, always treated as a path
     * @param fullFilenameToAdd  the filename (or path) to attach to the base
     * @return the concatenated path, or null if invalid
     */
    public static String concat(String basePath, String fullFilenameToAdd) {
        int prefix = getPrefixLength(fullFilenameToAdd);
        if (prefix < 0) {
            return null;
        }
        if (prefix > 0) {
            return normalize(fullFilenameToAdd);
        }
        if (basePath == null) {
            return null;
        }
        int len = basePath.length();
        if (len == 0) {
            return normalize(fullFilenameToAdd);
        }
        char ch = basePath.charAt(len - 1);
        if (isSeparator(ch)) {
            return normalize(basePath + fullFilenameToAdd);
        } else {
            return normalize(basePath + '/' + fullFilenameToAdd);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Converts all separators to the Unix separator of forward slash.
     * 
     * @param path  the path to be changed, null ignored
     * @return the updated path
     */
    public static String separatorsToUnix(String path) {
        if (path == null || path.indexOf(WINDOWS_SEPARATOR) == -1) {
            return path;
        }
        return path.replace(WINDOWS_SEPARATOR, UNIX_SEPARATOR);
    }

    /**
     * Converts all separators to the Windows separator of backslash.
     * 
     * @param path  the path to be changed, null ignored
     * @return the updated path
     */
    public static String separatorsToWindows(String path) {
        if (path == null || path.indexOf(UNIX_SEPARATOR) == -1) {
            return path;
        }
        return path.replace(UNIX_SEPARATOR, WINDOWS_SEPARATOR);
    }

    /**
     * Converts all separators to the system separator.
     * 
     * @param path  the path to be changed, null ignored
     * @return the updated path
     */
    public static String separatorsToSystem(String path) {
        if (path == null) {
            return null;
        }
        if (SYSTEM_SEPARATOR == WINDOWS_SEPARATOR) {
            return separatorsToWindows(path);
        } else {
            return separatorsToUnix(path);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Returns the length of the filename prefix, such as <code>C:/</code> or <code>~/</code>.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * <p>
     * The prefix length includes the first slash in the full filename
     * if applicable. Thus, it is possible that the length returned is greater
     * than the length of the input string.
     * <pre>
     * Windows:
     * a\b\c.txt           --> ""          --> relative
     * \a\b\c.txt          --> "\"         --> current drive absolute
     * C:a\b\c.txt         --> "C:"        --> drive relative
     * C:\a\b\c.txt        --> "C:\"       --> absolute
     * \\server\a\b\c.txt  --> "\\server\" --> UNC
     *
     * Unix:
     * a/b/c.txt           --> ""          --> relative
     * /a/b/c.txt          --> "/"         --> absolute
     * ~/a/b/c.txt         --> "~/"        --> current com.jcs.shop.user
     * ~                   --> "~/"        --> current com.jcs.shop.user (slash added)
     * ~com.jcs.shop.user/a/b/c.txt     --> "~com.jcs.shop.user/"    --> named com.jcs.shop.user
     * ~com.jcs.shop.user               --> "~com.jcs.shop.user/"    --> named com.jcs.shop.user (slash added)
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     * ie. both Unix and Windows prefixes are matched regardless.
     *
     * @param filename  the filename to find the prefix in, null returns -1
     * @return the length of the prefix, -1 if invalid or null
     */
    public static int getPrefixLength(String filename) {
        if (filename == null) {
            return -1;
        }
        int len = filename.length();
        if (len == 0) {
            return 0;
        }
        char ch0 = filename.charAt(0);
        if (ch0 == ':') {
            return -1;
        }
        if (len == 1) {
            if (ch0 == '~') {
                return 2;  // return a length greater than the input
            }
            return (isSeparator(ch0) ? 1 : 0);
        } else {
            if (ch0 == '~') {
                int posUnix = filename.indexOf(UNIX_SEPARATOR, 1);
                int posWin = filename.indexOf(WINDOWS_SEPARATOR, 1);
                if (posUnix == -1 && posWin == -1) {
                    return len + 1;  // return a length greater than the input
                }
                posUnix = (posUnix == -1 ? posWin : posUnix);
                posWin = (posWin == -1 ? posUnix : posWin);
                return Math.min(posUnix, posWin) + 1;
            }
            char ch1 = filename.charAt(1);
            if (ch1 == ':') {
                ch0 = Character.toUpperCase(ch0);
                if (ch0 >= 'A' && ch0 <= 'Z') {
                    if (len == 2 || isSeparator(filename.charAt(2)) == false) {
                        return 2;
                    }
                    return 3;
                }
                return -1;
                
            } else if (isSeparator(ch0) && isSeparator(ch1)) {
                int posUnix = filename.indexOf(UNIX_SEPARATOR, 2);
                int posWin = filename.indexOf(WINDOWS_SEPARATOR, 2);
                if ((posUnix == -1 && posWin == -1) || posUnix == 2 || posWin == 2) {
                    return -1;
                }
                posUnix = (posUnix == -1 ? posWin : posUnix);
                posWin = (posWin == -1 ? posUnix : posWin);
                return Math.min(posUnix, posWin) + 1;
            } else {
                return (isSeparator(ch0) ? 1 : 0);
            }
        }
    }

    /**
     * Returns the index of the last directory separator character.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * The position of the last forward or backslash is returned.
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     * 
     * @param filename  the filename to find the last path separator in, null returns -1
     * @return the index of the last separator character, or -1 if there
     * is no such character
     */
    public static int indexOfLastSeparator(String filename) {
        if (filename == null) {
            return -1;
        }
        int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
        int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
        return Math.max(lastUnixPos, lastWindowsPos);
    }

    /**
     * Returns the index of the last extension separator character, which is a dot.
     * <p>
     * This method also checks that there is no directory separator after the last dot.
     * To do this it uses {@link #indexOfLastSeparator(String)} which will
     * handle a file in either Unix or Windows format.
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     * 
     * @param filename  the filename to find the last path separator in, null returns -1
     * @return the index of the last separator character, or -1 if there
     * is no such character
     */
    public static int indexOfExtension(String filename) {
        if (filename == null) {
            return -1;
        }
        int extensionPos = filename.lastIndexOf(EXTENSION_SEPARATOR);
        int lastSeparator = indexOfLastSeparator(filename);
        return (lastSeparator > extensionPos ? -1 : extensionPos);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the prefix from a full filename, such as <code>C:/</code>
     * or <code>~/</code>.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * The prefix includes the first slash in the full filename where applicable.
     * <pre>
     * Windows:
     * a\b\c.txt           --> ""          --> relative
     * \a\b\c.txt          --> "\"         --> current drive absolute
     * C:a\b\c.txt         --> "C:"        --> drive relative
     * C:\a\b\c.txt        --> "C:\"       --> absolute
     * \\server\a\b\c.txt  --> "\\server\" --> UNC
     *
     * Unix:
     * a/b/c.txt           --> ""          --> relative
     * /a/b/c.txt          --> "/"         --> absolute
     * ~/a/b/c.txt         --> "~/"        --> current com.jcs.shop.user
     * ~                   --> "~/"        --> current com.jcs.shop.user (slash added)
     * ~com.jcs.shop.user/a/b/c.txt     --> "~com.jcs.shop.user/"    --> named com.jcs.shop.user
     * ~com.jcs.shop.user               --> "~com.jcs.shop.user/"    --> named com.jcs.shop.user (slash added)
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     * ie. both Unix and Windows prefixes are matched regardless.
     *
     * @param filename  the filename to query, null returns null
     * @return the prefix of the file, null if invalid
     */
    public static String getPrefix(String filename) {
        if (filename == null) {
            return null;
        }
        int len = getPrefixLength(filename);
        if (len < 0) {
            return null;
        }
        if (len > filename.length()) {
            return filename + UNIX_SEPARATOR;  // we know this only happens for unix
        }
        return filename.substring(0, len);
    }

    /**
     * Gets the path from a full filename, which excludes the prefix.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * The method is entirely text based, and returns the text before and
     * including the last forward or backslash.
     * <pre>
     * C:\a\b\c.txt --> a\b\
     * ~/a/b/c.txt  --> a/b/
     * a.txt        --> ""
     * a/b/c        --> a/b/
     * a/b/c/       --> a/b/c/
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     * <p>
     * This method drops the prefix from the result.
     * See {@link #getFullPath(String)} for the method that retains the prefix.
     *
     * @param filename  the filename to query, null returns null
     * @return the path of the file, an empty string if none exists, null if invalid
     */
    public static String getPath(String filename) {
        return doGetPath(filename, 1);
    }

    /**
     * Gets the path from a full filename, which excludes the prefix, and
     * also excluding the final directory separator.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * The method is entirely text based, and returns the text before the
     * last forward or backslash.
     * <pre>
     * C:\a\b\c.txt --> a\b
     * ~/a/b/c.txt  --> a/b
     * a.txt        --> ""
     * a/b/c        --> a/b
     * a/b/c/       --> a/b/c
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     * <p>
     * This method drops the prefix from the result.
     * See {@link #getFullPathNoEndSeparator(String)} for the method that retains the prefix.
     *
     * @param filename  the filename to query, null returns null
     * @return the path of the file, an empty string if none exists, null if invalid
     */
    public static String getPathNoEndSeparator(String filename) {
        return doGetPath(filename, 0);
    }

    /**
     * Does the work of getting the path.
     * 
     * @param filename  the filename
     * @param separatorAdd  0 to omit the end separator, 1 to return it
     * @return the path
     */
    private static String doGetPath(String filename, int separatorAdd) {
        if (filename == null) {
            return null;
        }
        int prefix = getPrefixLength(filename);
        if (prefix < 0) {
            return null;
        }
        int index = indexOfLastSeparator(filename);
        if (prefix >= filename.length() || index < 0) {
            return "";
        }
        return filename.substring(prefix, index + separatorAdd);
    }

    /**
     * Gets the full path from a full filename, which is the prefix + path.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * The method is entirely text based, and returns the text before and
     * including the last forward or backslash.
     * <pre>
     * C:\a\b\c.txt --> C:\a\b\
     * ~/a/b/c.txt  --> ~/a/b/
     * a.txt        --> ""
     * a/b/c        --> a/b/
     * a/b/c/       --> a/b/c/
     * C:           --> C:
     * C:\          --> C:\
     * ~            --> ~/
     * ~/           --> ~/
     * ~com.jcs.shop.user        --> ~com.jcs.shop.user/
     * ~com.jcs.shop.user/       --> ~com.jcs.shop.user/
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filename  the filename to query, null returns null
     * @return the path of the file, an empty string if none exists, null if invalid
     */
    public static String getFullPath(String filename) {
        return doGetFullPath(filename, true);
    }

    /**
     * Gets the full path from a full filename, which is the prefix + path,
     * and also excluding the final directory separator.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * The method is entirely text based, and returns the text before the
     * last forward or backslash.
     * <pre>
     * C:\a\b\c.txt --> C:\a\b
     * ~/a/b/c.txt  --> ~/a/b
     * a.txt        --> ""
     * a/b/c        --> a/b
     * a/b/c/       --> a/b/c
     * C:           --> C:
     * C:\          --> C:\
     * ~            --> ~
     * ~/           --> ~
     * ~com.jcs.shop.user        --> ~com.jcs.shop.user
     * ~com.jcs.shop.user/       --> ~com.jcs.shop.user
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filename  the filename to query, null returns null
     * @return the path of the file, an empty string if none exists, null if invalid
     */
    public static String getFullPathNoEndSeparator(String filename) {
        return doGetFullPath(filename, false);
    }

    /**
     * Does the work of getting the path.
     * 
     * @param filename  the filename
     * @param includeSeparator  true to include the end separator
     * @return the path
     */
    private static String doGetFullPath(String filename, boolean includeSeparator) {
        if (filename == null) {
            return null;
        }
        int prefix = getPrefixLength(filename);
        if (prefix < 0) {
            return null;
        }
        if (prefix >= filename.length()) {
            if (includeSeparator) {
                return getPrefix(filename);  // add end slash if necessary
            } else {
                return filename;
            }
        }
        int index = indexOfLastSeparator(filename);
        if (index < 0) {
            return filename.substring(0, prefix);
        }
        int end = index + (includeSeparator ?  1 : 0);
        return filename.substring(0, end);
    }

    /**
     * Gets the name minus the path from a full filename.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * The text after the last forward or backslash is returned.
     * <pre>
     * a/b/c.txt --> c.txt
     * a.txt     --> a.txt
     * a/b/c     --> c
     * a/b/c/    --> ""
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filename  the filename to query, null returns null
     * @return the name of the file without the path, or an empty string if none exists
     */
    public static String getName(String filename) {
        if (filename == null) {
            return null;
        }
        int index = indexOfLastSeparator(filename);
        return filename.substring(index + 1);
    }

    /**
     * Gets the base name, minus the full path and extension, from a full filename.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * The text after the last forward or backslash and before the last dot is returned.
     * <pre>
     * a/b/c.txt --> c
     * a.txt     --> a
     * a/b/c     --> c
     * a/b/c/    --> ""
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filename  the filename to query, null returns null
     * @return the name of the file without the path, or an empty string if none exists
     */
    public static String getBaseName(String filename) {
        return removeExtension(getName(filename));
    }

    /**
     * Gets the extension of a filename.
     * <p>
     * This method returns the textual part of the filename after the last dot.
     * There must be no directory separator after the dot.
     * <pre>
     * foo.txt      --> "txt"
     * a/b/c.jpg    --> "jpg"
     * a/b.txt/c    --> ""
     * a/b/c        --> ""
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filename the filename to retrieve the extension of.
     * @return the extension of the file or an empty string if none exists.
     */
    public static String getExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int index = indexOfExtension(filename);
        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Removes the extension from a filename.
     * <p>
     * This method returns the textual part of the filename before the last dot.
     * There must be no directory separator after the dot.
     * <pre>
     * foo.txt    --> foo
     * a\b\c.jpg  --> a\b\c
     * a\b\c      --> a\b\c
     * a.b\c      --> a.b\c
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filename  the filename to query, null returns null
     * @return the filename minus the extension
     */
    public static String removeExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int index = indexOfExtension(filename);
        if (index == -1) {
            return filename;
        } else {
            return filename.substring(0, index);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Checks whether two filenames are equal exactly.
     * <p>
     * No processing is performed on the filenames other than comparison,
     * thus this is merely a null-safe case-sensitive equals.
     *
     * @param filename1  the first filename to query, may be null
     * @param filename2  the second filename to query, may be null
     * @return true if the filenames are equal, null equals null
     */
    public static boolean equals(String filename1, String filename2) {
        return equals(filename1, filename2, false, false);
    }

    /**
     * Checks whether two filenames are equal using the case rules of the system.
     * <p>
     * No processing is performed on the filenames other than comparison.
     * The check is case-sensitive on Unix and case-insensitive on Windows.
     *
     * @param filename1  the first filename to query, may be null
     * @param filename2  the second filename to query, may be null
     * @return true if the filenames are equal, null equals null
     */
    public static boolean equalsOnSystem(String filename1, String filename2) {
        return equals(filename1, filename2, true, false);
    }

    //-----------------------------------------------------------------------
    /**
     * Checks whether two filenames are equal after both have been normalized.
     * <p>
     * Both filenames are first passed to {@link #normalize(String)}.
     * The check is then performed in a case-sensitive manner.
     *
     * @param filename1  the first filename to query, may be null
     * @param filename2  the second filename to query, may be null
     * @return true if the filenames are equal, null equals null
     */
    public static boolean equalsNormalized(String filename1, String filename2) {
        return equals(filename1, filename2, false, true);
    }

    /**
     * Checks whether two filenames are equal after both have been normalized
     * and using the case rules of the system.
     * <p>
     * Both filenames are first passed to {@link #normalize(String)}.
     * The check is then performed case-sensitive on Unix and
     * case-insensitive on Windows.
     *
     * @param filename1  the first filename to query, may be null
     * @param filename2  the second filename to query, may be null
     * @return true if the filenames are equal, null equals null
     */
    public static boolean equalsNormalizedOnSystem(String filename1, String filename2) {
        return equals(filename1, filename2, true, true);
    }

    /**
     * Checks whether two filenames are equal after both have been normalized
     * and optionally using the case rules of the system.
     * <p>
     * Both filenames are first passed to {@link #normalize(String)}.
     *
     * @param filename1  the first filename to query, may be null
     * @param filename2  the second filename to query, may be null
     * @param system  whether to use the system (windows or unix)
     * @param normalized  whether to normalize the filenames
     * @return true if the filenames are equal, null equals null
     */
    private static boolean equals(
            String filename1, String filename2,
            boolean system, boolean normalized) {
        if (filename1 == filename2) {
            return true;
        }
        if (filename1 == null || filename2 == null) {
            return false;
        }
        if (normalized) {
            filename1 = normalize(filename1);
            filename2 = normalize(filename2);
        }
        if (system && (SYSTEM_SEPARATOR == WINDOWS_SEPARATOR)) {
            return filename1.equalsIgnoreCase(filename2);
        } else {
            return filename1.equals(filename2);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Checks whether the extension of the filename is that specified.
     * <p>
     * This method obtains the extension as the textual part of the filename
     * after the last dot. There must be no directory separator after the dot.
     * The extension check is case-sensitive on all platforms.
     *
     * @param filename  the filename to query, null returns false
     * @param extension  the extension to check for, null or empty checks for no extension
     * @return true if the filename has the specified extension
     */
    public static boolean isExtension(String filename, String extension) {
        if (filename == null) {
            return false;
        }
        if (extension == null || extension.length() == 0) {
            return (indexOfExtension(filename) == -1);
        }
        String fileExt = getExtension(filename);
        return fileExt.equals(extension);
    }

    /**
     * Checks whether the extension of the filename is one of those specified.
     * <p>
     * This method obtains the extension as the textual part of the filename
     * after the last dot. There must be no directory separator after the dot.
     * The extension check is case-sensitive on all platforms.
     *
     * @param filename  the filename to query, null returns false
     * @param extensions  the extensions to check for, null checks for no extension
     * @return true if the filename is one of the extensions
     */
    public static boolean isExtension(String filename, String[] extensions) {
        if (filename == null) {
            return false;
        }
        if (extensions == null || extensions.length == 0) {
            return (indexOfExtension(filename) == -1);
        }
        String fileExt = getExtension(filename);
        for (int i = 0; i < extensions.length; i++) {
            if (fileExt.equals(extensions[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether the extension of the filename is one of those specified.
     * <p>
     * This method obtains the extension as the textual part of the filename
     * after the last dot. There must be no directory separator after the dot.
     * The extension check is case-sensitive on all platforms.
     *
     * @param filename  the filename to query, null returns false
     * @param extensions  the extensions to check for, null checks for no extension
     * @return true if the filename is one of the extensions
     */
    public static boolean isExtension(String filename, Collection extensions) {
        if (filename == null) {
            return false;
        }
        if (extensions == null || extensions.isEmpty()) {
            return (indexOfExtension(filename) == -1);
        }
        String fileExt = getExtension(filename);
        for (Iterator it = extensions.iterator(); it.hasNext();) {
            if (fileExt.equals(it.next())) {
                return true;
            }
        }
        return false;
    }

    //-----------------------------------------------------------------------
    /**
     * Checks a filename to see if it matches the specified wildcard matcher,
     * always testing case-sensitive.
     * <p>
     * The wildcard matcher uses the characters '?' and '*' to represent a
     * single or multiple wildcard characters.
     * This is the same as often found on Dos/Unix command lines.
     * The extension check is case-sensitive.
     * <pre>
     * wildcardMatch("c.txt", "*.txt")      --> true
     * wildcardMatch("c.txt", "*.jpg")      --> false
     * wildcardMatch("a/b/c.txt", "a/b/*")  --> true
     * wildcardMatch("c.txt", "*.???")      --> true
     * wildcardMatch("c.txt", "*.????")     --> false
     * </pre>
     * 
     * @param filename  the filename to match on
     * @param wildcardMatcher  the wildcard string to match against
     * @return true if the filename matches the wilcard string
     */
    public static boolean wildcardMatch(String filename, String wildcardMatcher) {
        return wildcardMatch(filename, wildcardMatcher, false);
    }

    /**
     * Checks a filename to see if it matches the specified wildcard matcher
     * using the case rules of the system.
     * <p>
     * The wildcard matcher uses the characters '?' and '*' to represent a
     * single or multiple wildcard characters.
     * This is the same as often found on Dos/Unix command lines.
     * The check is case-sensitive on Unix and case-insensitive on Windows.
     * <pre>
     * wildcardMatch("c.txt", "*.txt")      --> true
     * wildcardMatch("c.txt", "*.jpg")      --> false
     * wildcardMatch("a/b/c.txt", "a/b/*")  --> true
     * wildcardMatch("c.txt", "*.???")      --> true
     * wildcardMatch("c.txt", "*.????")     --> false
     * </pre>
     * 
     * @param filename  the filename to match on
     * @param wildcardMatcher  the wildcard string to match against
     * @return true if the filename matches the wilcard string
     */
    public static boolean wildcardMatchOnSystem(String filename, String wildcardMatcher) {
        return wildcardMatch(filename, wildcardMatcher, true);
    }

    /**
     * Checks a filename to see if it matches the specified wildcard matcher.
     * <p>
     * The wildcard matcher uses the characters '?' and '*' to represent a
     * single or multiple wildcard characters.
     * 
     * @param filename  the filename to match on
     * @param wildcardMatcher  the wildcard string to match against
     * @param system  whether to use the system (windows or unix)
     * @return true if the filename matches the wilcard string
     */
    private static boolean wildcardMatch(String filename, String wildcardMatcher, boolean system) {
        if (filename == null && wildcardMatcher == null) {
            return true;
        }
        if (filename == null || wildcardMatcher == null) {
            return false;
        }
        if (system && (SYSTEM_SEPARATOR == WINDOWS_SEPARATOR)) {
            filename = filename.toLowerCase();
            wildcardMatcher = wildcardMatcher.toLowerCase();
        }
        String[] wcs = splitOnTokens(wildcardMatcher);
        boolean anyChars = false;
        int textIdx = 0;
        int wcsIdx = 0;
        Stack backtrack = new Stack();
        
        // loop around a backtrack stack, to handle complex * matching
        do {
            if (backtrack.size() > 0) {
                int[] array = (int[]) backtrack.pop();
                wcsIdx = array[0];
                textIdx = array[1];
                anyChars = true;
            }
            
            // loop whilst tokens and text left to process
            while (wcsIdx < wcs.length) {
      
                if (wcs[wcsIdx].equals("?")) {
                    // ? so move to next text char
                    textIdx++;
                    anyChars = false;
                    
                } else if (wcs[wcsIdx].equals("*")) {
                    // set any chars status
                    anyChars = true;
                    if (wcsIdx == wcs.length - 1) {
                        textIdx = filename.length();
                    }
                    
                } else {
                    // matching text token
                    if (anyChars) {
                        // any chars then try to locate text token
                        textIdx = filename.indexOf(wcs[wcsIdx], textIdx);
                        if (textIdx == -1) {
                            // token not found
                            break;
                        }
                        int repeat = filename.indexOf(wcs[wcsIdx], textIdx + 1);
                        if (repeat >= 0) {
                            backtrack.push(new int[] {wcsIdx, repeat});
                        }
                    } else {
                        // matching from current position
                        if (!filename.startsWith(wcs[wcsIdx], textIdx)) {
                            // couldnt match token
                            break;
                        }
                    }
      
                    // matched text token, move text index to end of matched token
                    textIdx += wcs[wcsIdx].length();
                    anyChars = false;
                }
      
                wcsIdx++;
            }
            
            // full match
            if (wcsIdx == wcs.length && textIdx == filename.length()) {
                return true;
            }
            
        } while (backtrack.size() > 0);
  
        return false;
    }

    /**
     * Splits a string into a number of tokens.
     * 
     * @param text  the text to split
     * @return the tokens, never null
     */
    static String[] splitOnTokens(String text) {
        // used by wildcardMatch
        // package level so a unit test may run on this
        
        if (text.indexOf("?") == -1 && text.indexOf("*") == -1) {
            return new String[] { text };
        }

        char[] array = text.toCharArray();
        ArrayList list = new ArrayList();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            if (array[i] == '?' || array[i] == '*') {
                if (buffer.length() != 0) {
                    list.add(buffer.toString());
                    buffer.setLength(0);
                }
                if (array[i] == '?') {
                    list.add("?");
                } else if (list.size() == 0 ||
                        (i > 0 && list.get(list.size() - 1).equals("*") == false)) {
                    list.add("*");
                }
            } else {
                buffer.append(array[i]);
            }
        }
        if (buffer.length() != 0) {
            list.add(buffer.toString());
        }

        return (String[]) list.toArray(new String[0]);
    }
    
 // Configuration settings ///////////////////////////////////////////////////
    
    /**
     * Default 10 digit file storage distribution array. This means that if I 
     * want to name file as 10 digit number e.g. number 123 as 0000000123 or 
     * number 123456789 as 01234567890. Then the path constructed from number 
     * 1234567890 using distribution 2/2/2/4 would be 12/34/56/0123456789 
     */
    public static final int[] DEFAULT_STRORAGE_TREE_DISTRIBUTION = {2, 2, 2, 4};
    
    /**
     * How big buffer to use to process files.
     */
    public static final int BUFFER_SIZE = 65536;
    
    // Cached values ////////////////////////////////////////////////////////////

    
    /**
     * Temporary directory to use. It is guarantee that it ends with \ (or /)
     */
    protected static String s_strTempDirectory;
    
    // Constructors /////////////////////////////////////////////////////////////

    
   
    
    /**
     * Move file to a new location. If the destination is on different volume,
     * this file will be copied and then original file will be deleted.
     * If the destination already exists, this method renames it with different
     * name and leaves it in that directory and moves the new file along side 
     * the renamed one.
     * 
     * @param flCurrent - file to move
     * @param flDestination - destination file
     * @throws IOException - error message
     *
     */
    public static void moveFile(
       File flCurrent, 
       File flDestination
    ) throws IOException
    {
       // Make sure that the source exist, it might be already moved from 
       // a directory and we just don't know about it
       if (flCurrent.exists())
       {
          // Next check if the destination file exists
          if (flDestination.exists())
          {
             // If the destination exists, that means something went wrong
             // Rename the destination file under temporaty name and try to  
             // move the new file instead of it
           
             renameToTemporaryName(flDestination, "old");
          }
       
          // Make sure the directory exists and if not create it
          File flFolder;
          
          flFolder = flDestination.getParentFile();
          if ((flFolder != null) && (!flFolder.exists()))
          {
             if (!flFolder.mkdirs())
             {
                // Do not throw the exception if the directory already exists
                // because it was created meanwhile for example by a different 
                // thread
                if (!flFolder.exists())
                {
                   throw new IOException("Cannot create directory " + flFolder);
                }
             }
          }
          
          // Now everything should exist so try to rename the file first
          // After testing, this renames files even between volumes C to H 
          // so we don't have to do anything else on Windows but we still
          // have to handle erro on Unix 
          if (!flCurrent.renameTo(flDestination))
          {
             // Try to copy and delete since the rename doesn't work on Solaris
             // between file systems
             copyFile(flCurrent, flDestination);
             
             // Now delete the file
             if (!flCurrent.delete())
             {
                // Delete the destination file first since we haven't really moved
                // the file
                flDestination.delete();
                throw new IOException("Cannot delete already copied file " + flCurrent);
             }
          }
       }   
    }
  
    /**
     * Copy the current file to the destination file.
     * 
     * @param flCurrent - source file
     * @param flDestination - destination file
     * @throws IOException - error message
     *
     */  
    public static void copyFile(
       File flCurrent,
       File flDestination
    ) throws IOException
    {
      // Make sure the directory exists and if not create it
      File flFolder;
      
      flFolder = flDestination.getParentFile();
      if ((flFolder != null) && (!flFolder.exists()))
      {
         if (!flFolder.mkdirs())
         {
            // Do not throw the exception if the directory already exists
            // because it was created meanwhile for example by a different 
            // thread
            if (!flFolder.exists())
            {
               throw new IOException("Cannot create directory " + flFolder);
            }
         }
      }

       // FileChannel srcChannel = null;
       // FileChannel dstChannel = null;
       FileInputStream finInput = null;
       
       //MHALAS: This code is not working reliably on Solaris 8 with 1.4.1_01
       // Getting exceptions from native code
       /*
       // Create channel on the source
       srcChannel = new FileInputStream(flCurrent).getChannel();
       // Create channel on the destination
       dstChannel = new FileOutputStream(flDestination).getChannel();
  
       // Copy file contents from source to destination
       dstChannel.transferFrom(srcChannel, 0, srcChannel.size());   
          
       Don't forget to close the channels if you enable this code again
       */
       try
       {
          finInput = new FileInputStream(flCurrent);
       }
       catch (IOException ioExec)
       {
          if (finInput != null)
          {
             try
             {
                finInput.close();
             }
             catch (Throwable thr)
             {
               
             }
          }
          throw ioExec;
       }

        copyStreamToFile(finInput, flDestination);
    }
    
    /**
     * Rename the file to temporaty name with given prefix
     * 
     * @param flFileToRename - file to rename
     * @param strPrefix - prefix to use
     * @throws IOException - error message
     */
    public static void renameToTemporaryName(
       File   flFileToRename,
       String strPrefix
    ) throws IOException
    {
       assert strPrefix != null : "Prefix cannot be null.";
       
       String       strParent;
       StringBuffer sbBuffer = new StringBuffer();
       File         flTemp;
       int          iIndex = 0;
       
       strParent = flFileToRename.getParent();

       // Generate new name for the file in a deterministic way
       do
       {
          iIndex++;
          sbBuffer.delete(0, sbBuffer.length());
          if (strParent != null) 
          {
             sbBuffer.append(strParent);
             sbBuffer.append(File.separatorChar);
          }
          
          sbBuffer.append(strPrefix);
          sbBuffer.append("_");
          sbBuffer.append(iIndex);
          sbBuffer.append("_");
          sbBuffer.append(flFileToRename.getName());
                
          flTemp = new File(sbBuffer.toString());
       }      
       while (flTemp.exists());
       
       // Now we should have unique name
       if (!flFileToRename.renameTo(flTemp))
       {
          throw new IOException("Cannot rename " + flFileToRename.getAbsolutePath()
                                + " to " + flTemp.getAbsolutePath());
       }
    }

    /** 
     * Delete all files and directories in directory but do not delete the
     * directory itself.
     * 
     * @param strDir - string that specifies directory to delete
     * @return boolean - sucess flag
     */
    public static boolean deleteDirectoryContent(
       String strDir
    )
    {
       return ((strDir != null) && (strDir.length() > 0)) 
               ? deleteDirectoryContent(new File(strDir)) : false;
    }

    /** 
     * Delete all files and directories in directory but do not delete the
     * directory itself.
     * 
     * @param fDir - directory to delete
     * @return boolean - sucess flag
     */
    public static boolean deleteDirectoryContent(
       File fDir
    )
    {
       boolean bRetval = false;

       if (fDir != null && fDir.isDirectory()) 
       {
          File[] files = fDir.listFiles();
    
          if (files != null)
          {
             bRetval = true;
             boolean dirDeleted;
             
             for (int index = 0; index < files.length; index++)
             {
                if (files[index].isDirectory())
                {
                   // TODO: Performance: Implement this as a queue where you add to
                   // the end and take from the beginning, it will be more efficient
                   // than the recursion
                   dirDeleted = deleteDirectoryContent(files[index]);
                   if (dirDeleted)
                   {
                      bRetval = bRetval && files[index].delete();
                   }
                   else
                   {
                      bRetval = false;
                   }
                }
                else
                {
                   bRetval = bRetval && files[index].delete();
                }
             }
          }
       }

       return bRetval;
    }

    /**
     * Deletes all files and subdirectories under the specified directory including 
     * the specified directory
     * 
     * @param strDir - string that specifies directory to be deleted
     * @return boolean - true if directory was successfully deleted
     */
    public static boolean deleteDir(
       String strDir
    ) 
    {
       return ((strDir != null) && (strDir.length() > 0)) 
                 ? deleteDir(new File(strDir)) : false;
    }
    
    /**
     * Deletes all files and subdirectories under the specified directory including 
     * the specified directory
     * 
     * @param fDir - directory to be deleted
     * @return boolean - true if directory was successfully deleted
     */
    public static boolean deleteDir(
       File fDir
    ) 
    {
       boolean bRetval = false;
       if (fDir != null && fDir.exists())
       {
          bRetval = deleteDirectoryContent(fDir);
          if (bRetval)
          {
             bRetval = bRetval && fDir.delete();         
          }
       }
       return bRetval;
    }
    
    /**
     * Compare binary files. Both files must be files (not directories) and exist.
     * 
     * @param first  - first file
     * @param second - second file
     * @return boolean - true if files are binery equal
     * @throws IOException - error in function
     */
    public boolean isFileBinaryEqual(
       File first,
       File second
    ) throws IOException
    {
       // TODO: Test: Missing test
       boolean retval = false;
       
       if ((first.exists()) && (second.exists()) 
          && (first.isFile()) && (second.isFile()))
       {
          if (first.getCanonicalPath().equals(second.getCanonicalPath()))
          {
             retval = true;
          }
          else
          {
             FileInputStream firstInput = null;
             FileInputStream secondInput = null;
             BufferedInputStream bufFirstInput = null;
             BufferedInputStream bufSecondInput = null;

             try
             {            
                firstInput = new FileInputStream(first); 
                secondInput = new FileInputStream(second);
                bufFirstInput = new BufferedInputStream(firstInput, BUFFER_SIZE); 
                bufSecondInput = new BufferedInputStream(secondInput, BUFFER_SIZE);
    
                int firstByte;
                int secondByte;
                
                while (true)
                {
                   firstByte = bufFirstInput.read();
                   secondByte = bufSecondInput.read();
                   if (firstByte != secondByte)
                   {
                      break;
                   }
                   if ((firstByte < 0) && (secondByte < 0))
                   {
                      retval = true;
                      break;
                   }
                }
             }
             finally
             {
                try
                {
                   if (bufFirstInput != null)
                   {
                      bufFirstInput.close();
                   }
                }
                finally
                {
                   if (bufSecondInput != null)
                   {
                      bufSecondInput.close();
                   }
                }
             }
          }
       }
       
       return retval;
    }


       
    /**
     * Get path which represents temporary directory. It is guarantee that it 
     * ends with \ (or /).
     * 
     * @return String
     */
    public static String getTemporaryDirectory(
    )
    {
       return s_strTempDirectory;
    }
    

    /**
     * Copy any input stream to output file. Once the data will be copied
     * the stream will be closed.
     * 
     * @param input  - InputStream to copy from
     * @param output - File to copy to
     * @throws IOException - error in function
     * @throws
     */
    public static void copyStreamToFile(
       InputStream input,
       File        output
    ) throws IOException
    {
       FileOutputStream foutOutput = null;
        // open input file as stream safe - it can throw some IOException
       try
       {
          foutOutput = new FileOutputStream(output);
        }
       catch (IOException ioExec)
       {
          if (foutOutput != null)
          {
             try
             {
                 foutOutput.close();
             }
             catch (IOException ioExec2)
             {
            	 ioExec2.printStackTrace();
             }
          }            
          
          throw ioExec;
       }
  
       // all streams including os are closed in copyStreamToStream function 
       // in any case
       copyStreamToStream(input, foutOutput);
    }

    /**
     * Copy any input stream to output stream. Once the data will be copied
     * both streams will be closed.
     * 
     * @param input  - InputStream to copy from
     * @param output - OutputStream to copy to
     * @throws IOException - io error in function
     *
     */
    public static void copyStreamToStream(
       InputStream input,
       OutputStream output
    ) throws IOException
    {
       InputStream is = null;
       OutputStream os = null;
       int                 ch;

       try
       {
          if (input instanceof BufferedInputStream)
          {
             is = input;
          }
          else
          {
             is = new BufferedInputStream(input);
          }
          if (output instanceof BufferedOutputStream)
          {
             os = output;
          }
          else
          {
             os = new BufferedOutputStream(output);
          }
         
          while ((ch = is.read()) != -1)
          {
              os.write(ch);
          }
          os.flush();
       }
       finally
       {
          IOException exec1 = null;
          IOException exec2 = null;
          try
          {
             // because this close can throw exception we do next close in 
             // finally statement
             if (os != null)
             {
                try
                {
                   os.close();
                }
                catch (IOException exec)
                {
                   exec1 = exec;
                }
             }
          }
          finally
          {
             if (is != null)
             {
                try
                {
                   is.close();
                }
                catch (IOException exec)
                {
                   exec2 = exec;
                }
             }
          }
          if ((exec1 != null) && (exec2 != null))
          {
            throw exec1;
          }
          else if (exec1 != null)
          {
             throw exec1;
          }
          else if (exec2 != null)
          {
             throw exec2;
          }
       }
    }
    
  //Wpisuej dane do pliku 
  	static	boolean setBytesToFile(byte[] bytes, File file) throws IOException
  			{
  			FileOutputStream fos;
  			 
  				fos = new FileOutputStream(file);
  			    fos.write(bytes);
  				fos.flush();
  				fos.close();
  			  
  		   
  		     return true;
  			}
 // 	
 public static byte[]  getBytesFromFile(File file) throws  Exception 
     {
	    //
	    ByteArrayOutputStream bos ;
	    FileInputStream fIn;
	      
	   	//
	   	 bos=new ByteArrayOutputStream();
  	     fIn = new  FileInputStream(file);
	   	//
  	    int  readByte;
  	    while((readByte=fIn.read( ))!=-1)
  	        {
  	     	bos.write(readByte);
  	        }
        //
  	     byte[] fileData= bos.toByteArray() ;
  	     bos.close();
	     fIn.close();
        //
	    return fileData;
     }

 private void createTempFileOldWay() throws IOException {
 File tempFile = File.createTempFile("tempfile-old", ".tmp");
 PrintWriter writer = null;
 try {
     writer = new PrintWriter(new FileWriter(tempFile));
     writer.println("Line1");
     writer.println("Line2");
 } finally {
     if (writer != null)
         writer.close();
 }
  
 //System.out.printf("Wrote text to temporary file %s%n", tempFile.toString());
}


static public File createFile(String filePath )  {
	 
	 File tempFile=null;
try {
	   
	   // log.debug("createTempFileOldWayWithName3="+tempDir+"\\"+fileName);
	   tempFile = new File(filePath);
	  tempFile.getParentFile().mkdirs();
	  tempFile.createNewFile();
	return tempFile;
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
 
return null;
}

static public void copyInputStreamToFile( InputStream in, File file ) {
    try {
        OutputStream out = new FileOutputStream(file);
        byte[] buf = new byte[1024];
        int len;
        while((len=in.read(buf))>0){
            out.write(buf,0,len);
        }
        out.close();
        in.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

 public String readFile(String filename, String encoding)
	{
	   String content = null;
	   File file = new File(filename); //for ex foo.txt
	   try {
		   FileInputStream is = new FileInputStream(filename);
		   InputStreamReader isr = new InputStreamReader(is,encoding);
		   BufferedReader reader = new BufferedReader(isr);
	       char[] chars = new char[(int) file.length()];
	       reader.read(chars);
	       content = new String(chars);
	       reader.close();
	   } catch (IOException e) {
	       e.printStackTrace();
	   }
	   return content;
	}



    public String readFile(  File file)
    {
        String content = null;
        try {
            FileInputStream is = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(is,"UTF8");
            BufferedReader reader = new BufferedReader(isr);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
static public String readFile(String filename )
	{
	   String content = null;
	   File file = new File(filename); //for ex foo.txt
	   try {
		   
		   
		   FileInputStream is = new FileInputStream(filename);
		   InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8 );
		   BufferedReader reader = new BufferedReader(isr);
	       char[] chars = new char[(int) file.length()];
	       reader.read(chars);
	       content = new String(chars);
	       reader.close();
	   } catch (IOException e) {
	       e.printStackTrace();
	   }
	   return content;
	}
 public boolean exists(String filePathString)
 {
	 File f = new File(filePathString);
	 if(f.exists() && !f.isDirectory()) 
        return true;
	 else
		 return false;
 }
 public static boolean deleteFile( File fDir) 
	    {
	      //System.out.println("DELE="+fDir.getAbsolutePath());
	       boolean bRetval = false;
	       if (fDir != null && fDir.exists())
	       {
	           fDir.delete();         
	           
	       }
	       return bRetval;
	    }
 
 public static void writeToFile(String path, String data) throws IOException {
	      
	    File tempFile = new File(path);
	   
	    BufferedWriter writer = null;
	    try {
	          writer = new BufferedWriter( new OutputStreamWriter(new FileOutputStream(tempFile), "UTF-8") );
	 	      writer.write(data);
	       
	    } finally {
	        if (writer != null)
	            writer.close();
	    }
	    ////System.out.printf("Wrote text to  file ", tempFile.toString());
	}

    public static void writeToFile(File tempFile , String data) throws IOException {

         BufferedWriter writer = null;
        try {
            writer = new BufferedWriter( new OutputStreamWriter(new FileOutputStream(tempFile), "UTF-8") );
            writer.write(data);

        } finally {
            if (writer != null)
                writer.close();
        }
        ////System.out.printf("Wrote text to  file ", tempFile.toString());
    }

    public static void writeToFile(FileOutputStream tempFile , String data) throws IOException {

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter( new OutputStreamWriter( tempFile , "UTF-8") );
            writer.write(data);

        } finally {
            if (writer != null)
                writer.close();
        }
        ////System.out.printf("Wrote text to  file ", tempFile.toString());
    }


    String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}


