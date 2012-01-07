
package pt.uac.cafeteria.model.domain;

/**
 * A University course, that students can be enrolled in.
 */
public class Course implements DomainObject<Integer> {

    /** The course id. */
    private Integer id;

    /** The course name. */
    private String name;

    /**
     * Creates a new Course object.
     * <p>
     * If the id is not known, set it to null.
     *
     * @param id the course unique id number.
     * @param name the course name.
     */
    public Course(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    /** Gets the course name. */
    public String getName() {
        return name;
    }

    /** Updates the name to a new value. */
    public void setName(String newName) {
        name = newName;
    }

    @Override
    public String toString() {
        return name;
    }
}
