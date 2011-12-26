
package pt.uac.cafeteria.model.persistence;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import pt.uac.cafeteria.model.ApplicationException;
import pt.uac.cafeteria.model.domain.Account;


public class AccountMapper extends FileAccess implements DataMapper<Account, Integer> {

    private static final String DEFAULT_PATH = "data/contas.dat";

    public static AccountMapper instance;

    private Map<Integer, Account> loadedMap;

    private boolean autoSave = false;

    public static AccountMapper getInstance() {
        if (instance == null) {
            instance = new AccountMapper(DEFAULT_PATH);
        }
        return instance;
    }

    public AccountMapper(String filePath) {
        super(filePath);
    }

    public void setAutosave(boolean b) {
        autoSave = b;
    }

    private Map<Integer, Account> getLoaded() {
        if (loadedMap == null) {
            loadedMap = new HashMap<Integer, Account>();
            loadAll();
        }
        return loadedMap;
    }

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

    public Account find(int id) {
        return find(new Integer(id));
    }

    @Override
    public Account find(Integer id) {
        return getLoaded().get(id);
    }

    @Override
    public int update(Account account) {
        return insert(account) != null ? 1 : 0;
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
