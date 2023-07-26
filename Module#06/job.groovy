job('build_job') {
        scm {
          git('https://github.com/Fat-Frumos/Lab.git')
        }
        triggers {
          scm('H/5 * * * *')
        }
        steps {
          shell('mvn verify -e')
          shell('mvn -B package --file pom.xml')
          shell('mvn sonar:sonar')
        }
      }