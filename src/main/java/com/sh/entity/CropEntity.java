package com.sh.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="crops")
public class CropEntity 
{
	
	@Id
	private String id;
	
	private String name;
	
	private String buyPierre;
	
	private String buyJoja;
	
	private String saleRegular;
	
	private String saleSilver;
	
	private String saleGold;
	
	private String growing;
	
	private String produce;
	
	private String multip;
	
	private String img;
	
	private String season;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBuyPierre() {
		return buyPierre;
	}

	public void setBuyPierre(String buyPierre) {
		this.buyPierre = buyPierre;
	}

	public String getBuyJoja() {
		return buyJoja;
	}

	public void setBuyJoja(String buyJoja) {
		this.buyJoja = buyJoja;
	}

	public String getSaleRegular() {
		return saleRegular;
	}

	public void setSaleRegular(String saleRegular) {
		this.saleRegular = saleRegular;
	}

	public String getSaleSilver() {
		return saleSilver;
	}

	public void setSaleSilver(String saleSilver) {
		this.saleSilver = saleSilver;
	}

	public String getSaleGold() {
		return saleGold;
	}

	public void setSaleGold(String saleGold) {
		this.saleGold = saleGold;
	}

	public String getGrowing() {
		return growing;
	}

	public void setGrowing(String growing) {
		this.growing = growing;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getProduce() {
		return produce;
	}

	public void setProduce(String produce) {
		this.produce = produce;
	}

	public String getMultip() {
		return multip;
	}

	public void setMultip(String multip) {
		this.multip = multip;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}
	
	
	
}
