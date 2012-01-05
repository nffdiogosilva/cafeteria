
package pt.uac.cafeteria.model.validation;

import pt.uac.cafeteria.model.domain.Meal.Type;
import pt.uac.cafeteria.model.domain.Menu;

/**
 * Validates Menu domain objects.
 */
public class MenuValidator extends Validator<Menu> {

    @Override
    protected void doAssertions(Menu menu) {
        check(
            menu.getId() != null,
            "É necessário definir um dia e se é almoço ou jantar."
        );
        check(
            isAllEmpty(
                menu.getMainCourse(Type.MEAT),
                menu.getMainCourse(Type.FISH),
                menu.getMainCourse(Type.VEGETARIAN)),
            "É necessário introduzir no mínimo um tipo de prato."
        );
        check(
            isAnyEmpty(menu.getSoup(), menu.getDessert()),
            "É necessário introduzir uma sopa e sobremesa."
        );
    }
}
