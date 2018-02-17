package com.sh.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sh.entity.CropEntity;

public class ConnectionWiki {

	/*
	public static void main(String[] args) throws IOException
	{
		new ConnectionWiki().getInfofromWiki();
	}
	*/
	public List<CropEntity> getInfofromWiki() throws IOException
	{
		Connection conn =  Jsoup.connect("https://es.stardewvalleywiki.com/Cultivos");
		
		Response resp = conn.execute();
		Document doc = resp.parse();
		
		Map<String, String> mapaImg = getUrls(doc);
		List<CropEntity> lsRes = new ArrayList<CropEntity>();
		
		Elements ls = doc.select("table[id=roundedborder]");
		for(Element el : ls)
		{
			Elements lsTh = el.select("th");
			Elements rows = el.select("tr:eq(1)");
			Element row = rows.get(0);
			boolean hasColspan = false;
			Elements e = null;
			
			CropEntity entity = new CropEntity();
			
			for(Element th : lsTh)
			{
				if("Semillas".equals(th.text()) || "Seed".equals(th.text()))
				{
					e = row.select("td:eq("+th.elementSiblingIndex()+")");
					String title = e.get(0).select("center a").text();
					Pattern pat = Pattern.compile("(Semillas|Semilla|Kit|Bulbo|Brote|Grano)|(.*de)");
					Matcher matcher = pat.matcher(title);
					StringBuilder str = new StringBuilder();
					while(matcher.find())
					{
						for (int i = 1; i <= matcher.groupCount(); i++) 
						{
							if(matcher.group(i) != null)
								str.append(matcher.group(i));
						}
					}
					
					title = title.replaceFirst(str.toString()+" ", "");
					if(mapaImg.get(title) == null)
					{
						for(String k : mapaImg.keySet())
						{
							if(k.contains(title))
							{								
								entity.setImg(mapaImg.get(k).substring(mapaImg.get(k).lastIndexOf("/")+1));
								break;
							}
						}
					}
					else
					{
						entity.setImg(mapaImg.get(title).substring(mapaImg.get(title).lastIndexOf("/")+1));
					}
					entity.setName(title);
		
					Elements salePrice = e.get(0).select("p span");
					if(!salePrice.isEmpty() && salePrice != null)
					{
						
						entity.setBuyPierre(salePrice.get(0).text());
						if(salePrice.size() > 1)
						{
							
							entity.setBuyJoja(salePrice.get(1).text());
						}
					}
					
				}
				else if("Cosecha".equals(th.text()))
				{
					hasColspan = th.hasAttr("colspan");
					Elements r = el.select("tr:eq(2)");
					Element dias = r.last();
					Pattern ptGrow = Pattern.compile("(Total:.[0-9]?[0-9])");
					Matcher matGrow = ptGrow.matcher(dias.text());
					Pattern num = Pattern.compile("([0-9]?[0-9])");
					Matcher matNum = null;
					
					while(matGrow.find())
					{
						String gr = matGrow.group(1);
						matNum = num.matcher(gr);
						while(matNum.find())
						{
							//System.out.println("Growing in: "+matNum.group(1));
							entity.setGrowing(matNum.group(1));
						}
						
					}
					
					ptGrow = Pattern.compile("(produciendo.+\\.)");
					matGrow = ptGrow.matcher(dias.text());
					while(matGrow.find())
					{
						String gr = matGrow.group(1);
						matNum = num.matcher(gr);
						while(matNum.find())
						{
							//System.out.println("Producing in: "+matNum.group(1));
							entity.setProduce(matNum.group(1));
						}
					}
				}
				else if("Precio de venta".equals(th.text()))
				{
					int index = th.elementSiblingIndex();
					if(hasColspan)
						index++;
					e = row.select("td:eq("+index+")");
					if(e != null && !e.isEmpty())
					{
						Element td = e.get(0);
						Elements tbValues = td.select("table tr td:gt(0)");
						entity.setSaleRegular(tbValues.get(0).text());
						entity.setSaleSilver(tbValues.get(1).text());
						entity.setSaleGold(tbValues.get(2).text());
						
					}
				}
			}
			lsRes.add(entity);
		}
		
		return lsRes;
	}
	
	private Map<String, String> getUrls(Document doc) throws IOException
	{
		
	
		
		Map<String, String> mapa = new HashMap<String, String>();
		
		Elements root = doc.select(".mw-headline:has(a)");
		for(Element span : root)
		{
			Element img = span.select("img").first();
			Element title = span.select("a:eq(1)").first();
			if(title != null)
			{
				mapa.put(title.text().toLowerCase(), img.attr("src"));
				//System.out.println(title.text());
				//System.out.println(img.attr("src"));
			}
		}
		return mapa;
	}
	
}
