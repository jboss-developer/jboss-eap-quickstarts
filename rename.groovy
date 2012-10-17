def basedir = new File(".") 

println "Renaming project examples"

def newModules = []
def pom = new File(basedir, "pom.xml")
def pomParser = new XmlParser()
basedir.eachDir {moduleDir  ->
     def modPom = new File(moduleDir, "pom.xml")
     if (!modPom.exists() || moduleDir.name =~ "(template)|(dist)"){
       return
     }
     def p = pomParser.parse(modPom)
     if (p) {
         String artifactId = p.artifactId.text();
         if (artifactId != moduleDir.name){
            File newFolder = new File(basedir, artifactId)
            println "rename ${moduleDir} to ${newFolder}"
            moduleDir.renameTo(newFolder)
            def newPom = pom.text.replace("<module>${moduleDir.name}</module>", "<module>${artifactId}</module>")
            pom.text = newPom
         }
         newModules.add artifactId
     }
} 
