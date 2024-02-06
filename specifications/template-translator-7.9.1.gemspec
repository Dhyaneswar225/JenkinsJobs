# -*- encoding: utf-8 -*-
# stub: template-translator 7.9.1 ruby lib

Gem::Specification.new do |s|
  s.name = "template-translator".freeze
  s.version = "7.9.1"

  s.required_rubygems_version = Gem::Requirement.new(">= 0".freeze) if s.respond_to? :required_rubygems_version=
  s.require_paths = ["lib".freeze]
  s.authors = ["Bruce Freeman".freeze, "Nicole Sparenga".freeze]
  s.date = "2021-05-26"
  s.description = "Initialization takes a project-specific filepath location (to look for variable values), an environment string (to aid in the discovery of proper variable values), and a YAML file containing information that maps source ERB files to destination files. The main function of this Gem is to copy the text portions of the ERB template directly to the generated document while processing code identified by marker tags. These tags will be replaced by values found in the discovered YAML files. A specific file hierarchy and provided location will be used to determine where these YAML data files reside.".freeze
  s.email = "engr-re@shutterfly.com".freeze
  s.executables = ["configTemplate".freeze, "mkserviceconfig".freeze]
  s.files = ["bin/configTemplate".freeze, "bin/mkserviceconfig".freeze]
  s.homepage = "https://github.com/sflyinc-shutterfly/re-library/tree/master/ruby/translatorGem".freeze
  s.rubygems_version = "3.0.3.1".freeze
  s.summary = "Convert a template ERB based file into a proper file using a provided file hierarchy to search for values.".freeze

  s.installed_by_version = "3.0.3.1" if s.respond_to? :installed_by_version
end
