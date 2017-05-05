#!groovy

import jenkins.model.*
import hudson.security.*

def home_dir = System.getenv("JENKINS_HOME")
def instance = Jenkins.getInstance()
def properties = new ConfigSlurper().parse(new File("$home_dir/jenkins.properties").toURI().toURL())

println "--> creating local user 'admin'"

def hudsonRealm = new HudsonPrivateSecurityRealm(false)
hudsonRealm.createAccount('admin','admin123')
instance.setSecurityRealm(hudsonRealm)

println "--> set fixed JNLP port"

instance.setSlaveAgentPort(properties.global.jnlp_port)
println "Set JNLP port to:\n ${properties.global.jnlp_port}"

def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
strategy.setAllowAnonymousRead(false)
instance.setAuthorizationStrategy(strategy)
instance.save()
