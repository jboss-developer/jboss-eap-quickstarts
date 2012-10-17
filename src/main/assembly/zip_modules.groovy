
def basedir = project.basedir.canonicalPath.replace("\\", "/") 

def zipDir = new File(project.build.directory,"zips")

String examplesBaseUrl = System.getProperty('examples.base.url')
if (!examplesBaseUrl) {
   examplesBaseUrl = zipDir.toURI().toURL().toString()
}

if (examplesBaseUrl.endsWith("/")) {
    examplesBaseUrl = examplesBaseUrl.substring(0, examplesBaseUrl.length() -1);
}

println "Starting zipping ${project.name} modules"

if (zipDir.exists()){
  println "Deleting ${zipDir}"
  zipDir.delete()
}
zipDir.mkdirs()

int i=0

builder = new groovy.xml.StreamingMarkupBuilder()
builder.encoding = "UTF-8"
def pomParser = new XmlParser()
int warnings = 0
xmlDocument = builder.bind {
  mkp.xmlDeclaration()
  projects() {
     project.basedir.eachDir {moduleDir  -> 
         def modPom = new File(moduleDir, "pom.xml")
         if (!modPom.exists() || moduleDir.name =~ "(template)|(dist)"){
           return
         }
         def module = moduleDir.name
         def zip = new File(zipDir, "${module}.zip")
         
         
         def source = modPom.parent
         //println "Zipping ${source} to ${zip}"
         //ant is an AntBuilder instance accessible in gmaven's context
         ant.zip(baseDir: source, destFile: zip, excludes: "target/")
         i++
         if (zip.exists()) {
             def p = pomParser.parse(modPom)
             if (p) {
                 if (p.artifactId.text() != module){
                     println "[WARNING] module '${module}' has a non matching artifactId '${p.artifactId.text()}'"
                     warnings++
                 }
                 project {
                     id("jdf-"+module)
                     name(module)
                     category("JBoss Developer Framework")
                     shortDescription(sanitize(p.name.text()))
                     description(sanitize(p.description.text()))
                     url("${examplesBaseUrl}/${module}.zip")
                     size(zip.length())
                     importType("maven")
                     icon(path:"icons/jboss.png")
                     tags("central")
                     "included-projects"()
                     fixes() {
                       fix(type:"wtpruntime") {
                          property(name:"allowed-types", "org.jboss.ide.eclipse.as.runtime.71, org.jboss.ide.eclipse.as.runtime.eap.60")
                          property(name:"description", "This project example requires JBoss Enterprise Application Platform 6 or JBoss Application Server 7.1")
                          property(name:"downloadId", "org.jboss.tools.runtime.core.as.711")
                       }
                       fix(type:"plugin") {
                          property(name:"id", "org.eclipse.m2e.core")
                          property(name:"versions", "[1.0.0,2.0.0)")
                          property(name:"description", "This project example requires m2e &gt;= 1.1.")
                          property(name:"connectorIds", "org.eclipse.m2e.feature")
                       }
                       fix(type:"plugin") {
                          property(name:"id", "org.eclipse.m2e.wtp")
                          property(name:"versions", "[0.16.0,2.0.0)")
                          property(name:"description", "This project example requires m2e-wtp &gt;= 0.16.0.")
                          property(name:"connectorIds", "org.maven.ide.eclipse.wtp.feature")
                       }
                       fix(type:"plugin") {
                          property(name:"id", "org.jboss.tools.maven.core")
                          property(name:"versions", "[1.4.0,2.0.0)")
                          property(name:"description", "This project example requires JBoss Maven Tools.")
                          property(name:"connectorIds", "org.jboss.tools.maven.feature,org.jboss.tools.maven.cdi.feature,org.jboss.tools.maven.hibernate.feature,org.jboss.tools.maven.jaxrs.feature")
                       }
                     }
                 }
             }
         }
     }
  }
}


def descriptor = new File(zipDir, 'quickstarts.xml')
descriptor.text = groovy.xml.XmlUtil.serialize(xmlDocument.toString())
println "Zipped ${i} quickstart modules"
println "${descriptor} generated"
if (warnings) {
    def warningStatement = """

[WARNING] ${warnings} quickstart artifactIds mismatch with their folder name. 
This will prevent WTP from opening the proper url when running on a JBoss server.
"""
    
    println warningStatement
}

def String sanitize(String text) {
    text.replace("JBoss AS Quickstarts: ","")
        .replace("JBoss AS Quickstarts ","")
}

