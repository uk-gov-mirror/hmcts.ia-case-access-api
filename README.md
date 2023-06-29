# ia-case-access-api
Service to access CCD case data for IAC case types

### Background
There is a business need to query CCD case data for a reconciliation purpose.
The reconciliation process is requested by Liberata where there is a need to provide additional information (surname) by IAC Team.
There is a designed solution where IAC application will request case data querying CCD Elastic Search endpoint.
Exposed IAC endpoint will be generic and easy to extend if additional information is needed in the future.
`ia-case-access-api` allows accessing CCD case data information.

### Testing application
Unit tests and code style checks:
```
./gradlew build
```

Integration tests use Wiremock and Spring MockMvc framework:
```
./gradlew integration
```

Functional tests use started application instance:
```
./gradlew functional
```

### Running application

`ia-case-access-api` is common Spring Boot application. Command to run:
```
./gradlew bootRun
```

### Usage
API details about usages and error statuses are placed in [Swagger UI](http://ia-case-access-api-aat.service.core-compute-aat.internal/swagger-ui.html)


### Implementation
`ia-case-access-api` has a single POST endpoint called `supplementary-details`. The payload is a list of CCD Case Ids.

Authentication is defined as S2S `ServiceAuthorization` token.


## Adding Git Conventions

### Include the git conventions.
* Make sure your git version is at least 2.9 using the `git --version` command
* Run the following command:
```
git config --local core.hooksPath .git-config/hooks
```
Once the above is done, you will be required to follow specific conventions for your commit messages and branch names.

If you violate a convention, the git error message will report clearly the convention you should follow and provide
additional information where necessary.

*Optional:*
* Install this plugin in Chrome: https://github.com/refined-github/refined-github

  It will automatically set the title for new PRs according to the first commit message, so you won't have to change it manually.

  Note that it will also alter other behaviours in GitHub. Hopefully these will also be improvements to you.

*In case of problems*

1. Get in touch with your Technical Lead and inform them, so they can adjust the git hooks accordingly
2. Instruct IntelliJ not to use Git Hooks for that commit or use git's `--no-verify` option if you are using the command-line
3. If the rare eventuality that the above is not possible, you can disable enforcement of conventions using the following command

   `git config --local --unset core.hooksPath`

   Still, you shouldn't be doing it so make sure you get in touch with a Technical Lead soon afterwards.


## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
