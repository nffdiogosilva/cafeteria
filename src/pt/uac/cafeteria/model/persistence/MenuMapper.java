
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

public class MenuMapper implements DataMapper<Menu, Menu.Id> {

    private static final String ROOT = "ementa";

    private Map<Menu.Id, Menu> loadedMap = new HashMap<Menu.Id, Menu>();

    private String path;

    public MenuMapper(String path) {
        this.path = path;
    }

    public boolean hasMenu(Calendar day) {
        return getFile(day.getTime()).exists();
    }

    private File getFile(Date day) {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String date = df.format(day);
        return new File(path + date + ".xml");
    }

    private File getFile(Menu.Id id) {
        return getFile(id.getDay().getTime());
    }

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

    private String getMachine(Object human) {
        return human.toString().toLowerCase();
    }

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
            Element dishes = root.getChild(getMachine(id.getTime()));

            Element mainDish = dishes.getChild("carne");
            String meat = mainDish.getChildText("prato");
            String fish = dishes.getChild("peixe").getChildText("prato");
            String veggie = dishes.getChild("vegetariano").getChildText("prato");
            String soup = mainDish.getChildText("sopa");
            String dessert = mainDish.getChildText("sobremesa");

            result = new Menu.Builder(id.getDay(), id.getTime())
                    .setMeatCourse(meat)
                    .setFishCourse(fish)
                    .setVeggieCourse(veggie)
                    .setSoupAndDessert(soup, dessert)
                    .build();

            loadedMap.put(id, result);

        } catch (Exception e) {
            // do nothing (no valid menu found)
        }
        return result;
    }

    private boolean save(Menu menu) {
        String mealTime = getMachine(menu.getId().getTime());

        Element meal = new Element(mealTime);
        meal.addContent(dishElement("carne", Meal.Type.MEAT, menu));
        meal.addContent(dishElement("peixe", Meal.Type.FISH, menu));
        meal.addContent(dishElement("vegetariano", Meal.Type.VEGETARIAN, menu));

        Document doc;
        Element root;

        File file = getFile(menu.getId());

        try {
            doc = getDoc(file);
            root = doc.getRootElement();
            root.removeChild(mealTime);
            root.addContent(meal);

        } catch (Exception e) {
            root = new Element(ROOT);
            root.addContent(meal);
            doc = new Document(root);
        }

        doSave(doc, file);
        loadedMap.put(menu.getId(), menu);

        return true;
    }

    private Element dishElement(String identifier, Meal.Type mainCourse, Menu menu) {
        Element dish = new Element(identifier);
        dish.addContent(new Element("sopa").setText(menu.getSoup()));
        dish.addContent(new Element("prato").setText(menu.getMainCourse(mainCourse)));
        dish.addContent(new Element("sobremesa").setText(menu.getDessert()));
        return dish;
    }

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
        String mealTime = getMachine(menu.getId().getTime());

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
