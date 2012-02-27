
/**
 * Cafeteria management application
 * Copyright (c) 2011, 2012 Helder Correia
 * 
 * This file is part of Cafeteria.
 * 
 * Cafeteria is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Cafeteria is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Cafeteria.  If not, see <http://www.gnu.org/licenses/>.
 */

package pt.uac.cafeteria.model.persistence;

import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import pt.uac.cafeteria.model.ApplicationException;
import pt.uac.cafeteria.model.persistence.abstracts.DataMapper;
import pt.uac.cafeteria.model.persistence.abstracts.FileAccess;

/**
 * Data Mapper for persistence of a list of dishes.
 * <p>
 * This mapper doesn't map a domain object because they don't need an id,
 * so an Embeded Value object isn't needed. It's a simple list of strings,
 * ordered alphabetically.
 */
public class DishMapper extends FileAccess implements DataMapper<String, String> {

    /** Loaded set of dishes. Ordered alphabetically, with no duplicates. */
    private SortedSet<String> loaded;

    /**
     * Creates a new DishMapper instance.
     *
     * @param filepath the relative or absolute path to the data file.
     */
    public DishMapper(String filepath) {
        super(filepath);
    }

    /** Gets a collection of all dishes. */
    public Collection<String> findAll() {
        return Collections.unmodifiableCollection(getLoaded());
    }

    /**
     * Gets a reference to the loaded set. Loads all accounts the first time called.
     *
     * @return The loaded set of dishes, as strings.
     */
    private SortedSet<String> getLoaded() {
        if (loaded == null) {
            loaded = new TreeSet<String>();
            loadAll();
        }
        return loaded;
    }

    /**
     * Loads all dishes from file.
     *
     * @throws ApplicationException if there's a problem with file system operations.
     */
    private void loadAll() {
        loaded.clear();
        try {
            String dish;
            BufferedReader in = new BufferedReader(new FileReader(useFile()));
            while ((dish = in.readLine()) != null) {
                loaded.add(dish);
            }
            in.close();
        } catch (IOException e) {
            throw new ApplicationException("Erro ao escrever ficheiro de dados", e);
        }
    }

    /**
     * Saves all loaded dishes to file.
     *
     * @throws ApplicationException if there's a problem with file system operations.
     */
    private void save() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(useFile()));
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
    public boolean update(String dish) {
        return insert(dish) != null;
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
