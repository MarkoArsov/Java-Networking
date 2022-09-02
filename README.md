# Java Networking

Client-Server scenario using the TCP protocol, where the clients are able to send and receive files from a remote server and the server can support multiple clients simultaneously.
#
**TCP (Transmission Control Protocol) client** that is able to connect to a central server, send data to and read data from that server. The client initiates connection to a server specified by hostname/IP address and port number. The Client can send data to the server using an OutputStream and read data from the server using an InputStream. Separate threads are used for inbound and outbound communication with the server.
#
**TCP (Transmission Control Protocol) server** that is able to support multiple clients simultaneously. The server can create a server socket and bind it to a specific port number, listen for a connection from the client and accept it, read data from the client via an InputStream obtained from the client socket and send data to the client via the client socket’s OutputStream. 


The following commands are implemented in the server:

 - **CREATE**:  The client is able to create and send a file with .txt extension.
 - **LIST**:  When receiving this message, the server sends a list of all files with .txt extension in the current folder. The server sends filename, size and date created. Every separate file is given in a new line.
 - **DELETE**:  The client is able to delete a file. 
 - **DOWNLOAD**: The client requests a given file. After receiving this message, the server sends the specified file contents to the client. 
 - **COPY**:  The client is able to copy a file.
#
