## Issue Bot for GitHub issues

### Deploy

#### Deploy To ASA-E

You will need access to the `607116-2A - TNZ - Tanzu Spring EA` subscription in Azure.  The app is running under the `607116-2A - TNZ - Tanzu Spring EA` subscription and `spring-asa`
resource group.

To deploy or update the app do the following

```
$ az account list --output table                                    
Name                               CloudName    SubscriptionId                        TenantId                              State    IsDefault
---------------------------------  -----------  ------------------------------------  ------------------------------------  -------  -----------
607116-2A - TNZ - Tanzu Spring EA  AzureCloud   dc292c30-39d6-4396-994f-b78b1dd6e514  1194df16-3ae0-49aa-b48b-5c4da6e13689  Enabled  True
```

Set the subscription id to the subscription id for Spring Infra.

```
$ az account set --subscription subscrpitionid
```

Set the following environment variables

```
$ export ASAE_LOCATION=eastus
$ export ASAE_SUBSCRIPTION=subscrpition
$ export ASAE_SERVICE=spring-asa
$ export ASAE_RESOURCE_GROUP=spring-asa
```

Only need to do this when creating this step if the app doesn't already exist

```
$ az spring app create --resource-group $ASAE_RESOURCE_GROUP --service $ASAE_SERVICE --name spring-cloud-issue-bot  --env ISSUEBOT_GITHUB_CREDENTIALS_USERNAME=spring-cloud-issues ISSUEBOT_GITHUB_CREDENTIALS_PASSWORD=[ghp_spring-cloud-issue-bot from Vault]
```
Build the issue bot app

```
$ ./mvnw clean package
```
Deploy the app

```
$ az spring app deploy --resource-group $ASAE_RESOURCE_GROUP --service $ASAE_SERVICE --artifact-path target/issue-bot-0.1.0-SNAPSHOT.jar --name spring-cloud-issue-bot --runtime-version Java_17
```
To tail the logs

```
$ az spring app logs --name spring-cloud-issue-bot --resource-group $ASAE_RESOURCE_GROUP --service $ASAE_SERVICE --follow --lines 10000
```


#### Deploy To Cloud Foundry (No Longer Used)

`cf login -a https://api.sc2-04-pcf1-system.oc.vmware.com`

Set `ISSUEBOT_GITHUB_CREDENTIALS_PASSWORD` in `manifest.yaml` for the user `spring-cloud-issues`.  This should be a token stored in LastPass.

Then run `cf push` from the root of the repo.

