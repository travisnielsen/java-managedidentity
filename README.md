# Managed Identity Test

This is a basic test application written in Spring Boot to examine the performance charactaristics of Managed Identity in Azure App Service. Tests can be executed by invoking an HTTP GET on the following URI path: `/container/{some-name}`. When this happens, the service will use its Managed Identity to create a new container in an Azure Storage account.

To assist with performance benchmarking, the application is instrumented with the Application Insights 3.0 agent.

## Create Azure Services

Create a resource group and add the following services:

* Application Insights
* Storage Account: Standard V2 general purpose

## Configure the application runtime

Create an `application.properties` file in `demo\src\main\resources` and add the storage account setting as shown in the sample file provided.

Next, create an `ApplicationInsights.json` file in the project root folder (demo) and populate it:

```json
{
    "instrumentationSettings": {
      "connectionString": "InstrumentationKey=[your_app_insights_key]",
      "preview": {
        "roleName": "demo-service",
        "instrumentation": {
          "micrometer": { "enabled": false },
          "logging": { "threshold": "CONFIG" }
        }
      }
    }
}
```

## Build and publish container (Docker Hub)

Build and package the service:

```bash
mvn install
docker build -t [docker_id]/testservice:0.0.1 .
```

Publish the image to Docker Hub:

```bash
docker push [docker_id]/testservice:0.0.1
```

## Deploy to App Service

In the Azure portal, create an App Service Web App and select *Docker Container* (Linux) as the publish type. In the Docker tab of the setup page, enter your container image and tag.

Next, enable the *system assigned* managed identity by navigating to the *Identity* section of the Web App.

## Grant Permissions to the Storage Account

In the Azure Portal, navigate to your storage account and grant the following role assignments to the Managed Identity: *Contributor* and *Storage Blob Data Owner*
