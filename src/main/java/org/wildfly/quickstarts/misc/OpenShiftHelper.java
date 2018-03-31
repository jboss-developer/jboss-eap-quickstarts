/*
 * JBoss, Home of Professional Open Source
 * Copyright 2018, Red Hat, Inc., and individual contributors as indicated
 * by the @authors tag.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wildfly.quickstarts.misc;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tomaz Cerar (c) 2018 Red Hat Inc.
 */
public class OpenShiftHelper {

    private static final List<String> IGNORED_DIRS = Arrays.asList("shared-doc", "guide", "dist", "template");

    private final Path workDir;

    private OpenShiftHelper(Path path) {
        workDir = path;
    }

    public static void main(String[] args) throws IOException {
        Path workingPath = Paths.get(".");
        assert Files.isDirectory(workingPath);
        System.out.println(String.format("Working directory: %s", workingPath.toAbsolutePath()));

        new OpenShiftHelper(workingPath)
                .generateOSCommands()
                .forEach(System.out::println);
    }

    private List<String> generateOSCommands() throws IOException {
        List<Path> working = new LinkedList<>();
        List<Path> nonWorking = new LinkedList<>();

        try (DirectoryStream<Path> dirs = Files.newDirectoryStream(workDir, entry -> Files.isDirectory(entry)
                && (!entry.getFileName().toString().startsWith("."))
                && (!IGNORED_DIRS.contains(entry.getFileName().toString()))
        )
        ) {
            dirs.forEach(path -> {
                boolean problematic = false;
                try {
                    problematic = containsProblematicFiles(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (!problematic) {
                    working.add(path);
                } else {
                    nonWorking.add(path);
                    //System.out.println(String.format("Directory '%s' uses cli and/or add-user script, as such cannot run on openshift", path.getFileName()));
                }
            });
        }
        List<String> result = new ArrayList<>(working.size() * 2);
        working.forEach(path -> {
            String name = path.getFileName().toString();
            //String newApp = String.format("oc new-app templates/jboss-eap71-openshift:1.1~https://github.com/wildfly/quickstart.git --build-env=\"MAVEN_ARGS_APPEND=-Pjboss-community-repository\" --context-dir=%s --name=%s", name, name);
            String newApp = String.format("oc new-app templates/jboss-eap71-openshift:1.1~https://github.com/jbossas/eap-quickstarts.git#openshift --source-secret eap-key --build-env=\"MAVEN_ARGS_APPEND=-Pjboss-community-repository\" --context-dir=%s --name=%s", name, name);
            String expose = String.format("oc expose service/%s", name);
            result.add(newApp);
            result.add(expose);

        });
        int all = working.size() + nonWorking.size();
        System.out.println(String.format("working QS: %s/%s", working.size(), all));
        working.forEach(path -> System.out.println(path.getFileName()));
        System.out.println("\n\n");
        System.out.println(String.format("NON working QS: %s/%s", nonWorking.size(), all));
        nonWorking.forEach(path -> System.out.println("git rm -r " + path.getFileName()));
        return result;
    }

    private boolean containsProblematicFiles(Path qs) throws IOException {
        Path readme = qs.resolve("README.adoc");
        Path pom = qs.resolve("pom.xml");
        if (!Files.exists(pom)) {
            return true;
        }
        String packaging = Files.readAllLines(pom, StandardCharsets.UTF_8).stream()
                .filter(s -> s.trim().startsWith("<packaging>"))
                .collect(Collectors.joining("\n"))
                .trim();
        if ("".equals(packaging)) {
            packaging = "jar";
        } else {
            packaging = packaging.substring("<packaging>".length(), packaging.indexOf("</packaging>"));
        }

        String content = new String(Files.readAllBytes(readme), Charset.defaultCharset());
        if (content.contains("add-user.sh")) {
            return true;
        } else if (content.contains("XTS")) {
            return true;
        }

        try {
            try (DirectoryStream<Path> dirs = Files.newDirectoryStream(qs, "*.cli")) {
                if (dirs.iterator().hasNext()) {
                    return true;//cli scripts do not work on openshift s2i yet
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (!packaging.contains("war")) {
            System.out.println(String.format("%s is not of type war but is '%s', skipping", qs.getFileName(), packaging));
            return true;
        }
        return false; //all is good in the world
    }
}
