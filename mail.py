import sys
from socket import *

mailserver = 'smtp.bu.edu'
port = 25
clientSocket = socket(AF_INET, SOCK_STREAM)
clientSocket.connect((mailserver, port))	
recv = clientSocket.recv(1024)
print recv
if recv[:3] != '220':
	print '220 reply not received from server.'
heloCommand = 'HELO ME \r\n'
clientSocket.send(heloCommand)
recv1 = clientSocket.recv(1024)
print recv1
if recv1[:3] != '250':
	print '250 reply not received from server.'
mail_from = 'beaglebone@root.com'
mail_from = 'MAIL FROM:< ' + mail_from + '>\r\n'
clientSocket.send(mail_from)	
recv2 = clientSocket.recv(1024)
print recv2
rcpt_to = 'FireScoutLogs@yahoo.com' 
rcpt_to = 'RCPT TO:<' + rcpt_to + '>\r\n'
print rcpt_to
clientSocket.send(rcpt_to)
recv1 = clientSocket.recv(1024)
print recv1
clientSocket.send('DATA\r\n')
recv1 = clientSocket.recv(1024)
print recv1

version = str(sys.argv[1]);
filename = str(sys.argv[2]);

message = 'Subject: RUN {0} - {1}\r\n'.format(version, filename)
data = ''
print message
with open (filename, "r") as fi:
	data = fi.read().replace('\n','\r\n')
message += data
#print message
clientSocket.send(message)
clientSocket.send('\r\n.\r\n')

recv1 = clientSocket.recv(1024)
print recv1
clientSocket.send("QUIT\r\n")
recv1 = clientSocket.recv(1024)
print recv1
clientSocket.close()