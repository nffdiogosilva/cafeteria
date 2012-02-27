
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

import pt.uac.cafeteria.model.domain.Account;

/**
 * Data Mapper for old Student objects.
 * <p>
 * When a student finishes studies, his information is moved to an
 * historic of old students. This mapper manages this list.
 */
public class OldStudentMapper extends StudentMapper {

    /**
     * Creates a new OldStudentMapper instance.
     *
     * @param con a database connection object.
     */
    public OldStudentMapper(java.sql.Connection con) {
        super(con);
    }

    @Override
    protected String table() {
        return "histAlunos";
    }

    @Override
    protected Account createNewAccount(Integer id) {
        Account account = super.createNewAccount(id);
        account.close();
        return account;
    }
}
