package ma.ensaj.gestionprof.dao;

import java.util.List;

public interface IDao <T>{
    T create(T o);
    List<T> findAll();
    T update(T o);
    boolean delete(T o);
    T findById(Long id);
}
