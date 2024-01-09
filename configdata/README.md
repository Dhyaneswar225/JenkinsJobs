
It is required that each service execute with externalized configuration.  The intent is to build:
* an executable jar that is environment agnostic
* a set of configuration jars that are environment specific (dev through prod)

This enables the executable to be distributable and paired with the correct config jar.

The environment-specific yaml and property files within the configuration jar will be extracted
into a `./config` directory which the service loads at runtime. It will be used
in addition to any other configuration sources, such as a `src/main/resources/application.yml`
file or command line arguments. See the [Spring Boot documentation][boot-docs-config] for details.

This process is controlled by the Release Engineering (RE) team and this README will attempt to 
explain how it all works and what you need to do.

* Maven
* Ruby


Note: this is generally not executed as part of the parent maven build. If you want to build environment-specific
configuration to run locally, you would do that separately

```
// generates dev config by default
mvn
```

For a specific environment
```
mvn -Dbuild.env=<env>
mvn -Dbuild.env=beta
```


Springboard provides a convenient parent pom, "core.configdata-parent", which defines everything
necessary to successfully generate the environment specific config jars.  This parent pom is 
wrapping a ruby gem which RE provides and does the heavy lifting to generate the finished
populated properties and yaml files which are then packaged into the config jars.


1. Define where to install the ruby gem from.

    ```bash
    gem sources -a https://artifact.internal.shutterfly.com/artifactory/gems-local/
    ```
    
2.  Install template-translator ruby gem.

    ```bash
    gem install template-translator
    ```

**Note**: you may need to be root to do this and you need to have ruby installed.

The gem takes templates (`templates/*.erb` files) and populates the placeholders with values from the .yaml files
in the data directory.  It does so by overlaying a hierarchy of values starting with "default" and moving
up to the desired environment ("dev", "beta", "stage", "prod").  In addition, the gem
allows a "shared" resource to be brought in and overlaid as well.  This is where we pull in the 
various protected resources like database usernames and passwords.  These values are protected by 
RE.

This file is also provided to the ruby gem and defines the "from" and "to" data needed.  The "from" 
is where the gem should look for templates and the "to" is where the populated file should be
written to.

The following tells the gem to find the templates/application.yaml.erb template, replace its 
placeholders with real values and write the resulting file to ../config/application.yaml
```
templates/application.yaml.erb: ../config/application.yaml
```

[View template README here](templates/)

[View data README here](data/)

RE controls the perforce repository of shared data (db username/passwords/etc).  We have created 
a source jar which is available in the artifactory and available to maven at runtime which contains 
all of the raw data yaml files.  Maven will pull down this source jar, unpack it in the target
directory and make all of the data yaml files available to the ruby gem.  This allows the developer
to not have to worry about mapping to a perforce directory via symlinks making the onboarding 
process quicker.

Note that the source jar is read-only.  If any changes need to be made or added, you will have to 
install perforce, create a client spec and push the changes via rsubmit.


[boot-docs-config]: http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/html/boot-features-external-config.html
