
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

package pt.uac.cafeteria.model.persistence.abstracts;

import java.io.File;
import java.io.IOException;

/**
 * Simple abstraction for file access.
 * <p>
 * Meant to be used as a base class. It holds a reference to a File
 * object for subclasses that need one, and any common and useful methods
 * related to the file referenced.
 */
public abstract class FileAccess {

    /** File object that is a representation of a system file. */
    protected File file;

    /**
     * Base constructor.
     * <p>
     * Forces subclasses to declare a constructor that initializes the file
     * reference.
     *
     * @param filePath relative or absolute path to the file.
     */
    public FileAccess(String filePath) {
        this.file = new File(filePath);
    }

    /** Gets a reference to the File object. */
    public File getFile() {
        return file;
    }

    /**
     * Similar to getFile, but makes sure the file actually exists in the system.
     *
     * @return file object reference.
     * @throws IOException if can't create file.
     */
    protected File useFile() throws IOException {
        if (!file.exists()) {
            createNewFile();
        }
        return file;
    }

    /**
     * Attempts to create file and necessary parent directory.
     * Nothing happens if file already exists.
     *
     * @throws IOException if can't create file.
     */
    protected void createNewFile() throws IOException {
        file.getParentFile().mkdirs();
        file.createNewFile();
    }
}
