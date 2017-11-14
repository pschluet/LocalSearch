import textwrap
from glob import glob
import numpy as np

class LatexTableBuilder:
    HEADER = textwrap.dedent(r"""
    \begin{table}[]
    \centering
    \caption{Algorithm Performance}
    \label{algperf}
    \begin{tabular}{lrrrrrr}
    \hline
        & \multicolumn{3}{l}{Local Search 1} & \multicolumn{3}{l}{Local Search 2} \\ \hline
    Dataset & Time(s)    & VC Size   & Rel Error  & Time (s)   & VC Size   & Rel Error  \\ \hline
    """)

    FOOTER = textwrap.dedent(r"""
    \end{tabular}
    \end{table}
    """)

    def __init__(self):
        self.rows = []
        pass

    def add_row(self, data):
        format_str = r"{}    & {:.2f}   & {:.0f}      & {:.2f}   & {:.2f}   & {:.0f}      & {:.2f}   \\"
        if not data.alg1_rel_err:
            format_str = r"{}    & {}   & {}      & {}   & {:.2f}   & {:.0f}      & {:.2f}   \\"
        if not data.alg2_rel_err:
            format_str = r"{}    & {:.2f}   & {:.0f}      & {:.2f}   & {}   & {}      & {}   \\"
        if not data.alg1_rel_err and not data.alg2_rel_err:
            format_str = r"{}    & {}   & {}      & {}   & {}   & {}      & {}   \\"

        self.rows.append(format_str
                         .format(data.instance_name.replace("_"," "),
                                 xstr(data.alg1_time_sec),
                                 xstr(data.alg1_vc_size),
                                 xstr(data.alg1_rel_err),
                                 xstr(data.alg2_time_sec),
                                 xstr(data.alg2_vc_size),
                                 xstr(data.alg2_rel_err)))

    def add_rows(self, data_rows):
        for row in data_rows:
            self.add_row(row)

    def gen_table(self):
        return self.HEADER + "\n".join(self.rows) + self.FOOTER

def xstr(input):
    return "" if input is None else input

class DataLoader:
    def __init__(self, instance_name, optimum_vc_size):
        self.instance_name = instance_name
        self.opt_vc_size = optimum_vc_size

        self.alg1_time_sec = None
        self.alg1_vc_size = None
        self.alg1_rel_err = None

        self.alg2_time_sec = None
        self.alg2_vc_size = None
        self.alg2_rel_err = None

        alg1_data = self.__load_data("LS1")
        if not alg1_data:
            return

        alg2_data = self.__load_data("LS2")
        if not alg2_data:
            return

        self.alg1_time_sec = alg1_data[0]
        self.alg1_vc_size = alg1_data[1]
        self.alg1_rel_err = alg1_data[2]

        self.alg2_time_sec = alg2_data[0]
        self.alg2_vc_size = alg2_data[1]
        self.alg2_rel_err = alg2_data[2]

    def __load_data(self, algorithm):
        file_names = glob("output/*" + self.instance_name + ".graph*" + algorithm + "*.trace")

        if not file_names:
            return

        data = []
        for file_name in file_names:
            new_data = np.loadtxt(file_name, delimiter=",")
            new_data = new_data.reshape((-1,2))
            data.append(new_data[-1,:])
        data = np.array(data)

        # Take averages and compute relative sizes
        time_sec = data[:,0].mean()
        vc_size = data[:,1].mean()
        rel_err = (vc_size - self.opt_vc_size) / self.opt_vc_size

        return (time_sec, vc_size, rel_err)

OPT_VALS = [
    ('jazz', 158),
    ('karate', 14),
    ('football', 94),
    ('as-22july06', 3303),
    ('hep-th', 3926),
    ('star', 6902),
    ('star2', 4542),
    ('netscience', 899),
    ('email', 594),
    ('delaunay_n10', 703),
    ('power', 2203)
]

def get_all_data():
    latexBuilder = LatexTableBuilder()
    OPT_VALS.sort(key=lambda x: x[0])
    for opt in OPT_VALS:
        data = DataLoader(opt[0], opt[1])
        latexBuilder.add_row(data)
    return latexBuilder


if __name__ == "__main__":
    builder = get_all_data()
    print(builder.gen_table())