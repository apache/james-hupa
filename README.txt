###### Bulding ######
Hupa use maven2 as build tool. To build hupa download maven2 (http://maven.apache.org), unpack maven2 and install it.
After that change to hupa directory and execute the following cmd:

* mvn clean package

###### Configuring server side  ################

Hupa uses a properties file to know the IMAP and SMTP servers configuration.
There is an example configuration file in 'server/src/main/webapp/WEB-INF/conf/config.properties'

- You can put your configuration parameters in either of these files:
  $HOME/.hupa/config.properties
  /etc/default/hupa
- Or anywhere if you start your application server with the parameter:
  -Dhupa.config.file=full_path_to_your_properties_file

###### Using hupa in demo mode #################

In demo mode there is not necessary any imap or smtp server.
A bunch of example messages and folders are shown to the useri to be manipulated.
Almost every hupa feature work in demo mode.

To enable demo mode for incomming messages set 'IMAPServerAddress=demo-mode' and
'SMTPServerAddress=demo-mode' for outgoing messages.

To login into the system in thi mode use the user 'demo' with password 'demo'  

###### Eclipse GWT Plugin notes ################

- If you want to run hupa in hosted mode be sure to add the following line as "vm argument" in the Run configuration:
  -Dhupa.config.file=${project_loc}/server/src/main/webapp/WEB-INF/conf/config.properties

- If you receive this kind of errors in eclipse:
  >> 'Access restriction: The constructor Data[...] is not accessible due to restriction on required library [...]/jre/lib/rt.jar'

  Change the order of the libraries puting 'JRE System Library' at the bottom.

- To avoid these gwt-pugin's messages
  >> 'The output directory for the project should be set to /hupa/war/WEB-INF/classes'
  >> 'The web.xml file does not exist'

  In unix-like environments make these symbolic links: 
       $ ln -s client/war war
       $ ln -s ../../src/main/webapp/WEB-INF/web.xml war/WEB-INF/web.xml
  In windows 
       copy recursively 'client/war' to 'war'
       copy 'client/src/main/webapp/WEB-INF/web.xml' to 'war/WEB-INF'
  Then, set project's output directory to: /hupa/war/WEB-INF/classes
       properties -> Java Build Path -> Source -> Default output folder
