mkdir bin
javac -d bin src/com/usc/social/network/graph/*.java src/com/usc/social/network/search/*.java src/com/usc/social/network/search/bfs/*.java src/com/usc/social/network/search/dfs/*.java src/com/usc/social/network/search/uniform/cost/*.java src/com/usc/social/network/search/uniform/risk/*.java src/com/usc/social/network/main/*.java

#Execute the program
java -cp bin com/usc/social/network/main/Main ./social-network.txt ./out

