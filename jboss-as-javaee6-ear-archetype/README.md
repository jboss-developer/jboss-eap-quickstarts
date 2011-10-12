Generates a *very basic* multi module Java EE 6 project composed of a parent pom project and 4 nested modules : Java Utility, EJB 3.1, Web 3.0 and EAR 6.0. No JSF nor persistence involved

    git clone git://github.com/open-archetypes/multi-javaee6-archetype.github
    cd multi-javaee6-archetype
    mvn clean install

In command line
   mvn archetype:generate -Dfilter=multi-javaee6-archetype -U

In Eclipse, 
   create new Maven Project
   Select "Default local catalog"
   Check "Include snapshot archetypes"
