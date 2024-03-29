## Retired project

This Apache James sub-project had been retired on the 30/07/2021.

[Read more](https://www.mail-archive.com/server-dev@james.apache.org/msg70607.html)

##### Introduction #####
Hupa is a Rich IMAP-based Webmail application written in GWT.

Hupa has been entirely written in java to be coherent with the language used in the James project.
It has been a development reference using GWT good practices (MVP pattern and Unit testing)

It is ready for reading, sending, and managing messages and folders, but it still lacks of many features email clients nowadays have.

##### Bulding #####
Hupa use maven as building tool. To build hupa download maven (http://maven.apache.org), unpack maven and install it.
After that change to hupa directory and execute the following cmd:
$ mvn clean package

##### Configuring server side  #####
Hupa uses a properties file to know the IMAP and SMTP servers configuration.
There is an example configuration file in 'hupa/src/main/webapp/WEB-INF/conf/config.properties'

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

If you prefer to use any other servlet container you can deploy the provided .war file into it.

##### Hupa and IMAP/SMTP servers  #####
Hupa is able to discover most of the imap/smtp configuration based on the email domain part.
When you are prompted to login, type your email address and wait few seconds, if you click on the
gear button you can see the configuration discovered by Hupa, you can modify it if it does not match
your email provider configuration. Then type your inbox password and you will be logged into your
email provider servers.

Hupa is compatible with most email providers, gmail, yahoo, hotmail, outlook, exchange, james, etc.

##### Eclipse GWT Plugin notes #####
- Hupa uses maven to be built, before importing the project, you should have installed m2eclipse
and GWT Eclipse Plugin (3.0.0), then go to Import -> Existing Maven Projects and select the modules:
shared, mock, server, widgets, client and hupa.

- To run hupa in hosted mode, select hupa: Run As/Debug As -> GWT Development Mode with Jetty.
