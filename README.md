## Issue Bot for GitHub issues

### Deploy

`cf login -a https://api.sc2-04-pcf1-system.oc.vmware.com`

Set `ISSUEBOT_GITHUB_CREDENTIALS_PASSWORD` in `manifest.yaml` for the user `spring-cloud-issues`.  This should be a token stored in LastPass.

Then run `cf push` from the root of the repo.