git pull
classpath="src/:lib/hamcrest-core-1.3.jar:lib/junit-4.12.jar:classes/"

run_to_file () {
    echo "Compiling for and running ${2}..."
    javac -cp $classpath -d classes/ ${1} > compile_${2}.log 2>&1
    java -cp $classpath org.junit.runner.JUnitCore ${2} > output_${2}.log 2>&1
}

filesystem="src/FileData.java src/FileSystem.java"
filesystem_test="src/FileSystemTest.java"

bst="src/BST.java src/DefaultMap.java"
bst_test="src/BSTTest.java"
run_to_file "${bst} ${bst_test}" "BSTTest"
run_to_file "${filesystem} ${bst} ${filesystem_test}" "FileSystemTest"

# javac -cp $classpath -d classes/ src/*.java
# # javac -cp $classpath -d classes/ src/FileSystemTest.java
# java -cp $classpath org.junit.runner.JUnitCore FileSystemTest
