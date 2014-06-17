import sys, os, subprocess, re, string

print("Python Version - " + sys.version)

rootPath = "/run/media/sachin/VERBATIMHD1/GISNOV2012/MapServer_Old/data/"
output = "#"

def processResults(res, path):
    if res.startswith("FAILURE:"): return
    if res.startswith("ERROR:"): return
    #print res
    country = ""
    city = path.replace(rootPath, "")
    #city = city[0: city.index("/")]
    #print city, country
    layer = path[path.rindex("/"): path.rindex(".")]
    #layer = layer[2: len(layer)]
    #if (layer.startswith("o_")): layer = layer.replace("o_", "")
    layer = layer.upper()
    layer = layer.replace("/", "")
    #print layer
    
    feature_count = ""
    extent = ""
    geometry = ""    
    projection = ""
    format_ = "MapInfo/Vector"

    try:
        projection = res[res.index("PROJCS["):res.rindex("]]") + 2]
    except ValueError:
        projection = ""
    
    attr_list = []
    list = string.split(res, '\n')
    for l in list:
        if (l.startswith("Feature Count")): feature_count =  l[l.rindex(":") + 2: len(l)]
        if (l.startswith("Extent")): extent =  l[l.rindex(":") + 2: len(l)]
        if (l.startswith("Geometry")): geometry =  l[l.rindex(":") + 2: len(l)]
        if (not l.startswith("Extent") and not l.startswith("Geometry")):
                if ("(" in l and ")" in l):
                    try:
                        attr_list.append(l[0:l.index(":")])
                    except:
                        pass
          
    #print attr_list
    attributes = ""
    for a in attr_list: attributes = attributes + a + ", "
    global output
    output = output + "'" + layer + "';'" + city + "';'" + country + "';'" + feature_count + "';'" + extent + "';'" + geometry + "';'"  + "';'" + attributes + "';'" + format_ + projection.replace(";", "").replace(",","") + "'\n"
    print "----------------------------------------------------"
    #write to file
    f = open("/home/sachin/mapserver.csv", "w")
    f.write(output)


#main
for root, dirs, files in os.walk(rootPath): # Walk directory tree
    for f in files:
        path = os.path.join(root, f)
        if str(f).upper().endswith(".TAB") or str(f).upper().endswith(".SHP"):
            try:
                res = subprocess.check_output(["/usr/bin/ogrinfo", "-ro", "-al", "-so", str(path)])
            except subprocess.CalledProcessError as e:
                res = e.output
            processResults(res, path)




print "Finished"


