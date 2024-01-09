
These yaml files contain data values that will substitute into template file's placeholders.

Running `mvn` in the
/configdata folder will create either the environment-specific property jar file, or for local development will create a file /config/application.yaml
that Springboard will give priority to over the application's src/main/resources/application\<-env>.yaml property file.

Templates (.erb files) define placeholders with the notation:
```
<%= nameOfPlaceholder %>
```

At least one of the data/*.yaml files must contain the placeholder key with a value or the build
will fail (maven will complain about a missing key).

Remember that some values are brought in via a "shared" mechanism.  These values are protected by
RE and represent values for database usernames and passwords, etc.

default.yaml is the base data file that should contain values common to all templates.

Environment-specific yaml files should contain environment-specific values.  These values will 
override any values defined in the default.yaml file.
