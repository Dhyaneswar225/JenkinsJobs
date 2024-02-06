#!/usr/bin/ruby
# Generate configuration files from ERB templates using data hierarchy
# based on http://findingscience.com/linux/sysadmin/ruby/2010/10/27/config-template-class.html
# Overlay is equivalent of dev.env in Puppet configuration

require 'erb'
require 'yaml'
require 'fileutils'
require 'find'
require 'json'

# Add support for hashes in format strings to be compatible with Ruby v1.8
if RUBY_VERSION < '1.9.2'
  class String
    old_format = instance_method(:%)

    define_method(:%) do |arg|
      if arg.is_a?(Hash)
        self.gsub(/%\{(.*?)\}/) { arg[$1.to_sym] }
      else
        old_format.bind(self).call(arg)
      end
    end
  end
end

# ------------------------------------------------------
# :section: Struct
# ------------------------------------------------------
class Struct
  def get_binding
    binding
  end
end

# ------------------------------------------------------
# :section: ConfigTemplate
# ------------------------------------------------------
class ConfigTemplate

  @@setup = false

  # ------------------------------------------------------
  # call-seq:
  # initialize(hiera_vars, local, verbose, drop_secrets, secret_val, dump)
  # ------------------------------------------------------
  def initialize(hiera_vars, local=true, verbose=false, drop_secrets=false, secret_val=nil, dump=nil)
    if verbose
      STDERR.puts "Initializing with the following hiera variables:"
      hiera_vars.each do |k,v|
        STDERR.puts "#{k}: #{v}"
      end
    end

    @@verbose = verbose
    @@drop_secrets = drop_secrets
    # Only need binding once for all instances
    if not @@setup

      if File.file? "#{hiera_vars[:service_loc]}/hiera.yaml"
        hiera_file = "#{hiera_vars[:service_loc]}/hiera.yaml"
      elsif File.file? "#{hiera_vars[:common_loc]}/hiera.yaml"
        hiera_file = "#{hiera_vars[:common_loc]}/hiera.yaml"
      else
        abort "ERROR: hiera.yaml file not found"
      end
      hier = YAML.load_file(hiera_file)
      if (!local)
        hier = hier.delete_if { |path| path =~ /\/local$/ }
      end
      conf = {}
      hier.reverse_each do |f|
        yfile = mkfilename(f, hiera_vars)
        if yfile
          yaml = YAML.load_file(yfile)
          if yaml
            conf = conf.merge(yaml)
          end
          STDERR.puts "loading #{yfile}" if verbose
        end
      end
      if dump
        if dump == '-'
          f = $stdout
        else
          f = open(dump, 'w')
        end
        conf.keys.sort.each { |k| f.puts "#{k}: #{conf[k]}" }
        exit
      end
      # see if any secrets present in values
      cred = conf.inject([]) { |res, kv| res << kv if kv[1] =~ /^secret:\/\//; res }
      if cred.length > 0
        if drop_secrets
          cred.each { |k, v| conf[k] = secret_val } if secret_val
        elsif ENV['thycotic_api_username'] and ENV['thycotic_api_password']
          cred.each do |k, v|
            query_str = v.sub('secret://', '')
            fields = query_str.split('/')
            if fields.length == 1
              cred_value = query_thycotic_old(query_str, ENV['thycotic_api_username'], ENV['thycotic_api_password'])
            elsif fields.length > 1
              fields = query_str.split('/')
              field = fields.pop
              template = fields.pop
              secret = '\\' + fields.join('\\')
              cred_value = query_thycotic(secret, ENV['thycotic_api_username'], ENV['thycotic_api_password'], template, field)
            end
            conf[k] = cred_value
          end
        else
          abort "ERROR: Can't access Thycotic server. Environment variables thycotic_api_username and thycotic_api_password must be defined!"
        end
      end
      @@binding = make_binding(conf)
      @@setup = true
    end
  end

  # ------------------------------------------------------
  # call-seq:
  # convert_list(yaml)
  # ------------------------------------------------------
  def convert_list(yaml)
    mapping = YAML.load_file(yaml)
    mapping.each_pair do |dest, src|
      if src.is_a?(Array)
        convert_file(src.shift, dest, 'w')
        src.each { | f | convert_file(f, dest, 'a') }
      elsif dest =~ /\.erb$/
        # old mapping file format with src: dest
        convert_file(dest, src, 'w')
      else
        convert_file(src, dest, 'w')
      end
    end
  end

  # ------------------------------------------------------
  # call-seq:
  # convert_directory(loc)
  # ------------------------------------------------------
  def convert_directory(loc)
    # Traverse provided location recursively
    Find.find(loc) do |src|
      if File.readable? src
        # Convert any ERB files found
        if File.extname(src) == '.erb'
          # Determine XML name (strip off .erb)
          dest = File.dirname(src) + File::SEPARATOR + File.basename(src, ".erb")
          convert_file(src, dest, 'w')
        end
      else
        abort "ERROR: Can't open template file: " + src
      end
    end
  end

  # ------------------------------------------------------
  # call-seq:
  # convert_file(infile, outfile, mode)
  # ------------------------------------------------------
  def convert_file(infile, outfile, mode)
    STDERR.puts "Converting #{infile} to #{outfile}" if @@verbose
    abort "ERROR: Can't open template file: " + infile unless File.readable? infile
    erbtemplate = File.open(infile).read
    # strip out Puppet scope.function_hiera() calls if present
    # replace scope.lookupvar('::sfsite') with just a reference to sfsite (assumed defined in data)
    erbtemplate = erbtemplate.gsub(/scope\.function_hiera\("([^"]+)"\)/, '\1').gsub(/scope\.lookupvar\('::sfsite'\)/, 'sfsite')
    e = ERB.new(erbtemplate, nil, '-')
    FileUtils.mkdir_p(File.dirname(outfile))
    if @@drop_secrets
      content = e.result(@@binding).split(/\n/, -1).reject {|l| /secret:\/\// =~ l }.join("\n")
    else
      content = e.result(@@binding)
    end
    File.open(outfile, mode) { |f| f.write(content) }
  end

  # ------------------------------------------------------
  # *private*
  # call-seq:
  # make_binding(env)
  # ------------------------------------------------------
  private
  def make_binding(env)
    klass = Struct.new *env.keys.map { |k| k.intern }
    k = klass.new *env.values
    k.get_binding
  end

  # ------------------------------------------------------
  # call-seq:
  # mkfilename(f, hiera_vars)
  #
  # Take filename, replace variables defined in hiera_vars and make into absolute path
  # ------------------------------------------------------
  def mkfilename(f, hiera_vars)
    datafile = f % hiera_vars + ".yaml"
    STDERR.puts 'Assembling filename: ' + datafile if @@verbose

    if File.file? datafile
      datafile
    else
      false # file doesn't exist
    end
  end

  # ------------------------------------------------------
  # call-seq:
  # query_thycotic(secret, tuser, tpass, template, field)
  # ------------------------------------------------------
  def query_thycotic(secret, tuser, tpass, template, field)
    secret_value = `queryThycoticSecret.pl --tusername '#{tuser}' --tpassword '#{tpass}' --secret '#{secret}' --template '#{template}' --field '#{field}'`
    if $?.exitstatus == 0
      json_str = JSON.parse secret_value
      secret_value = json_str[field]
      if (secret_value.to_s.empty?)
        abort("ERROR: Could not find #{secret} in Thycotic")
      end
    else
      puts secret_value
      abort("ERROR: queryThycoticSecret.pl failed with status #{$?.exitstatus}")
    end
    return secret_value
  end

  # ------------------------------------------------------
  # call-seq:
  # query_thycotic_old(secret, tuser, tpass)
  # ------------------------------------------------------
  def query_thycotic_old(secret, tuser, tpass)
    secret_value = `cred_query.pl -tusername #{tuser} -tpassword '#{tpass}' -searchtext #{secret}`.split(' ')[1]
    abort "ERROR: Could not find #{secret} in Thycotic" unless $?.success?
    return secret_value
  end

end
