
package pt.uac.cafeteria.model.validation;

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
            isNoneEmpty(menu.getMeat(), menu.getFish(), menu.getVegetarian()),
            "É necessário introduzir no mínimo um tipo de prato."
        );
        check(
            isAnyEmpty(menu.getSoup(), menu.getDessert()),
            "É necessário introduzir uma sopa e sobremesa."
        );
    }
}
