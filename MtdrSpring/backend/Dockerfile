FROM openjdk:11
# In order to use the same application.yaml file both in the container
# and in the IDE, we need to copy the wallet directory under /target/classes/wallet.
# This way the JDBC URL can point to jdbc:oracle:thin:@xxx?TNS_ADMIN=target/classes/wallet-->

WORKDIR /app
EXPOSE 8080


#Thinking we don't need to add the wallet here since we're using a kubernetes secret
##ADD target/classes/wallet /target/classes/wallet

## add maven dependencies

COPY wallet /app/wallet
##adding the service itself
COPY target/MyTodoList-0.0.1-SNAPSHOT.jar MyTodoList.jar
ENTRYPOINT ["java", "-jar", "MyTodoList.jar"]
