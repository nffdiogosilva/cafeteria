
package pt.uac.cafeteria.model.persistence;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import pt.uac.cafeteria.model.ApplicationException;
import pt.uac.cafeteria.model.domain.Account;
import pt.uac.cafeteria.model.persistence.abstracts.DataMapper;
import pt.uac.cafeteria.model.persistence.abstracts.FileAccess;

/**
 * Data Mapper for Account domain objects.
 */
public class AccountMapper extends FileAccess implements DataMapper<Account, Integer> {

    /** Map with already loaded instances of the domain object. */
    private Map<Integer, Account> loadedMap;

    /** Automatically save to file on each insert, update or delete? */
    private boolean autoSave = false;

    /**
     * Creates a new instance of the mapper.
     *
     * @param filePath the path to the file used to persist Account objects.
     */
    public AccountMapper(String filePath) {
        super(filePath);
    }

    /**
     * Sets auto saving or not on each insert, update or delete.
     *
     * @param b true to turn on, false to turn off.
     */
    public void setAutosave(boolean b) {
        autoSave = b;
    }

    /**
     * Gets a reference to the loaded map. Loads all accounts the first time called.
     *
     * @return The loaded map of Account instances.
     */
    private Map<Integer, Account> getLoaded() {
        if (loadedMap == null) {
            loadedMap = new HashMap<Integer, Account>();
            loadAll();
        }
        return loadedMap;
    }

    /**
     * Loads all accounts from file.
     *
     * @throws ApplicationException if data file is corrupted, or problem with
     * file system operations.
     */
    public void loadAll() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(useFile()));
            loadedMap = (Map<Integer, Account>) in.readObject();
            in.close();

        } catch (ClassNotFoundException e) {
            throw new ApplicationException("Objecto desconhecido ao carregar contas.", e);
        } catch (EOFException e) {
            // do nothing (means the file is empty, so there's nothing to load)
        } catch (IOException e) {
            throw new ApplicationException("Problema ao recuperar dados das contas.", e);
        }
    }

    /**
     * Saves all accounts in loaded map to file.
     *
     * @throws ApplicationException if there's a problem with file system operations.
     */
    public void save() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(useFile()));
            out.writeObject(getLoaded());
            out.close();

        } catch (IOException e) {
            throw new ApplicationException("Problema ao guardar dados das contas.", e);
        }
    }

    @Override
    public Integer insert(Account account) {
        getLoaded().put(account.getId(), account);
        if (autoSave) {
            save();
        }
        return account.getId();
    }

    /** Finds all loaded accounts. */
    public Collection<Account> findAll() {
        return loadedMap.values();
    }

    /**
     * Finds an Account with an <code>int</code> from the java language.
     *
     * @param id account id number.
     * @return The Account domain object.
     */
    public Account find(int id) {
        return find(new Integer(id));
    }

    @Override
    public Account find(Integer id) {
        return getLoaded().get(id);
    }

    @Override
    public boolean update(Account account) {
        return insert(account) != null;
    }

    @Override
    public boolean delete(Account account) {
        Account removed = getLoaded().remove(account.getId());
        if (autoSave) {
            save();
        }
        return removed != null;
    }
}
