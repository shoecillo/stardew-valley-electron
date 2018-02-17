package com.sh.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.sh.entity.CropEntity;
import com.sh.repository.CropRepository;

@Service
public class CorpMongoService 
{
	
	@Autowired
	private MongoTemplate mongo;
	
	@Autowired
	private CropRepository rep;
	
	public void insertAll() throws IOException
	{
		ConnectionWiki wiki = new ConnectionWiki();
		List<CropEntity> lsIn =  wiki.getInfofromWiki();
		for(CropEntity crop : lsIn)
		{
			mongo.insert(crop);
		}
		
	}
	
	public List<CropEntity> getAll()
	{
		return mongo.findAll(CropEntity.class);
	}
	
	public CropEntity getByName(String name)
	{
		CropEntity res = null;
		List<CropEntity> ls = rep.findByName(name);
		if(!ls.isEmpty())
		{
			res = ls.get(0);
		}
		
		return res;
	}
	
}
