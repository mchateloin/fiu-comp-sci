This is a project I did for a class this semester. We had to build a **very limited** FTP server that supported a small subset of commands from the FTP protocol. It also only runs in active mode.

But hey, it was fun working on this thing and it reintroduced me to Python, so I thought I'd put it on here.

Running
==========

Open up a terminal and go to the project directory.

Run the following line:

	py server.py <port> <address>


The optional second argument allows you to specify the IP number. Without arguments, the default port will always be 21
and the default IP address will always be  127.0.0.1.

Your output should start out something like this:

	> py server.py 21
	[2015-02-18T23:18:15] Starting FTP server at port 21
	[2015-02-18T23:18:15] Serving at 127.0.0.1:21
	[2015-02-18T23:18:15] Waiting...

	
The root directory for clients will end up being the directory where the server is run.

Project structure
==========

config.json:
	Configuration file that defines some defaults, as well as all our reply codes. Codes include templates  for
	regularly served messages. Not all of the codes are used though, as we haven't implemented all the commands.
	Reply codes are taken directly from RFC959: https://tools.ietf.org/html/rfc959"""
	And no, professor, I don't think a configuration file is excessive for this project. Do you know how frustrating
	it was to keep track of ALL the reply codes? :)

config.py:
	Loads configuration file. Makes it available globally.

utils.py:
	Some basic helper functions used throughout the project.

user.py:
	Maintains the state for a connected client. Includes credentials as well as connection information.

server.py:
	The star of the show. Refer to running instructions above to drive this bad boy.