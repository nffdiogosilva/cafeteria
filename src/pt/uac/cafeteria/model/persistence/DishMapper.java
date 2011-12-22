
package pt.uac.cafeteria.model.persistence;

import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import pt.uac.cafeteria.model.ApplicationException;


public class DishMapper implements DataMapper<String, String> {

    private final String PATH = "data/dishes/";

    private File file;

    private SortedSet<String> loaded;

    private DishMapper(String key) {
        this.file = new File(PATH + key + ".txt");
    }

    public static DishMapper getMeatInstance() {
        return new DishMapper("carne");
    }

    public static DishMapper getFishInstance() {
        return new DishMapper("peixe");
    }

    public static DishMapper getVegetarianInstance() {
        return new DishMapper("veget");
    }

    public static DishMapper getSoupInstance() {
        return new DishMapper("sopa");
    }

    public static DishMapper getDessertInstance() {
        return new DishMapper("sobrem");
    }

    public Collection<String> findAll() {
        return Collections.unmodifiableCollection(getLoaded());
    }

    private SortedSet<String> getLoaded() {
        if (loaded == null) {
            loaded = new TreeSet<String>();
            loadAll();
        }
        return loaded;
    }

    private void loadAll() {
        loaded.clear();
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            String dish;
            BufferedReader in = new BufferedReader(new FileReader(file));
            while ((dish = in.readLine()) != null) {
                loaded.add(dish);
            }
            in.close();
        } catch (IOException e) {
            throw new ApplicationException("Erro ao escrever ficheiro de dados", e);
        }
    }

    private void save() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (String dish : getLoaded()) {
                out.write(dish);
                out.newLine();
            }
            out.close();
        } catch (IOException e) {
            throw new ApplicationException("Erro ao escrever em ficheiro de dados", e);
        }
    }

    @Override
    public String insert(String dish) {
        if (getLoaded().add(dish.trim())) {
            save();
            return dish.trim();
        }
        return null;
    }

    @Override
    public String find(String dish) {
        return getLoaded().contains(dish) ? dish : null;
    }

    @Override
    public int update(String dish) {
        return insert(dish) != null ? 1 : 0;
    }

    @Override
    public boolean delete(String dish) {
        if (getLoaded().remove(dish)) {
            save();
            return true;
        }
        return false;
    }
}
