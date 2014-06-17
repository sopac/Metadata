
String url = "http://paris.sopac.org/geonetwork/srv/en/csw?request=GetRecords&service=CSW&constraintLanguage=CQL_TEXT&typeNames=csw:Record&resultType=results&&maxRecords=500"

StringBuilder res = new StringBuilder()

String xml = new URL(url).getText()
def records = new XmlSlurper().parseText(xml).declareNamespace(csw:'http://www.opengis.net/cat/csw/2.0.2', gco:'http://www.isotc211.org/2005/gco', geonet:'http://www.fao.org/geonetwork', dc:'http://purl.org/dc/elements/1.1/', dct:'http://purl.org/dc/terms/')

int count = records.'csw:SearchResults'.'csw:SummaryRecord'.size() //'csw:SummaryRecord'.'dc:title'.text()

for (int i = 0; i < count; i++){
	String uuid = records.'csw:SearchResults'.'csw:SummaryRecord'[i].'dc:identifier'.text().trim().replaceAll(";", "")
	String title = records.'csw:SearchResults'.'csw:SummaryRecord'[i].'dc:title'.text().trim().replaceAll(";", "")
	String abstract_ = records.'csw:SearchResults'.'csw:SummaryRecord'[i].'dct:abstract'.text().replaceAll(";", "")
	String subjects = ""	
	for (int j = 0; j<=10; j++){
		try{
			String subject = records.'csw:SearchResults'.'csw:SummaryRecord'[i].'dc:subject'[j].text().trim()
			if (!subject.equals("")) subjects = subjects + subject + " | "

		} catch (Exception ex){}
	}
	subjects = subjects.replaceAll(";", "")

	String format = records.'csw:SearchResults'.'csw:SummaryRecord'[i].'dc:format'.text().trim().replaceAll(";", "")
	String countryCode = title.substring(0, 2).toLowerCase()
	//country code map

	String country = CountryCode.map.get(countryCode.toUpperCase())

	println countryCode + " - " + country


	//println title + " - " + uuid
	
	if (country != null)
	res.append("'" + title + "';'" + abstract_ + "';'" + subjects + "';'" + format + "';'" + country + "';'" + "PCRAFI Geonode" + "';'" + "" + "';'" + "" + "';'" + uuid + "'\n")
		
	
}

f = new File(System.getProperty("user.home") + "/paris-data.csv")
f.write(res.toString())


println "Finished"