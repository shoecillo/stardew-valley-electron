package com.sh.ctrl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sh.entity.CropEntity;
import com.sh.service.CorpMongoService;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/stardew")
public class CropsCtrl {

	@Autowired
	CorpMongoService serv;
	
	
	@RequestMapping("/insertions")
	public String insertAll()
	{
		try {
			serv.insertAll();
			return "INSERTIONS COMPLETE SUCESSFULLY";
		} catch (IOException e) {
			
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	@RequestMapping("/getCrops")
	public List<CropEntity> getAllCrops()
	{
		return serv.getAll();
	}
	
	@RequestMapping("/getCrops/{name}")
	public CropEntity getByName(@PathVariable String name)
	{
		return serv.getByName(name);
	}
}
