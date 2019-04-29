@echo off
cd code
javac -cp freetts-1.2.2.jar;. *.java
java -cp freetts-1.2.2.jar;. ChatBot
del *.class
