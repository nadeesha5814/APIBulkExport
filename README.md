# API Bulk Exporter for WSO2 API Manager

## What does this tool do?
API Bulk Exporter allows you to export all the API's available in a WSO2 API Manager deployment. The API's are exported as .zip files and are indivdually archived. 

## Prerequisites
* WSO2 API Manager 1.9 or higher <br/>
* API Import/Export web-app deployed on the above API Manager instance <br/>
* Apache Maven 


## What API's would this tool consume
The tool consumes two API's exposed by the WSO2 API Manager.<br />
1. Publisher API - Version 0.9 <br />
2. Export/Import API - Version 1.0.1

Both these API versions are configurable through the configuration file available in the project.

## Steps to run the tool.
1. Enable Basic Authentication in Publisher API - The tool is configured to use basic authentication when getting API information via the Publisher REST API. The Publisher REST API by default is configured to work with OAuth hence we need to change AuthenticationInterceptor to work with Basic Authentication. This can be done by editting the following file <br />
  <API-M Home>\repository\deployment\server\webapps\api#am#publisher#v0.9\WEB-INF\beans.xml <br />
Comment the OAuthAuthenticationInterceptor and enable BasicAuthenicationInterceptor <br />

2. Edit Configuration - Open the config.properties file and change the configuration based on your own setup. Keep the Publisher API and Export/Import API as it is if you are not sure on which version to use. <br/>
2. Build the project - Build the project by running `mvn clean package`.
3. Run the tool - Go to the target folder and run the tool by running `java -jar APIBulkExport-1.0-SNAPSHOT.jar`. You will notice that a copy of your properties file is created in the target folder for your convience. You can edit this file as required and run the `java -jar` command to see the changes get effected. Please also note that any changes made to the properties file in the target folder will be over written whenever you rebuild the code, hence it is encouraged to do any property changes to the main properties file.
 


