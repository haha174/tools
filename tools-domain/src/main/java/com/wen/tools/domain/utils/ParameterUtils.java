package com.wen.tools.domain.utils;


import com.wen.tools.domain.config.IConstantsDomain;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.*;
import java.util.*;

public class ParameterUtils {

    private final Map<String, String> data;
    public ParameterUtils(Map<String, String> data) {
        this.data = Collections.unmodifiableMap(new HashMap<>(data));
    }

    public static ParameterUtils fromMap(Map<String, String> map) {
        Preconditions.checkNotNull(map, "Unable to initialize from empty map");
        return new ParameterUtils(map);
    }

    public static ParameterUtils fromArgs(String[] args) {
        final Map<String, String> map = new HashMap<>(args.length / 2);
        int i = 0;
        while (i < args.length) {
            final String key;

            if (args[i].startsWith("--")) {
                key = args[i].substring(2);
            } else if (args[i].startsWith("-")) {
                key = args[i].substring(1);
            } else {
                throw new IllegalArgumentException(
                        String.format("Error parsing arguments '%s' on '%s'. Please prefix keys with -- or -.",
                                Arrays.toString(args), args[i]));
            }

            if (key.isEmpty()) {
                throw new IllegalArgumentException(
                        "The input " + Arrays.toString(args) + " contains an empty argument");
            }

            i += 1; // try to find the value

            if (i >= args.length) {
                map.put(key, IConstantsDomain.DefaultValue.NO_VALUE_KEY);
            } else if (NumberUtils.isNumber(args[i])) {
                map.put(key, args[i]);
                i += 1;
            } else if (args[i].startsWith("--") || args[i].startsWith("-")) {
                // the argument cannot be a negative number because we checked earlier
                // -> the next argument is a parameter name
                map.put(key, IConstantsDomain.DefaultValue.NO_VALUE_KEY);
            } else {
                map.put(key, args[i]);
                i += 1;
            }
        }

        return fromMap(map);
    }

    /**
     * get ParameterUtils by PropertiesFile
     *
     * @param name
     * @return
     * @throws IOException
     */
    public static ParameterUtils fromPropertiesFile(String name) throws IOException {
        ClassLoader classLoader = ParameterUtils.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(name);
        if (inputStream == null) {
            inputStream = new FileInputStream(new File(name));
        }
        if (inputStream == null) {
            throw new FileNotFoundException("Properties file name:" + name + " does not exist");
        }
        return fromPropertiesFile(inputStream);
    }

    /**
     * get properties by InputStream
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static ParameterUtils fromPropertiesFile(InputStream inputStream) throws IOException {
        Properties props = new Properties();
        props.load(inputStream);
        return fromMap((Map) props);
    }

    /**
     * get System properties
     *
     * @return
     */
    public static ParameterUtils fromSystemProperties() {
        return fromMap((Map) System.getProperties());
    }


    /**
     * Returns number of parameters in {@link ParameterUtils}.
     */
    public int getNumberOfParameters() {
        return data.size();
    }

    /**
     * Returns the String value for the given key.
     * If the key does not exist it will return null.
     */
    public String get(String key) {
        return data.get(key);
    }

    /**
     * Returns the String value for the given key.
     * If the key does not exist it will throw a {@link RuntimeException}.
     */
    public String getRequired(String key) {
        String value = get(key);
        if (value == null) {
            throw new RuntimeException("No data for required key '" + key + "'");
        }
        return value;
    }

    /**
     * Returns the String value for the given key.
     * If the key does not exist it will return the given default value.
     */
    public String get(String key, String defaultValue) {
        String value = get(key);
        if (value == null) {
            return defaultValue;
        } else {
            return value;
        }
    }

    /**
     * Check if value is set.
     */
    public boolean has(String value) {
        return data.containsKey(value);
    }

    // -------------- Integer

    /**
     * Returns the Integer value for the given key.
     * The method fails if the key does not exist or the value is not an Integer.
     */
    public int getInt(String key) {
        String value = getRequired(key);
        return Integer.parseInt(value);
    }

    /**
     * Returns the Integer value for the given key. If the key does not exists it will return the default value given.
     * The method fails if the value is not an Integer.
     */
    public int getInt(String key, int defaultValue) {
        String value = get(key);
        if (value == null) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    // -------------- LONG

    /**
     * Returns the Long value for the given key.
     * The method fails if the key does not exist.
     */
    public long getLong(String key) {
        String value = getRequired(key);
        return Long.parseLong(value);
    }

    /**
     * Returns the Long value for the given key. If the key does not exists it will return the default value given.
     * The method fails if the value is not a Long.
     */
    public long getLong(String key, long defaultValue) {
        String value = get(key);
        if (value == null) {
            return defaultValue;
        }
        return Long.parseLong(value);
    }

    // -------------- FLOAT

    /**
     * Returns the Float value for the given key.
     * The method fails if the key does not exist.
     */
    public float getFloat(String key) {
        String value = getRequired(key);
        return Float.valueOf(value);
    }

    /**
     * Returns the Float value for the given key. If the key does not exists it will return the default value given.
     * The method fails if the value is not a Float.
     */
    public float getFloat(String key, float defaultValue) {
        String value = get(key);
        if (value == null) {
            return defaultValue;
        } else {
            return Float.valueOf(value);
        }
    }

    // -------------- DOUBLE

    /**
     * Returns the Double value for the given key.
     * The method fails if the key does not exist.
     */
    public double getDouble(String key) {
        String value = getRequired(key);
        return Double.valueOf(value);
    }

    /**
     * Returns the Double value for the given key. If the key does not exists it will return the default value given.
     * The method fails if the value is not a Double.
     */
    public double getDouble(String key, double defaultValue) {
        String value = get(key);
        if (value == null) {
            return defaultValue;
        } else {
            return Double.valueOf(value);
        }
    }

    // -------------- BOOLEAN

    /**
     * Returns the Boolean value for the given key.
     * The method fails if the key does not exist.
     */
    public boolean getBoolean(String key) {
        String value = getRequired(key);
        return Boolean.valueOf(value);
    }

    /**
     * Returns the Boolean value for the given key. If the key does not exists it will return the default value given.
     * The method returns whether the string of the value is "true" ignoring cases.
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);
        if (value == null) {
            return defaultValue;
        } else {
            return Boolean.valueOf(value);
        }
    }

    // -------------- SHORT

    /**
     * Returns the Short value for the given key.
     * The method fails if the key does not exist.
     */
    public short getShort(String key) {
        String value = getRequired(key);
        return Short.valueOf(value);
    }

    /**
     * Returns the Short value for the given key. If the key does not exists it will return the default value given.
     * The method fails if the value is not a Short.
     */
    public short getShort(String key, short defaultValue) {
        String value = get(key);
        if (value == null) {
            return defaultValue;
        } else {
            return Short.valueOf(value);
        }
    }

    // -------------- BYTE

    /**
     * Returns the Byte value for the given key.
     * The method fails if the key does not exist.
     */
    public byte getByte(String key) {
        String value = getRequired(key);
        return Byte.valueOf(value);
    }

    /**
     * Returns the Byte value for the given key. If the key does not exists it will return the default value given.
     * The method fails if the value is not a Byte.
     */
    public byte getByte(String key, byte defaultValue) {
        String value = get(key);
        if (value == null) {
            return defaultValue;
        } else {
            return Byte.valueOf(value);
        }
    }

    /**
     * get properties from data map
     *
     * @return Properties
     */
    public Properties getProperties() {
        Properties props = new Properties();
        props.putAll(this.data);
        return props;
    }

    /**
     * Create a properties file with all the known parameters (call after the last get*() call).
     * Set the default value, if available.
     *
     * <p>Use this method to create a properties file skeleton.
     *
     * @param pathToFile Location of the default properties file.
     */
    public void createPropertiesFile(String pathToFile) throws IOException {
        createPropertiesFile(pathToFile, true);
    }

    /**
     * Create a properties file with all the known parameters (call after the last get*() call).
     * Set the default value, if overwrite is true.
     *
     * @param pathToFile Location of the default properties file.
     * @param overwrite  Boolean flag indicating whether or not to overwrite the file
     * @throws IOException If overwrite is not allowed and the file exists
     */
    public void createPropertiesFile(String pathToFile, boolean overwrite) throws IOException {
        final File file = new File(pathToFile);
        if (file.exists()) {
            if (overwrite) {
                file.delete();
            } else {
                throw new RuntimeException("File " + pathToFile + " exists and overwriting is not allowed");
            }
        }
        final Properties defaultProps = new Properties();

        try (final OutputStream out = new FileOutputStream(file)) {
            defaultProps.store(out, "Default file created by ParameterUtil.createPropertiesFile()");
        }
    }

    protected ParameterUtils clone() throws CloneNotSupportedException {
        return new ParameterUtils(this.data);
    }


    // ------------------------- ExecutionConfig.UserConfig interface -------------------------

    public Map<String, String> toMap() {
        return data;
    }

}
