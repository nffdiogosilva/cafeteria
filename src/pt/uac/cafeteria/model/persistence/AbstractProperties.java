
package pt.uac.cafeteria.model.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import pt.uac.cafeteria.model.ApplicationException;

/**
 * AbstractProperties decorates java.util.Properties.
 *
 * It abstracts away a default file store, and makes loading and saving
 * at each operation. This is useful in situations where it's important
 * to prevent data inconsistency by some unexpected event that exits the
 * application without proper closing procedures.
 *
 * For example, if a configuration property is used in managing the next
 * id in a database row, and if the application is unexpectedly closed by
 * an uncaught exception, the next time the application runs it will be
 * found in an inconsistent state, where the last saved id isn't up to date.
 *
 * This decorator also implements a few gateway methods that make it easier
 * to set and retrieve ints and doubles, using the default implementation
 * from java.util.Properties (which uses only strings).
 *
 * setFilePath should be called after instantiation of an object of this type.
 * Not setting a file path may produce NullPointerException errors.
 */
public abstract class AbstractProperties {

    /** Relative path and filename to the properties file. */
    protected String filePath;

    /** java.util.Properties object to be decorated. */
    protected Properties props;

    /** Automatically save file on each setProperty call? */
    protected boolean autoSave = true;

    /**
     * Sets a different file path to use.
     *
     * @param filepath Relative path and filename to the properties file.
     */
    public void setFilePath(String filepath) {
        this.filePath = filepath;
    }

    /**
     * Gets the current file path used.
     *
     * @param filepath Relative path and filename to the properties file.
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Sets auto saving or not on each setProperty call.
     *
     * @param b true to turn on, false to turn off.
     */
    public void setAutoSave(boolean b) {
        this.autoSave = b;
    }

    /**
     * Lazy loading using FileInputStream.
     *
     * If the file doesn't exist, attempt to create necessary
     * parent folders and properties file.
     *
     * @throws ApplicationException if file operations not permitted somehow.
     */
    protected void load() {
        if (props == null) {
            props = new Properties();
            try {
                try {
                    FileInputStream in = new FileInputStream(filePath);
                    props.load(in);
                    in.close();

                } catch (FileNotFoundException e) {
                    restoreFile();
                }
            } catch (IOException e) {
                throw new ApplicationException("Problema ao receber configurações", e);
            }
        }
    }

    /**
     * Save modifications to file using FileOutputStream.
     *
     * @throws ApplicationException if unable to save to file.
     */
    protected void save() {
        try {
            FileOutputStream out = new FileOutputStream(filePath);
            props.store(out, "--- Application configuration ---");
            out.close();

        } catch (IOException e) {
            throw new ApplicationException("Problema ao guardar configurações", e);
        }
    }

    /**
     * Attempt to create file.
     *
     * Intended to be used only when it's found that the file doesn't exist.
     * This attempts to recover from that.
     *
     * If the file does exist, calling this method will reset the default
     * properties. Other properties added will be left alone.
     *
     * @throws IOException if unable to create the file.
     */
    protected void restoreFile() throws IOException {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        file.createNewFile();
        load();
        setAutoSave(false);
        setDefaults();
        setAutoSave(true);
        save();
    }

    /**
     * Implements default properties.
     *
     * When creating a new properties file, these settings will be called.
     * to define a first set of important properties.
     */
    abstract protected void setDefaults();

    /**
     * Calls the Properties method setProperty, while ensuring that the
     * properties are loaded from the file, and allowing auto save, if set for it.
     *
     * @param key the key to be placed into the properties list.
     * @param value the value corresponding to key.
     */
    public void setProperty(String key, String value) {
        load();
        props.setProperty(key, value);
        if (autoSave) {
            save();
        }
    }

    /**
     * Calls the Properties method getProperty, while ensuring that the
     * properties are loaded from the file.
     *
     * @param key the property key.
     * @return the value in the property list with the specified key value.
     */
    public String getProperty(String key) {
        load();
        return props.getProperty(key);
    }

    /**
     * Same as setProperty, but used as a companion to the other primitive
     * setting methods, where the method name is descriptive of the value
     * type being set.
     *
     * @param key the property key.
     * @param value the string value corresponding to the key.
     */
    public void setString(String key, String value) {
        setProperty(key, value);
    }

    /**
     * Similar to setProperty, but using an int value.
     *
     * @param key the property key.
     * @param value the int value corresponding to the key.
     */
    public void setInt(String key, int value) {
        setProperty(key, String.valueOf(value));
    }

    /**
     * Similar to setProperty, but using a double value.
     *
     * @param key the property key.
     * @param value the double value corresponding to the key.
     */
    public void setDouble(String key, double value) {
        setProperty(key, String.valueOf(value));
    }

    /**
     * Same as getProperty, but used as a companion to the other primitive
     * getting methods, where the method name is descriptive of the value
     * type being returned.
     *
     * @param key the property key.
     * @return the string value corresponding to the key.
     */
    public String getString(String key) {
        return getProperty(key);
    }

    /**
     * Similar to getProperty, but using an int value.
     *
     * @param key the property key.
     * @return the int value corresponding to the key.
     */
    public int getInt(String key) {
        return Integer.parseInt(getProperty(key));
    }

    /**
     * Similar to getProperty, but using a double value.
     *
     * @param key the property key.
     * @return the double value corresponding to the key.
     */
    public double getDouble(String key) {
        return Double.parseDouble(getProperty(key));
    }
}
