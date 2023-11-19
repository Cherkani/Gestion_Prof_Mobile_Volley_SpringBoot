package ma.ensaj.gestionprof.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.ensaj.gestionprof.dao.IDao;
import ma.ensaj.gestionprof.entities.Professseur;
import ma.ensaj.gestionprof.entities.Specialite;
import ma.ensaj.gestionprof.repository.ProfesseurRepository;

@Service
public class ProfesseurService implements IDao<Professseur>{

    @Autowired
    ProfesseurRepository professeurRepository;
    @Override
    public Professseur create(Professseur o) {
       return professeurRepository.save(o);
    }

    @Override
    public List<Professseur> findAll() {
        return professeurRepository.findAll();
    }

    @Override
    public Professseur update(Professseur o) {
        return professeurRepository.save(o);
    }

    @Override
    public boolean delete(Professseur o) {
        try {
            professeurRepository.delete(o);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Professseur findById(Long id) {
        return professeurRepository.findById(id).orElse(null);
    }
    
    public List<Professseur> findBySpecialite(Specialite specialite) {
        return professeurRepository.findBySpecialite(specialite);
    }
    
    public List<Professseur> findByDateEmbaucheBetween(Date dateDebut, Date dateFin) {
        return professeurRepository.findByDateEmbaucheBetween(dateDebut, dateFin);
    }
    
}
