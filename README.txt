###### Bulding ######
Hupa use maven2 as build tool. To build hupa download maven2 (http://maven.apache.org), unpack maven2 and install it.
After that change to hupa directory and execute the following cmd:

* mvn clean package



###### Eclipse GWT Plugin notes ################

If you want to run hupa in hosted mode be sure to add the following line as "vm argument" in the Run configuration:

-Dhupa.config.file=${project_loc}/server/src/main/webapp/WEB-INF/conf/config.properties

