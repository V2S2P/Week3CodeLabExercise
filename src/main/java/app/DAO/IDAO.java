package app.DAO;

import java.util.List;
import java.util.Set;

public interface IDAO <T,I>{
    T create(T t);
    boolean update(T t);
    boolean delete(T t);
    T find(I id);
    List<T> getAll();
}
