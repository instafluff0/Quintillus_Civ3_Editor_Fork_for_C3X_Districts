echo "Building: " %1
@echo off
setlocal
set CP=c:/java/lib/j2ee/activation.jar:c:/java/lib/j2ee/common-annotations.jar:c:/java/lib/j2ee/connector.jar:
c:/java/lib/j2ee/ejb-api.jar:c:/java/lib/j2ee/el-api.jar:c:/java/lib/j2ee/jaxrpc.jar:c:/java/lib/j2ee/jms.jar:
c:/java/lib/j2ee/jsf-api.jar:c:/java/lib/j2ee/jsp-api.jar:c:/java/lib/j2ee/jstl.jar:c:/java/lib/j2ee/jta.jar:
c:/java/lib/j2ee/mail.jar:c:/java/lib/j2ee/persistence.jar:c:/java/lib/j2ee/rowset.jar:
c:/java/lib/j2ee/servlet-api.jar
ant -Dj2ee.platform.classpath=%CP% %1