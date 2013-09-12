###### Bulding ######
Hupa use maven2 as build tool. To build hupa download maven2 (http://maven.apache.org), unpack maven2 and install it.
After that change to hupa directory and execute the following cmd:
$ mvn clean package

###### Configuring server side  ########
Hupa uses a properties file to know the IMAP and SMTP servers configuration.
There is an example configuration file in 'server/src/main/webapp/WEB-INF/conf/config.properties'

- You can set your configuration parameters in either of these files:
  $HOME/.hupa/config.properties
  /etc/default/hupa
- Or in any other file if you start your application server with the parameter:
  -Dhupa.config.file=full_path_to_your_properties_file

##### Running Hupa #####
Hupa comes packaged with a servlet-container, so once you have compiled the app just run:
$ java -jar target/hupa-${version}.war

Then point your browser to the url:
http://localhost:8282

If you prefer to use any other servlet container you can deploy the provided .war file in it.

###### Hupa and GMail #################
By default hupa is configurated as a gmail imap/smtp client, use any gmail valid account to login.
NOTE: that previously to use a gmail account via imap you should enable imap in your gmail account.

###### Demo Mode #################
In demo mode it is not necessary any imap or smtp server.
A bunch of example messages and folders are shown to the user to be manipulated.
Almost every hupa feature work in demo mode.

To enable demo mode set 'IMAPServerAddress=hupa.demo' or start your servlet container with the 
parameter 'hupa.demo'

$ java -Dhupa.demo -jar target/hupa-${version}.war

To login into the system in this mode use the user 'demo' with password 'demo'  

###### Eclipse GWT Plugin notes ################
- Hupa uses maven to be built, before inporting the project, you shoul install m2eclipse 
and google plugins, then go to Import -> New maven project and select the modules:
shared, mock, server, widget and client.

- To run hupa in hosted mode, select: Run as -> (Google) Web application.
  * May be you need to specify the appropriate hupa configuration file if you do not want to
    run it in demo mode.
    If you don't have a suitable file in $HOME/.hupa or /etc/default/hupa you have to add the
    following line to the "vm argument" in the Run configuration:
    -Dhupa.config.file=folder_to_hupa_dources/server/src/main/webapp/WEB-INF/conf/config.properties 

- If you compile hupa with google's eclipse plugin and you get the message:
  >> 'GWT module hupa needs to be (re)compiled, please run a compile or use the Compile/Browse button in hosted mode'
 
  Configure the plugin to compile only the 'Hupa' module.
  Properties -> Google -> Web Toolkit -> Entry Point Modules -> Hupa - org.apache.hupa
 
