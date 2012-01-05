
package pt.uac.cafeteria.model.persistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import pt.uac.cafeteria.model.domain.Menu;
import pt.uac.cafeteria.model.domain.Meal;
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
public class MenuMapper implements DataMapper<Menu, Menu.Id> {

    /** Root element name in XML document. */
    private static final String ROOT = "ementa";

    /** File name format to be translated from a date (eg, 20120116.xml). */
    private final String FILENAME_FORMAT = "yyyyMMdd";

    /** Loaded map of Menu instances. */
    private Map<Menu.Id, Menu> loadedMap = new HashMap<Menu.Id, Menu>();

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
    public boolean hasMenu(Calendar day) {
        return getFile(day.getTime()).exists();
    }

    /**
     * Gets the file to the menu for a given day.
     *
     * @param day any day.
     * @return The File object pointing to the file corresponding to <code>day</code>.
     */
    private File getFile(Date day) {
        DateFormat df = new SimpleDateFormat(FILENAME_FORMAT);
        String date = df.format(day);
        return new File(path + date + ".xml");
    }

    /**
     * Gets the file for a given menu id.
     *
     * @param id the menu id.
     * @return The File object pointing to the corresponding menu.
     */
    private File getFile(Menu.Id id) {
        return getFile(id.getDay().getTime());
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

    /**
     * Translates a Meal.Time value to a machine readable name to
     * be used as an element in the XML document.
     *
     * @param time Meal.Time enum value.
     * @return String with corresponding key value.
     */
    private String getMealTimeKey(Meal.Time time) {
        return time.toString().toLowerCase();
    }

    /**
     * Finds a Menu object.
     * <p>
     * Menu objects have a compound key of day and Meal.Time values. This
     * is a convenience method when there's both values initialized, but not
     * the compounded id object.
     *
     * @param day the day.
     * @param mealTime the time of day (lunch or dinner).
     * @return The menu for the given day and time.
     */
    public Menu find(Calendar day, Meal.Time mealTime) {
        return find(new Menu.Id(day, mealTime));
    }

    @Override
    public Menu find(Menu.Id id) {
        Menu result = loadedMap.get(id);
        if (result != null) {
            return result;
        }
        try {
            Element root = getDoc(getFile(id)).getRootElement();
            Element dishes = root.getChild(getMealTimeKey(id.getTime()));

            Meal meat = readMeal(Meal.Type.MEAT, dishes, id);
            Meal fish = readMeal(Meal.Type.FISH, dishes, id);
            Meal veggie = readMeal(Meal.Type.VEGETARIAN, dishes, id);

            result = new Menu(meat, fish, veggie);

            loadedMap.put(id, result);

        } catch (Exception e) {
            // do nothing (assume no valid menu found)
        }
        return result;
    }

    /**
     * Reads a <code>Meal</code> object from a parsed DOM element.
     *
     * @param mealType the type of meal
     * @param parent the parent element where the meal is contained.
     * @param id the menu id.
     * @return a meal.
     */
    private Meal readMeal(Meal.Type mealType, Element parent, Menu.Id id) {
        String element = mealType.toString().toLowerCase();
        Element meal = parent.getChild(element);

        if (meal == null) {
            return null;
        }

        String mainCourse = meal.getChildText("prato");
        String soup = meal.getChildText("sopa");
        String dessert = meal.getChildText("sobremesa");

        return new Meal(id, mealType, mainCourse, soup, dessert);
    }

    /**
     * Saves a <code>Menu</code> object to its file.
     * <p>
     * If the file doesn't exist, a new one is created. If the a menu
     * already exists, it's replaced for the new one.
     *
     * @param menu the Menu object to save.
     * @return true if saved successfully.
     */
    private boolean save(Menu menu) {
        String mealTime = getMealTimeKey(menu.getId().getTime());

        Element meal = new Element(mealTime);
        addDish(dishElement("carne", Meal.Type.MEAT, menu), meal);
        addDish(dishElement("peixe", Meal.Type.FISH, menu), meal);
        addDish(dishElement("vegetariano", Meal.Type.VEGETARIAN, menu), meal);

        Document doc;
        Element root;

        File file = getFile(menu.getId());
        try {
            // existing file
            doc = getDoc(file);
            root = doc.getRootElement();
            root.removeChild(mealTime);
            root.addContent(meal);

        } catch (Exception e) {
            // any error (e.g., malformed document) warrants a new file
            root = new Element(ROOT);
            root.addContent(meal);
            doc = new Document(root);
        }

        doSave(doc, file);
        loadedMap.put(menu.getId(), menu);

        return true;
    }

    /**
     * Adds a dish element to a meal element if it isn't null.
     *
     * @param dish child element of <code>meal</dish>.
     * @param meal parent element of <code>dish</code>.
     */
    private void addDish(Element dish, Element meal) {
        if (dish != null) {
            meal.addContent(dish);
        }
    }

    /**
     * Translates a main dish choice from a menu to a JDOM Element object.
     *
     * @param identifier the name of the parent element for the dishes (type of meal).
     * @param mainCourse the type of meal (e.g., carne, peixe...).
     * @param menu the Menu object to extract the dishes from.
     * @return a JDOM Element object with the available dishes for a meal type.
     */
    private Element dishElement(String identifier, Meal.Type mainCourse, Menu menu) {
        if (menu.getMainCourse(mainCourse) == null) {
            return null;
        }
        Element dish = new Element(identifier);
        dish.addContent(new Element("sopa").setText(menu.getSoup()));
        dish.addContent(new Element("prato").setText(menu.getMainCourse(mainCourse)));
        dish.addContent(new Element("sobremesa").setText(menu.getDessert()));
        return dish;
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
    public Menu.Id insert(Menu menu) {
        return save(menu) ? menu.getId() : null;
    }

    @Override
    public boolean update(Menu menu) {
        return save(menu);
    }

    @Override
    public boolean delete(Menu menu) {
        File file = getFile(menu.getId());
        String mealTime = getMealTimeKey(menu.getId().getTime());

        try {
            Document doc = getDoc(file);
            Element root = doc.getRootElement();
            root.removeChild(mealTime);

            List children = root.getChildren();
            if (children.isEmpty()) {
                file.delete();
            }
            else {
                doSave(doc, file);
            }

            loadedMap.remove(menu.getId());
            return true;

        } catch (IOException e) {
            return false;
        }
    }
}
