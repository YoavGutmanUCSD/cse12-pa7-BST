classpath="src/:lib/hamcrest-core-1.3.jar:lib/junit-4.12.jar:classes/"

run_to_file (files, toRun) {
    echo "Compiling for and running ${toRun}..."
    javac -cp $classpath -d classes/ src/${files}.java > ${toRun}_compile.log 2>&1
    java -cp $classpath org.junit.runner.JUnitCore ${toRun} > ${toRun}_output.log 2>&1
}

filesystem="src/FileData.java src/FileSystem.java"
filesystem_test="src/FileSystemTest.java"

bst="src/BST.java src/DefaultMap"
bst_test="src/BSTTest.java"
run_to_file($bst$bst_test, "BSTTest.java")
run_to_file($filesystem$bst$filesystem_test, "FileSystemTest.java")

# javac -cp $classpath -d classes/ src/*.java
# # javac -cp $classpath -d classes/ src/FileSystemTest.java
# java -cp $classpath org.junit.runner.JUnitCore FileSystemTest
