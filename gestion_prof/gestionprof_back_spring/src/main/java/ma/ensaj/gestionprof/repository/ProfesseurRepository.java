package ma.ensaj.gestionprof.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.ensaj.gestionprof.entities.Professseur;
import ma.ensaj.gestionprof.entities.Specialite;
import java.util.Date;


@Repository
public interface ProfesseurRepository extends JpaRepository<Professseur, Long> {
	
	List<Professseur> findBySpecialite(Specialite specialite);
	
	List<Professseur> findByDateEmbaucheBetween(Date dateDebut, Date dateFin);

}
