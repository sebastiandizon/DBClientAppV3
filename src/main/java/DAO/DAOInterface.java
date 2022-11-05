package DAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
/**Interface for data access objects. Uses generic type T which is specified in implementation*/
public interface DAOInterface<T> {

    List<T> getAll() throws SQLException;

    void save(T t) throws SQLException;

    void update(T t) throws SQLException;

    void delete(T t) throws SQLException;
}
