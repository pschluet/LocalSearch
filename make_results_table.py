import textwrap

class LatexTableBuilder:
    HEADER = textwrap.dedent(r"""
    \begin{table}[]
    \centering
    \caption{Algorithm Performance}
    \label{algperf}
    \begin{tabular}{|l|l|l|l|l|l|l|}
    \hline
        & \multicolumn{3}{c|}{Local Search 1} & \multicolumn{3}{c|}{Local Search 2} \\ \hline
    Dataset & Time(s)    & VC Size   & Rel Error  & Time (s)   & VC Size   & Rel Error  \\ \hline
    """)

    FOOTER = textwrap.dedent(r"""
    \end{tabular}
    \end{table}
    """)

    def __init__(self):
        self.rows = []
        pass

    def add_row(self, data_file_name, alg1_time_sec, alg1_vc_size, alg1_rel_err, alg2_time_sec, alg2_vc_size, alg2_rel_err):
        self.rows.append(r"{}    & {:.2f}   & {}      & {:.2f}   & {:.2f}   & {}      & {:.2f}   \\ \hline"
                         .format(data_file_name, alg1_time_sec, alg1_vc_size, alg1_rel_err, alg2_time_sec, alg2_vc_size, alg2_rel_err))

    def gen_table(self):
        return self.HEADER + "\n".join(self.rows) + self.FOOTER


if __name__ == "__main__":
    rtb = LatexTableBuilder()
    rtb.add_row("set 1", 100.23402304, 2323, 0.2353242, 220.234023, 3524, 0.834242)
    rtb.add_row("set 2", 123.2134234, 2342, 0.898234, 234.234234, 23432, 0.62342)

    print(rtb.gen_table())