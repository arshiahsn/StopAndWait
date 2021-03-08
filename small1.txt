
GzipServer

Assignment 1
CPSC 441

Author: Majid Ghaderi
Email: mghaderi@ucalgary.ca

 
Running the server:
==============
Use the command line as follows

    java -jar gzipserver.jar 

To stop the server, type "quit".

The above command launches the server with a set of default parameters. 
The following options can be used to pass different command line arguments to the server:

	-v 	To specify the verbosity level of the server. 
		Available values are "all", "info" and "off".
		Passing "-v all" generates the most amount of logging messages, 
		while "-v off" turns off output messages completely.
		Default is "all".
			
	-b 	To specify the buffer size used for read/write operations.
		Default is 1000 bytes.

	-p 	To specify the server port number.
		It can be any integer greater than 1024, less than 64K.
		Default is 2025.
			