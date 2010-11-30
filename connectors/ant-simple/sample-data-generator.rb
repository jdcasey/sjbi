#!/usr/bin/env ruby

require 'rubygems'
require 'yaml'
require 'json/pure'

include YAML

DATA = {
  'buildFile' => 'build.xml',
  'mappings' => [
    'CLEAN' => 'clean',
    'PACKAGE' => 'build',
    'VERIFY' => 'dist',
   ],
  'projects' => [
    {
      'coord' => 'org.apache.maven:maven-artifact:3.0',
      'pom' => 'maven-artifact/pom.xml',
      'artifacts' => [
        'jar' => 'target/maven-artifact-3.0.jar',
        'jar:bin' => 'target/maven-artifact-3.0-bin.jar',
        'zip:bin' => 'target/maven-artifact-3.0-bin.zip',
        'tar.gz:bin' => 'target/maven-artifact-3.0-bin.tar.gz',
      ]
    },
    {
      'coord' => 'org.apache.maven:maven-core:3.0',
      'pom' => 'maven-core/pom.xml',
      'artifacts' => [
        'jar' => 'target/maven-core-3.0.jar',
        'jar:bin' => 'target/maven-core-3.0-bin.jar',
        'zip:bin' => 'target/maven-core-3.0-bin.zip',
        'tar.gz:bin' => 'target/maven-core-3.0-bin.tar.gz',
      ]
    },
    {
      'coord' => 'org.apache.maven:maven-compat:3.0',
      'pom' => 'maven-compat/pom.xml',
      'artifacts' => [
        'jar' => 'target/maven-compat-3.0.jar',
        'jar:bin' => 'target/maven-compat-3.0-bin.jar',
        'zip:bin' => 'target/maven-compat-3.0-bin.zip',
        'tar.gz:bin' => 'target/maven-compat-3.0-bin.tar.gz',
      ]
    },
  ]
}

# puts DATA.to_yaml
puts JSON.pretty_generate( DATA )