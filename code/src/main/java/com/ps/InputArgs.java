package com.ps;

/*
This file contains logic for parsing the command-line arguments.
 */

import com.ps.enums.AlgorithmType;
import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;

import java.util.Objects;

public class InputArgs {

    private final String filePath;
    private final String instanceName;
    private final int cutoffTimeSec;
    private final int randomSeed;
    private final AlgorithmType algorithmType;

    public InputArgs(String[] cmdLineArgs) {

        CommandLine cmd = parseArgs(cmdLineArgs);

        this.filePath = cmd.getOptionValue("filePath");
        this.instanceName =  FilenameUtils.getBaseName(cmd.getOptionValue("filePath"));
        this.cutoffTimeSec = Integer.parseInt(cmd.getOptionValue("time"));
        this.randomSeed = Integer.parseInt(cmd.getOptionValue("seed"));
        this.algorithmType = Objects.equals(cmd.getOptionValue("algorithm"), "LS1") ? AlgorithmType.LocalSearch1 :
                AlgorithmType.LocalSearch2;
    }

    public String getFilePath() { return filePath; }
    public String getInstanceName() { return instanceName; }
    public int getCutoffTimeSec() { return cutoffTimeSec; }
    public int getRandomSeed() { return randomSeed; }
    public AlgorithmType getAlgorithmType() { return algorithmType; }

    public String getFileNameBase() {
        return instanceName + "_" +
                algorithmType.toString() + "_" +
                String.valueOf(cutoffTimeSec) + "_" +
                String.valueOf(randomSeed);
    }

    private CommandLine parseArgs(String[] args) {
        Options options = new Options();
        //exec -inst <filename> -alg [BnB|Approx|LS1|LS2] -time <cutoff in seconds> -seed <random seed>

        Option input1 = new Option("inst", "filePath", true, "input file path");
        input1.setRequired(true);
        options.addOption(input1);

        Option input2 = new Option("alg", "algorithm", true, "algorithm to use");
        input2.setRequired(true);
        options.addOption(input2);

        Option input3 = new Option("time", "time", true, "time cutoff in sec");
        input3.setRequired(true);
        options.addOption(input3);

        Option input4 = new Option("seed", "seed", true, "random seed");
        input4.setRequired(true);
        options.addOption(input4);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            return cmd;
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
            return null;
        }
    }
}
