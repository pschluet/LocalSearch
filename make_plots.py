import numpy as np
from glob import glob
import re
from make_results_table import OPT_VALS
import matplotlib.pyplot as plt
import seaborn as sns
import os

PLOT_DIR = 'Report/plots'

def load_data():
    f_names = glob("output/*.trace")
    keys = [get_key_from_file_path(x) for x in f_names]

    data = dict()
    data["rel_errs"] = np.array([])
    data["times"] = np.array([])
    for f_name, key in zip(f_names, keys):
        new_data = np.loadtxt(f_name, delimiter=",")
        if key not in data.keys():
            data[key] = []

        # Replace actual quality with relative error in percent
        opt = [x[1] for x in OPT_VALS if x[0] + ".graph" in f_name]
        new_data[:,1] = (new_data[:,1] - opt) / opt * 100

        data[key].append(new_data)
    return data

def unpack_values(data, data_keys, column):
    out = np.array([])
    for key in data_keys:
        for data_item in data[key]:
            out = np.hstack((out, data_item[:,column]))
    return np.sort(out)

def make_qrtd(data, data_key, rel_err_curve_vals, time_lims):
    # Fix the quality (different curves for different rel. errors)
    make_distrib_plot(data, data_key, 1, rel_err_curve_vals, time_lims)
    plt.xlabel('Run Time [sec]')
    plt.legend(['{:.1f}%'.format(x) for x in rel_err_curve_vals])

def make_sqd(data, data_key, time_curve_vals, rel_err_lims):
    # Fix the time (different curves for different times)
    make_distrib_plot(data, data_key, 0, time_curve_vals, rel_err_lims)
    plt.xlabel('Relative Error [%]')
    plt.legend(['{:.2f}s'.format(x) for x in time_curve_vals])

def make_distrib_plot(data, data_key, col_to_fix, curve_vals, xlims):
    inst_data = data[key]
    x_col = 0 if col_to_fix == 1 else 1
    x_pts = np.linspace(xlims[0], xlims[1], 1000)

    plt.figure()
    for curve_val in curve_vals:
        y_pts = []
        for x_pt in x_pts:
            y_pts.append(get_percentage_solved(inst_data, x_col, x_pt, col_to_fix, curve_val))
        plt.plot(x_pts, y_pts)
    plt.ylabel('P(solve)')
    plt.title(data_key)

def get_percentage_solved(inst_data, col_a, lim_a, col_b, lim_b):
    n = len(inst_data)
    pct_solved = 0

    for run_data in inst_data:
        col_a_ndx = run_data[:,col_a] <= lim_a
        run_solved = np.any(run_data[:,col_b][col_a_ndx] <= lim_b)
        pct_solved += run_solved * 1 / n
    return pct_solved

def get_key_from_file_path(f_path):
    s1 = re.sub(r"output/", "", f_path)
    s2 = re.sub(r"\.graph", "", s1)
    return re.sub(r"_600_.+", "", s2)

def get_all_data_lims(data, data_keys, column):
    all_vals = unpack_values(data, data_keys, column)
    return (all_vals.min(), all_vals.max())

    return vals

def get_curve_vals(data, data_keys, column, num_curves, method):
    min_val = -float('inf')
    max_val = float('inf')

    if method == 1:
        min_val = -min_val
        max_val = -max_val

    for key in data_keys:
        inst_data = data[key]
        for run_data in inst_data:
            if method == 0:
                min_val = max((min_val, np.min(run_data[:,column])))
                max_val = min((max_val, np.max(run_data[:,column])))
            else:
                min_val = min((min_val, np.min(run_data[:, column])))
                max_val = max((max_val, np.max(run_data[:, column])))

    if method == 1:
        min_val *= 1.3
        # max_val *= 0.7

    return np.linspace(min_val, max_val, num_curves)

def get_times_required_to_reach_score(data, score):
    times = []
    for d in data:
        last_ndx = np.argwhere(d[:,1] <= score)[-1][0]
        times.append(d[last_ndx,0])
    return times

def get_boxplot_data(data, keys):
    # Find max error
    d1 = [x[-1,:] for x in data[keys[0]]]
    d2 = [x[-1,:] for x in data[keys[1]]]
    d1_max_score = np.max([x[1] for x in d1])
    d2_max_score = np.max([x[1] for x in d2])
    max_score = max(d1_max_score, d2_max_score)

    # Get times required to reach max_score
    d1_times = get_times_required_to_reach_score(data[keys[0]], max_score)
    d2_times = get_times_required_to_reach_score(data[keys[1]], max_score)

    return (max_score, d1_times, d2_times)

def make_box_plots(data, data_key_sets):
    data_keys = [item for sublist in data_key_sets for item in sublist]

    power_keys = [x for x in data_keys if 'power' in x]
    power_data = get_boxplot_data(data, power_keys)

    star2_keys = [x for x in data_keys if 'star2' in x]
    star2_data = get_boxplot_data(data, star2_keys)

    plt.figure()
    sns.boxplot(['LS1', 'LS2'], [power_data[1], power_data[2]])
    plt.ylabel('Run Time [sec]')
    plt.title('Time Required to Reach {:.2f}% Relative Error on power'.format(power_data[0]))
    plt.savefig(PLOT_DIR + '/boxplot_power.png')

    plt.figure()
    sns.boxplot(['LS1', 'LS2'], [star2_data[1], star2_data[2]])
    plt.ylabel('Run Time [sec]')
    plt.title('Time Required to Reach {:.2f}% Relative Error on star2'.format(star2_data[0]))
    plt.savefig(PLOT_DIR + '/boxplot_star2.png')

if __name__=="__main__":
    data = load_data()

    data_key_sets = [['power_LS1', 'power_LS2'], ['star2_LS1', 'star2_LS2']]

    # Make plot directory if it doesn't exist
    if not os.path.exists(PLOT_DIR):
        os.makedirs(PLOT_DIR)

    for data_keys in data_key_sets:
        rel_err_curve_vals = get_curve_vals(data, data_keys, 1, 4, method=0)

        rel_err_lims = get_all_data_lims(data, data_keys, 1)
        time_curve_vals_both = get_curve_vals(data, data_keys, 0, 4, method=1)
        time_curve_vals_single1 = get_curve_vals(data, [data_keys[0]], 0, 4, method=0)
        time_curve_vals_single2 = get_curve_vals(data, [data_keys[1]], 0, 4, method=0)
        time_lims = get_all_data_lims(data, data_keys, 0)

        for key in data_keys:
            time_curve_vals = get_curve_vals(data, [key], 0, 4, method=1)
            time_curve_vals[0] = time_curve_vals_both[0]
            make_qrtd(data, key, rel_err_curve_vals, time_lims)
            plt.savefig(PLOT_DIR + '/qrtd_' + key + '.png')
            make_sqd(data, key, time_curve_vals, rel_err_lims)
            plt.savefig(PLOT_DIR + '/sqd_' + key + '.png')

    make_box_plots(data, data_key_sets)