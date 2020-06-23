package com.rostenross.springboot.backend.apirest.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rostenross.springboot.backend.apirest.models.dao.IClienteDao;
import com.rostenross.springboot.backend.apirest.models.entity.Cliente;

@Service
public class ClienteService implements IClienteService{

	@Autowired
	private IClienteDao clienteDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll(){
		// TODO Auto-generated method stub
		return (List<Cliente>) clienteDao.findAll();
	}

}
