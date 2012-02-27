
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import pt.uac.cafeteria.model.ApplicationException;
import pt.uac.cafeteria.model.domain.Day;
import pt.uac.cafeteria.model.domain.Meal;
import pt.uac.cafeteria.model.domain.Menu;
import pt.uac.cafeteria.model.persistence.abstracts.DataMapper;

/**
 * Data Mapper for Menu domain objects.
 * <p>
 * Each menu for a day is saved in an independent XML file with the following
 * name: [yyyyMMdd].xml (e.g., 20120116.xml).
 * <p>
 * Document format should be as follows:
 * <pre>
 *   &lt;ementa&gt;
 *     &lt;almoco&gt;
 *       &lt;carne&gt;
 *         &lt;sopa>O nome da sopa&lt;/sopa&gt;
 *         &lt;prato>O nome do prato&lt;/prato&gt;
 *         &lt;sobremesa>O nome da sobremesa&lt;/sobremesa&gt;
 *       &lt;/carne&gt;
 *       &lt;peixe&gt;
 *         &lt;sopa>O nome da sopa&lt;/sopa&gt;
 *         &lt;prato>O nome do prato&lt;/prato&gt;
 *         &lt;sobremesa>O nome da sobremesa&lt;/sobremesa&gt;
 *       &lt;/peixe&gt;
 *       ...
 *     &lt;/almoco&gt;
 *     &lt;jantar&gt;
 *       &lt;carne&gt;
 *         &lt;sopa>O nome da sopa&lt;/sopa&gt;
 *         &lt;prato>O nome do prato&lt;/prato&gt;
 *         &lt;sobremesa>O nome da sobremesa&lt;/sobremesa&gt;
 *       &lt;/carne&gt;
 *       ...
 *     &lt;/jantar&gt;
 *   &lt;ementa&gt;
 * </pre>
 */
public class MenuMapper implements DataMapper<Menu, Day> {

    /** Root element name in XML document. */
    private static final String ROOT = "ementa";

    /** File name format to be translated from a date (e.g., 2012-01-16.xml). */
    private final String FILENAME_FORMAT = "yyyy-MM-dd";

    /** Loaded map of Menu instances. */
    private Map<Day, Menu> loadedMap = new HashMap<Day, Menu>();

    /** Path to the directory where files for each menu should be saved. */
    private String path;

    /**
     * Creates a new MenuMapper instance.
     *
     * @param path the path to the directory where files for each menu should be saved.
     */
    public MenuMapper(String path) {
        this.path = path;
    }

    /**
     * Checks the availability of a meal menu for a given day.
     * <p>
     * Technically, if the file for a given day doesn't exist, then there's no
     * available menu.
     *
     * @param day the day to check menu availability.
     * @return
     */
    public boolean hasMenu(Day day) {
        return getFile(day).exists();
    }

    /**
     * Gets the file to the menu for a given day.
     *
     * @param day any day.
     * @return The File object pointing to the file corresponding to <code>day</code>.
     */
    private File getFile(Day day) {
        return new File(path + day.format(FILENAME_FORMAT) + ".xml");
    }

    /**
     * Gets a JDOM Document object parsed from an XML file.
     *
     * @param file the XML file.
     * @return A JDOM Document parsed.
     * @throws IOException if the XML document is malformed.
     */
    private Document getDoc(File file) throws IOException {
        try {
            SAXBuilder builder = new SAXBuilder(false);
            Document doc = builder.build(file);
            return doc;

        } catch (JDOMException e) {
            throw new ApplicationException(
                "Má formatação do documento XML no ficheiro " + file.getPath(), e
            );
        }
    }

    @Override
    public Menu find(Day id) {
        Menu result = loadedMap.get(id);
        if (result != null) {
            return result;
        }
        try {
            Element root = getDoc(getFile(id)).getRootElement();

            Menu menu = new Menu(id);
            for (Object child : root.getChildren()) {
                menu.addMeals(readMenu((Element) child, id));
            }

            result = menu;
            loadedMap.put(id, result);

        } catch (Exception e) {
            // do nothing (assume no valid menu found)
        }
        return result;
    }

    /**
     * Reads an array of meal objects for lunch or dinner.
     *
     * @param menu the JDOM Element corresponding to lunch or dinner.
     * @param day the day of the menu.
     * @return meals that belong to a sub-menu for a time of day.
     */
    private Meal[] readMenu(Element menu, Day day) {
        Meal.Time mealTime = getEnum(Meal.Time.class, menu.getName());

        List<Meal> meals = new ArrayList<Meal>();
        for (Object child : menu.getChildren()) {
            meals.add(readMeal((Element) child, day, mealTime));
        }
        return meals.toArray(new Meal[]{});
    }

    /**
     * Reads a meal object from a corresponding JDOM Element.
     *
     * @param meal the JDOM Element corresponding to the meal type.
     * @param day the day of the menu.
     * @param mealTime the time of day for the sub-menu.
     * @return the corresponding Meal object parsed.
     */
    private Meal readMeal(Element meal, Day day, Meal.Time mealTime) {
        Meal.Type mealType = getEnum(Meal.Type.class, meal.getName());

        String soup = meal.getChildText("sopa");
        String mainCourse = meal.getChildText("prato");
        String dessert = meal.getChildText("sobremesa");

        return new Meal(day, mealTime, mealType, soup, mainCourse, dessert);
    }


    /**
     * Translates an Enum value to a machine readable name to
     * be used as an element in the XML document.
     *
     * @param e the enum to translate.
     * @return Machine name of <code>e</code>.
     */
    private String getEnumValue(Enum e) {
        return e.toString().toLowerCase();
    }

    /**
     * Gets an enum based on its machine readable name.
     *
     * @param <E> the type of the enum object.
     * @param type the class object for the enum.
     * @param value the machine readable name
     * @return the enum object corresponding to <code>value</code>.
     */
    public <E extends Enum<E>> E getEnum(Class<E> type, String value) {
       for (final E element : EnumSet.allOf(type)) {
           if (getEnumValue(element).equals(value)) {
               return element;
           }
       }
       return null;
    }

    /**
     * Saves a <code>Menu</code> object to its file.
     * <p>
     * Any existing file will be replaced. Any error found will assume
     * malformed document, warranting an overwrite.
     *
     * @param menu the Menu object to save.
     * @return true if saved successfully, or false on any error found.
     */
    private boolean save(Menu menu) {
        try {
            Element root = new Element(ROOT);

            // iterate lunch, dinner...
            for (Meal.Time mealTime : menu.getMeals().keySet()) {
                Element submenu = new Element(getEnumValue(mealTime));

                // iterate meat, fish, vegetarian...
                for (Meal.Type mealType : menu.getMeals(mealTime).keySet()) {
                    Meal meal = menu.getMeal(mealTime, mealType);
                    submenu.addContent(mealElement(meal));
                }

                root.addContent(submenu);
            }

            Document doc = new Document(root);
            File file = getFile(menu.getId());

            doSave(doc, file);
            loadedMap.put(menu.getId(), menu);

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Translates a <code>Meal</code> to a JDOM Element object.
     *
     * @param meal the meal to save.
     * @return a JDOM Element object with the meal dishes.
     */
    private Element mealElement(Meal meal) {
        String mealType = getEnumValue(meal.getType());

        Element mealElement = new Element(mealType);
        mealElement.addContent(new Element("sopa").setText(meal.getSoup()));
        mealElement.addContent(new Element("prato").setText(meal.getMainCourse()));
        mealElement.addContent(new Element("sobremesa").setText(meal.getDessert()));

        return mealElement;
    }

    /**
     * Does the actual saving from a fully formed JDOM Document object to
     * a system file.
     *
     * @param doc the fully formed JDOM Document.
     * @param file the file to save the document to.
     */
    private void doSave(Document doc, File file) {
        XMLOutputter out = new XMLOutputter();
        out.setFormat(Format.getPrettyFormat());

        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileOutputStream fileStream = new FileOutputStream(file);
            out.output(doc, fileStream);

        } catch (IOException e) {
            throw new ApplicationException("Erro ao guardar menu em ficheiro", e);
        }
    }

    @Override
    public Day insert(Menu menu) {
        return save(menu) ? menu.getId() : null;
    }

    @Override
    public boolean update(Menu menu) {
        return save(menu);
    }

    @Override
    public boolean delete(Menu menu) {
        getFile(menu.getId()).delete();
        loadedMap.remove(menu.getId());
        return true;
    }
}
