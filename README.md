# APIBulkExport
## What does this tool?
API Bulk Export allows you to export all the API's available in a WSO2 API Manager deployment. The API's are exported as .zip files and are indivdually archived. 

## Pre-requists
* WSO2 API Manager 1.9 or higher <br/>
* API Import/Export web-app deployed on the above API Manager instance <br/>
* Apache Maven 


## What API's would this tool consume
The tool consumes two API's exposed by the WSO2 API Manager.<br />
1. Publisher API - Version 0.9 <br />
2. Export/Import API - Version 1.0.1


Both API versions are configurable through the properties file available in the project.

## Steps to run the tool.
1. Edit Configuration - Open the config.properties file and change the configuration based on your own setup. Keep the Publisher API and Export/Import API as it is if you are not sure on which version to use. <br/>
2. Build Project - Build the project by running mvn clean package.
3. Run the tool - Go to the target folder and run the tool by running `java -jar APIBulkExport-1.0-SNAPSHOT.jar. You will notice that a copy of your properties file is created in the target folder for your convience. You can edit this file as required and run the java -jar command.
4. 

