import os
from glob import glob
from subprocess import call
import random

if __name__ == "__main__":
    random.seed(5763424)
    
    data_files = [os.path.abspath(x) for x in glob("./Data/*")]
    algorithms = ["LS1", "LS2"]
    num_random_seeds_min = 10
    num_random_seeds_max = 100
    time_limit_sec = 600
    file_name = "out.txt"

    if os.path.isfile(file_name):
        os.remove(file_name)

    for data_file in data_files:
        if 'star2' in data_file or 'power' in data_file:
            num_random_seeds = num_random_seeds_max
        else:
            num_random_seeds = num_random_seeds_min
        for algorithm in algorithms:
            for i in range(1,num_random_seeds + 1):
                seed = int(random.randrange(0,1e5))
                args = "-inst {} -alg {} -time {} -seed {}".format(data_file, algorithm, time_limit_sec, seed)
                cmd = "java -jar Main-1.0-SNAPSHOT.jar {}".format(args)

                txt = "Running {}".format(args)
                with open(file_name, "a") as file:
                    file.write(txt + "\n")
                print(txt)

                returnCode = call(cmd, shell=True, cwd="./code/")

                if returnCode != 0:
                    txt = "*"*80 +"\nError on " + args + "\n" + "*"*80
                    with open(file_name, "a") as file:
                        file.write(txt + "\n")
                    print(txt)
    txt = "Done!"
    with open(file_name, "a") as file:
        file.write(txt + "\n")
    print(txt)

    returnCode = call("./moveOutput.sh", shell=True)