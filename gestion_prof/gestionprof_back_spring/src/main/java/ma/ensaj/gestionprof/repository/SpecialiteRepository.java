package ma.ensaj.gestionprof.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.ensaj.gestionprof.entities.Specialite;
@Repository
public interface SpecialiteRepository extends JpaRepository<Specialite, Long> {

}
