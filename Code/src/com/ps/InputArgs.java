package com.ps;


import com.ps.algorithms.Algorithm;
import com.ps.enums.AlgorithmType;
import org.apache.commons.cli.*;

import java.util.Objects;

public class InputArgs {

    protected final String fileName;
    protected final int cutoffTimeSec;
    protected final int randomSeed;
    protected final AlgorithmType algorithmType;

    //public InputArgs(String fileName, int cutoffTimeSec, int randomSeed, AlgorithmType algorithmType) {
    public InputArgs(String[] cmdLineArgs) {

        CommandLine cmd = parseArgs(cmdLineArgs);

        this.fileName = cmd.getOptionValue("fileName");
        this.cutoffTimeSec = Integer.parseInt(cmd.getOptionValue("time"));
        this.randomSeed = Integer.parseInt(cmd.getOptionValue("seed"));
        this.algorithmType = Objects.equals(cmd.getOptionValue("algorithm"), "LS1") ? AlgorithmType.LocalSearch1 :
                AlgorithmType.LocalSearch2;
    }

    public String getFileName() { return fileName; }
    public int getCutoffTimeSec() { return cutoffTimeSec; }
    public int getRandomSeed() { return randomSeed; }
    public AlgorithmType getAlgorithmType() { return algorithmType; }

    public String getFileNameBase() {
        return fileName + "_" +
                algorithmType.toString() + "_" +
                String.valueOf(cutoffTimeSec) + "_" +
                String.valueOf(randomSeed);
    }

    private CommandLine parseArgs(String[] args) {
        Options options = new Options();
        //exec -inst <filename> -alg [BnB|Approx|LS1|LS2] -time <cutoff in seconds> -seed <random seed>

        Option input1 = new Option("inst", "fileName", true, "input file path");
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
