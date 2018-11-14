dir /s /B *.java > sources.txt
javac @sources.txt -d compiled_win
del "sources.txt"
cd compiled_win
java game.Main