package org.bissis.dm;

import org.bissis.dm.generate.ProblemGenerationConfiguration;
import org.bissis.dm.generate.TestDataGenerator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Main class for the problem instance generator for executing the tool from the command line.
 * @author Markus Ullrich
 */
public class ProblemGenerator {

    private static final String NEWLINE = System.getProperty("line.separator");
    private static final String LINE = "------------------------------------------------------------------------------------------";

    public static void main (String[] args) {
        if(args.length > 0) {
            String arg1 = args[0];
            if (arg1.equals("--config")) {
                if (args.length == 1) {
                    System.out.println("Please provide a configuration file.");
                } else {
                    String configFileName = args[1];
                    File configFile = new File(configFileName);
                    if (!configFile.exists()) {
                        System.out.println("The configuration file does not exist.");
                    } else {
                        if (configFile.isDirectory()) {
                            System.out.println("Please provide a file, not a directory.");
                        } else {
                            //Everything okay so far
                            JSONParser parser = new JSONParser();
                            try {
                                Reader is = new FileReader(configFile);
                                JSONObject jsonConfiguration = (JSONObject) parser.parse(is);
                                ProblemGenerationConfiguration problemGenerationConfiguration = new ProblemGenerationConfiguration(jsonConfiguration);
                                TestDataGenerator generator = new TestDataGenerator(problemGenerationConfiguration);
                                File outputFile = new File(configFile.getParent(), configFile.getName().replace(".json", ".csv"));
                                generator.generateAndWriteToFile(outputFile);
                            }catch (IOException | ParseException e) {
                                e.printStackTrace();
                            }

                            System.exit(0);
                        }
                    }
                }
            } else if (!(arg1.equals("-?") || arg1.equals("--help"))) {
                System.out.printf("Command %s does not exist.%s", arg1, NEWLINE);
            }
        }
        printHelp();
    }

    private static void printHelp() {
        StringBuilder helpString = new StringBuilder();
        helpString.append(LINE).append(NEWLINE);
        helpString.append("Arguments:").append(NEWLINE);
        helpString.append("-?, --help\t\t\tDisplays this help message.").append(NEWLINE);
        helpString.append("--config [Path]\t\tGenerates a problem instance using the specifications from the provided configuration file.").append(NEWLINE);
        helpString.append(LINE);
        System.out.println(helpString);
    }

}