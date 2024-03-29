package commands;

import util.CollectionManager;
import util.IO;
import util.Invoker;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 *  Command 'Execute File Script'. Reads and executes script from the written file.
 */

public class ExecuteFileScriptCommand extends Command {
    private static HashMap<String, Boolean> executeFiles = new HashMap<>();
    private final CollectionManager collectionManager;
    private String fileName;
    public ExecuteFileScriptCommand(String fileName, CollectionManager collectionManager){
        super("execute_script file_name", "read and execute script from the written file");
        this.collectionManager = collectionManager;
        this.fileName = fileName;
    }

    @Override
    public void execute(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(args[0])));
            String line;
            Invoker invoker = Invoker.getInstance(fileName, new CollectionManager());

            if (executeFiles.containsKey(args[0])) {
                System.out.println("Recursion of files execution");
                return;
            }
            executeFiles.put(args[0], true);
            IO.changeMod(args[0]);
            while ((line = reader.readLine()) != null) {
                String[] words = line.trim().split("\\s+");
                String command = words[0];
                String[] argv = Arrays.copyOfRange(words, 1, words.length);
                invoker.execute(command, argv);
            }
            IO.changeMod("");
        }
        catch (IndexOutOfBoundsException e){
            System.out.println("no argument");

        }
        catch (FileNotFoundException e){
            System.out.println("file not found");

        }
        catch (IOException e){
            System.out.println("not enough access rights");
        }
        executeFiles.clear();
    }
}
