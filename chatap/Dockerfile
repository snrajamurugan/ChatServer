FROM paasmule/java-maven-git-alpine
RUN mkdir -p /var/chat
RUN  cd /var/chat/ && git clone https://github.com/snrajamurugan/ChatServer.git && cd ChatServer/
RUN cd /var/chat/ChatServer && mvn clean install
CMD cd /var/chat/ChatServer ; mvn exec:java -Dexec.mainClass="com.chatapp.server.Server"