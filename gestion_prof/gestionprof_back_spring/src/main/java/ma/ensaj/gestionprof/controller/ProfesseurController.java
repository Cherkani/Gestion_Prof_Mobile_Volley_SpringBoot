package ma.ensaj.gestionprof.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ma.ensaj.gestionprof.entities.Professseur;
import ma.ensaj.gestionprof.entities.Specialite;
import ma.ensaj.gestionprof.services.ProfesseurService;

@RestController
@RequestMapping("/api/v1/professeurs")
public class ProfesseurController {
    @Autowired
    private ProfesseurService service;

    @GetMapping
    public List<Professseur> Q() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        Professseur professeur = service.findById(id);
        if (professeur == null) {
            return new ResponseEntity<Object>("Professseur avec Id " + id + " nexiste pas", HttpStatus.BAD_REQUEST);

        } else {
            return ResponseEntity.ok(professeur);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletProfessseur(@PathVariable Long id) {
        Professseur professeur = service.findById(id);
        if (professeur == null) {
            return new ResponseEntity<Object>("Professseur avec Id " + id + " nexiste pas", HttpStatus.BAD_REQUEST);
        } else {
            service.delete(professeur);
            return ResponseEntity.ok("professeur avec id " + id + " suprime");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProfessseur(@PathVariable Long id, @RequestBody Professseur newprofesseur) {

        Professseur oldProfessseur = service.findById(id);
        if (oldProfessseur == null) {
            return new ResponseEntity<Object>("professeur avec id" + id + "nexiste pas ", HttpStatus.BAD_REQUEST);

        } else {
            newprofesseur.setId(id);
            return ResponseEntity.ok(service.update(newprofesseur));
        }
    }

    @PostMapping
    public Professseur creatProfessseur(@RequestBody Professseur professeur) {
        professeur.setId(0L);
        return service.create(professeur);
    }
    
    @GetMapping("/specialite")
    public List<Professseur> findProfesseurBySpecialite(@RequestBody Specialite specialite) {
        return service.findBySpecialite(specialite);
    }
    
    @GetMapping("/filterByDate")
    public List<Professseur> findByDateEmbaucheBetween(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateDebut,@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFin) {
        return service.findByDateEmbaucheBetween(dateDebut, dateFin);
    }
}
