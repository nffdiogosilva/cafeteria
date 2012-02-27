
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

package pt.uac.cafeteria.model.validation;

import pt.uac.cafeteria.model.domain.Meal;
import pt.uac.cafeteria.model.domain.Menu;

/**
 * Validates Menu domain objects.
 */
public class MenuValidator extends Validator<Menu> {

    @Override
    protected void doAssertions(Menu menu) {
        check(
            menu.getId() != null,
            "É necessário definir um dia para a ementa."
        );
        check(
            !menu.isEmpty(),
            "É necessário introduzir no mínimo uma refeição."
        );
        for (Meal.Time mealTime : menu.getMeals().keySet()) {
            check(
                !menu.isEmpty(),
                "Ementa para '" + mealTime.toString() + "' vazia."
            );
        }
    }
}
