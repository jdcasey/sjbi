build: build.xml
mappings {
  CLEAN: 'clean',
  PACKAGE; 'build',
  VERIFY: 'dist',
  INSTALL: 'install',
}
projects {
  'org.apache.maven:maven-artifact:3.0': [
    pom: 'maven-artifact/pom.xml',
    artifacts: [
      'jar': 'target/maven-artifact-3.0.jar',
      'jar:bin': 'target/maven-artifact-3.0-bin.jar'
    ]
  ],
  
]