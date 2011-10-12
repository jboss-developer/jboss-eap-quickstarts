Generates a *very basic* multi module Java EE 6 project composed of a parent pom project and 4 nested modules : Java Utility, EJB 3.1, Web 3.0 and EAR 6.0. No JSF nor persistence involved


To install in your local repository :

    git clone git://github.com/open-archetypes/multi-javaee6-archetype.github
    cd multi-javaee6-archetype
    mvn clean install

To use in command line :

   mvn archetype:generate -Dfilter=multi-javaee6-archetype -U
   
  Fill in the required properties and proceed

To use in Eclipse : 

   create new Maven Project
   Next
   Select "Default local catalog"
   Check "Include snapshot archetypes"
   Select multi-javaee6-archetype
   Next
   Fill in the required properties and proceed
   
