ICArUS Challenge Problem Software and Data - DEPLOYMENT README
Copyright 2015 The MITRE Corporation. All rights reserved

The document provides instructions on building the desktop version of the application,
as well as a Java Web Start version of the application and an Applet version of the application.

To build the project, first use Ant or NetBeans to run build.xml to compile classes 
into build/classes. 

-------------------------------------------------------------------
Creating the Java Desktop Application

Use Ant or NetBeans to run Icarus_TE_GUI_Ant_Build_Script.xml using the default target,
"create_desktop_application". This will package the application in the distrib/Icarus_TE_Application
folder. It will also copy Phase 1 and Phase 2 exams to the application's data folder. You 
may modify the build script to change the exams that are distributed with the application.
The runnable jar file is for the full version of the Test & Evaluation GUI that was used 
to conduct human subject experiments in Phases 1 and 2. It also contains functionality to playback
model performance on an exam, open feature vector files, validate feature vector files,
and validate exam files. Functionality to interact with the Test Harness has been removed. 
Zip and distribute the Icarus_TE_Application folder to users. Users double click 
"Icarus_TE_GUI.jar" to launch the application.

-------------------------------------------------------------------
Creating the Java WebStart Application and Applet

The Test & Evaluation GUI can also be packaged as a Java Webstart Application or
a Java Applet. The web folder contains JNLP files and HTML files that launch
the applet or Webstart application. To create the jar file for these versions, use
Ant or NetBeans to run the Icarus_TE_GUI_Ant_Build_Script.xml using the target
"create_web_jar". This target will also package Phase 1 and Phase 2 exams in the jar
file. You may modify the build script to change the exams that are packaged in the 
jar file. If you do modify the exams, you must also modify the file 
src/main/java/exams.xml, which contains the list of Phase 1 and Phase 2 exams.
This list is used to populate a dialog box when the user opens a Phase 1 or
Phase 2 exam. The exams packaged with the jar file are located in data/data/Phase_1_CPD/exams
and data/data/Phase_2_CPD/exams.

Note that this version of the application is tailored as the
"ICArUS Training Suite", and it can only open and play through exams. Data recording
and Test Harness interaction is not supported.

If you would like to deploy the application to your web server, copy all files
from the web folder to a location on your server. The "icarus_applet.html" file
is an HTML page that launches the applet version, and the "icarus_webstart.html"
file is an HTML page that launches the Webstart version. Note that you should
sign the jar file ("Icarus_TE_GUI.jar") with your organization's code signing
certificate. Otherwise, current default Java security restrictions will prevent 
users from opening the applet or Webstart application.
