
package pt.uac.cafeteria.model.persistence.abstracts;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import pt.uac.cafeteria.model.ApplicationException;

/**
 * AbstractProperties decorates java.util.Properties.
 * <p>
 * It abstracts away a default file store, and makes loading and saving
 * at each operation. This is useful in situations where it's important
 * to prevent data inconsistency by some unexpected event that exits the
 * application without proper closing procedures.
 * <p>
 * For example, if a configuration property is used in managing the next
 * id in a database row, and if the application is unexpectedly closed by
 * an uncaught exception, the next time the application runs it will be
 * found in an inconsistent state, where the last saved id isn't up to date.
 * <p>
 * This decorator also implements a few gateway methods that make it easier
 * to set and retrieve ints and doubles, using the default implementation
 * from java.util.Properties (which uses only strings).
 */
public abstract class AbstractProperties extends FileAccess {

    /** java.util.Properties object to be decorated. */
    protected Properties props;

    /** Automatically save file on each set call? */
    protected boolean autoSave = true;

    /**
     * Constructor.
     *
     * @param filePath relative or absolute file path.
     */
    public AbstractProperties(String filePath) {
        super(filePath);
    }

    /**
     * Sets auto saving or not on each set call.
     *
     * @param b true to turn on, false to turn off.
     */
    public void setAutoSave(boolean b) {
        this.autoSave = b;
    }

    /**
     * Lazy loading using FileInputStream.
     * <p>
     * If the file doesn't exist, attempt to create necessary
     * parent folders and properties file.
     *
     * @throws ApplicationException if file operations not permitted somehow.
     */
    public void load() {
        if (props == null) {
            props = new Properties();
            try {
                try {
                    FileInputStream in = new FileInputStream(getFile());
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
     * Save modifications to file.
     *
     * @throws ApplicationException if unable to save to file.
     */
    public void save() {
        try {
            FileOutputStream out = new FileOutputStream(getFile());
            props.store(out, "--- Application configuration ---");
            out.close();

        } catch (IOException e) {
            throw new ApplicationException("Problema ao guardar configurações", e);
        }
    }

    /**
     * Attempt to create file.
     * <p>
     * Intended to be used only when it's found that the file doesn't exist.
     * This attempts to recover from that.
     * <p>
     * If the file does exist, calling this method will reset the default
     * properties. Other properties added will be left alone.
     *
     * @throws IOException if unable to create the file.
     */
    protected void restoreFile() throws IOException {
        createNewFile();
        load();
        boolean b = autoSave;
        setAutoSave(false);
        setDefaults();
        setAutoSave(b);
        save();
    }

    /**
     * Implements default properties.
     * <p>
     * When creating a new properties file, these settings will be called.
     * to define a first set of important properties.
     */
    abstract protected void setDefaults();

    /**
     * Calls the Properties method set, while ensuring that the
     * properties are loaded from the file, and allowing auto save, if set for it.
     *
     * @param key the key to be placed into the properties list.
     * @param value the value corresponding to key.
     */
    public void set(String key, String value) {
        load();
        props.setProperty(key, value);
        if (autoSave) {
            save();
        }
    }

    /**
     * Calls the Properties method get, while ensuring that the
     * properties are loaded from the file.
     *
     * @param key the property key.
     * @return the value in the property list with the specified key value.
     */
    public String get(String key) {
        load();
        return props.getProperty(key);
    }

    /**
     * Same as set, but used as a companion to the other primitive
     * setting methods, where the method name is descriptive of the value
     * type being set.
     *
     * @param key the property key.
     * @param value the string value corresponding to the key.
     */
    public void setString(String key, String value) {
        set(key, value);
    }

    /**
     * Similar to set, but using an int value.
     *
     * @param key the property key.
     * @param value the int value corresponding to the key.
     */
    public void setInt(String key, int value) {
        set(key, String.valueOf(value));
    }

    /**
     * Similar to set, but using a double value.
     *
     * @param key the property key.
     * @param value the double value corresponding to the key.
     */
    public void setDouble(String key, double value) {
        set(key, String.valueOf(value));
    }

    /**
     * Same as get, but used as a companion to the other primitive
     * getting methods, where the method name is descriptive of the value
     * type being returned.
     *
     * @param key the property key.
     * @return the string value corresponding to the key.
     */
    public String getString(String key) {
        return get(key);
    }

    /**
     * Similar to get, but using an int value.
     *
     * @param key the property key.
     * @return the int value corresponding to the key.
     */
    public int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    /**
     * Similar to get, but using a double value.
     *
     * @param key the property key.
     * @return the double value corresponding to the key.
     */
    public double getDouble(String key) {
        return Double.parseDouble(get(key));
    }
}
