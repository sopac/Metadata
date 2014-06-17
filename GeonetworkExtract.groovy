

String url = "http://geonetwork.sopac.org/geonetwork/srv/en/iso19139.xml?id="

StringBuilder res = new StringBuilder()
ArrayList<String> idList = new ArrayList<String>()
ArrayList<String> uuidList = new ArrayList<String>()

(1..10000).each {
	String id = it.toString()
	String xml = new URL(url + id).getText()
	
	if (xml.contains("<gmd:")){
		//println id
		//extract title, abstract, purpose, fileNames ,fileIdentifier (uuid - for linking to geonetwork)
		def records = new XmlSlurper().parseText(xml).declareNamespace(gmd:'http://www.isotc211.org/2005/gmd', gco:'http://www.isotc211.org/2005/gco')

		String title = records.'gmd:identificationInfo'.'gmd:MD_DataIdentification'.'gmd:citation'.'gmd:CI_Citation'.'gmd:title'.'gco:CharacterString'.text().replaceAll(";", "")
		String abstract_ = records.'gmd:identificationInfo'.'gmd:MD_DataIdentification'.'gmd:abstract'.'gco:CharacterString'.text().replaceAll(";", "")
		String purpose = records.'gmd:identificationInfo'.'gmd:MD_DataIdentification'.'gmd:purpose'.'gco:CharacterString'.text().replaceAll(";", "")
		String uuid = records.'gmd:fileIdentifier'.'gco:CharacterString'.text().replaceAll(";", "")		
		String country = records.'gmd:identificationInfo'.'gmd:MD_DataIdentification'.'gmd:descriptiveKeywords'[1].'gmd:MD_Keywords'.'gmd:keyword'.'gco:CharacterString'.text().replaceAll(";", "")
		String type= records.'gmd:identificationInfo'.'gmd:MD_DataIdentification'.'gmd:descriptiveKeywords'[0].'gmd:MD_Keywords'.'gmd:keyword'.'gco:CharacterString'.text().replaceAll(";", "")
		String file = records.'gmd:identificationInfo'.'gmd:MD_DataIdentification'.'gmd:graphicOverview'[0].'gmd:MD_BrowseGraphic'.'gmd:fileName'.'gco:CharacterString'.text().replaceAll(";", "")

		
		idList.add(id)
		uuidList.add(uuid)

		//Title, Abstract, Types, DataFormat (Vector/Raster), Country, Source, Description (Purpose), File, Attributes, AttributeCount, 

		res.append("'" + title + "';'" + abstract_ + "';'" + type + "';'" + "Raster" + "';'" + country + "';'" + "OIP Geonetwork" + "';'" + purpose + "';'" + file + "';'" + uuid + "'\n")
		
	}
}

f = new File(System.getProperty("user.home") + "/geonetwork-data.csv")
f.write(res.toString())

f = new File(System.getProperty("user.home") + "/geonetwork-idlist.txt")
f.write(idList.toString())

f = new File(System.getProperty("user.home") + "/geonetwork-uuidlist.txt")
f.write(uuidList.toString());


println "Finished"

