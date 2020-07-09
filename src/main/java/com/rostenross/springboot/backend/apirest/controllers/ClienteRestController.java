package com.rostenross.springboot.backend.apirest.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.rostenross.springboot.backend.apirest.models.entity.Cliente;
import com.rostenross.springboot.backend.apirest.models.service.IClienteService;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ClienteRestController {
	
	@Autowired
	private IClienteService clienteService;
	
	@GetMapping("/clientes")
	public List<Cliente> index(){
		
		return clienteService.findAll();
	}
	
	
	@GetMapping("/clientes/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Cliente cliente;
		Map<String, Object>response= new HashMap<>();
		try {
			cliente=clienteService.findById(id);

		} catch (DataAccessException e) {
			response.put("mensaje","¡¡Error al realizar la consulta en la base de datos!!");
			response.put("error",e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		if (cliente == null) {
			response.put("mensaje","¡¡El cliente ID:".concat(id.toString().concat(" no existe en la base de datos!!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK); 
	}
	
	@PostMapping("/clientes")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@RequestBody Cliente cliente) {
		Cliente clienteNew;
		Map<String, Object> response=new HashMap<>();
		try {
			clienteNew= clienteService.save(cliente);
		} catch (DataAccessException e) {
			response.put("mensaje", "¡¡Error al insertar en la base de datos!!");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "¡¡El cliente ah sido creado con éxito!!");
		response.put("cliente", clienteNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> update(@RequestBody Cliente cliente, @PathVariable Long id) {
		Cliente clienteActual= clienteService.findById(id);
		Cliente clienteUpdate = null; 
		Map<String, Object> response= new HashMap<>();
		
		if (clienteActual == null) {
			response.put("mensaje", "¡¡Error no se pudo editar, el cliente ID:".concat(id.toString().concat(" no existe en la base de datos!!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			clienteActual.setLastname(cliente.getLastname());
			clienteActual.setEmail(cliente.getEmail());
			clienteActual.setName(cliente.getName());
			clienteActual.setCreateAt(cliente.getCreateAt());
			
			clienteUpdate= clienteService.save(clienteActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "¡¡Error al actualizar el cliente en la base de datos!!");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);		
		}
		response.put("mensaje", "¡¡El cliente"+clienteUpdate.getName()+" "+clienteUpdate.getLastname()+" ha sido actualizado con éxito!!");
		response.put("cliente", clienteUpdate);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/cliente/{id}")
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response= new HashMap<>();
		
		try {
			clienteService.delete(id);

		} catch (DataAccessException e) {
			response.put("mensaje", "¡¡Error al eliminar el cliente en la base de datos!!");
			response.put("error", e.getMessage().concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "¡¡El cliente fue eliminado con exito!!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);		
	}
}
