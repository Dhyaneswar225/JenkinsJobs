
Any file suffixed with .erb is considered a template and will be processed as such.  Templates can
be `*.properties.erb` or `*.yaml.erb`


Placeholders are defined as 

```
<%= nameOfPlaceholder %>
```

When processed, the `nameOfPlaceholder` key will be searched for in the data yaml files.  When found, 
the whole tag will be replaced the proper value.

Placeholders are replaced based on the order found, which each subsequent layer taking precedence:
* `shared/default.yaml`
* `shared/<env>.yaml`
* `data/default.yaml`
* `data/<env>.yaml`

The deployConfig directory contains a special file used by RE during deploy time.  The name of the 
file must match the service being deployed (based on the name in the pom).  This file contains
properties such as JVM memory options, garbage collection options and which ports to start the
service on.  This file is not a yaml or properties file, but uses unix environment notation.

This file is a template to allow for environment specific settings.  We leverage it to supply varying 
memory sizes depending on environment as well as which ports to start on.
