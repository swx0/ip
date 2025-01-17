package duke.storage;

import duke.exception.InvalidInputException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents a storage for user's task in hard disk.
 * A Storage object has associated methods to load and update tasks.
 */
public class Storage {

    private ArrayList<String> list = new ArrayList<>();

    public Storage() {

    }

    private String getTask(int taskNum) {
        return this.list.get(taskNum - 1);
    }

    public void addTask(String message) {
        this.list.add("unmark " + message);

        updateStorage();
    }

    /**
     * Updates status of task from the task list.
     *
     * @param message Contents of message.
     */
    public void markTask(String message) {
        String[] messageSplit = message.split(" ");
        assert messageSplit.length >= 2;
        int taskNum = Integer.parseInt(messageSplit[1]);
        String markStatus = messageSplit[0];

        String task = this.getTask(taskNum);
        String[] taskSplit = task.split(" ", 2);

        int taskIndex = taskNum - 1;
        String updatedTask = String.format("%s %s", markStatus, taskSplit[1]);
        this.list.set(taskIndex, updatedTask);

        updateStorage();
    }

    /**
     * Removes task from the task list.
     *
     * @param message Contents of message.
     */
    public void deleteTask(String message) {
        String[] messageSplit = message.split(" ");
        assert messageSplit.length >= 2;
        int taskNum = Integer.parseInt(messageSplit[1]);

        // remove task from arraylist
        list.remove(taskNum - 1);

        updateStorage();
    }

    private void updateStorage() {
        Path file = Paths.get("data/temp.txt");

        try {
            Files.write(file, this.list, Charset.defaultCharset());
        } catch (IOException e) {
            System.out.println(e);
        }

        try {
            Files.move(Paths.get("data/temp.txt"),
                    Paths.get("data/tasklist.txt"),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    /**
     * Returns array list of saved tasks from disk.
     *
     * @return ArrayList object consisting of the string representation for saved tasks.
     */
    public ArrayList<String> load() {
        File taskFile = new File("data/tasklist.txt");

        if (!taskFile.exists()) {
            new File("data").mkdirs();
            try {
                taskFile.createNewFile();
            } catch (IOException err) {
                System.out.println(err);
            }
            return this.list;
        }

        try {
            Scanner s = new Scanner(taskFile);
            while (s.hasNext()) {
                this.list.add(s.nextLine());
            }
            s.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }

        return this.list;


    }


}

