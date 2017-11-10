import os
from glob import glob
from subprocess import call

if __name__ == "__main__":
    data_files = [os.path.basename(x) for x in glob("Data/*")]
    algorithms = ["LS1", "LS2"]
    num_random_seeds = 10
    time_limit_sec = 600
    classPath = "Main.jar:lib/commons-cli-1.4.jar:lib/jgrapht-core-1.0.1.jar"
    file_name = "out.txt"

    for file in data_files:
        for algorithm in algorithms:
            for seed in range(1,num_random_seeds + 1):
                args = "-inst {} -alg {} -time {} -seed {}".format(file, algorithm, time_limit_sec, seed)
                cmd = "java -cp {} com.ps.Main {}".format(classPath, args)

                txt = "Running {}\n".format(args)
                with (file_name, "w") as file:
                    file.write(txt + "\n")
                print(txt)
                
                returnCode = call(cmd, shell=True, cwd="./Code/")

                if returnCode != 0:
                    txt = "*"*80 +"\nError on " + args + "\n" + "*"*80
                    with (file_name, "w") as file:
                        file.write(txt + "\n")
                    print(txt)
    txt = "Done!"
    with (file_name, "w") as file:
        file.write(txt + "\n")
    print(txt)
