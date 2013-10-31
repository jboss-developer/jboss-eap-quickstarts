Quickstarts Contributing Guide
==============================

Purpose of the quickstarts
--------------------------

- To demonstrate Java EE 6 technologies

- To provide developers with working examples and instructions that are easy to follow .

- To allow examples to be copied by developers and used as the basis for their own projects.


Basic Steps
-----------

To contribute to the quickstarts, fork the quickstart repository to your own Git, clone your fork, commit your work on topic branches, and make pull requests. 

If you don't have the Git client (`git`), get it from: <http://git-scm.com/>

Here are the steps in detail:

1. [Fork](https://github.com/jboss-developer/jboss-eap-quickstarts/fork_select) the project. This creates a the project in your own Git with the default remote name 'origin'.

2. Clone your fork. This creates and populates a directory in your local file system.

        git clone https://github.com/<your-username>/jboss-eap-quickstarts.git

3. Change to the `jboss-eap-quickstarts` directory.

4. Add the remote `upstream` repository so you can fetch any changes to the original forked repository.

        git remote add upstream https://github.com/jboss-developer/jboss-eap-quickstarts.git

5. Get the latest files from the `upstream` repository.

        git fetch upstream

6. Create a new topic branch to contain your new quickstart, features, changes, or fixes using the `git checkout -b  <topic-branch-name> upstream/master` command. For example:

        git checkout -b helloworld-fix upstream/master

7. Contribute new code or make changes to existing files. Make sure that you follow the [General Guidelines](#general-guidelines) below.

8. To verify if your code followed the General Guidelines you can run [QS Tools](http://www.jboss.org/jdf/quickstarts/qstools/) on your project.
   * To run QS Tools, go to your quickstart project root and execute:
   
           mvn -U org.jboss.maven.plugins:maven-qstools-plugin:check
   This will generate a report on `QUICKSTART_HOME/target/site/qschecker.html`. Review the report to determine if your quickstart project violates any item in the *General Guidelines*.

9. Use the `git add` command to add new or changed file contents to the staging area.
   * If you create a new quickstart, you can add files using the subfolder and file names. The following is an example of new quickstart folders and files you may want to stage:
   
            git add src/
            git add pom.xml
            git add README.md
   _Note: It is probably best not to add the entire quickstart root folder because you may unintentionally add classes or other target files that should not be in source control._
   * If you only modified a few files, use `git add <filename>` for every file you create or change. For example:

            git add README.md       
10. Use the git status command to view the status of the files in the directory and in the staging area and ensure that all modified files are properly staged:

        git status        
11. Commit your changes to your local topic branch. 

        git commit -m 'Description of change...'
12. Update your branch with any changes made upstream since you started.
   * Fetch the latest changes from upstream

        git fetch upstream
   * Apply those changes to your branch
   
        git rebase upstream/master
   * If anyone has commited changes to files that you have also changed, you may see conflicts. 
   Resolve the conflicted files, add them using `git add`, and continue the rebase:
   
        git add <conflicted-file-name>
        git rebase --continue
   * If there were conflicts, it is a good idea to test your changes again to make they still work.
        
13. Push your local topic branch to your github forked repository. This will create a branch on your Git fork repository with the same name as your local topic branch name. 

        git push origin HEAD            
   _Note: The above command assumes your remote repository is named 'origin'. You can verify your forked remote repository name using the command `git remote -v`_.
14. Browse to the <topic-branch-name> branch on your forked Git repository and [open a Pull Request](http://help.github.com/send-pull-requests/). Give it a clear title and description.


General Guidelines
------------------

* The sample project should be formatted using the JBoss AS profiles found at <http://github.com/jboss/ide-config/tree/master/>

 - Code should be well documented with good comments. Please add an author tag (@author) to credit yourself for writing the code.
 - You should use readable variable names to make it easy for users to read the code.

* The package must be `org.jboss.quickstarts.<product-type>`, for example: org.jboss.quickstarts.eap, org.jboss.quickstarts.wfk, org.jboss.quickstarts.jdg, org.jboss.quickstarts.brms, org.jboss.quickstarts.fuse, etc.

* The quickstart project or folder name should match the quickstart name. Each sample project should have a unique name, allowing easy identification by users and developers.

* The `<name>` in the quickstart `pom.xml` file should follow the template: `JBoss <target-product> Quickstart: <quickstart-name> < - optional-subfolder-name>` where `target-product` is the `Target Product` metadata specified in the README.md file,  `quickstart-name` is the quickstart folder name, and `optional-subfolder-name` is the name of any nested subfolder containing a `pom.xml` file. The following are a few examples of quickstart pom files and the correct name tags:

        greeter/pom.xml ==> `JBoss EAP Quickstart: greeter`
        helloworld-errai/pom.xml ==> `JBoss WFK Quickstart: helloworld-errai`
        kitchensink-ear/pom.xml ==> `JBoss EAP Quickstart: kitchensink-ear`
        kitchensink-ear/ear/pom.xml --> `JBoss EAP Quickstart: kitchensink-ear - ear`
        kitchensink-ear/ejb/pom.xml ==> `JBoss EAP Quickstart: kitchensink-ear - ejb`
        kitchensink-ear/web/pom.xml ==> `JBoss EAP Quickstart: kitchensink-ear - web`

* The `<artifactId>` in the quickstart `pom.xml` file should follow the template: `jboss-<target-product>-<quickstart-name>`. For example, the `<artifactId>` for the `greeter` quickstart in the EAP project is `jboss-as-greeter`. The `<artifactId>` for `errors` quickstart in the Fuse project is `jboss-fuse-errors`.

* The JBoss developer Maven repository that contains newly staged artifacts is located at [developer.github.io](http://jboss-developer.github.io/temp-maven-repo/). 
   To access these artifacts, do one of the following:

        * You can simply copy the `contributor-settings.xml` located in the root of the quickstart to your `${user.home}/.m2/` and rename it to `settings.xml`. 
        
        * Or, if you prefer to manually edit the settings, copy the following profile to your `settings.xml` file.

              <profile>
                  <id>jboss-developer-repository</id>
                  <repositories>
                      <repository>
                          <id>jboss-developer-repository</id>
                          <url> http://jboss-developer.github.io/temp-maven-repo/</url>
                          <releases>
                             <enabled>true</enabled>
                          </releases>
                          <snapshots>
                            <enabled>false</enabled>
                          </snapshots>
                      </repository>
                  </repositories>
                  <pluginRepositories>
                      <pluginRepository>
                          <id>jboss-developer-plugin-repository</id>
                          <url> http://jboss-developer.github.io/temp-maven-repo/</url>
                          <releases>
                            <enabled>true</enabled>
                          </releases>
                          <snapshots>
                            <enabled>false</enabled>
                          </snapshots>
                      </pluginRepository>
                  </pluginRepositories>
              </profile>

            Then add `<activeProfile>jboss-developer-repository</activeProfile>` to the `<activeProfiles>` section of the file.
  _Note: Regardless of the method you choose to configure your Maven settings, you must also delete the existing `${user.home}/.m2/repository/`._
  
* If you create a quickstart that uses a database table, make sure the name you use for the table is unique across all quickstarts. 

* The project must follow the structure used by existing quickstarts such as [numberguess](https://github.com/jboss-jdf/jboss-as-quickstart/tree/master/numberguess). A good starting point would be to copy the  `numberguess` project.

* The sample project should be importable into JBoss Developer Studio/JBoss Tools and be deployable from there.

* Maven POMs must be used. No other build system is allowed unless the purpose of the quickstart is to show another build system in use. If using Maven it should:

 - Not inherit from another POM
 - Maven POMs must use the Java EE spec BOM/POM imports
 - The POMs must be commented, with a comment each item in the POM
 - Import the various BOMs, either directly from a project, or from [JBoss BOMs](http://www.jboss.org/jdf/stack/stacks/), to determine version numbers. You should aim to have no dependencies declared directly. If you do, work with the jdf team to get them added to a BOM.
 - Use the JBoss AS Maven Plugin to deploy the example

* The sample project must contain a `README.md` file using the `template/README.md` file as a guideline

* Don't forget to update the `pom.xml` in the quickstart root directory. Add your quickstart to the 'modules' section.

* The project must target Java 6

 - CDI should be used as the programming model
 - Avoid using a web.xml if possible. Use faces-config.xml to activate JSF if needed.
 - Any tests should use Arquillian.

* If the quickstart persists to a database, you must use a unique datasource JNDI name and connection URL for the application and for any Arquillian tests that it provides. Do not use the JNDI name `java:jboss/datasources/ExampleDS`. Failure to use unique names can result in a `DuplicateServiceException` when more than one quickstart is deployed to the same server.

* If possible, create a cheat sheet for the quickstart to guide users and developers through the example. See the [Quickstart Cheat Sheet Contributing Guide](#quickstart-cheat-sheet-contributing-guide) for more information.

Kitchensink variants
--------------------

  There are multiple quickstarts based on the kitchensink example.  Each showcases different technologies and techniques including pure EE6, JSF, HTML5, and GWT.

  If you wish to contribute a kitchensink variant is it important that you follow the look and feel of the original so that useful comparisons can be made.  This does not mean that variants can not expand, and showcase additional functionality.  Multiple variants already do that.  These include mobile interfaces, push updates, and more.

  Below are rules for the *look and feel* of the variants:

  * Follow the primary layout, style, and graphics of the original.

  * Projects can have 3-4 lines directly under the AS/EAP banner in the middle section to describe what makes this variant different.
     * How projects use that space is up to them, but options include plain text, bullet points, etc....

  * Projects can have their logo in the left side of the banner.
    * The sidebar area can contain a section with links to the related projects, wiki, tutorials, etc...
       * This should be below any AS/EAP link areas.

    If appropriate for the technology the application should expose RESTful endpoints following the example of the original kitchensink quickstart.  This should also include the RESTful links in the member table.
    
Setup your environment
----------------------

The quickstart README.md files are converted to HTML using markdown. We recommend using redcarpet, as that is what github uses, but you can use any markdown tool really.

There are two scripts, `dist/github-flaoured-markdown.rb`, that will convert an indivdual file, and `dist/release-utils.sh -m`, that will convert all the files.

To setup the environment you need to follow these steps.

1. Install Ruby *1.9.X*

    For RHEL you can use this [spec](https://github.com/lnxchk/ruby-1.9.3-rpm)
    
    In general, you're better off not relying on your OSs ruby install, they are often quite broken.

2. Install Ruby GEMs

        gem install nokogiri pygments.rb redcarpet fileutils

3. Install Python Eggs

    You'll need python eggs installed, which often isn't available on OS installs of python. Google to find out how to install it

4. Install pygments

            sudo easy_install pygments

License Information and Contributor Agreement
---------------------------------------------

  JBoss Developer Framework is licensed under the Apache License 2.0, as we believe it is one of the most permissive Open Source license. This allows developers to easily make use of the code samples in JBoss Developer Framework. 

  There is no need to sign a contributor agreement to contribute to JBoss Developer Framework. You just need to explicitly license any contribution under the AL 2.0. If you add any new files to JBoss Developer Framework, make sure to add the correct header.

### Java,  Javascript and CSS files 

      /** 
       * JBoss, Home of Professional Open Source
       * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
       * contributors by the @authors tag. See the copyright.txt in the 
       * distribution for a full listing of individual contributors.
       *
       * Licensed under the Apache License, Version 2.0 (the "License");
       * you may not use this file except in compliance with the License.
       * You may obtain a copy of the License at
       * http://www.apache.org/licenses/LICENSE-2.0
       * Unless required by applicable law or agreed to in writing, software
       * distributed under the License is distributed on an "AS IS" BASIS,  
       * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       * See the License for the specific language governing permissions and
       * limitations under the License.
       */

### HTML, XML, XSD and XHTML files

      <!--
       JBoss, Home of Professional Open Source
       Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
       contributors by the @authors tag. See the copyright.txt in the 
       distribution for a full listing of individual contributors.

       Licensed under the Apache License, Version 2.0 (the "License");
       you may not use this file except in compliance with the License.
       You may obtain a copy of the License at
       http://www.apache.org/licenses/LICENSE-2.0
       Unless required by applicable law or agreed to in writing, software
       distributed under the License is distributed on an "AS IS" BASIS,  
       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       See the License for the specific language governing permissions and
       limitations under the License.
       -->

### Properties files and Bash Scripts

       # JBoss, Home of Professional Open Source
       # Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
       # contributors by the @authors tag. See the copyright.txt in the 
       # distribution for a full listing of individual contributors.
       #
       # Licensed under the Apache License, Version 2.0 (the "License");
       # you may not use this file except in compliance with the License.
       # You may obtain a copy of the License at
       # http://www.apache.org/licenses/LICENSE-2.0
       # Unless required by applicable law or agreed to in writing, software
       # distributed under the License is distributed on an "AS IS" BASIS,  
       # WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       # See the License for the specific language governing permissions and
       # limitations under the License.

### SQL files

      --
      -- JBoss, Home of Professional Open Source
      -- Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
      -- contributors by the @authors tag. See the copyright.txt in the
      -- distribution for a full listing of individual contributors.
      --
      -- Licensed under the Apache License, Version 2.0 (the "License");
      -- you may not use this file except in compliance with the License.
      -- You may obtain a copy of the License at
      -- http://www.apache.org/licenses/LICENSE-2.0
      -- Unless required by applicable law or agreed to in writing, software
      -- distributed under the License is distributed on an "AS IS" BASIS,
      -- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
      -- See the License for the specific language governing permissions and
      -- limitations under the License.
      --

### JSP files

      <%--
      JBoss, Home of Professional Open Source
      Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
      contributors by the @authors tag. See the copyright.txt in the
      distribution for a full listing of individual contributors.

      Licensed under the Apache License, Version 2.0 (the "License");
      you may not use this file except in compliance with the License.
      You may obtain a copy of the License at
      http://www.apache.org/licenses/LICENSE-2.0
      Unless required by applicable law or agreed to in writing, software
      distributed under the License is distributed on an "AS IS" BASIS,
      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
      See the License for the specific language governing permissions and
      limitations under the License.
      --%>
      

Quickstart Cheat Sheet Contributing Guide
==============================


Purpose of the Cheat Sheets
--------------------------

- Cheat sheets function as a tutorial and provide a step by step guide through a quickstart. 
- They provide a way to step through and explain the code in an interactive way.
- They can provide an in-depth analysis of specific sections of code.


Basic Steps to Create a Cheat Sheet
-----------

You can create a cheat sheet using the Eclipse Wizard or you can copy and modify an existing cheat sheet from another quickstart. This section describes how to create a cheat sheet using the Eclipse wizard.

_Note: Be sure your project folder is located outside of the Eclipse workspace before you begin this process._

1.  Import your quickstart into JBoss Developer Studio (JDBS)
    1.  From the menu, choose `File` --> `Import` --> `Maven` --> `Existing Maven Projects`, then click `Next`.
    2.  Navigate to your quickstart, select it, then click `OK`.
    3.  Click `Finish`.
2.  Create the cheat sheet.
    1.  Select the imported quickstart project.
    2.  From the menu, choose `File` --> `New` --> `Other` --> `User Assistance` --> `Cheat Sheet`, then click `Next`.
    3.  Select the quickstart folder, give it a name 'cheatsheet.xml', and choose `Simple Cheat Sheet`.
    4.  Click `Finish`. When it prompts you to open the cheatsheet for the quickstart project, click `Yes`.
3.  Populate the cheatsheet with useful information to help a user understand the quickstart.
    1.  Expand the `Title` in the content section on the left. 
    2.  Select the `Title` field and modify it to something useful, for example: `helloworld`
    3.  Select the `intro` field and add introduction text to the `Body`, for example: `This quickstart demonstrates the use of CDI 1.0 and Servlet 3.0. It is a simple application that can be used to verify the JBoss EAP server is configured and running correctly.`
    4.  Select `item`, then under `Command`, click `browse` and select 'Get current project' under `Uncategorized`. This adds the following XML to the cheat sheet: 
    
            <command 
            required="true" 
            returns="currentProject"
            serialization="org.jboss.tools.project.examples.cheatsheet.getProjectForCheatsheet"/>
    This command allows you to use the variable `${currentProject}` instead of a hard-coded path name and ensures your cheat sheet will work regardless of the project location.
         
    5.  Add an `item` for each file or class you want to describe. 
        *  This is dependent on the quickstart features you plan to demonstrate.
        *  Provide a good description.
        *  Add subitems to describe code sections and provide the line numbers that are referenced.
4. Test your cheat sheet by opening it in JDBS.
    1.  Go through each step and make sure the descriptions are valid.
    2.  Click on each link to make sure it opens the file and highlights the correct lines of code.
5. When you finish testing the cheat sheet, rename the file from `cheatsheet.xml` to `.cheatsheet.xml` and make sure it is located in the root directory of the quickstart.
6. Add the `.cheatsheet.xml` file using `git add`, commit the change, push it to your forked repository, and issue a pull.
7. If your cheat sheet is for the quickstart based on an archetype, it will automatically generate the cheat sheet for the archetype. However, you must add an `<include>.cheatsheet.*</include>` to the fileset for the root directory in the corresponding archetype's `archetype-metadata.xml` file. See the `jboss-javaee6-webapp-archetype` archetype for an example.


General Guidelines
------------------

* If your project folder is located in the Eclipse workspace when you generate your cheat sheet using the Eclipse wizard, it will generate an invalid project name and attempts to open source code will fail. Be sure your project folder is located outside the Eclipse workspace before you begin.
* The cheat sheet should be created in the root of the quickstart directory and named `.cheatsheet.xml`. Eclipse will not let you name the file with a leading '.', so you will need to rename it after it is created.
* Make sure you add the 'Get current project' command and use the replaceable `${currentProject}`  value to avoid hard-coding the project path. This ensures that if the quickstart folder is moved, the cheat sheet will work as expected.
* Do not use the `<action>` tag if it can be avoided. It is more fragile than the `<command>` tag, which uses parameters names instead of indexes.
* Try to highlight the most important features and code for the quickstart. Pay particular attention to areas that might confuse developers. Cheat sheets require that users execute or skip each step, so you don't want to bore developers with the code that has no impact on the purpose of the quickstart.
* Make sure `<?xml version="1.0" encoding="UTF-8"?>` is the first line in the `.cheatsheet.xml` file, before the license information. This enables the cheat sheet to open automatically when you import the project into JBoss Developer Studio.

Find Help
------------------

You can find additional help at the following locations:

* [Eclipse Help: Cheat sheets](http://help.eclipse.org/kepler/index.jsp?topic=%2Forg.eclipse.platform.doc.isv%2Fguide%2Fua_cheatsheet.htm&resultof=%22cheat%22%20%22sheet%22%20)
* [Recommended Work Flow for Cheat Sheet Development](http://www.eclipse.org/pde/pde-ui/articles/cheat_sheet_dev_workflow/)
* [Max's cheat sheet example](https://github.com/maxandersen/cheatsheet-helloworld)


