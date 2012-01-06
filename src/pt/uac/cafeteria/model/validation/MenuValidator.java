
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
