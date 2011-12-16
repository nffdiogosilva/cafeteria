
package pt.uac.cafeteria.model.persistence;


interface StatementSource {
    String sql();
    Object[] parameters();
}
