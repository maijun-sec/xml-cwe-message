# xml-cwe-message

used to extract some messages from cwe xml file and insert all messages into database.

Now support all weaknesses, categories and views.

## cwe

https://cwe.mitre.org/.

In the program, will extract message from cwe 4.6. all cwe documents is located under `doc/` folder.

## usage

1. If want to insert the mmessage into other database, you can change the database message in the main.kt
2. If want to extract some other message, can change data.kt for the basic data structure and cwe.kt.
