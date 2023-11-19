package ma.ensaj.gestionprof.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.ensaj.gestionprof.entities.Specialite;
import ma.ensaj.gestionprof.services.SpecialiteService;

@RestController
@RequestMapping("/api/v1/specialites")
public class SpecialiteController {
	@Autowired
	private SpecialiteService service;

	@GetMapping
	public List<Specialite> findAllSpecialites() {
		return service.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> findById(@PathVariable Long id) {
		Specialite specialite = service.findById(id);
		if (specialite == null) {
			return new ResponseEntity<Object>("Specialite avec Id " + id + " nexiste pas", HttpStatus.BAD_REQUEST);

		} else {
			return ResponseEntity.ok(specialite);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deletSpecialite(@PathVariable Long id) {
		Specialite specialite = service.findById(id);
		if (specialite == null) {
			return new ResponseEntity<Object>("Specialite avec Id " + id + " nexiste pas", HttpStatus.BAD_REQUEST);
		} else {
			service.delete(specialite);
			return ResponseEntity.ok("specialite avec id " + id + " suprimee");
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> updateSpecialite(@PathVariable Long id, @RequestBody Specialite newspecialite) {

		Specialite oldSpecialite = service.findById(id);
		if (oldSpecialite == null) {
			return new ResponseEntity<Object>("specialite avec id" + id + "nexiste pas ", HttpStatus.BAD_REQUEST);

		} else {
			newspecialite.setId(id);
			return ResponseEntity.ok(service.update(newspecialite));
		}
	}

	@PostMapping
	public Specialite creatSpecialite(@RequestBody Specialite specialite) {
		specialite.setId(0L);
		return service.create(specialite);

	}

}
