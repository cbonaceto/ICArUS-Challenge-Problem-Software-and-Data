ICArUS Challenge Problem Software and Data - MODEL DEVELOPER README
Copyright 2015 The MITRE Corporation. All rights reserved

This document provides instructions on using the software to assist in the development
of computational cognitive-neuroscience models. models.

The software contains all source code previously included in the ICArUS Developer Tools.
See DEPLOYMENT_README.txt for details on building the jar file that you may include
in projects to use model developer functionality to parse and validate exam and feature vector 
files for the Phase 1 and Phase 2 Challenge Problem formats.
The software also includes functionality to compute normative solutions for Phases 1 and 2, 
and to "play back" model performance on an exam for Phase 1. Lastly, the software contains
the XML schemas for the Phase 1 and Phase 2 XML formats.

For more information on the Phase 1 and Phase 2 formats, please see the Phase 1 and Phase 2 
development guides (ICArUS_Phase_1_Development_Guide.pdf, ICArUS_Phase_2_Development_Guide.pdf).

Example code has also been provided to demonstrate this functionality,  as described below:

-------------------------------------------------------------------
Phase 1

The src\main\java\org\mitre\icarus\cps\examples\phase_1 package contains
examples to parse and validate exam and feature vector files, generate responses, compute normative solutions,
 marshall data back to XML, and download from and upload results to the Test Harness, 
and programmatically control the Phase 1 player to demonstrate a model performing 
an exam in real-time. To play back model performance on an exam using the GUI,
place the model performance exam file in the 
Icarus_TE_Application\data\Phase_1_CPD\player_data\model_data folder. At present,
play back is only supported for the Phase 1 Final Exam.

------------------------------------------------------------------- 
Phase 2

The src\main\java\org\mitre\icarus\cps\examples\phase_1 package contains
examples to parse and validate exam and feature vector files, generate responses, 
marshall data back to XML, and compute normative solutions. Exam playback is not
currently supported in Phase 2.