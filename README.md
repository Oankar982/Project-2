# Project-2
Computer Network Project
Chris Laney, Seamus Collins, Oankar Ghorpade

Steps to enjoy chat room

1) Start running server
    -navigate to the project folder from terminal then type java -jar Project-2-Server.jar
2) Start running client
    - navigate to the project folder from a new terminal then type java -jar Project-2-Client.jar - this is the terminal you will run client commands from
    - understand that the IP for the server is within your wifi settings -> wifi connection -> IPV4
    - type "%connect <IPV4 address> 12345" (we used port 12345, this can be changed in server)

After entering address you will be prompted for username 

3) Enter username
    - Once entering username, a list of the current users will be printed and it will print that your name joined the server, it will also print available groups to join

Currently you are in the main chat room, from here you can stay at utilize the main chatroom. The commands for the main room are; 

    POST; the format for this command is "%post <subject>~<message body>" then press enter

    LIST OF USERS; the format for this command is "%users" then press enter

    SEARCH FOR MESSAGE; the format for this command is "%message <msgID>" then press enter

    EXIT SERVER; the format for this "%exit" then press enter

Within this server there are also groups a user can join, in order to utilize the group commands a user first needs to join a group. A user can join multiple groups but can only post to one at a time post

    LIST GROUPS; the format for this command is "%groups" then press enter

    JOIN A GROUP; the format for this command is "%groupjoin <group number OR name>" then press enter

    GROUP POST; the format for this command is "%grouppost <group number> <subject>~<message body>" then press enter

    LIST GROUP USERS; the format for this command is "%groupusers" then press enter

    SEARCH GROUP MESSAGE; the format for this command is "%groupmessage <group number>.<message number>"
        - example "%groupmessage 2.3" this will find message 3 from group 2

    LEAVE A GROUP; the format for this command is "%groupleave <group number>"




