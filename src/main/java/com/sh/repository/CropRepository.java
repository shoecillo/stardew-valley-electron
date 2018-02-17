package com.sh.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sh.entity.CropEntity;

public interface CropRepository extends MongoRepository<CropEntity, String> {
	
	public List<CropEntity> findByName(String name);

}
